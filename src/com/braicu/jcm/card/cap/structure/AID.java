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

public class AID {

	private byte[] aid;

	/**
	 * @param aid
	 */
	public AID(byte[] aid) {
		super();
		this.aid = aid;
	}


	public AID(InputStream inputstream)
	throws IOException
	{
		aid = null;
		byte byte0 = (byte)(inputstream.read() & 0xff);
		aid = new byte[byte0];
		if(inputstream.read(aid) != byte0)
			throw new IOException("Expecting AID length " + byte0);
		else
			return;
	}


	public byte[] getAid() {
		return aid;
	}
	public void setAid(byte[] aid) {
		this.aid = aid;
	}
}
