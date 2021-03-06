/*
		* Copyright (c) 2010 Stiftung Deutsches Elektronen-Synchrotron,
		* Member of the Helmholtz Association, (DESY), HAMBURG, GERMANY.
		*
		* THIS SOFTWARE IS PROVIDED UNDER THIS LICENSE ON AN "../AS IS" BASIS.
		* WITHOUT WARRANTY OF ANY KIND, EXPRESSED OR IMPLIED, INCLUDING BUT
		NOT LIMITED
		* TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR PARTICULAR PURPOSE
		AND
		* NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
		BE LIABLE
		* FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
		CONTRACT,
		* TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
		SOFTWARE OR
		* THE USE OR OTHER DEALINGS IN THE SOFTWARE. SHOULD THE SOFTWARE PROVE
		DEFECTIVE
		* IN ANY RESPECT, THE USER ASSUMES THE COST OF ANY NECESSARY SERVICING,
		REPAIR OR
		* CORRECTION. THIS DISCLAIMER OF WARRANTY CONSTITUTES AN ESSENTIAL PART
		OF THIS LICENSE.
		* NO USE OF ANY SOFTWARE IS AUTHORIZED HEREUNDER EXCEPT UNDER THIS
		DISCLAIMER.
		* DESY HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
		ENHANCEMENTS,
		* OR MODIFICATIONS.
		* THE FULL LICENSE SPECIFYING FOR THE SOFTWARE THE REDISTRIBUTION,
		MODIFICATION,
		* USAGE AND OTHER RIGHTS AND OBLIGATIONS IS INCLUDED WITH THE
		DISTRIBUTION OF THIS
		* PROJECT IN THE FILE LICENSE.HTML. IF THE LICENSE IS NOT INCLUDED YOU
		MAY FIND A COPY
		* AT HTTP://WWW.DESY.DE/LEGAL/LICENSE.HTM
		*/
package org.csstudio.sds.components.model.eventing;

import org.csstudio.sds.components.model.AbstractMarkedWidgetModel;
import org.csstudio.sds.eventhandling.AbstractEnsureInvariantsCommand;
import org.csstudio.sds.eventhandling.AbstractWidgetPropertyPostProcessor;
import org.eclipse.gef.commands.Command;

/**
 * TODO (hrickens) :
 *
 * @author hrickens
 * @author $Author: hrickens $
 * @version $Revision: 1.1 $
 * @since 30.04.2010
 */
public class AbstractMarkedWidgetShowHiPostProcessor extends AbstractWidgetPropertyPostProcessor<AbstractMarkedWidgetModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected Command doCreateCommand(final AbstractMarkedWidgetModel widget) {
        assert widget != null : "widget != null";
        return new AbstractEnsureInvariantsCommand<AbstractMarkedWidgetModel>(widget, AbstractMarkedWidgetModel.PROP_SHOW_HI) {
            @Override
            protected boolean shouldHideProperties(final AbstractMarkedWidgetModel widget,
                    final String propertyId) {
                return !widget.getBooleanProperty(propertyId);
            }

            @Override
            protected String[] getPropertyIds() {
                return new String[] {AbstractMarkedWidgetModel.PROP_HI_COLOR};
            }
        };
    }
}
