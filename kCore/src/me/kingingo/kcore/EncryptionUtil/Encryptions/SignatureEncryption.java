package me.kingingo.kcore.EncryptionUtil.Encryptions;

import java.util.Map;
/**
 * @author Ch4t4r | Daniel Wolf
 * @license Free for commercial use. Do not distribute.
 */
public enum SignatureEncryption{
	
	RSA("NONEwithRSA"),
	MD5_RSA("MD5withRSA"),
	MD2_RSA("MD2withRSA"),
	DSA("NONEwithDSA"),
	SHA512_RSA("SHA512withRSA"),
	SHA256_RSA("SHA256withRSA");

	protected final String code;
	SignatureEncryption(String code){
		this.code = code;
	}
	
	SignatureEncryption(Map<String,Object> map){
		this((String)map.get(map.keySet().toArray(new String[map.size()])[0]));
	}
	
	@Override
	public final String toString(){
		return code;
	}
	
	/**
	 * @param code A valid String representing an algorithm 
	 * @return Returns an SignatureEncryption Object represented by the given code
	 */
	public final  static SignatureEncryption fromCode(String code){
		switch(code.toLowerCase()){
		case "nonewithrsa": return RSA;
		case "md5withrsa": return MD5_RSA;
		case "md2withrsa": return MD2_RSA;
		case "nonewithdsa": return DSA;
		case "sha512withrsa": return SHA512_RSA;
		default: return SHA256_RSA;
		}
	}
}
