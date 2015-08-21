package me.kingingo.kcore.EncryptionUtil.Streams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
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
public class EncryptedOutputStream extends OutputStream{
protected final OutputStream out;
protected final SecretKey sessionKey;
protected final SymmetricEncryption enc;
private boolean closed = false;

	/**
	 * Writes an String
	 * @param sessionKey The sessionkey used to encrypt/decrypt
	 * @param os The original OutputSream (Unencrypted)
	 * @throws IOException
	 */
	public EncryptedOutputStream(final SecretKey sessionKey,final OutputStream os){
		out = os;
		this.sessionKey = sessionKey;
		enc = SymmetricEncryption.fromCode(sessionKey.getAlgorithm());
	}
	
	/**
	 * Sends an Integer
	 * @param c An Integer which should be written
	 * @throws IOException
	 */
	public final void write(int c) throws IOException{
		write(intToByteArray(c));
	}
	
	/**
	 * Sends an Bytearray
	 * @param b An bytearray which should be written
	 * @throws IOException
	 */
	public final void write(byte[] b) throws IOException{
		write(b,0,b.length);
	}
	
	/**
	 * Sends an bytearray with the choice to automaticly send the length of it
	 * @param b An bytearray which should be written
	 * @param appendLength Indicates wether the length of the bytearray should be written before the actual bytearray. On the reading-side this length has to be read first.
	 * @throws IOException
	 */
	public final void write(byte[] b,boolean appendLength) throws IOException{
		write(b,0,b.length,appendLength);
	}
	
	/**
	 * Sends an total of (len-off) bytes in the bytearray.
	 * @param b An bytearray which should be written
	 * @param off Indicates where to start writing bytes of the bytearray
	 * @param len Indicates till which position the bytes should be written
	 * @throws IOException
	 */
	public final void write(byte[] b,int off,int len) throws IOException{
		write(b,off,len,false);
	}
	
	/**
	 * @param b An bytearray which should be written
	 * @param off Indicates where to start writing bytes of the bytearray
	 * @param len Indicates till which position the bytes should be written
	 * @param appendLength Indicates wether the length of the bytearray should be written before the actual bytearray. On the reading-side this length has to be read first.
	 * @throws IOException
	 */
	private final void write(byte[] b,int off,int len,boolean appendLength) throws IOException{
		try {
			byte[] b2;
			if(len != b.length || off != 0){
				byte[] buffer = new byte[len-off];System.arraycopy(b, off, buffer, 0, len);
				b2 = EncryptionUtil.encrypt(enc, buffer, sessionKey);
			}else{
				b2 = EncryptionUtil.encrypt(enc, b, sessionKey);
			}
			if(appendLength)write(b2.length);
			out.write(b2,0,((int)len/b.length)*b2.length);
//			System.out.println("Schreibe byteArray mit Laenge " + ((int)len/b.length)*b2.length + " (Uhrspruenglich " + b.length + ")");
		} catch (InvalidKeyException | NoSuchAlgorithmException| NoSuchPaddingException | IllegalBlockSizeException| BadPaddingException e) {
			throw new IOException(e.getMessage());
		}
	}
	
	/**
	 * @param s An string which should be written
	 * @throws IOException, UnsupportedEncodingException
	 */
	public final void write(String s) throws UnsupportedEncodingException, IOException{
		write(s,0,s.length());
	}
	
	/**
	 * Writes an char over the network
	 * @param c An char which should be written
	 * @throws IOException
	 */
	public final void write(char c) throws IOException{
		write((int)c);
	}
	
	/**
	 * Writes an chararry over the network
	 * @param c An chararray which should be written.
	 * @throws IOException, UnsupportedEncodingException
	 */
	public final void write(char[] c) throws UnsupportedEncodingException, IOException{
		write(new String(c));
	}
	
	/**
	 * Writes an String
	 * @param s An string which should be written
	 * @param off Indicates at which position writing should be started.
	 * @param len Indicates till which position the string is written
	 * @throws IOException
	 */
	public final void write(String s,int off,int len) throws UnsupportedEncodingException, IOException{
		write(Base64.encode(s.subSequence(off, len).toString().getBytes("UTF-8")).getBytes(),true);
	}
	
	/**
	 * Writes an chararray
	 * @param c An chararry which should be written
	 * @param off Indicates at which position writing should be started.
	 * @param len Indicates till which position the array is written
	 * @throws IOException
	 */
	public final void write(char[] c,int off,int len) throws UnsupportedEncodingException, IOException{
		write(Base64.encode(new String(c).subSequence(off, len).toString().getBytes("UTF-8")).getBytes("UTF-8"),true);
	}
	
	/**
	 * Writes an file over the network.
	 * @param f The file which should be written
	 * @throws IOException
	 */
	public final void writeFile(File f) throws IOException{
		byte[] b = new byte[(int)f.length()];FileInputStream fis = new FileInputStream(f);
		fis.read(b, 0, b.length);write(b,true);fis.close();
	}
	
	/**
	 * Writes an Object by splitting it into bytes.
	 * @param o An Object which should be written.
	 * @throws IOException
	 */
	public final void writeObject(Object o) throws IOException{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();ObjectOutputStream out2 = new ObjectOutputStream(bos);
		out2.writeObject(o);byte[] b = bos.toByteArray();write(b,true);
	}
	
	/**
	 * Flushes the stream, writing everything since the last flush to the target.
	 * @throws IOException
	 */
	public final void flush() throws IOException{
		out.flush();
		super.flush();
	}
	
	/**
	 * Closes the stream.
	 * @throws IOException
	 */
	public final void close() throws IOException{
		out.close();
		closed = true;
		super.close();
	}
	
	public boolean isClosed(){
		return closed;
	}
	
	private final byte[] intToByteArray(int val){
        byte[] buffer = new byte[4];
        buffer[0] = (byte) (val >>> 24);
        buffer[1] = (byte) (val >>> 16);
        buffer[2] = (byte) (val >>> 8);
        buffer[3] = (byte) val;
        return buffer;
	}
}
