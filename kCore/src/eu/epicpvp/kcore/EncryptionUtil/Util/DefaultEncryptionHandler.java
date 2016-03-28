package eu.epicpvp.kcore.EncryptionUtil.Util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;

import eu.epicpvp.kcore.EncryptionUtil.Encryptions.AsymmetricEncryption;
import eu.epicpvp.kcore.EncryptionUtil.Encryptions.SignatureEncryption;
import eu.epicpvp.kcore.EncryptionUtil.Encryptions.SymmetricEncryption;

/**
 * @author Ch4t4r | Daniel Wolf
 * @license Free for commercial use. Do not distribute.
 */
public class DefaultEncryptionHandler implements EncryptionHandler{
	
	/* X.0 for Mode 0, X.1 for Mode 1
	 * 
	 * 
	 * 0. Client sends the Mode. 0 = Client knows nothing about the server, 1 = Client knows the Server's PublicKey, so we specify the SessionKey (Outside of the EncryptionHandler)
	 *  1.0 Client sends his PublicKey (Unencrypted)
	 *  1.1 Client sends his PublicKey (Encrypted with the Server's PublicKey)
	 * 2.0 Server sends the SessionKey (Encrypted with the Client's PublicKey)
	 * 2.1 Server sends the SessionKey (Encrypted with the Client's PublicKey)
	 *  3.0 Server sends his PublicKey (Encrypted with the SessionKey)
	 * 	3.1 No Validation. Connection initialized
	 * 4.0 Server sends his Signature (Encrypted with the SessionKey)
	 * 5.0 Client sends his Signature (Encrypted with the SessionKey)
	 * 6.0 The Signatures are validated. (Outside of the EncryptionHandler)
	 * 7.0 Connection initialized. The Signatures give a bit of safety, but it can easily be bypassed!
	 * 
	 * 
	 * No Connections will be closed as they'd close the parent streams!
	 */

	/**
	 * Sets up the symmetric Encryption with an Client. This method is used by the Server.
	 * @param sessionKey The Sessionkey used for Encryption with the Client after exchanging PublicKeys
	 * @param es The EncryptedSocket representing the Client
	 * @param myPublic The Publickey of the Server used to safely transfer the Clients PublicKey
	 * @param myPrivate The PrivateKey of the Server used to decrypt the PublicKey send by the client.
	 * @throws InvalidKeyException, ClassNotFoundException,IllegalBlockSizeException, BadPaddingException,NoSuchAlgorithmException, NoSuchPaddingException, IOException, SignatureException
	 */
	@Override
	public final Storage<SignatureContext,PublicKey> initializeConnectionToClient(final SecretKey sessionKey,final EncryptedSocket client,final PublicKey serverPublic,final PrivateKey serverPrivate)throws InvalidKeyException, ClassNotFoundException,IllegalBlockSizeException, BadPaddingException,NoSuchAlgorithmException, NoSuchPaddingException, IOException, SignatureException {
		final Storage<SignatureContext,PublicKey> stor = new Storage<SignatureContext,PublicKey>();
		final ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());final ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
		
		final PublicKey pKey = (PublicKey)ois.readObject(); //The Client's PublicKey
		oos.writeObject(EncryptionUtil.encryptObject(AsymmetricEncryption.fromCode(pKey.getAlgorithm()), sessionKey, pKey)); //Sending the SessionKey.
		EncryptionUtil.writeObject(SymmetricEncryption.Blowfish, sessionKey, serverPublic, oos); //Sending the Server's PublicKey
		EncryptionUtil.writeObject(SymmetricEncryption.Blowfish, sessionKey, 
				EncryptionUtil.generateSignatureContext(SignatureEncryption.MD5_RSA, serverPrivate, "legit"), oos); //Sending our Signature. THIS ISN'T TOTALLY SAFE!!
		stor.add((SignatureContext)EncryptionUtil.decryptObject(SymmetricEncryption.Blowfish, (SealedObject)ois.readObject(),sessionKey),pKey); //
		
		return stor; //We return the Client's signature and it's PublicKey.
	}

	/**
	 * Sets up the symmetric Encryption with a Server. This method is used by the Client.
	 * @param es The EncryptedSocket representing the Server
	 * @param myPublic The Publickey of the Client used to safely transfer the Servers PublicKey
	 * @param myPrivate The PrivateKey of the Client used to decrypt the PublicKey send by the Server.
	 * @throws InvalidKeyException, ClassNotFoundException,IllegalBlockSizeException, BadPaddingException,NoSuchAlgorithmException, NoSuchPaddingException, IOException, SignatureException
	 */
	@Override
	public final TripleStorage<SignatureContext, SecretKey,PublicKey> initializeConnectionToServer(final EncryptedSocket es,final PublicKey clientPublic,final PrivateKey clientPrivate) throws InvalidKeyException,ClassNotFoundException, IllegalBlockSizeException,BadPaddingException, NoSuchAlgorithmException,NoSuchPaddingException, IOException, SignatureException {
		final TripleStorage<SignatureContext, SecretKey,PublicKey> stor = new TripleStorage<SignatureContext, SecretKey,PublicKey>();
		final ObjectOutputStream oos = new ObjectOutputStream(es.getOutputStream());final ObjectInputStream ois = new ObjectInputStream(es.getInputStream());
		oos.writeObject(clientPublic);oos.flush(); //Writing the Client's PublicKey
		final SecretKey sKey = (SecretKey)EncryptionUtil.readObject(AsymmetricEncryption.fromCode(clientPublic.getAlgorithm()), ois, clientPrivate); //The Server sends the SessionKey
		final PublicKey pKey = (PublicKey)EncryptionUtil.readObject(SymmetricEncryption.fromCode(sKey.getAlgorithm()), ois, sKey); //The Server sends his PublicKey
		SignatureContext sc = EncryptionUtil.generateSignatureContext(SignatureEncryption.MD5_RSA, clientPrivate, "legit");
		oos.writeObject(EncryptionUtil.encryptObject(SymmetricEncryption.Blowfish, sc, sKey));
		//EncryptionUtil.writeObject(SymmetricEncryption.fromCode(sKey.getAlgorithm()), sKey,sc , oos); //The Client sends his Signatures
		stor.add((SignatureContext)EncryptionUtil.readObject(SymmetricEncryption.fromCode(sKey.getAlgorithm()), ois, sKey),sKey,pKey); //Reading the Server's Signature
		
		return stor; //We return the Server's Signature, the SessionKey und the Server's PublicKey
	}

	@Override
	public SecretKey initializeConnectionToServer(EncryptedSocket socket,PublicKey serverPublic) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, IOException, BadPaddingException {
		final ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		
		String sessionString = EncryptionUtil.randomString(60);
		byte[] sessionKeyBits = EncryptionUtil.encrypt(AsymmetricEncryption.fromCode(serverPublic.getAlgorithm()), sessionString.getBytes("UTF-8"), serverPublic);
		out.writeObject(sessionKeyBits);out.flush();
		
		return EncryptionUtil.getKey(SymmetricEncryption.Blowfish, sessionString);
	}
	
	@Override
	public SecretKey initializeConnectionToClient(EncryptedSocket client, PrivateKey serverPrivate) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException,IOException, ClassNotFoundException{
		final ObjectInputStream in = new ObjectInputStream(client.getInputStream());

		byte[] encryptedSessionKeyBits = (byte[])in.readObject();
		byte[] decryptedSessionKeyBits = EncryptionUtil.decrypt(AsymmetricEncryption.fromCode(serverPrivate.getAlgorithm()), encryptedSessionKeyBits, serverPrivate);
		
		return EncryptionUtil.getKey(SymmetricEncryption.Blowfish, new String(decryptedSessionKeyBits,"UTF-8"));
	}
}
