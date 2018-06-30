//LearnCom, Inc.
// Copyright 1998-1999 LearnCom, Inc. All rights reserved.

//NOTICE: All information contained herein or attendant hereto is, and
//remains, the property of LearnCom, Inc. Many of the
//intellectual and technical concepts contained herein are
//proprietary to LearnCom, Inc. and may be covered by U.S. and
//Foreign Patents or Patents Pending, or are protected as trade
//secrets. Any dissemination of this information or
//reproduction of this material is strictly forbidden unless
//prior written permission is obtained from LearnCom, Inc.

//--------------------------------------------------------------------
//Original Author: Ronald M. Jacobs
// Original Date: November 21, 1999
//--------------------------------------------------------------------

/* $History: Checksum.java $
*
* *****************  Version 1  *****************
* User: Rjacobs      Date: 11/22/99   Time: 10:46a
* Created in $/gforce/elearnserver/latest/src/com/gforce/server/util
* Initial check in
*/
package bichromate.core;

public final class checkSum
{
 private checkSum()
 {
 }

 public static int generate(final String string)
 {
     int checksum = 0;
     final int stringLength = string.length();

     for (int index = 0; index < stringLength; index++) {
         checksum = 31*checksum + string.charAt(index);
     }

     return checksum;
 }
}