/*******************************************************************************
 * Copyright (c) 2010 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.apputil.ui.workbench;

import org.eclipse.osgi.util.NLS;

/** @author Kay Kasemir */
public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.csstudio.apputil.ui.workbench.messages"; //$NON-NLS-1$
    public static String OpenPerspectiveReset;
    public static String OpenPerspectiveResetQuestion;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages()
    {
        // Prevent instantiation
    }
}
