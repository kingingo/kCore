package me.kingingo.kcore.EncryptionUtil.Util;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
/**
 * @author Ch4t4r | Daniel Wolf
 * @license Free for commercial use. Do not distribute.
 */
public interface EncryptionHandler { 
	public Storage<SignatureContext, PublicKey> initializeConnectionToClient(final SecretKey sessionKey,final EncryptedSocket es
			,final PublicKey myPublic,final PrivateKey myPrivate) throws InvalidKeyException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException,SignatureException ;
	
	public TripleStorage<SignatureContext, SecretKey,PublicKey> initializeConnectionToServer(final EncryptedSocket es
			,final PublicKey myPublic,final PrivateKey myPrivate) throws InvalidKeyException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException,SignatureException ;
	
	public SecretKey initializeConnectionToServer(EncryptedSocket socket,PublicKey serverPublic) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, IOException, BadPaddingException;	

	public SecretKey initializeConnectionToClient(EncryptedSocket client, PrivateKey serverPrivate) throws InvalidKeyException, ClassNotFoundException,NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException,IOException;
}
