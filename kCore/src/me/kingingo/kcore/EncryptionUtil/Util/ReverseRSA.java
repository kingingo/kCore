package me.kingingo.kcore.EncryptionUtil.Util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import me.kingingo.kcore.EncryptionUtil.Encryptions.AsymmetricEncryption;
import me.kingingo.kcore.EncryptionUtil.Encryptions.HashEncryption;
import me.kingingo.kcore.EncryptionUtil.Exceptions.InvalidDataException;
import me.kingingo.kcore.EncryptionUtil.Exceptions.WrongKeyException;


public final class ReverseRSA { //Best practice for maximum safety: Use a hardcoded key
	protected final RSAPublicKey puKey;
	protected final RSAPrivateKey prKey;
	protected final Thread usedThread;
	
	public ReverseRSA(final PublicKey publicKey,final PrivateKey privateKey) throws InvalidKeySpecException, NoSuchAlgorithmException{
		puKey = (RSAPublicKey) publicKey;
		prKey = (RSAPrivateKey) privateKey;
		usedThread = Thread.currentThread();
	}
	
	public final byte[] encrypt(final byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		Cipher c = Cipher.getInstance(AsymmetricEncryption.RSA.toString());
		c.init(Cipher.ENCRYPT_MODE, prKey);
		return c.doFinal(data);
	}
	
	public final byte[] decrypt(final byte[] data) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		Cipher c = Cipher.getInstance(AsymmetricEncryption.RSA.toString());
		c.init(Cipher.DECRYPT_MODE, puKey);
		return c.doFinal(data);
	}
	
	public final void encrypt(final File in,final File out) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
		final byte[] b = new byte[(int) in.length()];
		final FileInputStream inStream = new FileInputStream(in);
		inStream.read(b);
		final FileOutputStream outStream = new FileOutputStream(out);
		byte[] data = encryptBlocked(b);
		new ReverseFileHeader(data, puKey).writeTo(outStream);
		outStream.write(data);
		inStream.close();outStream.close();
	}
	
	public final void decrypt(final File in,final File out) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, ClassNotFoundException, WrongKeyException, InvalidDataException{
		final FileInputStream inStream = new FileInputStream(in);
		ReverseFileHeader rfh = readHeaderFrom(inStream);
		final byte[] b = new byte[(int)inStream.available()];
		inStream.read(b);
		rfh.checkAll(puKey, b);
		final FileOutputStream outStream = new FileOutputStream(out);
		decryptBlocked(b, outStream);
		inStream.close();outStream.close();
	}
	
	public final byte[] encrypt(final File in) throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		final byte[] b = new byte[(int) in.length()];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		final FileInputStream inStream = new FileInputStream(in);
		inStream.read(b);inStream.close();
		ReverseFileHeader rfh = new ReverseFileHeader(b, puKey);rfh.writeTo(out);out.write(encryptBlocked(b));
		return out.toByteArray();
}
	
	public final byte[] decrypt(final File in) throws InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, ClassNotFoundException, WrongKeyException, InvalidDataException{
		final FileInputStream inStream = new FileInputStream(in);
		ReverseFileHeader rfh = readHeaderFrom(inStream);
		final byte[] b = new byte[(int)inStream.available()];
		inStream.read(b);rfh.checkAll(puKey, b);inStream.close();
		return decryptBlocked(b);
	}
	
	public final MemoryClassLoader loadClasses(final File jarFile) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, WrongKeyException, InvalidDataException{
		JarInputStream in = new JarInputStream(new ByteArrayInputStream(decrypt(jarFile)));//Block-like decryption ;
		JarEntry je;
		MemoryClassLoader loader = new MemoryClassLoader();
        while((je=in.getNextJarEntry())!=null){
        	if(je.isDirectory() || !je.getName().endsWith(".class"))continue;
        	loader.setContents(readTillEnd(in),jarFile);
        	loader.loadClass(je.getName().substring(0,je.getName().length()-6).replace("/", "."));//Try to load the class
        }
        in.close();
        return loader;
	}
	
	public final MemoryClassLoader loadClasses(final File jarFile,final MemoryClassLoader loader) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, WrongKeyException, InvalidDataException{
		JarInputStream in = new JarInputStream(new ByteArrayInputStream(decrypt(jarFile)));//Block-like decryption ;
		JarEntry je;
        while((je=in.getNextJarEntry())!=null){
        	if(je.isDirectory() || !je.getName().endsWith(".class"))continue;
        	loader.setContents(readTillEnd(in),jarFile);
        	loader.loadClass(je.getName().substring(0,je.getName().length()-6).replace("/", "."));//Try to load the class
        }
        in.close();
        return loader;
	}
	
	private final byte[] readTillEnd(final InputStream in) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		while(true){
			int q = in.read();
			if(q == -1)break;
			out.write(q);
		}
		return out.toByteArray();
	}
	
	private final byte[] loadAll(final File f) throws IOException{
		byte[] b = new byte[(int)f.length()];
		FileInputStream in = new FileInputStream(f);
		in.read(b);in.close();
		return b;
	}
	
	public void desc(File f) throws IOException{
		ZipInputStream in = new ZipInputStream(new ByteArrayInputStream(loadAll(f)));
		ZipEntry z;
		byte[] buffer = new byte[251658240];
		while((z = in.getNextEntry()) != null){
			in.read(buffer);
			buffer = trim(buffer);
//			print(buffer);
			System.out.println("ENTRY: " + z.getName() +", COMPRESSED: " + z.getCompressedSize() + " UNCOMPRESSED: " + z.getSize() + " READ: " + buffer.length);
			buffer = new byte[251658240];
		}
		in.close();
	}
	
	public void print(byte[] b){
		for(byte b2 : b)System.out.println(b2);
	}
	
	public final MemoryClassLoader loadAllClasses(final File folder) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, IOException, WrongKeyException, InvalidDataException{
		final MemoryClassLoader loader = new MemoryClassLoader();
		for(File f: folder.listFiles()){
			if(f.isDirectory())loadAllClasses(f, loader);
			else loadClasses(f, loader);
		}
		return loader;
	}
	
	public final MemoryClassLoader loadAllClasses(final File folder,final MemoryClassLoader loader) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, IOException, WrongKeyException, InvalidDataException{
		for(File f:folder.listFiles()){
			if(f.isDirectory())loadAllClasses(folder, loader);
			else loadClasses(f,loader);
		}
		return loader;
	}

	public static byte[] trim(byte[] bytes){
	    int i = bytes.length - 1;
	    while (i >= 0 && bytes[i] == 0)
	    {
	        --i;
	    }

	    return Arrays.copyOf(bytes, i + 1);
	}

	
	public final MemoryClassLoader loadClasses(final byte[] jarFile,final String originPath) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException, ClassNotFoundException{
		JarInputStream in = new JarInputStream(new ByteArrayInputStream(decrypt(jarFile)));
		JarEntry je;
		MemoryClassLoader loader = new MemoryClassLoader();byte[] buffer;
        while((je=in.getNextJarEntry())!=null){
        	if(je.isDirectory() || !je.getName().endsWith(".class"))continue;
        	buffer = new byte[(int) je.getSize()];in.read(buffer);
        	loader.setContents(buffer,originPath);
        	loader.loadClass(je.getName().substring(0,je.getName().length()-6).replace("/", "."));
        }
        in.close();
        return loader;
	}
	
	public final void encryptBlocked(final byte[] b,final OutputStream out,final int blockSize) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException{
		ByteArrayInputStream in = new ByteArrayInputStream(b);
		byte[] buffer = new byte[blockSize > b.length ? b.length : blockSize];
		while(in.available() != 0){
			if(in.available() < b.length)buffer = new byte[in.available()];
			in.read(buffer);
			out.write(encrypt(buffer));out.flush();
		}
		in.close();out.close();
	}
	
	public final byte[] encryptBlocked(final byte[] b,final int blockSize) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException{
		ByteArrayInputStream in = new ByteArrayInputStream(b);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[blockSize > b.length ? b.length : blockSize];
		while(in.available() != 0){
			if(in.available() < buffer.length)buffer = new byte[in.available()];
			in.read(buffer);
			out.write(encrypt(buffer));out.flush();
		}
		in.close();out.close();out.close();
		return out.toByteArray();
	}
	
	public final byte[] decryptBlocked(final byte[] b,final int blockSize) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		ByteArrayInputStream in = new ByteArrayInputStream(b);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[blockSize];
		while(in.available() != 0){
			if(buffer.length > in.available())buffer = new byte[in.available()];
			in.read(buffer);out.write(decrypt(buffer));out.flush();
		}
		in.close();out.close();
		return out.toByteArray();
	}
	
	public final void decryptBlocked(final byte[] b,final OutputStream out,final int blockSize) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		ByteArrayInputStream in = new ByteArrayInputStream(b);
		byte[] buffer = new byte[blockSize];
		while(in.available() != 0){
			if(buffer.length > in.available())buffer = new byte[in.available()];
			in.read(buffer);out.write(decrypt(buffer));out.flush();
		}
		in.close();out.close();
	}
	
	public final void encryptBlocked(final byte[] b,final OutputStream out) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException{
		int blockSize = ((puKey.getModulus().bitLength()/8)-11);
		ByteArrayInputStream in = new ByteArrayInputStream(b);
		byte[] buffer = new byte[blockSize > b.length ? b.length : blockSize];
		while(in.available() != 0){
			if(in.available() < buffer.length)buffer = new byte[in.available()];
			in.read(buffer);
			out.write(encrypt(buffer));out.flush();
		}
		in.close();out.close();
	}
	
	public final byte[] encryptBlocked(final byte[] b) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException{
		int blockSize = ((puKey.getModulus().bitLength()/8)-11);
		ByteArrayInputStream in = new ByteArrayInputStream(b);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[blockSize > b.length ? b.length : blockSize];
		while(in.available() != 0){
			if(in.available() < buffer.length)buffer = new byte[in.available()];
			in.read(buffer);
			out.write(encrypt(buffer));out.flush();
		}
		in.close();out.close();out.close();
		return out.toByteArray();
	}
	
	public final byte[] decryptBlocked(final byte[] b) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		int blockSize = ((puKey.getModulus().bitLength()/8));
		ByteArrayInputStream in = new ByteArrayInputStream(b);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[blockSize];
		while(in.available() != 0){
			if(buffer.length > in.available())buffer = new byte[in.available()];
			in.read(buffer);out.write(decrypt(buffer));out.flush();
		}
		in.close();out.close();
		return out.toByteArray();
	}
	
	public final void decryptBlocked(final byte[] b,final OutputStream out) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		int blockSize = ((puKey.getModulus().bitLength()/8));
		ByteArrayInputStream in = new ByteArrayInputStream(b);
		byte[] buffer = new byte[blockSize];
		while(in.available() != 0){
			if(buffer.length > in.available())buffer = new byte[in.available()];
			in.read(buffer);out.write(decrypt(buffer));out.flush();
		}
		in.close();out.close();
	}

	protected final ReverseFileHeader readHeaderFrom(final InputStream in) throws IOException{
		byte[] buff = new byte[4];in.read(buff);
		final byte[] decLen = new byte[ByteCaster.reverseToInt(buff)];in.read(decLen);
		final String dec = new String(decLen,"UTF-8");
		in.read(buff);
		final byte[] hash = new byte[ByteCaster.reverseToInt(buff)];in.read(hash);
		return new ReverseFileHeader(dec, hash);
	}
	
	protected final class ReverseFileHeader{ //Header for checking if a file was altered, this is no explicit safety
		protected final String decHash;
		protected final byte[] dataHash;
		
		protected ReverseFileHeader(final byte[] data,final PublicKey readKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException{
			this(EncryptionUtil.createHash(HashEncryption.MD5, readKey.getEncoded()),encryptBlocked(EncryptionUtil.createHash(HashEncryption.MD5,data).getBytes("UTF-8")));
		}
		
		protected ReverseFileHeader(final String decHash,final byte[] dataHash){
//			if(Thread.currentThread() != usedThread)throw new IllegalStateException("Async FileHeader Creation!"); //For safety no async calls are possible.
			this.decHash = decHash;this.dataHash = dataHash;
		}
		
		protected final boolean checkKey(final PublicKey key) throws UnsupportedEncodingException, NoSuchAlgorithmException{
			return decHash.equalsIgnoreCase(EncryptionUtil.createHash(HashEncryption.MD5, key.getEncoded()));
		}
		
		protected final boolean checkHash(final byte[] data) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException{
			return Arrays.equals(decryptBlocked(dataHash),EncryptionUtil.createHash(HashEncryption.MD5,data).getBytes("UTF-8"));
		}
		
		protected final void writeTo(final OutputStream out) throws UnsupportedEncodingException, IOException{
//			if(Thread.currentThread() != usedThread)throw new IllegalStateException("Async FileHeader Serialization!");
			out.write(ByteCaster.cast(decHash.getBytes("UTF-8").length));
			out.write(decHash.getBytes("UTF-8"));
			out.write(ByteCaster.cast(dataHash.length));
			out.write(dataHash);
			out.flush();
		}
		
		protected final boolean checkAll(final PublicKey key,final byte[] data) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, WrongKeyException, InvalidDataException{
			if(!checkKey(key))throw new WrongKeyException("[ReverseRSA] The PublicKey given can't decrypt the data.");
			else if(!checkHash(data))throw new InvalidDataException("The Data does not match the hash given in the file's header!");
			return true;
		}
	}
}
