package eu.epicpvp.kcore.EncryptionUtil.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.SecretKey;

import eu.epicpvp.kcore.EncryptionUtil.Encryptions.HashEncryption;
import eu.epicpvp.kcore.EncryptionUtil.Encryptions.SymmetricEncryption;


public class EncryptedFileHeader {
	long fileLength;
	final SymmetricEncryption fEnc;
	final String kHash;
	
	public static EncryptedFileHeader read(final InputStream in) throws IOException{
		byte[] buffer = new byte[8];in.read(buffer);
		long fLen = ByteCaster.reverseToLong(buffer);int encInt,hashLen;buffer = new byte[4];in.read(buffer);
		encInt = ByteCaster.reverseToInt(buffer);in.read(buffer);hashLen = ByteCaster.reverseToInt(buffer);
		buffer = new byte[hashLen];in.read(buffer);
		return new EncryptedFileHeader(fLen, SymmetricEncryption.fromNumberCode(encInt), new String(buffer,"UTF-8"));
	}
	
	private EncryptedFileHeader(long fLen,SymmetricEncryption enc,String keyHash){
		this.fEnc = enc;
		this.fileLength = fLen;
		this.kHash = keyHash;
	}
	
	public boolean matches(final SecretKey k) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		return new String(EncryptionUtil.createHash(HashEncryption.MD5, k).getBytes("UTF-8"),"UTF-8").equalsIgnoreCase(kHash);
	}
	
	public static byte[] generate(final SecretKey key,final File encrypted) throws IOException, NoSuchAlgorithmException{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] hash = EncryptionUtil.createHash(HashEncryption.MD5, key).getBytes("UTF-8");
		out.write(ByteCaster.cast(encrypted.length()));
		out.write(SymmetricEncryption.fromCode(key.getAlgorithm()).numberCode());
		out.write(hash.length);out.write(hash);out.flush();out.close();
		return out.toByteArray();
	}
	
	public static byte[] generate(final SecretKey key,final long fileLen) throws IOException, NoSuchAlgorithmException{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] hash = EncryptionUtil.createHash(HashEncryption.MD5, key).getBytes("UTF-8");
		out.write(ByteCaster.cast(fileLen));
		out.write(ByteCaster.cast(SymmetricEncryption.fromCode(key.getAlgorithm()).numberCode()));
		out.write(ByteCaster.cast(hash.length));out.write(hash);out.flush();out.close();
		return out.toByteArray();
	}
	
	public static byte[] generate(final SecretKey key,final byte[] encrypted) throws IOException, NoSuchAlgorithmException{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] hash = EncryptionUtil.createHash(HashEncryption.MD5, key).getBytes("UTF-8");
		out.write(ByteCaster.cast((long)encrypted.length));
		out.write(ByteCaster.cast(SymmetricEncryption.fromCode(key.getAlgorithm()).numberCode()));
		out.write(ByteCaster.cast(hash.length));out.write(hash);out.flush();out.close();
		byte[] b = out.toByteArray();
		return b;
	}
	
	public long getFileLength(){
		return fileLength;
	}
	
}
