/*
 * @(#)keyGenerator.java	1.0 10/11/2002
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


import java.security.MessageDigest;

import java.io.File;
import java.io.FileOutputStream;

class KeyGenerator {
    //
    // given an input string, a serial number is returned
    //
    public String generate(String input) throws java.security.NoSuchAlgorithmException
    {
        return computeSoftwareKey(MessageDigest.getInstance("MD5"), input);
    }

    //
    // Given a serial number the input string is returned.
    //
    public String extractInputString(String licenseFile)
    {
        return null;
    }

    private int generateChecksum(final String string)
    {
        int checksum = 0;
        final int stringLength = string.length();

        for (int index = 0; index < stringLength; index++) {
            checksum = 31*checksum + string.charAt(index);
        }

        return checksum;
    }

    private String computeSoftwareKey (MessageDigest algorithm, String input) {
        if (input != null) {
            byte[] content = input.getBytes();
            algorithm.reset();
            algorithm.update (content);

            byte digest[] = algorithm.digest();
            StringBuffer hexString = new StringBuffer();

            int digestLength = digest.length;

            for (int i=0;i<digestLength;i+=3) {
                hexString.append (hexDigit(digest[i]));

                if(i%4 == 0)
                    hexString.append ("-");
            }

            String cstr = hexString.toString() + "#" + generateChecksum(hexString.toString())/591;

            cstr = generateChecksum(cstr)/591 + "#" + cstr;

            return cstr;
        } else {

            return "";
        }
    }

    private String hexDigit(byte x) {
        StringBuffer sb = new StringBuffer();

        char c;

        // First nibble
        c = (char) ((x >> 4) & 0xf);

        if (c > 9) {
          c = (char) ((c - 10) + 'a');
        } else {
          c = (char) (c + '0');
        }

        sb.append (c);

        // Second nibble
        c = (char) (x & 0xf);

        if (c > 9) {
          c = (char)((c - 10) + 'a');
        } else {
          c = (char)(c + '0');
        }

        sb.append (c);

        return sb.toString();

    }

    //
    // TEST DRIVER
    //
    public static void main(String args[]) {
        try {
            if(args.length < 1) {
                System.err.println("USAGE: java KeyGenerator \"<input_string>\"");
                return;
            }

            KeyGenerator factory = new KeyGenerator();

            System.err.println(" *** Generating key for Registration_name ==> " + args[0]);

            String key = factory.generate(args[0]);
            File outputFile = new File("\\license\\license-bundle.properties");
            FileOutputStream fileStream = new FileOutputStream(outputFile);
            byte[] EOL = {(byte) '\r', (byte) '\n'};

            fileStream.write(
                    new String("/* file license-bundle.properties, OpenLaneQADBTool License Information */").getBytes());
            fileStream.write(EOL);
            fileStream.write(new String("Registration_Name=" + args[0]).getBytes());
            fileStream.write(EOL);
            fileStream.write(new String("Software_Key=").getBytes());
            fileStream.write(key.getBytes());
            fileStream.write(EOL);

            fileStream.close();
        }
        catch(Exception e) {
        }
    }
}