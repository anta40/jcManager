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
 */
/** 
 * 2008
 * Please use according to the LGPL license 
 * @author: Stefan Braicu
 */
package com.braicu.jcm.utils;

import java.io.ByteArrayOutputStream;

public final class ByteUtils
{

	public static String dumpHex(byte byte0)
    {
        if((byte0 & 0xff) < 16)
            return "0" + Integer.toHexString(byte0 & 0xff);
        else
            return Integer.toHexString(byte0 & 0xff);
    }

    public static String dumpHex(byte abyte0[], int i, int j)
    {
        StringBuffer stringbuffer = new StringBuffer();
        for(int k = i; k < abyte0.length && k < i + j; k++)
        {
            stringbuffer.append(dumpHex(abyte0[k]));
            stringbuffer.append(" ");
        }

        return stringbuffer.toString();
    }

    public static String dumpHex(byte abyte0[])
    {
        return dumpHex(abyte0, 0, abyte0.length);
    }


    public static boolean isBytesEqual(byte abyte0[], byte abyte1[])
    {
        if(abyte0.length != abyte1.length)
            return false;
        for(int i = 0; i < abyte0.length; i++)
            if(abyte0[i] != abyte1[i])
                return false;

        return true;
    }


    public static final byte[] intToBytes(int i)
    {
        int j = (Integer.toHexString(i).length() + 1) / 2;
        byte abyte0[] = new byte[j];
        for(int k = 0; k < j; k++)
            abyte0[k] = (byte)(i >>> 8 * (j - 1 - k) & 0xff);

        return abyte0;
    }

    public static final int bytesToInt(byte abyte0[], int i, int j)
    {
        int k = 0;
        for(int l = 0; l < j; l++)
            k = k << 8 | abyte0[l + i] & 0xff;

        return k;
    }

    public static final byte[] toLengthOctets(int i)
    {
        byte abyte0[] = null;
        if(i < 128)
        {
            abyte0 = new byte[1];
            abyte0[0] = (byte)i;
        } else
        {
            byte abyte1[] = intToBytes(i);
            abyte0 = new byte[1 + abyte1.length];
            System.arraycopy(abyte1, 0, abyte0, 1, abyte1.length);
            abyte0[0] = (byte)(abyte1.length | 0x80);
        }
        return abyte0;
    }

    public static byte[] strToBytes(String s)
    {
        if(s.length() % 2 != 0)
            throw new IllegalArgumentException("String length % 2 != 0");
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        for(int i = 0; i < s.length(); i += 2)
        {
            byte byte0 = (byte)(Character.digit(s.charAt(i), 16) & 0xff);
            byte0 <<= 4;
            byte0 |= Character.digit(s.charAt(i + 1), 16);
            bytearrayoutputstream.write(byte0);
        }

        return bytearrayoutputstream.toByteArray();
    }

	private static String numbers = "0123456789abcdef";

    
	public static byte[] stoh(String s) {
		s = s.replaceAll(" ", "").toLowerCase();
		if (s == null)
			return null;
		if (s.length() % 2 != 0)
			throw new RuntimeException("invalid length");
		byte[] result = new byte[s.length() / 2];
		for (int i = 0; i < s.length(); i += 2) {
			int i1 = numbers.indexOf(s.charAt(i));
			if (i1 == -1)
				throw new RuntimeException("invalid number");
			int i2 = numbers.indexOf(s.charAt(i + 1));
			if (i2 == -1)
				throw new RuntimeException("invalid number");
			result[i / 2] = (byte) ((i1 << 4) | i2);
		}
		return result;
	}

	public static String htos(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			String tmp = Integer.toHexString(((int) bytes[i]) & 0xFF);
			while (tmp.length() < 2)
				tmp = "0" + tmp;
			if (i!=bytes.length-1)
				sb.append(tmp + " ");
			else
				sb.append(tmp);

		}
		return sb.toString().toUpperCase();
	}
}
