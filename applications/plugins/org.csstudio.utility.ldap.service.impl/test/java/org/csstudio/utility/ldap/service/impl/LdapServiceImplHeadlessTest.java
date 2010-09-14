/*
 * Copyright (c) 2010 Stiftung Deutsches Elektronen-Synchrotron,
 * Member of the Helmholtz Association, (DESY), HAMBURG, GERMANY.
 *
 * THIS SOFTWARE IS PROVIDED UNDER THIS LICENSE ON AN "../AS IS" BASIS.
 * WITHOUT WARRANTY OF ANY KIND, EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR PARTICULAR PURPOSE AND
 * NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR
 * THE USE OR OTHER DEALINGS IN THE SOFTWARE. SHOULD THE SOFTWARE PROVE DEFECTIVE
 * IN ANY RESPECT, THE USER ASSUMES THE COST OF ANY NECESSARY SERVICING, REPAIR OR
 * CORRECTION. THIS DISCLAIMER OF WARRANTY CONSTITUTES AN ESSENTIAL PART OF THIS LICENSE.
 * NO USE OF ANY SOFTWARE IS AUTHORIZED HEREUNDER EXCEPT UNDER THIS DISCLAIMER.
 * DESY HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS,
 * OR MODIFICATIONS.
 * THE FULL LICENSE SPECIFYING FOR THE SOFTWARE THE REDISTRIBUTION, MODIFICATION,
 * USAGE AND OTHER RIGHTS AND OBLIGATIONS IS INCLUDED WITH THE DISTRIBUTION OF THIS
 * PROJECT IN THE FILE LICENSE.HTML. IF THE LICENSE IS NOT INCLUDED YOU MAY FIND A COPY
 * AT HTTP://WWW.DESY.DE/LEGAL/LICENSE.HTM
 */
package org.csstudio.utility.ldap.service.impl;

import static org.csstudio.utility.ldap.treeconfiguration.LdapEpicsControlsConfiguration.COMPONENT;
import static org.csstudio.utility.ldap.treeconfiguration.LdapEpicsControlsConfiguration.FACILITY;
import static org.csstudio.utility.ldap.treeconfiguration.LdapEpicsControlsConfiguration.IOC;
import static org.csstudio.utility.ldap.treeconfiguration.LdapEpicsControlsConfiguration.RECORD;
import static org.csstudio.utility.ldap.treeconfiguration.LdapEpicsControlsConfiguration.UNIT;
import static org.csstudio.utility.ldap.treeconfiguration.LdapEpicsControlsFieldsAndAttributes.ATTR_FIELD_RESPONSIBLE_PERSON;
import static org.csstudio.utility.ldap.treeconfiguration.LdapEpicsControlsFieldsAndAttributes.EPICS_CTRL_FIELD_VALUE;
import static org.csstudio.utility.ldap.treeconfiguration.LdapFieldsAndAttributes.ATTR_FIELD_OBJECT_CLASS;
import static org.csstudio.utility.ldap.treeconfiguration.LdapFieldsAndAttributes.ATTR_VAL_COM_OBJECT_CLASS;
import static org.csstudio.utility.ldap.treeconfiguration.LdapFieldsAndAttributes.ATTR_VAL_FAC_OBJECT_CLASS;
import static org.csstudio.utility.ldap.treeconfiguration.LdapFieldsAndAttributes.ATTR_VAL_IOC_OBJECT_CLASS;
import static org.csstudio.utility.ldap.treeconfiguration.LdapFieldsAndAttributes.ATTR_VAL_REC_OBJECT_CLASS;
import static org.csstudio.utility.ldap.treeconfiguration.LdapFieldsAndAttributes.ORGANIZATION_UNIT_FIELD_NAME;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import junit.framework.Assert;

import org.csstudio.platform.test.TestDataProvider;
import org.csstudio.utility.ldap.LdapActivator;
import org.csstudio.utility.ldap.service.ILdapContentModelBuilder;
import org.csstudio.utility.ldap.service.ILdapReadCompletedCallback;
import org.csstudio.utility.ldap.service.ILdapSearchResult;
import org.csstudio.utility.ldap.service.ILdapService;
import org.csstudio.utility.ldap.treeconfiguration.LdapEpicsControlsConfiguration;
import org.csstudio.utility.ldap.utils.LdapSearchParams;
import org.csstudio.utility.ldap.utils.LdapSearchResult;
import org.csstudio.utility.ldap.utils.LdapUtils;
import org.csstudio.utility.treemodel.ContentModel;
import org.csstudio.utility.treemodel.CreateContentModelException;
import org.csstudio.utility.treemodel.ISubtreeNodeComponent;
import org.eclipse.core.runtime.jobs.Job;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Test class for LDAP service actions.
 *
 * Initializes the LDAP service to an LDAP test instance.
 */
public class LdapServiceImplHeadlessTest {

    /**
     * Property holder for end of job test purpose.
     *
     * @author bknerr
     * @author $Author: bknerr $
     * @version $Revision: 1.7 $
     * @since 14.09.2010
     */
    private static class Holder<T> {

        private T _value;
        /**
         * Constructor.
         * @param val
         */
        public Holder(@Nonnull final T val) {
            _value = val;
        }
        @Nonnull
        public T getValue() {
            return _value;
        }
        public void setValue(@Nonnull final T value) {
            _value = value;
        }
    }

    private static TestDataProvider PROV;

    private static ILdapService LDAP_SERVICE;

    private static Random RANDOM = new Random(System.currentTimeMillis());
    private static String EFAN_NAME = "Test" + String.valueOf(Math.abs(RANDOM.nextInt())) + "Efan1";


    private static Attributes EFAN_ATTRS = new BasicAttributes();
    private static Attributes ECOM_ATTRS = new BasicAttributes();
    private static Attributes ECON_ATTRS = new BasicAttributes();
    private static Attributes EREN_ATTRS = new BasicAttributes();

    static {
        EFAN_ATTRS.put(ATTR_FIELD_OBJECT_CLASS, ATTR_VAL_FAC_OBJECT_CLASS);
        ECOM_ATTRS.put(ATTR_FIELD_OBJECT_CLASS, ATTR_VAL_COM_OBJECT_CLASS);
        ECON_ATTRS.put(ATTR_FIELD_RESPONSIBLE_PERSON, "bastian.knerr@desy.de");
        ECON_ATTRS.put(ATTR_FIELD_OBJECT_CLASS, ATTR_VAL_IOC_OBJECT_CLASS);
        EREN_ATTRS.put(ATTR_FIELD_OBJECT_CLASS, ATTR_VAL_REC_OBJECT_CLASS);
    }


    @BeforeClass
    public static void setUp() {
        setUpCreateTestDataProvider();

        setUpCreateLdapConnection();

        setUpCreateComponents();
    }

    private static void setUpCreateTestDataProvider() {
        try {
            PROV = TestDataProvider.getInstance(LdapActivator.PLUGIN_ID);
        } catch (final Exception e) {
            Assert.fail("Unexpected exception");
        }
    }

    /**
     * Tests the method {@link ILdapService#reInitializeLdapConnection(Map)}
     */
    private static void setUpCreateLdapConnection() {
        try {
            final String url = (String) PROV.get("ldap.url");
            final String dn = (String) PROV.get("ldap.userDn");
            final String pw = (String) PROV.get("ldap.userPassword");

            // Hard-coded properties
            final Map<String, String> map = new HashMap<String, String>(5);
            map.put(Context.PROVIDER_URL, url);
            map.put(Context.SECURITY_PRINCIPAL, dn);
            map.put(Context.SECURITY_CREDENTIALS, pw);

            LDAP_SERVICE = LdapActivator.getDefault().getLdapService();
            Assert.assertTrue(LDAP_SERVICE.reInitializeLdapConnection(map));
        } catch (final Exception e) {
            Assert.fail("Unexpected exception");
        }
    }

    /**
     * Tests the method {@link ILdapService#createComponent(LdapName, Attributes)}.
     */
    private static void setUpCreateComponents() {
        try {
            final LdapName name =
                LdapUtils.createLdapName(FACILITY.getNodeTypeName(), EFAN_NAME,
                                         ORGANIZATION_UNIT_FIELD_NAME, EPICS_CTRL_FIELD_VALUE);
            Assert.assertTrue(LDAP_SERVICE.createComponent(name, EFAN_ATTRS));


            name.add(new Rdn(COMPONENT.getNodeTypeName(), "TestEcom1"));
            Assert.assertTrue(LDAP_SERVICE.createComponent(name, ECOM_ATTRS));

            name.add(new Rdn(IOC.getNodeTypeName(), "TestEcon1"));
            Assert.assertTrue(LDAP_SERVICE.createComponent(name, ECON_ATTRS));

            name.add(new Rdn(RECORD.getNodeTypeName(), "TestEren1"));
            Assert.assertTrue(LDAP_SERVICE.createComponent(name, EREN_ATTRS));

            name.remove(name.size() - 1);
            name.add(new Rdn(RECORD.getNodeTypeName(), "TestEren2"));
            Assert.assertTrue(LDAP_SERVICE.createComponent(name, EREN_ATTRS));

            final LdapName name2 =
                LdapUtils.createLdapName(FACILITY.getNodeTypeName(), EFAN_NAME,
                                         ORGANIZATION_UNIT_FIELD_NAME, EPICS_CTRL_FIELD_VALUE);

            name2.add(new Rdn(COMPONENT.getNodeTypeName(), "TestEcom2"));
            Assert.assertTrue(LDAP_SERVICE.createComponent(name2, ECOM_ATTRS));

            name2.add(new Rdn(IOC.getNodeTypeName(), "TestEcon2"));
            Assert.assertTrue(LDAP_SERVICE.createComponent(name2, ECON_ATTRS));

            name2.add(new Rdn(RECORD.getNodeTypeName(), "TestEren3"));
            Assert.assertTrue(LDAP_SERVICE.createComponent(name2, EREN_ATTRS));

            name2.remove(name2.size() - 1);
            name2.add(new Rdn(RECORD.getNodeTypeName(), "TestEren4"));
            Assert.assertTrue(LDAP_SERVICE.createComponent(name2, EREN_ATTRS));

        } catch (final InvalidNameException e) {
            Assert.fail("LDAP name composition failed.");
        }
    }


    @Test
    public void testLdapContentModelBuilder() {
        final LdapName name =
            LdapUtils.createLdapName(FACILITY.getNodeTypeName(), EFAN_NAME,
                                     UNIT.getNodeTypeName(), UNIT.getRootTypeValue());
        final ILdapSearchResult result =
            LDAP_SERVICE.retrieveSearchResultSynchronously(name,
                                                           LdapUtils.any(RECORD.getNodeTypeName()),
                                                           SearchControls.SUBTREE_SCOPE);
        Assert.assertNotNull(result);

        final ILdapContentModelBuilder builder = LDAP_SERVICE.getLdapContentModelBuilder(UNIT, result);
        Assert.assertNotNull(builder);

        try {
            builder.build();
        } catch (final CreateContentModelException e) {
            Assert.fail("Content model could not be created.");
        }
        final ContentModel<LdapEpicsControlsConfiguration> model = builder.getModel();

        final Map<String, ISubtreeNodeComponent<LdapEpicsControlsConfiguration>> records =
            model.getByType(RECORD);

        Assert.assertEquals(4, records.size());
    }

    @Test
    public void testLdapReaderJob() {
        final LdapName name =
            LdapUtils.createLdapName(FACILITY.getNodeTypeName(), EFAN_NAME,
                                     UNIT.getNodeTypeName(), UNIT.getRootTypeValue());

        final ILdapSearchResult result = new LdapSearchResult();
        final Holder<Boolean> read = new Holder<Boolean>(Boolean.FALSE);
        final Job job = LDAP_SERVICE.createLdapReaderJob(new LdapSearchParams(name, LdapUtils.any(RECORD.getNodeTypeName())),
                                                         result,
                                                         new ILdapReadCompletedCallback() {
                                                            @Override
                                                            public void onLdapReadComplete() {
                                                                read.setValue(Boolean.TRUE);
                                                            }
                                                        });
        job.schedule();
        try {
            job.join();
        } catch (final InterruptedException e) {
            Assert.fail("Not supposed to be interrupted.");
        }
        Assert.assertTrue(read.getValue());
        Assert.assertEquals(4, result.getAnswerSet().size());
    }

    @Test
    public void testAttributeAccess() {
        final LdapName name =
            LdapUtils.createLdapName(IOC.getNodeTypeName(), "TestEcon2",
                                     COMPONENT.getNodeTypeName(), "TestEcom2",
                                     FACILITY.getNodeTypeName(), EFAN_NAME,
                                     ORGANIZATION_UNIT_FIELD_NAME, EPICS_CTRL_FIELD_VALUE);

        try {
            Attributes attrs = LDAP_SERVICE.getAttributes(name);
            Assert.assertNotNull(attrs);
            Attribute attr = attrs.get(ATTR_FIELD_RESPONSIBLE_PERSON);
            Assert.assertNotNull(attr);
            String value = (String) attr.get();
            Assert.assertEquals("bastian.knerr@desy.de", value);

            ModificationItem[] items = new ModificationItem[]{new ModificationItem(DirContext.REMOVE_ATTRIBUTE, attr)};
            LDAP_SERVICE.modifyAttributes(name, items);

            attrs = LDAP_SERVICE.getAttributes(name);
            Assert.assertNotNull(attrs);
            final Attribute attrNull = attrs.get(ATTR_FIELD_RESPONSIBLE_PERSON);
            Assert.assertNull(attrNull);

            items = new ModificationItem[]{new ModificationItem(DirContext.ADD_ATTRIBUTE, attr)};
            LDAP_SERVICE.modifyAttributes(name, items);

            attrs = LDAP_SERVICE.getAttributes(name);
            Assert.assertNotNull(attrs);
            attr = attrs.get(ATTR_FIELD_RESPONSIBLE_PERSON);
            Assert.assertNotNull(attr);
            value = (String) attr.get();
            Assert.assertEquals("bastian.knerr@desy.de", value);

        } catch (final NamingException e) {
            Assert.fail("Unexpected Exception on attribute modification.");
        }
    }

    @Test
    public void testRenameAndLookup() {
        final LdapName name1 =
            LdapUtils.createLdapName(RECORD.getNodeTypeName(), "TestEren3",
                                     IOC.getNodeTypeName(), "TestEcon2",
                                     COMPONENT.getNodeTypeName(), "TestEcom2",
                                     FACILITY.getNodeTypeName(), EFAN_NAME,
                                     ORGANIZATION_UNIT_FIELD_NAME, EPICS_CTRL_FIELD_VALUE);

        final LdapName name2 =
            LdapUtils.createLdapName(RECORD.getNodeTypeName(), "NedFlanders",
                                     IOC.getNodeTypeName(), "TestEcon2",
                                     COMPONENT.getNodeTypeName(), "TestEcom2",
                                     FACILITY.getNodeTypeName(), EFAN_NAME,
                                     ORGANIZATION_UNIT_FIELD_NAME, EPICS_CTRL_FIELD_VALUE);

        try {
            LDAP_SERVICE.rename(name1, name2);
            Assert.assertNotNull(LDAP_SERVICE.lookup(name2));
            LDAP_SERVICE.rename(name2, name1);
            Assert.assertNotNull(LDAP_SERVICE.lookup(name1));
        } catch (final NamingException e) {
            Assert.fail("Rename failed");
        }
    }

    /**
     * Tests the service method {@link ILdapService#removeComponent(Enum, LdapName)} and consequently
     * the {@link ILdapService#removeLeafComponent(LdapName)}.
     */
    @AfterClass
    public static void removeTestLdapStructure() {

        final LdapName name =
            LdapUtils.createLdapName(FACILITY.getNodeTypeName(), EFAN_NAME,
                                     UNIT.getNodeTypeName(), UNIT.getRootTypeValue());
        try {
            Assert.assertTrue(LDAP_SERVICE.removeComponent(UNIT, name));
        } catch (final InvalidNameException e) {
            Assert.fail("Unexpected exception:\n" + e.getMessage());
        } catch (final CreateContentModelException e) {
            Assert.fail("Content model could not be created:\n" + e.getMessage());
        }
    }

}