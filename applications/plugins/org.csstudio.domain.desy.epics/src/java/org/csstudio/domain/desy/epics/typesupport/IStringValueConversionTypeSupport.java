/*
 * Copyright (c) 2011 Stiftung Deutsches Elektronen-Synchrotron,
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
package org.csstudio.domain.desy.epics.typesupport;
import java.util.List;

import javax.annotation.Nonnull;

import org.csstudio.domain.desy.epics.alarm.EpicsAlarm;
import org.csstudio.domain.desy.epics.alarm.EpicsSystemVariable;
import org.csstudio.domain.desy.system.ControlSystem;
import org.csstudio.domain.desy.time.TimeInstant;
import org.csstudio.domain.desy.types.CssValueType;
import org.csstudio.domain.desy.typesupport.BaseTypeConversionSupport;
import org.csstudio.domain.desy.typesupport.TypeSupportException;
import org.csstudio.platform.data.IStringValue;

import com.google.common.collect.Lists;

/**
 * IStringIValue conversion support
 *
 * @author bknerr
 * @since 15.12.2010
 */
final class IStringValueConversionTypeSupport extends
        AbstractIValueConversionTypeSupport<IStringValue> {
    /**
     * Constructor.
     */
    public IStringValueConversionTypeSupport() {
        super(IStringValue.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    protected EpicsSystemVariable<?> convertToSystemVariable(@Nonnull final String name,
                                                             @Nonnull final IStringValue value) throws TypeSupportException {

        final String[] values = value.getValues();
        if (values == null || values.length == 0) {
            throw new TypeSupportException("IStringValue doesn't have any values. Conversion failed.", null);
        }

        final EpicsAlarm alarm = EpicsIValueTypeSupport.toEpicsAlarm(value.getSeverity(),
                                                                     value.getStatus().toUpperCase());
        final TimeInstant timestamp = BaseTypeConversionSupport.toTimeInstant(value.getTime());
        if (values.length == 1) {
            return new EpicsSystemVariable<String>(name,
                                                   new CssValueType<String>(values[0]),
                                                   ControlSystem.EPICS_DEFAULT,
                                                   timestamp,
                                                   alarm);
        }
        return new EpicsSystemVariable<List<String>>(name,
                                                    new CssValueType<List<String>>(Lists.newArrayList(values)),
                                                    ControlSystem.EPICS_DEFAULT,
                                                    timestamp,
                                                    alarm);
    }
}