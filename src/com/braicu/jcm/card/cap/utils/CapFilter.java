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
package com.braicu.jcm.card.cap.utils;

import java.io.File;
import javax.swing.filechooser.*;

public class CapFilter extends FileFilter {

    // Accept all directories and all cap files.
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

	if (f != null) {
            if (f.getName().toLowerCase().endsWith(".cap")) {
                    return true;
            } else {              
                return false;
            }
    	}

        return false;
    }

    // The description of this filter
    public String getDescription() {
        return "*.cap (CAP files)";
    }



}
