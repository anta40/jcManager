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



public class StaticField
{
    private int size;
    private int imageSize;
    private int referenceCount;
    private ArrayInitInfo arrayInitInfos[];
    private int defaultValueCount;
    private byte nonDefaultValues[];
    public static final byte STATICFIELD_FILE_TAG = 8;
    public static final byte u2_DATA_SIZE = 2;

    public StaticField(InputStream inputstream)
        throws IOException
    {
        size = 0;
        imageSize = 0;
        referenceCount = 0;
        arrayInitInfos = null;
        nonDefaultValues = null;
        byte byte0 = (byte)(inputstream.read() & 0xff);
        if(byte0 != 8)
            throw new IOException("Invalid tag for StaticField component");
        byte abyte0[] = new byte[2];
        if(inputstream.read(abyte0, 0, 2) != 2)
            throw new IOException("Error reading StaticField's component");
        size = ByteUtils.bytesToInt(abyte0, 0, 2);
        if(inputstream.read(abyte0, 0, 2) != 2)
            throw new IOException("Error reading StaticField's component");
        imageSize = ByteUtils.bytesToInt(abyte0, 0, 2);
        if(inputstream.read(abyte0, 0, 2) != 2)
            throw new IOException("Error reading StaticField's component");
        referenceCount = ByteUtils.bytesToInt(abyte0, 0, 2);
        if(inputstream.read(abyte0, 0, 2) != 2)
            throw new IOException("Error reading StaticField's component");
        int i = ByteUtils.bytesToInt(abyte0, 0, 2);
        if(i > 0)
        {
            arrayInitInfos = new ArrayInitInfo[i];
            for(int j = 0; j < i; j++)
                arrayInitInfos[j] = new ArrayInitInfo(inputstream);

        }
        if(inputstream.read(abyte0, 0, 2) != 2)
            throw new IOException("Error reading StaticField's component");
        defaultValueCount = ByteUtils.bytesToInt(abyte0, 0, 2);
        if(inputstream.read(abyte0, 0, 2) != 2)
            throw new IOException("Error reading StaticField's component");
        int k = ByteUtils.bytesToInt(abyte0, 0, 2);
        if(k > 0)
        {
            nonDefaultValues = new byte[k];
            if(inputstream.read(nonDefaultValues, 0, k) != k)
                throw new IOException("Error reading StaticField's component");
        }
    }

    public int getImageSize()
    {
        return imageSize;
    }

    public int getReferenceCount()
    {
        return referenceCount;
    }

    public int getSize()
    {
        return size;
    }

    public ArrayInitInfo[] getArrayInitInfos()
    {
        return arrayInitInfos;
    }

    public int getDefaultValueCount()
    {
        return defaultValueCount;
    }

    public byte[] getNonDefaultValues()
    {
        return nonDefaultValues;
    }

    public int getNonDefaultValuesSize()
    {
        if(nonDefaultValues != null)
            return nonDefaultValues.length;
        else
            return 0;
    }
}
