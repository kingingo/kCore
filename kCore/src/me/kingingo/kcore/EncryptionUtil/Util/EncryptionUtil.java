package me.kingingo.kcore.EncryptionUtil.Util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import me.kingingo.kcore.EncryptionUtil.Encryptions.AsymmetricEncryption;
import me.kingingo.kcore.EncryptionUtil.Encryptions.HashEncryption;
import me.kingingo.kcore.EncryptionUtil.Encryptions.SignatureEncryption;
import me.kingingo.kcore.EncryptionUtil.Encryptions.SymmetricEncryption;
import me.kingingo.kcore.EncryptionUtil.Exceptions.KeysAlreadyGeneratedException;
import me.kingingo.kcore.EncryptionUtil.Exceptions.LockFileNotFound;
import me.kingingo.kcore.EncryptionUtil.Exceptions.WrongKeyException;
import me.kingingo.kcore.EncryptionUtil.Exceptions.WrongStoragePasswordException;



/**
 * @author Ch4t4r | Daniel Wolf
 * @license Free for commercial use. Do not distribute.
 */
public class EncryptionUtil {
	  protected final File asymKeystore;
	  protected PublicKey publicKey;
	  protected PrivateKey privateKey;
	  protected final char[] pw;
	  protected static final SecureRandom rand = new SecureRandom();
	  
	  public EncryptionUtil(final String password,final File storeFolder) throws InvalidKeyException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException, LockFileNotFound, WrongStoragePasswordException, WrongKeyException {
		  if(!storeFolder.exists())storeFolder.mkdirs();
		  asymKeystore = storeFolder;
		  pw = password.toCharArray();
		  if(keysPresent()){
			  final SecretKey k = getKey(SymmetricEncryption.Blowfish, pw);
			  publicKey = (PublicKey)loadObjectDecrypted(SymmetricEncryption.Blowfish,new File(storeFolder,"public.key"),k); 
			  privateKey = (PrivateKey)loadObjectDecrypted(SymmetricEncryption.Blowfish,new File(storeFolder,"private.key"),k);  
		  }else if(publicKeyPresent()){
			  final SecretKey k = getKey(SymmetricEncryption.Blowfish, pw);
			  publicKey = (PublicKey)loadObjectDecrypted(SymmetricEncryption.Blowfish,new File(storeFolder,"public.key"),k); 
		  }else if(privateKeyPresent()){
			  final SecretKey k = getKey(SymmetricEncryption.Blowfish, pw);
			  privateKey = (PrivateKey)loadObjectDecrypted(SymmetricEncryption.Blowfish,new File(storeFolder,"private.key"),k);  
		  }
		  System.gc();
	  }
	  
	  public static boolean testPassword(final String password, final File file,final SymmetricEncryption storeEncryption){
		  try {
			  loadObjectDecrypted(storeEncryption, file, getKey(storeEncryption, password.toCharArray()));
		  } catch (Exception e) {
			  return false;
		  }
		  return true;
	  }
	  
	  public final void generateKeyPair(final AsymmetricEncryption encType,final int keySizeBits) throws InvalidKeyException, IllegalBlockSizeException, NoSuchAlgorithmException, NoSuchPaddingException, IOException, BadPaddingException, KeysAlreadyGeneratedException {
	    	if(keysPresent())throw new KeysAlreadyGeneratedException("Keys already generated.");
		    System.out.println("\tGenerating an " + keySizeBits + " bit " + encType.name() + " key. This may take a while.");
	    	final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(encType.toString());
		    keyGen.initialize(keySizeBits);
		    final KeyPair key = keyGen.generateKeyPair();
		    final SecretKey sk = getKey(SymmetricEncryption.Blowfish, pw);
		    System.out.println("\tKeys generated, saving...");
			saveObjectEncrypted(SymmetricEncryption.Blowfish,new File(asymKeystore,"public.key"),sk,key.getPublic());
			saveObjectEncrypted(SymmetricEncryption.Blowfish,new File(asymKeystore,"private.key"),sk,key.getPrivate());
			publicKey = key.getPublic();
			privateKey = key.getPrivate();
			System.out.println("\tThe Keys have been generated and saved.");
	  }
	  
	  public final void generatePublicKey(final AsymmetricEncryption encType,final int keySizeBits) throws NoSuchAlgorithmException, KeysAlreadyGeneratedException, InvalidKeyException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, IOException{
		  if(publicKeyPresent())throw new KeysAlreadyGeneratedException("The publickey was already generated.");
		  final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(encType.toString());
		  keyGen.initialize(keySizeBits);
		  final KeyPair key = keyGen.generateKeyPair();
		  final SecretKey sk = getKey(SymmetricEncryption.Blowfish, pw);
		  saveObjectEncrypted(SymmetricEncryption.Blowfish,new File(asymKeystore,"public.key"),sk,key.getPublic());
		  publicKey = key.getPublic();
	  }
	  
	  public final void generatePrivateKey(final AsymmetricEncryption encType,final int keySizeBits) throws NoSuchAlgorithmException, KeysAlreadyGeneratedException, InvalidKeyException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, IOException{
		  if(privateKeyPresent())throw new KeysAlreadyGeneratedException("The privatekey was already generated.");
		  final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(encType.toString());
		  keyGen.initialize(keySizeBits);
		  final KeyPair key = keyGen.generateKeyPair();
		  final SecretKey sk = getKey(SymmetricEncryption.Blowfish, pw);
		  saveObjectEncrypted(SymmetricEncryption.Blowfish,new File(asymKeystore,"private.key"),sk,key.getPrivate());
		  privateKey = key.getPrivate();
	  }
	  
	  public static void setBlockSize(){
		  
	  }
	  
	  public final boolean keysPresent(){
		  return new File(asymKeystore,"public.key").exists() && new File(asymKeystore,"private.key").exists();
	  }
	  
	  public final boolean keysPresent(File path){
		  return new File(path,"public.key").exists() && new File(path,"private.key").exists();
	  }
	  
	  public final boolean publicKeyPresent(){
		  return new File(asymKeystore,"public.key").exists();
	  }
	  
	  public final boolean privateKeyPresent(){
		  return new File(asymKeystore,"private.key").exists();
	  }
	  
	  public final boolean publicKeyPresent(File path){
		  return new File(path,"public.key").exists();
	  }
	  
	  public final boolean privateKeyPresent(File path){
		  return new File(path,"private.key").exists();
	  }
	  
	  /* ####################################################
	   * ENCRYPTION START
	   * ####################################################
	   */
	  
	  public static final byte[] encrypt(final AsymmetricEncryption enc,final byte[] content, final PublicKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
	    	final Cipher cipher = Cipher.getInstance(enc.toString());
		    cipher.init(Cipher.ENCRYPT_MODE, key);
		    return cipher.doFinal(content);
	  }
		  
	  public static final byte[] encrypt(final SymmetricEncryption enc,final byte[] content,final SecretKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
			  final Cipher cipher = Cipher.getInstance(enc.toString());
			  cipher.init(Cipher.ENCRYPT_MODE, key);
			  return cipher.doFinal(content);
	  }
	  
	  public static final byte[] decrypt(final AsymmetricEncryption enc,final byte[] text,final PrivateKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		  final Cipher cipher = Cipher.getInstance(enc.toString());
		  cipher.init(Cipher.DECRYPT_MODE, key);
		  return cipher.doFinal(text);
	  }
	  
	  public static final byte[] decrypt(final SymmetricEncryption enc,final byte[] text,final SecretKey key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		  final Cipher cipher = Cipher.getInstance(enc.toString());
		  cipher.init(Cipher.DECRYPT_MODE, key);
		  return cipher.doFinal(text);
	  }
	  
	  
	  
//	  public static final int encryptInteger(final SymmetricEncryption enc,final int i,final SecretKey key) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException{
//		  return fromByteArray(encrypt(enc, intToByteArray(i), key));
//	  }
	  
	  /* ####################################################
	   * ENCRYPTION END
	   * ####################################################
	   */
	  
	  public final PublicKey getPublicKey(){
		  return publicKey;
	  }
	  
	  public final PrivateKey getPrivateKey(){
		  return privateKey;
	  }
	  
	  /* ####################################################
	   * SIGNATURE START
	   * ####################################################
	   */
	  
	  public static final SignatureContext generateSignatureContext(final SignatureEncryption se,final PrivateKey signKey,final String signText) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException, UnsupportedEncodingException{
			 Signature sig = Signature.getInstance(se.toString());
			 sig.initSign(signKey);
			 sig.update(signText.getBytes("UTF-8"));
			 byte[] signBytes = sig.sign();
			 return new SignatureContext(signText, Base64.encode(sig.sign()),se);
	  }
	  
	  public static final Signature generateSignature(final SignatureEncryption se,final PrivateKey signKey,final String signText) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException, UnsupportedEncodingException{
			 Signature sig = Signature.getInstance(se.toString());
			 sig.initSign(signKey);
			 sig.update(signText.getBytes("UTF-8"));
			 return sig;
	  }
	  
	  public static final boolean verifySignature(final SignatureEncryption se,final String signText,final byte[] sigBytes,final PublicKey publicKey) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, UnsupportedEncodingException{
		  Signature sig = Signature.getInstance(se.toString());
		  sig.initVerify(publicKey);
		  sig.update(signText.getBytes("UTF-8"));
		  return sig.verify(sigBytes);
	  }
	  
	  public static final boolean verifySignature(final SignatureContext context,final SignatureEncryption se,final PublicKey pk) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, UnsupportedEncodingException{
		  Signature sig = Signature.getInstance(se.toString());
		  sig.initVerify(pk);
		  sig.update(context.getMessage().getBytes("UTF-8"));
		  return sig.verify(Base64.decode(context.getSignature()));
	  }
	  
	  /* ####################################################
	   * SIGNATURE END
	   * ####################################################
	   */
	  
	  
	  /* ####################################################
	   * OBJECTENCRYPTION START
	   * ####################################################
	   */
	  
	  public static final void saveObjectEncrypted(final SymmetricEncryption se,final File output,final SecretKey pk,final Serializable o) throws IOException, IllegalBlockSizeException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException {
		  FileOutputStream fos = new FileOutputStream(output);
		  byte[] b = encrypt(se, ByteCaster.cast(o), pk);
		  fos.write(EncryptedFileHeader.generate(pk, b));
		  fos.write(b,0,b.length);fos.flush();fos.close();
	  }
	  
	  public static final Object loadObjectDecrypted(final SymmetricEncryption se,final File input,final SecretKey pk) throws ClassNotFoundException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, LockFileNotFound, WrongKeyException {
		  final FileInputStream in = new FileInputStream(input);
		  final EncryptedFileHeader header = EncryptedFileHeader.read(in);
		  if(!header.matches(pk)){
			  throw new WrongKeyException("The given SecretKey cannot decipher the given file.");
		  }else{
			  byte[] buffer = new byte[(int)header.getFileLength()];in.read(buffer);
			  buffer = decrypt(se, buffer, pk);
			  return ByteCaster.reverseToObject(buffer);
		  }
	  }
	  
	  public static final void writeObject(final AsymmetricEncryption ae,final PublicKey key,final Serializable o,final ObjectOutputStream out) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException {
		  final Cipher cipher = Cipher.getInstance(ae.toString());
		  cipher.init(Cipher.ENCRYPT_MODE, key);
		  final SealedObject sealedObject = new SealedObject(o,cipher);
		  out.writeObject(sealedObject);out.flush();
	  }
	  
	  public static final Object readObject(final AsymmetricEncryption ae,final ObjectInputStream in,final PrivateKey pk) throws ClassNotFoundException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		  final Cipher cipher = Cipher.getInstance(ae.toString());
		  cipher.init(Cipher.DECRYPT_MODE, pk);
		  final SealedObject sealedObject = (SealedObject)in.readObject();
		  return sealedObject.getObject(cipher);
	  }
	  
	  public static final void writeObject(final SymmetricEncryption ae,final SecretKey key,final Serializable o,final ObjectOutputStream out) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException {
		  final Cipher cipher = Cipher.getInstance(ae.toString());
		  cipher.init(Cipher.ENCRYPT_MODE, key);
		  final SealedObject sealedObject = new SealedObject(o,cipher);
		  out.writeObject(sealedObject);out.flush();
	  }
	  
	  public static final Object readObject(final SymmetricEncryption ae,final ObjectInputStream in,final SecretKey pk) throws ClassNotFoundException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		  final Cipher cipher = Cipher.getInstance(ae.toString());
		  cipher.init(Cipher.DECRYPT_MODE, pk);
		  final SealedObject sealedObject = (SealedObject)in.readObject();
		  return sealedObject.getObject(cipher);
	  }
	  
	  public static final Object decryptObject(final SymmetricEncryption se,final SealedObject object,final SecretKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException{
		  final Cipher cipher = Cipher.getInstance(se.toString());
		  cipher.init(Cipher.DECRYPT_MODE, key);
		  return object.getObject(cipher);
	  }
	  
	  public static final SealedObject encryptObject(final SymmetricEncryption se,final Serializable object,final SecretKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException{
		  final Cipher cipher = Cipher.getInstance(se.toString());
		  cipher.init(Cipher.ENCRYPT_MODE, key);
		  return new SealedObject(object, cipher);
	  }
	  
	  public static final Object decryptObject(final AsymmetricEncryption se,final SealedObject object,final PrivateKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException{
		  final Cipher cipher = Cipher.getInstance(se.toString());
		  cipher.init(Cipher.DECRYPT_MODE, key);
		  return object.getObject(cipher);
	  }
	  
	  public static final SealedObject encryptObject(final AsymmetricEncryption se,final Serializable object,final PublicKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException{
		  final Cipher cipher = Cipher.getInstance(se.toString());
		  cipher.init(Cipher.ENCRYPT_MODE, key);
		  return new SealedObject(object, cipher);
	  }
	  
	  /* ####################################################
	   * OBJECTENCRYPTION END
	   * ####################################################
	   */
	  /* ####################################################
	   * BLOCKENCRYPTION START
	   * ####################################################
	   */
	  
	  public static final void encryptBlocked(SymmetricEncryption se,SecretKey key,File in,File out,int blockSize,Informer i) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		  FileInputStream fin = new FileInputStream(in);FileOutputStream fout = new FileOutputStream(out);
		  byte[] b = new byte[fin.available() < blockSize ? fin.available() : blockSize],buffer;
		  while(fin.available() != 0){
			  if(fin.available() < b.length)b = new byte[fin.available()];
			  fin.read(b);buffer = encrypt(se, b, key);fout.write(buffer);
			  i.onProgressUpdate((int)(100 - (((double)fin.available()/(double)in.length())*100)));
		  }
		  fin.close();fout.flush();fout.close();
	  }
	  
	  public static final void decryptBlocked(SymmetricEncryption se,SecretKey key,File in,File out,int blockSize,Informer i) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		  int adjusted = (int)(blockSize + (8*((double)((double)blockSize/(double)se.getBlocksize()) - ((int)(blockSize/se.getBlocksize())))));
		  FileInputStream fin = new FileInputStream(in);FileOutputStream fout = new FileOutputStream(out);
		  byte[] b = new byte[fin.available() < adjusted ? fin.available() : adjusted];
		  while(fin.available() != 0){
			  if(fin.available() < b.length)b = new byte[fin.available()];
			  fin.read(b);fout.write(decrypt(se, b, key));
			  i.onProgressUpdate((int)(100 - (((double)fin.available()/(double)in.length())*100)));
		  }
		  fin.close();fout.flush();fout.close();
	  }
	  
//	  public static final byte[][] encryptBlocked(SymmetricEncryption se,SecretKey key,int blockSize,Informer i) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
//		  byte[] b = new byte[fin.available() < blockSize ? fin.available() : blockSize],buffer;
//		  while(fin.available() != 0){
//			  if(fin.available() < b.length)b = new byte[fin.available()];
//			  fin.read(b);buffer = encrypt(se, b, key);fout.write(buffer);
//			  i.onProgressUpdate((int)(100 - (((double)fin.available()/(double)in.length())*100)));
//		  }
//		  fin.close();fout.flush();fout.close();
//	  }
	  
	  /* ####################################################
	   * BLOCKENCRYPTION END
	   * ####################################################
	   */
	  
	  /* ####################################################
	   * FILEENCRYPTION START
	   * ####################################################
	   */
	  
	  public static final void encryptFile(final SymmetricEncryption se,final SecretKey key,final File input,final File output) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		  final FileInputStream fis = new FileInputStream(input);
		  byte[] b = new byte[(int)input.length()]; fis.read(b);
		  final FileOutputStream out = new FileOutputStream(output);
		  byte[] enc = encrypt(se, b, key);
		  out.write(EncryptedFileHeader.generate(key, enc));
		  out.write(enc);fis.close();out.close();
	  }
	  
	  public static final void decryptFile(final SymmetricEncryption se,final SecretKey key,final File input,final File output) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, IOException, WrongKeyException, ClassNotFoundException{
		  final FileInputStream in = new FileInputStream(input);
		  final EncryptedFileHeader header = EncryptedFileHeader.read(in);
		  if(!header.matches(key)){
			  throw new WrongKeyException("The given SecretKey cannot decipher the given file.");
		  }else{
			  byte[] b = new byte[(int)header.getFileLength()];
			  in.read(b);
			  final FileOutputStream fos = new FileOutputStream(output);
			  fos.write(decrypt(se, b, key));fos.flush();
			  in.close();fos.close();
		  }
	  }
	  
	  /* ####################################################
	   * FILEENCRYPTION END
	   * ####################################################
	   */
	  /* ####################################################
	   * HASH START
	   * ####################################################
	   */
	  
	  public static final String createHash(final HashEncryption se,final String plainText) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		  MessageDigest md = MessageDigest.getInstance(se.toString());
		  byte[] hash = md.digest(plainText.getBytes("UTF-8"));
		  StringBuilder sb = new StringBuilder(2*hash.length);
          for(byte b : hash)sb.append(String.format("%02x", b&0xff));
          return sb.toString();
	  }
	  
	  public static final String createHash(final HashEncryption se,final SecretKey key) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		  MessageDigest md = MessageDigest.getInstance(se.toString());
		  byte[] hash = md.digest(key.getEncoded());
		  StringBuilder sb = new StringBuilder(2*hash.length);
          for(byte b : hash)sb.append(String.format("%02x", b&0xff));
          return sb.toString();
	  }
	  
	  public static final String createHash(final HashEncryption se,final byte[] buffer) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		  MessageDigest md = MessageDigest.getInstance(se.toString());
		  byte[] hash = md.digest(buffer);
		  StringBuilder sb = new StringBuilder(2*hash.length);
          for(byte b : hash)sb.append(String.format("%02x", b&0xff));
          return sb.toString();
	  }
	  
	  public static final String createHash(final File f) throws NoSuchAlgorithmException, IOException{
		   byte[] b = createChecksum(f);
	       String result = "";

	       for (int i=0; i < b.length; i++) {
	           result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
	       }
	       return result;
	  }
	  
	  private static byte[] createChecksum(File filename) throws NoSuchAlgorithmException, IOException {
	       InputStream fis =  new FileInputStream(filename);
	       byte[] buffer = new byte[1024];
	       MessageDigest complete = MessageDigest.getInstance("MD5");
	       int numRead;
	       do {
	           numRead = fis.read(buffer);
	           if (numRead > 0) {
	               complete.update(buffer, 0, numRead);
	           }
	       } while (numRead != -1);

	       fis.close();
	       return complete.digest();
	   }
	  
	  
	  /* ####################################################
	   * HASH END
	   * ####################################################
	   */
	  public static final SecretKey getKey(final SymmetricEncryption encrpytion,final String base){
		  return getKey(encrpytion,base.toCharArray());
		  //final byte[] enc = Base64.decode(base);
		  //return new SecretKeySpec(enc,0,enc.length,encrpytion.getCipherCode());
	  }
	  
	  public static final SecretKey getKey(final SymmetricEncryption encrpytion,final char[] base){
		  final byte[] enc = Base64.decode(new String(base));
		  return new SecretKeySpec(enc,0,enc.length,encrpytion.getCipherCode());
	  }
	  
	  public static final String keyToString(final SecretKey sk){
		  return Base64.encode(sk.getEncoded());
	  }
	  
	  public static final String randomString(final int bits){
		  return new BigInteger(bits, rand).toString(32);
	  }
	  
//	  private static final byte[] readEOF(final InputStream in,final SecretKey sk) throws IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException{
//		  List<Byte> b = new ArrayList<Byte>();int i;
//		  while((i = in.read()) != -1){b.add((byte)i);System.out.println(i);}i = 0;
//		  byte[] b2 = new byte[b.size()];for(Byte bb:b){b2[i] = bb;i++;}
//		  return decrypt(SymmetricEncryption.fromCode(sk.getAlgorithm()), b2, sk);
//	  }
//	  public static final byte[] intToByteArray(int value) {
//		  return new byte[] {
//		            (byte)(value >>> 24),
//		            (byte)(value >>> 16),
//		            (byte)(value >>> 8),
//		            (byte)value};
//	  }
//		
//	  public static final int fromByteArray(byte[] bytes) {
//		     return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
//	  }
	  
//	  public static final byte[] checkPadding(byte[] toSend,final SymmetricEncryption enc){
//          if(enc == SymmetricEncryption.Blowfish && toSend.length % 8 != 0){ //not a multiple of 8
//              System.out.println("decodedBytes is not padded properly in 8 bits");
//              byte[] padded = new byte[toSend.length + 8 - (toSend.length % 8)];
//              System.arraycopy(toSend, 0, padded, 0, toSend.length);
//              toSend = padded;
//          }else{
//              System.out.println("decodedBytes is padded properly in 8 bits");
//          }
//          return toSend;
//	  }

	  public final void destroy(){
		  publicKey = null;privateKey = null;
	  }
}