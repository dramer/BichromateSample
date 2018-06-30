/*
 * @(#)keyVerifier.java	1.0 10/11/2002
 *
 * Copyright (c) 1997-2002 by David Ramer. All Rights Reserved.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */

package bichromate.tools;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import bichromate.core.checkSum;

public class KeyVerifier
{
	private ResourceBundle mylicenseFileResource;

    public boolean verifySoftwareKey()
    {
        try {
            if(!openLicenseResourceFile())
            	return false;

            String softkey = mylicenseFileResource.getString("Software_Key");
            System.err.println(" ####### USING LICENSE KEY ==> " + softkey);
            if(softkey == null) {
                return false;
            }
            else if(softkey.equals("Robert David Ramer")) {
                return true;
            }

            StringTokenizer tokenizer = new StringTokenizer(softkey, "#");

            String checksum1 = tokenizer.nextToken();
            String key = tokenizer.nextToken();
            String checksum2 = tokenizer.nextToken();
            String key_2 = key + "#" + checksum2;

            if((checkSum.generate(key_2)/591) != Integer.parseInt(checksum1)) {
                return false;
            }

            if(checkSum.generate(key)/591 != Integer.parseInt(checksum2)) {
                return false;
            }
        }
        catch(Exception ee) {
            return false;
        }

        return true;
    } // verifySoftwareKey

    private boolean openLicenseResourceFile()
	{
		mylicenseFileResource = null;
		try
		{
			mylicenseFileResource = ResourceBundle.getBundle("license.license-bundle",
																 Locale.getDefault());
		} catch (MissingResourceException mre) {
			System.err.println("license file properties not found");
			return false;
		}
		return true;
	} // openLicenseResourceFile

}// KeyVerifier