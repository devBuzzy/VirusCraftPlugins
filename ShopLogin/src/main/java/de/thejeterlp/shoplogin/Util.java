/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.thejeterlp.shoplogin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author TheJeterLP
 */
public class Util {

    public static String getMd5Hash(String toHash) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(toHash.getBytes());

            byte[] bytes = digest.digest();

            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                builder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}
