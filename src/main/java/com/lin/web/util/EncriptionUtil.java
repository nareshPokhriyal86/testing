package com.lin.web.util;

import java.math.BigInteger;
import java.security.MessageDigest;
/*import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;
import sun.misc.BASE64Encoder;*/


import java.security.SecureRandom;

import org.apache.commons.codec.digest.DigestUtils;

public class EncriptionUtil {
	public static String getEncriptedStrMD5(String password) {
		String encreptedPassword = "";
		MessageDigest md = null;
		final String salt = "$#5yIUMNm12#$";
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] passwordByte = (password+salt).getBytes();
			md.update(passwordByte, 0, passwordByte.length);
			byte[] encodedPassword = md.digest(); 
			encreptedPassword = DigestUtils.md5Hex(encodedPassword);

		} catch (Exception e) {

		}
		return encreptedPassword;
	}

	public static void main(String[] args) {
		//getEncriptedStrMD5("Test");
		System.out.println(new BigInteger(130, new SecureRandom()).toString(32));
	}
/*	
	public static String getDecryptableEncriptedStrAES(String password) {
		String encreptedPassword = "";
		try {
			String plainData="hello",cipherText,decryptedText;
		    KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		    keyGen.init(128);
		    SecretKey secretKey = keyGen.generateKey();
		    Cipher aesCipher = Cipher.getInstance("AES");
		    aesCipher.init(Cipher.ENCRYPT_MODE,secretKey);
		    byte[] byteDataToEncrypt = plainData.getBytes();
		    byte[] byteCipherText = aesCipher.doFinal(byteDataToEncrypt);
		    cipherText = new BASE64Encoder().encode(byteCipherText);
		    aesCipher.init(Cipher.DECRYPT_MODE,secretKey,aesCipher.getParameters());
		    byte[] byteDecryptedText = aesCipher.doFinal(byteCipherText);
		    decryptedText = new String(byteDecryptedText);
		    System.out.println("\n Plain Data : "+plainData+" \n Cipher Data : "+cipherText+" \n Decrypted Data : "+decryptedText);

		} catch (Exception e) {

		}
		return encreptedPassword;
	}*/
	
	
}
