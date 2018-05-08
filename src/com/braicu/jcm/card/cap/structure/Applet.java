/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */
/** 
 * 2008
 * Please use according to the LGPL license 
 * @author: Stefan Braicu
 */
package com.braicu.jcm.card.cap.structure;

import java.io.IOException;
import java.io.InputStream;

import com.braicu.jcm.utils.ByteUtils;



public class Applet
{
  private int appletCount;
 // private int size;
  private AID[] aid;
  private int installOffset[];

    public Applet(InputStream inputstream)
        throws IOException
    {
        appletCount = 0;
  //      size = 0;
        aid = null;
        installOffset = null;
        byte byte0 = (byte)(inputstream.read() & 0xff);
        if(byte0 != 3)
            throw new IOException("Invalid tag for StaticField component");
        byte abyte0[] = new byte[2];
        if(inputstream.read(abyte0, 0, 2) != 2)
            throw new IOException("Error reading StaticField's component");
   //     size = CardUtils.bytesToInt(abyte0, 0, 2);
        appletCount = (byte)(inputstream.read() & 0xff);
        if(appletCount > 0)
        {
            aid = new AID[appletCount];
            installOffset = new int[appletCount];
            for(int i = 0; i < appletCount; i++)
            {
                aid[i] = new AID(inputstream);
                if(inputstream.read(abyte0, 0, 2) != 2)
                    throw new IOException("Error reading StaticField's component");
                installOffset[i] = ByteUtils.bytesToInt(abyte0, 0, 2);
            }

        }
    }

    public int getAppletCount()
    {
        return appletCount;
    }

    public int getInstallOffset(int i)
    {
        return installOffset[i];
    }

    public AID getAID(int i)
    {
        return aid[i];
    }

    public int[] getInstallOffset()
    {
        return installOffset;
    }

    public AID[] getAIDs()
    {
        return aid;
    }

}
