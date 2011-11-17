
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

package org.csstudio.archive.sdds.server.type;

import javax.annotation.Nonnull;

/**
 * TODO (mmoeller) :
 *
 * @author mmoeller
 * @since 16.11.2011
 */
public class IntegerConverter implements ITypeConverter {

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public final synchronized Double toDouble(@Nonnull final Object o) {
        return ((Integer) o).doubleValue();
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public final synchronized Float toFloat(@Nonnull final Object o) {
        return ((Integer) o).floatValue();
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public final synchronized Long toLong(@Nonnull final Object o) {
        return ((Integer) o).longValue();
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public final synchronized Integer toInteger(@Nonnull final Object o) {
        return (Integer) o;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public final synchronized String toString(@Nonnull final Object o) {
        return ((Integer) o).toString();
    }
}
