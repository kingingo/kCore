package me.kingingo.kcore.EncryptionUtil.Util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketImpl;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import me.kingingo.kcore.EncryptionUtil.Encryptions.AsymmetricEncryption;
import me.kingingo.kcore.EncryptionUtil.Encryptions.HashEncryption;
import me.kingingo.kcore.EncryptionUtil.Encryptions.SymmetricEncryption;
import me.kingingo.kcore.EncryptionUtil.Streams.EncryptedInputStream;
import me.kingingo.kcore.EncryptionUtil.Streams.EncryptedOutputStream;

/**
 * @author Ch4t4r | Daniel Wolf
 * @license Free for commercial use. Do not distribute.
 */
public class EncryptedSocket extends Socket{
	protected final PublicKey publicKey;
	protected final PrivateKey privateKey;
	protected PublicKey partnerPublic;
	protected final EncryptionHandler eHandler;
	protected boolean init = false;
	protected SecretKey sessionKey;
	protected  boolean trusted;
	protected EncryptedInputStream input;
	protected EncryptedOutputStream output;
	protected final int manualMode;
	
	/**
	 * Used by the server to initialize the connection to a client
	 * @param handler
	 * @param myPrivate
	 * @param myPublic
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws SignatureException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws NoSuchPaddingException
	 */
	protected EncryptedSocket(final EncryptionHandler handler,final PrivateKey myPrivate,final PublicKey myPublic) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException, IOException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException{
		super((SocketImpl)null);
		manualMode = -1;
		this.privateKey = myPrivate;
		this.publicKey = myPublic;
		eHandler = handler;
	}
	
	protected EncryptedSocket(final EncryptionHandler handler,final PrivateKey myPrivate,boolean clientKnowsPublicKey) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException, IOException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException{
		super((SocketImpl)null);
		manualMode = clientKnowsPublicKey ? 1 : 0;
		this.privateKey = myPrivate;
		//this.publicKey = myPublic;
		this.publicKey = null;
		eHandler = handler;
	}
	
	/**
	 * Used to establish a connection to an EncryptedServerSocket. In this case we don't know anything about the Server and thus have to ask for everything.
	 * 
	 * @param handler The EncryptionHandler which should be used to initialize the connection.
	 * @param myPublic The PublicKey used by the Client.
	 * @param myPrivate The PrivateKey used by the Client.
	 * @param host The Host which the Socket should connect to.
	 * @param port The port which the Socket should connect to.
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws SignatureException
	 * @throws ClassNotFoundException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws NoSuchPaddingException
	 */
	public EncryptedSocket(final String host,final int port,final EncryptionHandler handler,final PublicKey myPublic,final PrivateKey myPrivate) throws UnknownHostException, IOException, InvalidKeyException, NoSuchAlgorithmException, SignatureException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException{
		super(host,port);
		manualMode = -1;
		this.publicKey = myPublic;
		this.privateKey = myPrivate;
		eHandler = handler;
		
		getOutputStream().write(0);getOutputStream().flush();
		TripleStorage<SignatureContext, SecretKey, PublicKey> d = eHandler.initializeConnectionToServer(this, myPublic, myPrivate);
		
		trusted = EncryptionUtil.verifySignature(d.getFirst(0), d.getFirst(0).getEncryption(), d.getThird(0));
		partnerPublic = d.getThird(0);
		sessionKey = d.getSecond(0);
		input = new EncryptedInputStream(sessionKey, this.getInputStream());
		output = new EncryptedOutputStream(sessionKey, this.getOutputStream());
	}
	
	/**
	 * Used to establish a connection to an EncryptedServerSocket. In this case we know the server's PublicKey and thus define the SessionKey and do not have to test whether our target really is the server.
	 * @param handler
	 * @param serverPublic
	 * @param host
	 * @param port
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public EncryptedSocket(String host,int port,final EncryptionHandler handler,final PublicKey serverPublic) throws UnknownHostException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		super(host,port);
		manualMode = -1;
		publicKey = null;
		privateKey = null;
		partnerPublic = serverPublic;
		eHandler = handler;
		
		getOutputStream().write(1);getOutputStream().flush();
		sessionKey = eHandler.initializeConnectionToServer(this, serverPublic);
		input = new EncryptedInputStream(sessionKey, this.getInputStream());
		output = new EncryptedOutputStream(sessionKey, this.getOutputStream());
	}
	
	/**
	 * Initializes the Encryption with a Client. This should be used by the Server only.
	 * @return Returns whether the Signature matched the Clients PublicKey
	 * @throws InvalidKeyException
	 * @throws ClassNotFoundException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws SignatureException
	 * @throws IOException
	 */
	public final boolean init() throws InvalidKeyException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, SignatureException, IOException{
		if(init)return false;
		int mode = getInputStream().read();
		mode = manualMode == -1 ? mode : manualMode;
		System.out.println("MODE: " + mode + " MANUALMODE: " + manualMode);
		if(mode == 0){//Client doesn't know the publicKey
			sessionKey = EncryptionUtil.getKey(SymmetricEncryption.Blowfish, EncryptionUtil.randomString(80));
			Storage<SignatureContext, PublicKey> d = eHandler.initializeConnectionToClient(sessionKey,this,publicKey,privateKey);
			trusted = EncryptionUtil.verifySignature(d.getFirst(0), d.getFirst(0).getEncryption(), d.getSecond(0));
			partnerPublic = d.getSecond(0);
			input = new EncryptedInputStream(sessionKey, this.getInputStream());
			output = new EncryptedOutputStream(sessionKey, this.getOutputStream());
			init = true;
			return trusted;
		}else{ //Client does know
			sessionKey = eHandler.initializeConnectionToClient(this, privateKey);
			trusted = true;
			input = new EncryptedInputStream(sessionKey, this.getInputStream());
			output = new EncryptedOutputStream(sessionKey, this.getOutputStream());
			init = true;
			return true;
		}
	}
	
	/**
	 * @return Returns the EncryptenInputstream which can be used to transfer data.
	 */
	public final EncryptedInputStream getEncryptedInputStream(){
		return input;
	}
	
	/**
	 * @return Returns the EncryptenOuputstream which can be used to transfer data.
	 */
	public final EncryptedOutputStream getEncryptedOutputStream(){
		return output;
	}
	
	/**
	 * Closes the Socket and all it streams.
	 * @throws IOException
	 */
	@Override
	public final void close() throws IOException{
		if(this.isClosed())return;
		super.close();
		if(output != null)output.close();
		if(input != null)input.close();
	}

	/**
	 * Obsolete. Use getEncryptedInputStream();
	 * @return Returns always null since this shouldn't be used anymore.
	 */
	@Deprecated
	@Override
	public final InputStream getInputStream() throws IOException {
		return super.getInputStream();
	}

	/**
	 * Obsolete. Use getEncryptedOutputStream
	 * @return Returns always null since this shouldn't be used anymore.
	 */
	@Deprecated
	@Override
	public final OutputStream getOutputStream() throws IOException {
		return super.getOutputStream();
	}
	
	public final boolean isTrusted(){
		return trusted;
	}
}
