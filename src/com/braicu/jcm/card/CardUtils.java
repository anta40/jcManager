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
 * @author: Stefan Braicu
 */

package com.braicu.jcm.card;


public final class CardUtils
{

	/*
	 * List of APDU responses according to the GP specification
	 */
    public static String decodeSW(int i)
    {
        switch(i)
        {
        case 0x6100: 
            return "Response Bytes Remaining";

        case 0x6283: 
            return "Selected Applet blocked, or the Card Managed is locked";

        case 0x6301: 
            return "Success Packet";

        case 0x6310: 
            return "More data available";

        case 0x6400: 
            return "Insufficient Memory";

        case 0x6681: 
            return "Bad Master PIN";

        case 0x6700: 
            return "Wrong Length";

        case 0x6901: 
            return "Invalid AID Length";

        case 0x6902: 
            return "Invalid API Version";

        case 0x6903: 
            return "Invalid Password";

        case 0x6904: 
            return "Invalid Signature Length";

        case 0x6905: 
            return "Hash Corruption";

        case 0x6906: 
            return "Hash Failure";

        case 0x6982: 
            return "Invalid Signature";

        case 0x6984: 
            return "Data Invalid";

        case 0x6985: 
            return "Conditions Of Use Not Satisfied";

        case 0x6999: 
            return "No more applets are selected";

        case 0x6A80: 
            return "Wrong Data";

        case 0x6A81: 
            return "Function Not Supported";

        case 0x6A82: 
            return "Unable to Select Applet";

        case 0x6A84: 
            return "Class Length Overrun";

        case 0x6A86: 
            return "Invalid Loader Command";

        case 0x6A87: 
            return "Incomplete Packet";

        case 0x6B00: 
            return "Incorrect Parameters (P1,P2)";

        case 0x6C00: 
            return "Incorrect Expected Length";

        case 0x6D00: 
            return "INS Value Not Supported";

        case 0x6E00: 
            return "Wrong Class Byte (CLA)";

        case 0x6F00: 
            return "Uncaught Exception";

        case 0x8450: 
            return "Unable to Find Applet";

        case 0x8453: 
            return "Unable to Select Applet";

        case 0x9000: 
            return "Success";
        }
        if(i >= 27648 && i < 27904)
        {
            int j = i - 27648;
            return "Incorrect Expected Length (Le).  Expected " + Integer.toHexString(j);
        }
        if(i >= 24832 && i < 25088)
            return Integer.toHexString(i - 24832) + " bytes remaining to read";
        StringBuffer stringbuffer = new StringBuffer("Unrecognized SW ");
        if(i < 4096)
            stringbuffer.append('0');
        if(i < 256)
            stringbuffer.append('0');
        if(i < 16)
            stringbuffer.append('0');
        stringbuffer.append(Integer.toHexString(i));
        return stringbuffer.toString();
    }

}
