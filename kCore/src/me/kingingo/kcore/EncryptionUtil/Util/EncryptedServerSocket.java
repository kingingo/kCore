package me.kingingo.kcore.EncryptionUtil.Util;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.PrivateKey;
import java.security.PublicKey;
/**
 * @author Ch4t4r | Daniel Wolf
 * @license Free for commercial use. Do not distribute.
 */
public class EncryptedServerSocket extends ServerSocket{
	
	/**
	 * Initiats an ServerSocket used for Encryption. Don't use accept()!
	 * @param port The port on which the Server should be hosted on
	 * @throws IOException
	 */
	public EncryptedServerSocket(int port) throws IOException {
		super(port);
	}
	
	/**
	 * @return Returns a client which connects to the socket.
	 * @param handler The EncryptionHandler which should be used to safely transfer PublicKeys and Signatures.
	 * @param serverKey The PrivateKey used for Encryption/Decryption.
	 * @param serverPublic The PublicKey used for Encryption/Decryption.
	 */
	public final EncryptedSocket accept(final EncryptionHandler handler,final PrivateKey serverKey,final PublicKey serverPublic) throws Exception{
		if(isClosed())throw new SocketException("Socket is closed");
		if(!isBound())throw new SocketException("Socket is not bound yet");
		final EncryptedSocket s = new EncryptedSocket(handler,serverKey,serverPublic);
		implAccept(s);
		s.init();
		return s;
	}
	
	/** Use this method when the client knows the servers PublicKey
	 * @return Returns a client which connects to the socket.
	 * @param handler
	 * @param serverKey
	 * @return
	 * @throws Exception
	 */
	public final EncryptedSocket accept(final EncryptionHandler handler,final PrivateKey serverKey) throws Exception{
		if(isClosed())throw new SocketException("Socket is closed");
		if(!isBound())throw new SocketException("Socket is not bound yet");
		final EncryptedSocket s = new EncryptedSocket(handler,serverKey,true);
		implAccept(s);
		s.init();
		return s;
	}
	
	/**
	 * Obsolete. Use accept(EncryptionHandler,PrivateKey,PublicKey)
	 * @return Returns always null since this shouldn't be used.
	 */
	@Override
	public final Socket accept(){
		return null;
	}
}
