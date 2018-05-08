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



public class ArrayInitInfo
{

    private byte tag;
    private byte values[];
    private static final int u2_SIZE = 2;
    private static final byte BOOLEAN_TAG = 2;
    private static final byte BYTE_TAG = 3;
    private static final byte SHORT_TAG = 4;
    private static final byte INT_TAG = 5;

	
    public ArrayInitInfo(InputStream inputstream)
        throws IOException
    {
        tag = 0;
        values = null;
        byte abyte0[] = new byte[2];
        tag = (byte)(inputstream.read() & 0xff);
        if(inputstream.read(abyte0, 0, 2) != 2)
            throw new IOException("Error reading Static field size's image size");
        int i = ByteUtils.bytesToInt(abyte0, 0, 2);
        values = new byte[i];
        if(inputstream.read(values, 0, i) != i)
            throw new IOException("Error reading Static field size's image size");
        else
            return;
    }

    public byte getTag()
    {
        return tag;
    }

    public int getSize()
    {
        return values.length;
    }

    public byte[] getValues()
    {
        return values;
    }

}
