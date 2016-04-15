package eu.epicpvp.kcore.EncryptionUtil.Util;

import java.util.Arrays;

public class Base64 {
//	private static BASE64Encoder encoder = new BASE64Encoder();
//	private static BASE64Decoder decoder = new BASE64Decoder();
	
	
	public static String encode(String s) {
		return encode(s.getBytes());
	}

	public static String encode(byte[] source) {
		return java.util.Base64.getEncoder().encodeToString(source);
		//return DatatypeConverter.printBase64Binary(source);
		//return encode(source, 0, source.length);
	}

	public static String encode(byte[] source, int off, int len) {
		//return InnerBase64.encode(source, off, len);
		//return encoder.encode(Arrays.copyOfRange(source, off, len));
		//return DatatypeConverter.printBase64Binary(Arrays.copyOfRange(source, off, len));
		return java.util.Base64.getEncoder().encodeToString(Arrays.copyOfRange(source, off, len));
		//return org.bittec.EncryptionUtil.references.Base64.getEncoder().encodeToString(Arrays.copyOfRange(source, off, len));
	}

	public static byte[] decode(String s) {
		//return InnerBase64.decode(s);
		/*try{
			return decoder.decodeBuffer(s);
		}catch(Exception e){
			e.printStackTrace();
		}
		return new byte[]{};*/
		//return DatatypeConverter.parseBase64Binary(s);
		return java.util.Base64.getDecoder().decode(s);
		//return org.bittec.EncryptionUtil.references.Base64.getDecoder().decode(s);
	}
}