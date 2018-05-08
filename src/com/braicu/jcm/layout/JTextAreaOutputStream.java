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
package com.braicu.jcm.layout;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

import com.braicu.jcm.utils.Settings;

public class JTextAreaOutputStream extends OutputStream
{
  private JTextArea ta;
  
  public JTextAreaOutputStream(JTextArea ta)
  {
    this.ta=ta;
  }
    
  
  public void write (byte b)
  {
    if (Settings.isDebugMode())
      ta.append (String.valueOf ((char) b));
  }

  public void write (String s)
  {
    if (Settings.isDebugMode())
      ta.append (s);
  }

  public void write (Object o)
  {
    if (Settings.isDebugMode())    
      ta.append (o.toString());
  }
  
  
  public void write(int b) throws IOException {
    if (Settings.isDebugMode())
      ta.append (String.valueOf ((char) b));  

  }

  public JTextArea getTextArea() {
    return ta;
  }
} 
