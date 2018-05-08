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

package com.braicu.jcm.card;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JComboBox;


import com.braicu.jcm.card.cap.structure.Applet;
import com.braicu.jcm.card.cap.structure.Header;
import com.braicu.jcm.card.cap.structure.StaticField;
import com.braicu.jcm.card.cap.utils.CAPToIJCInputStream;
import com.braicu.jcm.layout.JTextAreaOutputStream;
import com.braicu.jcm.utils.ByteUtils;
import com.braicu.jcm.utils.Settings;
import javax.smartcardio.*;

/*
 * Class containing all card APDU commands/operations
 */
public class CardWorker {
	private Card card;
	private CardChannel channel;
	private byte[] static_S_ENC = new byte[16];
	private byte[] static_S_MAC = new byte[16];
	private byte[] static_DEK  = new byte[16];


	private byte[] static_S_ENC_24 = new byte[24];
	private byte[] static_S_MAC_24 = new byte[24];
	private byte[] static_DEK_24 = new byte[24];

	private byte[] S_ENC = new byte[16];
	private byte[] S_MAC = new byte[16];
	private byte[] DEK = new byte[16];

	private byte[] S_ENC_24 = new byte[24];
	private byte[] S_MAC_24 = new byte[24];
	private byte[] DEK_24 = new byte[24];

	private JTextAreaOutputStream jtaos;

	private static final String capFiles[] = {
		"Header.cap", "Directory.cap", "Import.cap", "Applet.cap", "Class.cap", "Method.cap", "StaticField.cap", "Export.cap", "ExportDescription.cap", "ConstantPool.cap", 
		"RefLocation.cap", "Descriptor.cap"
	};

	private byte[] capAID;
	private byte[] appletAID;
	private byte[] forLoadData;
//	private byte[] forRegisterData;

//	private byte[] issuerDomain= ByteUtils.stoh("A0 00 00 00 03 00 00 00");

	private static byte[] iv_zero = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x00 };
	private byte[] arrayPAD = { (byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
			(byte) 0x00 };

	private byte SCP;
	private byte[] hostChallenge;
	private byte[] cardChallenge;
	private byte[] sequence;
	private byte[] cardCryptogram;

	private byte[] respAPDU;

	private byte[] C_MAC = new byte[8];

	public CardWorker() {
	}

	public void setTextArea(JTextAreaOutputStream jtaos){
		this.jtaos = jtaos;    
	}


	private boolean authenticate_SCP01(String[] keys, boolean deriveKeys) {

		byte[] cardCryptoData = new byte[24];
		System.arraycopy(hostChallenge, 0, cardCryptoData, 0, hostChallenge.length);
		System.arraycopy(cardChallenge, 0, cardCryptoData, 8, cardChallenge.length);
		System.arraycopy(arrayPAD, 0, cardCryptoData, 16, arrayPAD.length);

		byte[] hostCryptoData = new byte[24];
		System.arraycopy(cardChallenge, 0, hostCryptoData, 0, cardChallenge.length);
		System.arraycopy(hostChallenge, 0, hostCryptoData, 8, hostChallenge.length);
		System.arraycopy(arrayPAD, 0, hostCryptoData, 16, arrayPAD.length);

		byte[] dd = new byte[16];
		System.arraycopy(cardChallenge, (cardChallenge.length / 2), dd, 0, (cardChallenge.length / 2));
		System.arraycopy(hostChallenge, 0, dd, 4, (hostChallenge.length / 2));
		System.arraycopy(hostChallenge, (hostChallenge.length / 2), dd, 12, (hostChallenge.length / 2));
		System.arraycopy(cardChallenge, 0, dd, 8, (cardChallenge.length / 2));

		print("Derivation Data is " + ByteUtils.htos(dd));

		print("Host Cryptogram Data (to encrypt) " + ByteUtils.htos(hostCryptoData));
		print("Card Cryptogram Data (to encrypt for verification) " + ByteUtils.htos(cardCryptoData));
		try {
			Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");

			static_S_ENC = ByteUtils.stoh(keys[0]);
			static_S_MAC = ByteUtils.stoh(keys[1]);
			static_DEK = ByteUtils.stoh(keys[2]);

			System.arraycopy(static_S_ENC, 0, static_S_ENC_24, 0, static_S_ENC.length);
			System.arraycopy(static_S_ENC, 0, static_S_ENC_24, static_S_ENC.length, static_S_ENC.length / 2);

			System.arraycopy(static_S_MAC, 0, static_S_MAC_24, 0, static_S_MAC.length);
			System.arraycopy(static_S_MAC, 0, static_S_MAC_24, static_S_MAC.length, static_S_MAC.length / 2);

			System.arraycopy(static_DEK, 0, static_DEK_24, 0, static_DEK.length);
			System.arraycopy(static_DEK, 0, static_DEK_24, static_DEK.length, static_DEK.length / 2);

			DESedeKeySpec kspec = new DESedeKeySpec(static_S_ENC_24);

			SecretKeyFactory kfact = SecretKeyFactory.getInstance("DESede");
			SecretKey skey = kfact.generateSecret(kspec);

			cipher.init(Cipher.ENCRYPT_MODE, skey);
			S_ENC = cipher.doFinal(dd);
			System.arraycopy(S_ENC, 0, S_ENC_24, 0, S_ENC.length);
			System.arraycopy(S_ENC, 0, S_ENC_24, S_ENC.length, S_ENC.length / 2);
			print("S_ENC key: " + ByteUtils.htos(S_ENC_24));

			kspec = new DESedeKeySpec(static_S_MAC_24);
			kfact = SecretKeyFactory.getInstance("DESede");
			skey = kfact.generateSecret(kspec);

			cipher.init(Cipher.ENCRYPT_MODE, skey);
			S_MAC = cipher.doFinal(dd);
			System.arraycopy(S_MAC, 0, S_MAC_24, 0, S_MAC.length);
			System.arraycopy(S_MAC, 0, S_MAC_24, S_MAC.length, S_MAC.length / 2);
			print("S_MAC key is " + ByteUtils.htos(S_MAC_24));


			kspec = new DESedeKeySpec(S_ENC_24);
			skey = kfact.generateSecret(kspec);
			IvParameterSpec IvSpec = new IvParameterSpec(iv_zero);
			cipher = Cipher.getInstance("DESede/CBC/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, skey, IvSpec);
			byte[] calculatedCardCryptogram = cipher.doFinal(cardCryptoData);
			byte[] calculatedHostCryptogram = cipher.doFinal(hostCryptoData);
			print("Encrypted CardCryptoGram is " + ByteUtils.htos(calculatedCardCryptogram));
			print("Encrypted HostCryptoGram is " + ByteUtils.htos(calculatedHostCryptogram));

			byte[] extauthAPDU1 = new byte[13];
			extauthAPDU1[0] = (byte) 0x84;
			extauthAPDU1[1] = (byte) 0x82;
			extauthAPDU1[2] = (byte) 0x03;
			extauthAPDU1[3] = (byte) 0x00;
			extauthAPDU1[4] = (byte) 0x08;
			System.arraycopy(calculatedHostCryptogram, calculatedHostCryptogram.length - 8, extauthAPDU1, 5, 8);

			C_MAC = generateMAC_SCP01(iv_zero, extauthAPDU1);

			byte[] extauthAPDU = new byte[21];
			System.arraycopy(extauthAPDU1, 0, extauthAPDU, 0, 13);
			extauthAPDU[4] = 0x10;
			System.arraycopy(C_MAC, 0, extauthAPDU, 13, 8);


			respAPDU = cardTransmit(extauthAPDU, 0, extauthAPDU.length);

			if (ByteUtils.htos(respAPDU).trim().equalsIgnoreCase("90 00")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			alwaysPrint(e.getMessage());
			return false;
		}
	}   

	private boolean authenticate_SCP02(String[] keys) {

		print("HostChallenge: " + ByteUtils.htos(hostChallenge));
		print("CardChallenge: " + ByteUtils.htos(cardChallenge));
		print("Card Calculated Card Cryptogram: " + ByteUtils.htos(cardCryptogram));

		byte[] cardCryptoData = new byte[24];
		System.arraycopy(hostChallenge, 0, cardCryptoData, 0, hostChallenge.length);
		System.arraycopy(sequence, 0, cardCryptoData, 8, sequence.length);
		System.arraycopy(cardChallenge, 0, cardCryptoData, 10, cardChallenge.length);
		System.arraycopy(arrayPAD, 0, cardCryptoData, 16, arrayPAD.length);

		byte[] hostCryptoData = new byte[24];
		System.arraycopy(sequence, 0, hostCryptoData, 0, sequence.length);
		System.arraycopy(cardChallenge, 0, hostCryptoData, 2, cardChallenge.length);
		System.arraycopy(hostChallenge, 0, hostCryptoData, 8, hostChallenge.length);
		System.arraycopy(arrayPAD, 0, hostCryptoData, 16, arrayPAD.length);

		byte[] dd = {0x01,(byte)0x82, sequence[0], sequence[1], 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

		print("Derivation Data is " + ByteUtils.htos(dd));

		print("Host Cryptogram Data (to encrypt) " + ByteUtils.htos(hostCryptoData));
		print("Card Cryptogram Data (to encrypt for verification) " + ByteUtils.htos(cardCryptoData));
		try {
			Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding");
			static_S_ENC = ByteUtils.stoh(keys[0]);
			static_S_MAC = ByteUtils.stoh(keys[1]);
			static_DEK = ByteUtils.stoh(keys[2]);

			System.arraycopy(static_S_ENC, 0, static_S_ENC_24, 0, static_S_ENC.length);
			System.arraycopy(static_S_ENC, 0, static_S_ENC_24, static_S_ENC.length, static_S_ENC.length / 2);

			System.arraycopy(static_S_MAC, 0, static_S_MAC_24, 0, static_S_MAC.length);
			System.arraycopy(static_S_MAC, 0, static_S_MAC_24, static_S_MAC.length, static_S_MAC.length / 2);

			System.arraycopy(static_DEK, 0, static_DEK_24, 0, static_DEK.length);
			System.arraycopy(static_DEK, 0, static_DEK_24, static_DEK.length, static_DEK.length / 2);


			DESedeKeySpec kspec = new DESedeKeySpec(static_S_ENC_24);

			cipher = Cipher.getInstance("DESede/CBC/NoPadding");

			SecretKeyFactory kfact = SecretKeyFactory.getInstance("DESede");
			SecretKey skey = kfact.generateSecret(kspec);
			IvParameterSpec IvSpec = new IvParameterSpec(iv_zero);
			cipher.init(Cipher.ENCRYPT_MODE, skey, IvSpec);
			S_ENC = cipher.doFinal(dd);
			System.arraycopy(S_ENC, 0, S_ENC_24, 0, S_ENC.length);
			System.arraycopy(S_ENC, 0, S_ENC_24, S_ENC.length, S_ENC.length / 2);
			print("S_ENC: " + ByteUtils.htos(S_ENC_24));

			dd[1] = (byte) 0x01;
			kspec = new DESedeKeySpec(static_S_MAC_24);
			kfact = SecretKeyFactory.getInstance("DESede");
			skey = kfact.generateSecret(kspec);
			cipher.init(Cipher.ENCRYPT_MODE, skey, IvSpec);            
			S_MAC = cipher.doFinal(dd);
			System.arraycopy(S_MAC, 0, S_MAC_24, 0, S_MAC.length);
			System.arraycopy(S_MAC, 0, S_MAC_24, S_MAC.length, S_MAC.length / 2);
			print("The Current session MAC key is " + ByteUtils.htos(S_MAC));

			dd[1] = (byte) 0x81;
			kspec = new DESedeKeySpec(static_DEK_24);
			kfact = SecretKeyFactory.getInstance("DESede");
			skey = kfact.generateSecret(kspec);
			cipher.init(Cipher.ENCRYPT_MODE, skey, IvSpec);
			DEK = cipher.doFinal(dd);
			System.arraycopy(DEK, 0, DEK_24, 0, DEK.length);
			System.arraycopy(DEK, 0, DEK_24, DEK.length, DEK.length / 2);
			print("The Current session DEK key is " + ByteUtils.htos(DEK));

			kspec = new DESedeKeySpec(S_ENC_24);
			skey = kfact.generateSecret(kspec);
			cipher = Cipher.getInstance("DESede/CBC/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, skey, IvSpec);
			byte[] calculatedCardCryptogram = cipher.doFinal(cardCryptoData);
			byte[] calculatedHostCryptogram = cipher.doFinal(hostCryptoData);
			print("Encrypted CardCryptoGram is " + ByteUtils.htos(calculatedCardCryptogram));
			print("Encrypted HostCryptoGram is " + ByteUtils.htos(calculatedHostCryptogram));

			byte[] extauthAPDU1 = new byte[13];
			extauthAPDU1[0] = (byte) 0x84;
			extauthAPDU1[1] = (byte) 0x82;
			extauthAPDU1[2] = (byte) 0x03;
			extauthAPDU1[3] = (byte) 0x00;
			extauthAPDU1[4] = (byte) 0x08;
			System.arraycopy(calculatedHostCryptogram, calculatedHostCryptogram.length - 8, extauthAPDU1, 5, 8);

			C_MAC = ByteUtils.stoh("00 00 00 00 00 00 00 00");

			C_MAC = generateMAC_SCP02(C_MAC, extauthAPDU1);

			byte[] extauthAPDU = new byte[21];
			System.arraycopy(extauthAPDU1, 0, extauthAPDU, 0, 13);
			extauthAPDU[4] = 0x10;
			System.arraycopy(C_MAC, 0, extauthAPDU, 13, 8);


			respAPDU = cardTransmit(extauthAPDU, 0, extauthAPDU.length);

			if (ByteUtils.htos(respAPDU).trim().equalsIgnoreCase("90 00")) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			alwaysPrint(e.getMessage());
			return false;
		}        
	}

	public boolean authenticate(String[] keys, boolean deriveKeys) throws CardException {
		print("Open terminal ...");

		System.out.println("EstablishContext(): ...");
		TerminalFactory tf = TerminalFactory.getDefault();
		CardTerminals terminals = tf.terminals();
		
		
		
		if (terminals.list().size() == 0){
			alwaysPrint("No reader detected. Make sure reader is avalaiable and start again.");
			return false;
		}

		System.out.println("Wait for card in a certain reader ...");
		System.out.println("Pick reader ...");

		CardTerminal term;
		try {
			term = terminals.list().get(Settings.getSelectedReader());
			card = term.connect("*");
			channel = card.getBasicChannel();

		} catch (CardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		print("**********************");
		print("Selecting Card Manager");
		print("***********************");
		byte[] issuerDomain=ByteUtils.stoh(Settings.props.getProperty("CMAID"));
		byte[] selectAPDU = ByteUtils.stoh("00A404000" + issuerDomain.length + ByteUtils.htos(issuerDomain));
		byte[] respAPDU = cardTransmit(selectAPDU, 0, selectAPDU.length);

		if (!ByteUtils.htos(respAPDU).endsWith("90 00"))
			return false;
	

		print("************");
		print("Init Update");
		print("*************");
		hostChallenge = new byte[8];
		Random rnd = new Random();
		rnd.nextBytes(hostChallenge);

		byte[] initUpdateAPDU = new byte[13];
		initUpdateAPDU[0] = (byte) 0x80;
		initUpdateAPDU[1] = (byte) 0x50;
		initUpdateAPDU[2] = (byte) 0x00;
		initUpdateAPDU[3] = (byte) 0x00;
		initUpdateAPDU[4] = (byte) 0x08;
		System.arraycopy(hostChallenge, 0, initUpdateAPDU, 5, 8);

		respAPDU = cardTransmit(initUpdateAPDU, 0, initUpdateAPDU.length);
		if (!ByteUtils.htos(respAPDU).endsWith("90 00"))
			return false;
		SCP = respAPDU[11];
		boolean auth = false;
		if (SCP==1) {
			cardChallenge = new byte[8];
			cardCryptogram = new byte[8];
			System.arraycopy(respAPDU, 12, cardChallenge, 0, 8);
			System.arraycopy(respAPDU, 20, cardCryptogram, 0, 8);
			auth = authenticate_SCP01(keys, deriveKeys);

		}
		else {
			cardChallenge = new byte[6];
			sequence = new byte[2];
			cardCryptogram = new byte[8];
			System.arraycopy(respAPDU, 12, sequence, 0, 2);
			System.arraycopy(respAPDU, 14, cardChallenge, 0, 6);
			System.arraycopy(respAPDU, 20, cardCryptogram, 0, 8);            
			auth = authenticate_SCP02(keys);
		}



		if (auth) {
			alwaysPrint("Authenticated");
			return true;
		} else {
			alwaysPrint("Could not authenticate. Aborting.");
			return false;
		}
	}

	public boolean getInfo(String[] keys, boolean deriveKeys) {
		try {
			if (!authenticate(keys, deriveKeys))
				return false;

			//alwaysPrint(new String(cardManager.getCard().getATR().getHistoricalChars()));
			alwaysPrint("SCP: 0" + SCP);

			alwaysPrint("Installed applets: ");
			byte[] getinfo;
			if (SCP==1)
				getinfo = prepareForSecureChannel_SCP01(ByteUtils.stoh("84 F2 40 00 02 4F 00"));
			else
				getinfo = prepareForSecureChannel_SCP02(ByteUtils.stoh("84 F2 40 00 02 4F 00"));
			respAPDU = cardTransmit(getinfo, 0, getinfo.length);
			int idx = 0;
			while (idx<respAPDU.length-2) {
				int lengthAID = (byte)(respAPDU[idx] & 0xFF);
				idx++;
				byte[] aid = new byte[(byte)lengthAID];
				System.arraycopy(respAPDU, idx, aid, 0, lengthAID);
				alwaysPrint(ByteUtils.htos(aid) + " (" + new String(aid) + ")");
				idx += lengthAID + 2;
			}

			alwaysPrint("Installed packages: ");
			if (SCP==1)
				getinfo = prepareForSecureChannel_SCP01(ByteUtils.stoh("84 F2 20 00 02 4F 00"));
			else
				getinfo = prepareForSecureChannel_SCP02(ByteUtils.stoh("84 F2 20 00 02 4F 00"));
			respAPDU = cardTransmit(getinfo, 0, getinfo.length);
			idx = 0;
			while (idx<respAPDU.length-2) {
				int lengthAID = (byte)(respAPDU[idx] & 0xFF);
				idx++;
				byte[] aid = new byte[(byte)lengthAID];
				System.arraycopy(respAPDU, idx, aid, 0, lengthAID);
				alwaysPrint(ByteUtils.htos(aid) + " (" + new String(aid) + ")");
				idx += lengthAID + 2;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;

	}

	public boolean resetCard(String[] keys, boolean deriveKeys) {
		try {
			if (!authenticate(keys, deriveKeys))
				return false;

			byte[] getinfo = null;
			if (SCP==1)
				getinfo = prepareForSecureChannel_SCP01(ByteUtils.stoh("84 F2 40 00 02 4F 00"));
			else
				getinfo = prepareForSecureChannel_SCP02(ByteUtils.stoh("84 F2 40 00 02 4F 00"));

			respAPDU = cardTransmit(getinfo, 0, getinfo.length);
			int idx = 0;
			while (idx<respAPDU.length-2) {
				int lengthAID = (byte)(respAPDU[idx] & 0xFF);
				idx++;
				byte[] aid = new byte[(byte)lengthAID];
				System.arraycopy(respAPDU, idx, aid, 0, lengthAID);
				byte[] delAPDU;
				if (SCP==1)
					delAPDU = prepareForSecureChannel_SCP01(getDeleteAPDU(aid));
				else
					delAPDU = prepareForSecureChannel_SCP02(getDeleteAPDU(aid));
				byte[] dr = cardTransmit(delAPDU, 0, delAPDU.length);
				if (ByteUtils.htos(dr).endsWith("90 00"))                    
					alwaysPrint("Deleted APPLET: " + ByteUtils.htos(aid) + " (" + new String(aid) + ")");
				idx += lengthAID + 2;
			}

			if (SCP==1)
				getinfo = prepareForSecureChannel_SCP01(ByteUtils.stoh("84 F2 20 00 02 4F 00"));
			else
				getinfo = prepareForSecureChannel_SCP02(ByteUtils.stoh("84 F2 20 00 02 4F 00"));
			respAPDU = cardTransmit(getinfo, 0, getinfo.length);
			idx = 0;
			while (idx<respAPDU.length-2) {
				int lengthAID = (byte)(respAPDU[idx] & 0xFF);
				idx++;
				byte[] aid = new byte[(byte)lengthAID];
				System.arraycopy(respAPDU, idx, aid, 0, lengthAID);
				byte[] delAPDU;
				if (SCP==1)
					delAPDU = prepareForSecureChannel_SCP01(getDeleteAPDU(aid));
				else
					delAPDU = prepareForSecureChannel_SCP02(getDeleteAPDU(aid));
				byte[] dr = cardTransmit(delAPDU, 0, delAPDU.length);
				if (ByteUtils.htos(dr).endsWith("90 00"))                    
					alwaysPrint("Deleted PACKAGE: " + ByteUtils.htos(aid) + " (" + new String(aid) + ")");                

				idx += lengthAID + 2;
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;

	}

	public void disconnectCard(){
		try {
			channel.close();
			card.disconnect(true);
		}
		catch (Exception ex) {

		}
	}
	public void changekeys(String[] authKeys, String[] newKeys, int keySet, boolean deriveKeys, boolean isAdd)
	throws Exception {
		try {
			if (!authenticate(authKeys, deriveKeys))
				return;

			print("************");
			print("Change Keys");
			print("*************");

			byte[] changeKeys = new byte[72];
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(0x84);
			baos.write(0xD8);
			if (isAdd)
				baos.write(0);
			else
				baos.write(1);
			baos.write((byte) (0x81));
			baos.write(0);
			baos.write(keySet);

			byte tt[] = baos.toByteArray();


			System.arraycopy(tt, 0, changeKeys, 0, tt.length);

			byte nk1[] = new byte[24];
			System.arraycopy(ByteUtils.stoh(newKeys[0]), 0, nk1, 0, 16);
			System.arraycopy(ByteUtils.stoh(newKeys[0]), 0, nk1, 16, 8);
			byte nk2[] = new byte[24];
			System.arraycopy(ByteUtils.stoh(newKeys[1]), 0, nk2, 0, 16);
			System.arraycopy(ByteUtils.stoh(newKeys[1]), 0, nk2, 16, 8);
			byte nk3[] = new byte[24];
			System.arraycopy(ByteUtils.stoh(newKeys[2]), 0, nk3, 0, 16);
			System.arraycopy(ByteUtils.stoh(newKeys[2]), 0, nk3, 16, 8);
			byte[] k1;
			byte[] k2;
			byte[] k3;

			if (SCP == 1) {
				k1 = encodeKey(ByteUtils.htos(nk1), ByteUtils.htos(static_DEK_24));
				k2 = encodeKey(ByteUtils.htos(nk2), ByteUtils.htos(static_DEK_24));
				k3 = encodeKey(ByteUtils.htos(nk3), ByteUtils.htos(static_DEK_24));
			}
			else {
				k1 = encodeKey(ByteUtils.htos(nk1), ByteUtils.htos(DEK_24));
				k2 = encodeKey(ByteUtils.htos(nk2), ByteUtils.htos(DEK_24));
				k3 = encodeKey(ByteUtils.htos(nk3), ByteUtils.htos(DEK_24));
			}

			System.arraycopy(k1, 0, changeKeys, 6, k1.length);
			System.arraycopy(k2, 0, changeKeys, 28, k2.length);
			System.arraycopy(k3, 0, changeKeys, 50, k3.length);

			changeKeys[4] = (byte) (changeKeys.length - 5);
			// changeKeys[72] = (byte) (0x00);
			byte[] changeKeysEnc = null;
			if (SCP==1)
				changeKeysEnc = prepareForSecureChannel_SCP01(changeKeys);
			if (SCP==2)
				changeKeysEnc = prepareForSecureChannel_SCP02(changeKeys);

			byte[] respAPDU = cardTransmit(changeKeysEnc, 0, changeKeysEnc.length);
			if (ByteUtils.htos(respAPDU).trim().endsWith("90 00")) {
				if (isAdd)
					alwaysPrint("Keys added on keyset " + keySet);
				else
					alwaysPrint("Keys changed on keyset " + keySet);
			} else {
				alwaysPrint("Could not add/change keys. Response: " + ByteUtils.htos(respAPDU));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private byte[] generateMAC_SCP01(byte[] iv, byte[] data) throws Exception {

		//padding
		int tmpL = data.length+1;
		while (tmpL%8!=0) {
			tmpL++;
		}

		byte[] dataWithPAD = new byte[tmpL];
		System.arraycopy(data, 0, dataWithPAD, 0, data.length);
		System.arraycopy(arrayPAD, 0, dataWithPAD, data.length, tmpL-data.length);
		//modify Lc with C-MAC length
		dataWithPAD[4] += (byte)8;

		//full 3DES
		Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding");
		SecretKeySpec desKey = new SecretKeySpec(S_MAC_24, "DESede");
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, desKey, ivSpec);

		dataWithPAD = cipher.doFinal(dataWithPAD);
		byte[] cMac = new byte[8];
		System.arraycopy(dataWithPAD, dataWithPAD.length - 8, cMac, 0, 8);
		return cMac;
	}  


	private byte[] prepareForSecureChannel_SCP01(byte[] APDU) {

		try {
			C_MAC = generateMAC_SCP01(C_MAC, APDU);
		} catch (Exception e1) {
			return null;        
		}

		int Lc = APDU[4] & 0xFF;
		Lc = Lc + 8;

		int L = APDU[4] & 0xFF;

		byte[] DataField = new byte[L];
		System.arraycopy(APDU, 5, DataField, 0, L);

		int toEncLength = L + 1;
		int padLength = 0;
		while (toEncLength%8!=0) {
			toEncLength++;
			padLength++;
		}
		byte[] toEnc = new byte[toEncLength];
		toEnc[0] = (byte)L;
		System.arraycopy(DataField, 0, toEnc, 1, L);
		if (padLength > 0 )
			System.arraycopy(arrayPAD, 0, toEnc, L+1, padLength);

		try {
			SecretKeySpec skey = new SecretKeySpec(S_ENC_24, "DESede");
			IvParameterSpec ivSpec = new IvParameterSpec(iv_zero);
			Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding", "SunJCE");
			cipher.init(Cipher.ENCRYPT_MODE, skey, ivSpec);
			toEnc = cipher.doFinal(toEnc);
		} catch (Exception e) {
			return  null;
		}

		byte[] resp  = new byte[5+toEncLength + 8];
		System.arraycopy(APDU, 0, resp, 0, 4);
		resp[4] = (byte)((byte)Lc + 1 + (byte)padLength);
		System.arraycopy(toEnc, 0, resp, 5, toEncLength);
		System.arraycopy(C_MAC, 0, resp, 5 + toEncLength, 8);

		return resp;

	}


	public byte[] generateMAC_SCP02(byte[] iv, byte[] data) throws Exception {

		int l = data[4] & 0xFF;
		l = l + 8;
		//padding
		int tmpL = data.length+1;
		while (tmpL%8!=0) {
			tmpL++;
		}

		byte[] dataWithPAD = new byte[tmpL];
		System.arraycopy(data, 0, dataWithPAD, 0, data.length);
		System.arraycopy(arrayPAD, 0, dataWithPAD, data.length, tmpL-data.length);
		//modify Lc with C-MAC length
		//c-mac generation on modified APDU, just comment this to have c-mac generation on unmodified APDU
		dataWithPAD[4] = (byte)l;



		SecretKeySpec desSingleKey = new SecretKeySpec(S_MAC_24,0, 8,"DES");
		Cipher singleDesCipher = Cipher.getInstance("DES/CBC/NoPadding", "SunJCE");
		int noOfBlocks = dataWithPAD.length/8;
		byte ivForNextBlock[] = iv;
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		int startIndex = 0;
		for(int i=0; i < (noOfBlocks - 1); i++) {
			singleDesCipher.init(Cipher.ENCRYPT_MODE, desSingleKey, ivSpec);
			ivForNextBlock = singleDesCipher.doFinal(dataWithPAD, startIndex, 8);
			startIndex +=8;
			ivSpec = new IvParameterSpec(ivForNextBlock);
		}
		int offset = dataWithPAD.length - 8;

		SecretKeySpec desKey = new SecretKeySpec(S_MAC_24, "DESede");
		Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding", "SunJCE");
		//Generate C-MAC. Use 8-LSB
		//For the last block, you can use TripleDES EDE with ECB mode, now I select the CBC and
		//use the last block of the previous encryption result as ICV
		cipher.init(Cipher.ENCRYPT_MODE, desKey, ivSpec);
		byte cMac[] = cipher.doFinal(dataWithPAD, offset, 8);
		return cMac;
	}  

	private byte[] prepareForSecureChannel_SCP02(byte[] APDU) {
		try {
			SecretKeySpec desSingleKey = new SecretKeySpec(S_MAC,0, 8,"DES");
			Cipher singleDesCipher = Cipher.getInstance("DES/CBC/NoPadding", "SunJCE");
			IvParameterSpec ivSpec = new IvParameterSpec(iv_zero);
			singleDesCipher.init(Cipher.ENCRYPT_MODE, desSingleKey, ivSpec);
			byte[] iv = singleDesCipher.doFinal(C_MAC);      
			C_MAC = generateMAC_SCP02(iv, APDU);

		} catch (Exception e1) {
			return null;        
		}

		int Lc = APDU[4] & 0xFF;
		Lc = Lc + 8;

		int L = APDU[4] & 0xFF;

		byte[] DataField = new byte[L];
		System.arraycopy(APDU, 5, DataField, 0, L);

		int toEncLength = L + 1;
		int padLength = 1;
		while (toEncLength%8!=0) {
			toEncLength++;
			padLength++;
		}
		byte[] toEnc = new byte[toEncLength];
		System.arraycopy(DataField, 0, toEnc, 0, L);
		System.arraycopy(arrayPAD, 0, toEnc, L, toEncLength-L);

		try {
			SecretKeySpec skey = new SecretKeySpec(S_ENC_24, "DESede");
			IvParameterSpec ivSpec = new IvParameterSpec(iv_zero);
			Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding", "SunJCE");
			cipher.init(Cipher.ENCRYPT_MODE, skey, ivSpec);
			toEnc = cipher.doFinal(toEnc);
		} catch (Exception e) {
			return  null;
		}

		byte[] resp  = new byte[5+toEncLength + 8];
		System.arraycopy(APDU, 0, resp, 0, 4);
		resp[4] = (byte)((byte)Lc + (byte)padLength);
		System.arraycopy(toEnc, 0, resp, 5, toEncLength);
		System.arraycopy(C_MAC, 0, resp, 5 + toEncLength, 8);

		return resp;

	}

	public void uploadCAP(File capFile, String[] authKeys, boolean deriveKeys, byte[] registerParam)
	throws Exception {

		boolean allOK = true;

		try {
			if (!authenticate(authKeys, deriveKeys))
				return;

			print("************");
			print("UplaodCAP");
			print("*************");

			if ( !getAIDSfromCAP(capFile) ) {
				alwaysPrint("Aid not valid. Aborting");
				return;
			}
			print("AID:" + ByteUtils.htos(capAID));
			print("Applet AID:" + ByteUtils.htos(appletAID));


			byte[] respAPDU;
			//Attempt to delete if existing appl. with this aid (first application, then applet)
			alwaysPrint("Try to delete if existing...");
			byte[] deleteAPDU;

			if (SCP == 1)
				deleteAPDU = prepareForSecureChannel_SCP01(getDeleteAPDU(appletAID));
			else
				deleteAPDU = prepareForSecureChannel_SCP02(getDeleteAPDU(appletAID));


			respAPDU = cardTransmit(deleteAPDU, 0, deleteAPDU.length);

			if (SCP == 1)
				deleteAPDU = prepareForSecureChannel_SCP01(getDeleteAPDU(capAID));
			else
				deleteAPDU = prepareForSecureChannel_SCP02(getDeleteAPDU(capAID));
			respAPDU = cardTransmit(deleteAPDU, 0, deleteAPDU.length);



			//           if (!authenticate(authKeys, deriveKeys, debugMode))
			//               return;

			alwaysPrint("Loading cap file. Please wait...");


			print("Install for Load");
			byte[] installForLoadAPDU;
			if (SCP == 1)
				installForLoadAPDU = prepareForSecureChannel_SCP01(getInstallForLoadAPDU(capAID));
			else
				installForLoadAPDU = prepareForSecureChannel_SCP02(getInstallForLoadAPDU(capAID));

			respAPDU = cardTransmit(installForLoadAPDU, 0, installForLoadAPDU.length);

			print("Load CAP");
			CAPToIJCInputStream fis = new CAPToIJCInputStream(capFile.getAbsolutePath());


			//            FileOutputStream fos = new FileOutputStream("bb5plus.cap");

			int bufSize = 200;
			int i = fis.available();
			int j = i / bufSize;
			if (i % bufSize >= 1 )
				j++;
			byte[] block = new byte[bufSize];
			for (int count = 0;count < j-1; count ++) {
				fis.read(block);
				//                fos.write(block);
				if (!loadBlock(false, count, block)) {
					allOK = false;
					break;
				}
			}
			if (allOK) {
				int k = i % bufSize;
				if (k == 0)
					k = bufSize;
				block = new byte[k];
				fis.read(block);
				//                fos.write(block);
				allOK = loadBlock(true, j-1, block);
			}

			//            fos.flush();
			//            fos.close();

			byte[] registerAPDU;
			if (SCP == 1)
				registerAPDU = prepareForSecureChannel_SCP01(getRegisterAPDU(capAID, appletAID, registerParam));
			else
				registerAPDU = prepareForSecureChannel_SCP02(getRegisterAPDU(capAID, appletAID, registerParam));

			respAPDU = cardTransmit(registerAPDU, 0, registerAPDU.length);
			//print(ByteUtils.htos(respAPDU));
			if (!ByteUtils.htos(respAPDU).trim().endsWith("90 00"))
				allOK = false;



		} catch (Exception e) {
			alwaysPrint(e.getMessage());
			e.printStackTrace();
			allOK=false;
		}
		if (allOK)
			alwaysPrint("Applet loaded & registered");
		else
			alwaysPrint("Could not load applet. See debug for more info");


	}  


	private boolean loadBlock(boolean last, int count, byte[] b) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(0x84);
		baos.write(0xE8);
		int b1 = 0;
		if (last)
			b1 |= 0x80;
		baos.write(b1);
		baos.write((byte)count);
		baos.write(b.length);
		baos.write(b);
		byte[] loadBlockAPDUEnc = null;
		if (SCP==1)
			loadBlockAPDUEnc = prepareForSecureChannel_SCP01(baos.toByteArray());
		else
			loadBlockAPDUEnc = prepareForSecureChannel_SCP02(baos.toByteArray());

		byte[] respAPDU = cardTransmit(loadBlockAPDUEnc, 0, loadBlockAPDUEnc.length);
		//print("block " + count+ ": " + ByteUtils.htos(respAPDU));
		if (ByteUtils.htos(respAPDU).trim().endsWith("90 00"))
			return true;
		else
			return false;
	}

	private byte[] encodeKey(String key, String _KEKKey) throws Exception {
		ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
		if (SCP==1)
			bytearrayoutputstream.write(0x81);
		else
			bytearrayoutputstream.write(0x80);

		bytearrayoutputstream.write(0x10);
		Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
		SecretKeySpec kKey = new SecretKeySpec(ByteUtils.stoh(_KEKKey), "DESede");
		cipher.init(1, kKey);
		bytearrayoutputstream.write(cipher.doFinal(ByteUtils.stoh(key), 0, 16));
		bytearrayoutputstream.write(3);
		SecretKeySpec ky = new SecretKeySpec(ByteUtils.stoh(key), "DESede");
		cipher.init(1, ky);
		byte abyte0[] = cipher.doFinal(iv_zero);
		bytearrayoutputstream.write(abyte0, 0, 3);
		return bytearrayoutputstream.toByteArray();
	}

	private void print(String s) {
			System.out.println(s);
	}

	private void alwaysPrint(String s) {
		String olds = Settings.props.getProperty("DebugMode");
		Settings.props.setProperty("DebugMode", "1");
		System.out.println(s);
		Settings.props.setProperty("DebugMode", olds);	
	}

	private boolean getAIDSfromCAP(File capf)
	{
		print("Get AID from header.cap file");
		try {
			JarFile jarfile = new JarFile(capf);
			int size1 = 0;
			int size2 = 0;
			for(Enumeration enumeration = jarfile.entries(); enumeration.hasMoreElements();)
			{
				JarEntry jarentry = (JarEntry)enumeration.nextElement();
				for(int i = 0; i < capFiles.length; i++)
					if(jarentry.getName().lastIndexOf(capFiles[i]) >= 0)
						size1 += jarentry.getSize();
				if(jarentry.getName().lastIndexOf(capFiles[6]) >= 0)
				{
					StaticField staticfield = new StaticField(jarfile.getInputStream(jarentry));
					size2 += staticfield.getReferenceCount() * 8;
					size2 += staticfield.getNonDefaultValuesSize();
					size2 += staticfield.getDefaultValueCount();
				} else

					if(jarentry.getName().lastIndexOf(capFiles[0]) >= 0)
					{
						Header header = new Header(jarfile.getInputStream(jarentry));
						capAID = header.getPackageInfo().getAID();
					}
				if(jarentry.getName().lastIndexOf(capFiles[3]) >= 0)
				{
					Applet applet = new Applet(jarfile.getInputStream(jarentry));
					appletAID = applet.getAID(0).getAid();
				}

			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.reset();
			baos.write((byte)0xEF);
			baos.write((byte)0x04);
			baos.write((byte)0xC6);
			baos.write((byte)0x02);
			baos.write(ByteUtils.intToBytes(size1 + size2));
			forLoadData = baos.toByteArray();
			alwaysPrint("FOR LOAD DATA: " + ByteUtils.htos(forLoadData));
/*
			baos.reset();
			baos.write(0xEF);
			baos.write(4);
			baos.write(0xC8);
			byte abyte0[] = ByteUtils.intToBytes(size1 + size2);
			baos.write(2);
			if(abyte0.length == 1)
				baos.write(0);
			baos.write(abyte0);
			baos.write(0xC9);
			baos.write(1);
			baos.write(0);
			forRegisterData = baos.toByteArray();          
*/

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}


	protected byte[] getInstallForLoadAPDU(byte[] aid) {
		int i = 0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try
		{
			baos.reset();
			baos.write(0x84);
			baos.write(0xE6);
			baos.write(2);
			baos.write(0);
			baos.write(0);
			baos.write((byte)aid.length);
			i++;
			baos.write(aid);
			i += aid.length;
			
			byte[] issuerDomain=ByteUtils.stoh(Settings.props.getProperty("CMAID"));

			if(issuerDomain != null)
			{
				baos.write((byte)issuerDomain.length);
				baos.write(issuerDomain);
				i += (byte)(issuerDomain.length + 1);
			} else
			{
				baos.write(0);
				i++;
			}

			baos.write(0);
			i++;

		//	if(forLoadData != null)
		//	{
		//		baos.write((byte)forLoadData.length);
		//		baos.write(forLoadData);
		//		i += forLoadData.length + 1;
		//	} else
		//	{
				baos.write(0);
				i++;
		//	}

			baos.write(0);
			i++;
		}
		catch(IOException ioexception)
		{
			throw new RuntimeException(ioexception.getMessage());
		}

		byte[] installForLoadAPDU = baos.toByteArray();
		installForLoadAPDU[4] = (byte)(i);
		return installForLoadAPDU;
	}



	public byte[] getDeleteAPDU(byte[] aid)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try
		{
			baos.write(0x84);
			baos.write(0xE4);
			baos.write(0);
			baos.write(0x00);
			baos.write((byte)(2 + aid.length));
			baos.write(0x4F);
			baos.write(aid.length);
			baos.write(aid);
			//   baos.write(0x00);
		}
		catch(IOException ioexception)
		{
			throw new RuntimeException(ioexception.getMessage());
		}

		return baos.toByteArray();
	}


	public byte[] getRegisterAPDU(byte[] aid, byte[] appletAid, byte[] parameters) {
		int dataLength = 0;
		ByteArrayOutputStream  baos = new ByteArrayOutputStream();
		try
		{
			baos.write(0x84);
			baos.write(0xE6);
			baos.write(0x0C);
			baos.write(0);
			baos.write(dataLength);
			if(aid != null)
			{
				baos.write((byte)aid.length);
				dataLength++;
				baos.write(aid);
				dataLength += aid.length;
				baos.write((byte)appletAid.length);
				dataLength++;
				baos.write(appletAid);
				dataLength += appletAid.length;
			} else
			{
				baos.write(0);
				baos.write(0);
				dataLength += 2;
			}
			baos.write((byte)appletAid.length);
			dataLength++;
			baos.write(appletAid);
			dataLength += appletAid.length;
			baos.write(1);
			dataLength++;
			baos.write(0);
			dataLength++;

			baos.write(1 + 1 + parameters.length);
			dataLength++;
			baos.write((byte)0xC9);
			dataLength++;
			baos.write(parameters.length);
			dataLength++;
			baos.write(parameters);            
			dataLength += parameters.length;
			baos.write(0);
			dataLength++;
		}
		catch(IOException ioexception)
		{
			throw new RuntimeException(ioexception.getMessage());
		}
		byte[] registerAPDU = baos.toByteArray();
		registerAPDU[4] = (byte)dataLength;
		return registerAPDU;
	}

	public String[] getReaders() throws CardException {
		TerminalFactory tf = TerminalFactory.getDefault();
		CardTerminals terminals = tf.terminals();
		terminals.list();
		List<CardTerminal> list = terminals.list(CardTerminals.State.ALL);
		String[] sa = new String[list.size()];
		for (int i=0; i<list.size(); i++) {
			sa[i] = list.get(i).getName();
		}	
		return sa;
	}
	
	
	private byte[] cardTransmit(byte[] apdu, int p, int apduLength) {
		byte[] resp = null;
		try {
			CommandAPDU command = new CommandAPDU(apdu);
			
			print("-> " + ByteUtils.htos(apdu));
			ResponseAPDU response;
			response = channel.transmit(command);
			resp = response.getBytes();
			print("<- " + ByteUtils.htos(resp));
		} catch (CardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;

		
		
	}

}