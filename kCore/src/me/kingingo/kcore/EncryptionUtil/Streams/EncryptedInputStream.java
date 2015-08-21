package me.kingingo.kcore.EncryptionUtil.Streams;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import me.kingingo.kcore.EncryptionUtil.Encryptions.SymmetricEncryption;
import me.kingingo.kcore.EncryptionUtil.Util.Base64;
import me.kingingo.kcore.EncryptionUtil.Util.EncryptionUtil;

/**
 * @author Ch4t4r | Daniel Wolf
 * @license Free for commercial use. Do not distribute.
 */
public class EncryptedInputStream extends InputStream{
	protected final InputStream in;
	protected final SecretKey sessionKey;
	protected final SymmetricEncryption enc;
	private boolean closed = false;
	
	public EncryptedInputStream(final SecretKey sessionKey,final InputStream in){
		this.in = in;
		this.sessionKey = sessionKey;
		enc = SymmetricEncryption.fromCode(sessionKey.getAlgorithm());
	}
	
	public final int read(byte b[]) throws IOException{
		return read(b,0,b.length);
	}
	
	public final byte[] readWithAppend() throws IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException{
		byte[] b = new byte[read()];
		in.read(b,0,b.length);
		b = EncryptionUtil.decrypt(enc, b, sessionKey);
		return b;
	}
	
	public final int read(byte b[],int off,int len) throws IOException{
		in.read(b, off, len);
		try {
			b = EncryptionUtil.decrypt(enc, b, sessionKey);
//			System.out.println("Lese byteArray mit Laenge " + b.length + " (Urspruenglich " + len + ")");
		} catch (InvalidKeyException | NoSuchAlgorithmException| NoSuchPaddingException | IllegalBlockSizeException| BadPaddingException e) {
			throw new IOException(e.getMessage());
		}
		return b.length;
	}
	
	public final String readString() throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException{
		byte[] b = new byte[read()];in.read(b);
		return new String(Base64.decode(new String(EncryptionUtil.decrypt(enc, b, sessionKey),"UTF-8")),"UTF-8");
	}
	
	public final char readChar() throws IOException{
		return (char)read();
	}
	
	public final char[] readCharArray() throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException{
		return readString().toCharArray();
	}
	
	public final void readFile(File saveFile) throws IOException{
		FileOutputStream fos = new FileOutputStream(saveFile);
		byte[] b = new byte[read()];read(b);fos.write(b);
		fos.close();
	}
	
	public final Object readObject() throws IOException, ClassNotFoundException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException{
		byte[] b = readWithAppend();
		ByteArrayInputStream bis = new ByteArrayInputStream(b);
		ObjectInputStream in = new ObjectInputStream(bis);
		return in.readObject();
	}
	
	public final long skip(long n) throws IOException{
		return in.skip(n);
	}
	
	public final int available() throws IOException{
		return in.available();
	}
	
	public final void close() throws IOException{
		in.close();
		closed = true;
		super.close();
	}
	
	public final synchronized void mark(int readlimit){
		in.mark(readlimit);
		super.mark(readlimit);
	}
	
	public final synchronized void reset() throws IOException{
		in.reset();
		super.reset();
	}
	
	public final boolean markSupported(){
		return in.markSupported();
	}

	@Override
	public final int read() throws IOException {
		try {
			byte[] b = new byte[8];in.read(b);b = EncryptionUtil.decrypt(enc, b, sessionKey);
			return byteArrayToInt(b);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException(e.getMessage());
		}
	}
	
	public final static String byteArrayToString(byte[] arr){
		String s = "";
		for(byte b: arr)s += b;
		return s;
	}
	
	public boolean isClosed(){
		return closed;
	}
	
	private final int byteArrayToInt(byte[] buffer){
		 if (buffer.length != 4) {
	            throw new IllegalArgumentException("buffer length must be 4 bytes!");
	     }
	     int value  = (0xFF & buffer[0]) << 24 ;
	     value |= (0xFF & buffer[1]) << 16;
	     value |= (0xFF & buffer[2]) << 8;
	     value |= (0xFF & buffer[3]);
	     return value;
	}
}
