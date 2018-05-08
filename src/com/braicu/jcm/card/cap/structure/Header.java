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



public class Header
{

  
  private int size;
  private int magic;
  private byte minorVersion;
  private byte majorVersion;
  private byte flag;
  private PackageInfo packageInfo;

    public Header(InputStream inputstream)
        throws IOException
    {
        size = 0;
        magic = 0;
        minorVersion = 0;
        majorVersion = 0;
        flag = 0;
        packageInfo = null;
        byte byte0 = (byte)(inputstream.read() & 0xff);
        if(byte0 != 1)
            throw new IOException("Invalid tag for Header component");
        byte abyte0[] = new byte[4];
        if(inputstream.read(abyte0, 0, 2) != 2)
            throw new IOException("Error reading size");
        size = ByteUtils.bytesToInt(abyte0, 0, 2);
        if(inputstream.read(abyte0, 0, 4) != 4)
            throw new IOException("Error reading magic");
        magic = ByteUtils.bytesToInt(abyte0, 0, 4);
        minorVersion = (byte)(inputstream.read() & 0xff);
        majorVersion = (byte)(inputstream.read() & 0xff);
        flag = (byte)(inputstream.read() & 0xff);
        packageInfo = new PackageInfo(inputstream);
    }

    public int getMagic()
    {
        return magic;
    }

    public byte getMajorVersion()
    {
        return majorVersion;
    }

    public byte getMinorVersion()
    {
        return minorVersion;
    }

    public byte getFlag()
    {
        return flag;
    }

    public PackageInfo getPackageInfo()
    {
        return packageInfo;
    }

    public int getDataSize()
    {
        return size;
    }
}
