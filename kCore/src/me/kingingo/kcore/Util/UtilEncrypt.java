package me.kingingo.kcore.Util;

import java.io.*;
import java.security.*;
import java.util.Arrays;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class UtilEncrypt {

	 /** Verschluesseln */
	   public static void encrypt( String srcFile, String encryptedDstFile, String algorithm, String password )
	         throws GeneralSecurityException, IOException
	   {
	      encrypt( new FileInputStream( srcFile ), new FileOutputStream( encryptedDstFile ), algorithm, password );
	   }

	   /** Verschluesseln (Streams werden mit close() geschlossen) */
	   public static void encrypt( InputStream inpStream, OutputStream encryptedOutStream, String algorithm, String password )
	         throws GeneralSecurityException, IOException
	   {
	      SecretKey secKey = new SecretKeySpec( hashPwd( password, algorithm ), algorithm );
	      Cipher    cipher = Cipher.getInstance( algorithm );
	      byte[]    byteBuffer = new byte[64 * 1024];
	      int       n;
	      cipher.init( Cipher.ENCRYPT_MODE, secKey );
	      CipherOutputStream cos = new CipherOutputStream( encryptedOutStream, cipher );
	      try {
	         while( (n = inpStream.read( byteBuffer )) > 0 ) { cos.write( byteBuffer, 0, n ); }
	      } finally {
	         cos.close();
	         encryptedOutStream.close();
	         inpStream.close();
	      }
	   }

	   /** Entschluesseln */
	   public static void decrypt( String encryptedSrcFile, String decryptedDstFile, String algorithm, String password )
	         throws GeneralSecurityException, IOException
	   {
	      decrypt( new FileInputStream( encryptedSrcFile ), new FileOutputStream( decryptedDstFile ), algorithm, password );
	   }

	   /** Entschluesseln (Streams werden mit close() geschlossen) */
	   public static void decrypt( InputStream encryptedInpStream, OutputStream decryptedOutStream, String algorithm, String password )
	         throws GeneralSecurityException, IOException
	   {
	      SecretKey secKey = new SecretKeySpec( hashPwd( password, algorithm ), algorithm );
	      Cipher    cipher = Cipher.getInstance( algorithm );
	      byte[]    byteBuffer = new byte[64 * 1024];
	      int       n;
	      cipher.init( Cipher.DECRYPT_MODE, secKey );
	      CipherInputStream cis = new CipherInputStream( encryptedInpStream, cipher );
	      try {
	         while( (n = cis.read( byteBuffer )) > 0 ) { decryptedOutStream.write( byteBuffer, 0, n ); }
	      } finally {
	         cis.close();
	         encryptedInpStream.close();
	         decryptedOutStream.close();
	      }
	   }

	   /** MD5-Hash */
	   private static byte[] hashPwd( String password, String algorithm ) throws NoSuchAlgorithmException, UnsupportedEncodingException{
	      MessageDigest md = MessageDigest.getInstance( "MD5" );
	      md.update( password.getBytes( "ISO-8859-1" ) );
	      return ( "DES".equals( algorithm ) ) ? Arrays.copyOf( md.digest(), 8 ) : md.digest();
	   }
	
}
