/*******************************************************************************
 * Copyright (c) 2012 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * The scan engine idea is based on the "ScanEngine" developed
 * by the Software Services Group (SSG),  Advanced Photon Source,
 * Argonne National Laboratory,
 * Copyright (c) 2011 , UChicago Argonne, LLC.
 *
 * This implementation, however, contains no SSG "ScanEngine" source code
 * and is not endorsed by the SSG authors.
 ******************************************************************************/
package org.csstudio.scan.server;

import java.io.Serializable;
import java.util.Date;

/** Scan: ID, Name, Date
 *
 *  <p>The ID uniquely identifies a scan within a scan engine.
 *  If the scan engine is backed by a persistend log (RDB, ...),
 *  then the ID may persist through restarts of the scan engine.
 *  With a simple in-memory log, the IDs are only unique within
 *  one Scan Engine JVM, and get re-used as the scan engine restarts.
 *
 *  @author Kay Kasemir
 */
@SuppressWarnings("nls")
public class Scan implements Serializable
{
    /** Serialization ID */
    final private static long serialVersionUID = ScanServer.SERIAL_VERSION;

    final private long id;
    final private String name;
    final private Date created;

    /** Initialize
     *  @param id Scan ID
     *  @param name Name
     *  @param created Time when scan was created (submitted to server)
     */
    public Scan(final long id, final String name, final Date created)
    {
        this.id = id;
        this.name = name;
        this.created = created;
    }

    /** @return Unique scan identifier (within JVM of the scan engine) */
    public long getId()
    {
        return id;
    }

    /** @return Name of the scan */
    public String getName()
    {
        return name;
    }

    /** @return Time when scan was created on server */
    public Date getCreated()
    {
        return created;
    }

    /** Hash on most elements
     *  {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + name.hashCode();
        return result;
    }

    /** Compare all elements
     *  {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (! (obj instanceof Scan))
            return false;
        final Scan other = (Scan) obj;
        return id == other.id;
    }

    /** @return String representation for GUI */
    @Override
    public String toString()
    {
        final StringBuilder buf = new StringBuilder();
        buf.append("Scan '").append(name).append("' [").append(id).append("]");
        return buf.toString();
    }
}
