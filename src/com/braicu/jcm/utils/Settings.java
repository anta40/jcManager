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
package com.braicu.jcm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Properties;

public class Settings {
	
    public static SortedProperties props;
    private static String PROPERTIES_FILE = "jcManager.config"; 

    private static int selectedReader = -1;
    
    public static int getSelectedReader() {
		return selectedReader;
	}

	public static void setSelectedReader(int selectedReader) {
		Settings.selectedReader = selectedReader;
	}

	public static void initContext(){
        try {
            props = new SortedProperties();
            File f = new File(PROPERTIES_FILE);
            FileInputStream fos;
            fos = new FileInputStream(f);
            props.load(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }      
    }
 
    public static void saveContext() {
        File f = new File(PROPERTIES_FILE);
        try {
        FileWriter fw = new FileWriter(f);
        props.store(fw,"");
        }
        catch(Exception e){
        	e.printStackTrace();
        }
    }
    
    public static boolean isDebugMode(){
    	if (props.getProperty("DebugMode").equalsIgnoreCase("1"))
    		return true;
    	return false;
    }
}
