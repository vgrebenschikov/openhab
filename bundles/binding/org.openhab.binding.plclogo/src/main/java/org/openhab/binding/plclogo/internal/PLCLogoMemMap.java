package org.openhab.binding.plclogo.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*******************
 * Utility Class to help with memory mapping
 * @author g8kmh
 * @since 1.5.0
 *
 */
public class PLCLogoMemMap {
	private int[] retval = new int[2]; // bit location[0] and real mem loc[1]

	private static final Logger logger =
			LoggerFactory.getLogger(PLCLogoBinding.class);

	public int[] convertToReal(String memloc) {
		retval[0] = -1;
		retval[1] = -1;
		// I , Q and M have bit values derived: I1 is equivalent to VB923.0
		// TODO Add some validation to input parameters!
		
		if (memloc.length() < 2)
			return null;

		String mt; // normalizaed memory type VB|VW|I|Q|M|AO|AQ|AM
		int idx;   // normalized index of element (starting from 0), for all but VB|VW
		if (Character.isDigit(memloc.charAt(1)))
		{
			mt = memloc.substring(0,1).toUpperCase();
			idx = Integer.parseInt(memloc.substring(1)) - 1;
		}
		else
		{
			mt = memloc.substring(0,2).toUpperCase();
			idx = Integer.parseInt(memloc.substring(2)) - 1;
		}

		if (mt.equals("VB") || mt.equals("VW")) {
			retval[1] = Integer.parseInt(memloc.substring(2));
		} else
		if (mt.equals("I")) {
			// I starts at 923 for three bytes
			retval[1] = 923 + idx/8;
			retval[0] = idx%8;
		} else
		if (mt.equals("Q")) {
			// Q starts at 942 for two bytes
			retval[1] = 942 + idx/8;
			retval[0] = idx%8;
		} else
		if (mt.equals("M")) {
			// Markers starts at 948 for two bytes
			retval[1] = 948 + idx/8;
			retval[0] = idx%8;
		} else
		if (mt.equals("AI")) {
			// AI starts at 926 for 8 words
			retval[1] =  926 + idx*2;
		} else
		if (mt.equals("AQ")) {
			// AQ starts at 944 for 2 words
			retval[1] =  944 + idx*2;
		} else
		if (mt.equals("AM")) {
			// AM starts at 952 for 16 words
			retval[1] =  952 + idx*2;
		}

		logger.debug("Memory map for " + memloc + " = " + retval[1] + ((retval[0] != -1)?("." + retval[0]):""));
		return retval;
	}

	
	
	
}
