package eu.epicpvp.kcore.EncryptionUtil.Util;

import java.io.Serializable;

import eu.epicpvp.kcore.EncryptionUtil.Encryptions.SignatureEncryption;

/**
 * @author Ch4t4r | Daniel Wolf
 * @license Free for commercial use. Do not distribute.
 */
public class SignatureContext implements Serializable{
	private static final long serialVersionUID = -1669409638833625472L;
	protected final String message;
	protected final String sign;
	protected final SignatureEncryption enc;
	
	public SignatureContext(final String msg,final String sign,final SignatureEncryption encryption){
		this.message = msg;
		this.sign = sign;
		enc = encryption;
	}
	
	/**
	 * @return Returns a string representing the Signtext.
	 */
	public final String getMessage(){
		return message;
	}
	
	/**
	 * @return Returns a string representing the Signaturebytes.
	 */
	public final String getSignature(){
		return sign;
	}
	
	/**
	 * @return Returns the SignatureEncryption used for validation
	 */
	public final SignatureEncryption getEncryption(){
		return enc;
	}
}
