package me.kingingo.kcore.EncryptionUtil.Util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

public class ByteCaster {

	public static byte[] cast(Object o) throws IOException{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out2 = new ObjectOutputStream(bos);out2.writeObject(o);
		return bos.toByteArray();
	}
	
	public static byte[] cast(int val){
        byte[] buffer = new byte[4];
        buffer[0] = (byte) (val >>> 24);
        buffer[1] = (byte) (val >>> 16);
        buffer[2] = (byte) (val >>> 8);
        buffer[3] = (byte) val;
        return buffer;
	}
	
	public static byte[] cast(long val){
		byte[] b = new byte[8];
		ByteBuffer buffer = ByteBuffer.wrap(b);
	    buffer.putLong(val);
	    return b;
	}
	
	public static Object reverseToObject(byte[] b) throws IOException, ClassNotFoundException{
		ByteArrayInputStream bis = new ByteArrayInputStream(b);
		ObjectInputStream in = new ObjectInputStream(bis);
		return in.readObject();
	}
	
	public static int reverseToInt(byte[] buffer){
		return ByteBuffer.wrap(buffer).getInt();
	}
	
	public static long reverseToLong(byte[] buffer){
	    return ByteBuffer.wrap(buffer).getLong();
	}
}
