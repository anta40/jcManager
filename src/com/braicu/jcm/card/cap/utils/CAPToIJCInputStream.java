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
package com.braicu.jcm.card.cap.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.braicu.jcm.utils.ByteUtils;



public class CAPToIJCInputStream extends InputStream
{

	
    private static final String CAPNAMES[] = {
        "Header.cap", "Directory.cap", "Import.cap", "Applet.cap", "Class.cap", "Method.cap", "StaticField.cap", "Export.cap", "ExportDescription.cap", "ConstantPool.cap", 
        "RefLocation.cap", "Descriptor.cap"
    };
    private byte buffer[];
    private int bufferOffset;
    private byte header[];
    private int headerOffset;
    private byte FILE_TAG;
    private ByteArrayOutputStream baos;

	
    public CAPToIJCInputStream(String s)
        throws IOException
    {
        buffer = null;
        bufferOffset = 0;
        header = null;
        headerOffset = 0;
        FILE_TAG = -60;
        baos = new ByteArrayOutputStream();
        JarFile jarfile = new JarFile(s);
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        Enumeration<JarEntry> enumeration = jarfile.entries();
        String s1 = null;
        if (enumeration.hasMoreElements()) {
        	s1 = getPrefix(((JarEntry)enumeration.nextElement()).getName());
        	if ("META-INF/".equalsIgnoreCase(s1))
        		s1 = getPrefix(((JarEntry)enumeration.nextElement()).getName());
        }
        else
            throw new IOException("Cannot find any entries in CAP file");
        for(int i = 0; i < CAPNAMES.length; i++)
        {
            byte abyte0[] = getJarData(jarfile, s1 + CAPNAMES[i]);
            
            if(abyte0 != null)
                bytearrayoutputstream.write(abyte0);
        }

        buffer = bytearrayoutputstream.toByteArray();
        bufferOffset = 0;
        bytearrayoutputstream.reset();
        bytearrayoutputstream.write(FILE_TAG);
        bytearrayoutputstream.write(ByteUtils.toLengthOctets(buffer.length));
        header = bytearrayoutputstream.toByteArray();
        headerOffset = 0;
    }

    public CAPToIJCInputStream(InputStream inputstream)
    {
        buffer = null;
        bufferOffset = 0;
        header = null;
        headerOffset = 0;
        FILE_TAG = -60;
        baos = new ByteArrayOutputStream();
        throw new UnsupportedOperationException("Method is not yet implemented");
    }

    public int available()
        throws IOException
    {
        return buffer.length + header.length;
    }

    public void close()
        throws IOException
    {
    }

    public void mark(int i)
    {
    }

    public void reset()
        throws IOException
    {
    }

    public boolean markSupported()
    {
        return false;
    }

    public int read()
        throws IOException
    {
        if(headerOffset < header.length)
            return header[headerOffset++];
        if(bufferOffset < buffer.length)
            return buffer[bufferOffset++];
        else
            return -1;
    }

    public int read(byte abyte0[])
        throws IOException
    {
        return read(abyte0, 0, abyte0.length);
    }

    public int read(byte abyte0[], int i, int j)
        throws IOException
    {
        int k = 0;
        int l = i;
        int i1 = j;
        k = header.length - headerOffset;
        if(k > 0)
            if(i1 > k)
            {
                System.arraycopy(header, headerOffset, abyte0, l, k);
                headerOffset += k;
                l += k;
                i1 -= k;
            } else
            {
                System.arraycopy(header, headerOffset, abyte0, l, i1);
                headerOffset += i1;
                return i1;
            }
        k = buffer.length - bufferOffset;
        if(k > 0)
        {
            if(i1 > k)
            {
                System.arraycopy(buffer, bufferOffset, abyte0, l, k);
                bufferOffset += k;
                l += k;
                return i - l;
            } else
            {
                System.arraycopy(buffer, bufferOffset, abyte0, l, i1);
                bufferOffset += i1;
                l += i1;
                return i - l;
            }
        } else
        {
            return -1;
        }
    }

    public long skip(long l)
        throws IOException
    {
        return 0L;
    }

    private byte[] getJarData(JarFile jarfile, String s)
        throws IOException
    {
        JarEntry jarentry = jarfile.getJarEntry(s);
        if(jarentry == null)
            return null;
        InputStream inputstream = jarfile.getInputStream(jarentry);
        int i = 0;
        baos.reset();
        while((i = inputstream.read()) != -1) 
            baos.write(i);
        return baos.toByteArray();
    }

    private String getPrefix(String s)
    {
        int i = s.lastIndexOf("/");
        if(i > 0)
            return s.substring(0, i + 1);
        else
            throw new RuntimeException("Cannot find applet prefix");
    }

    
}
