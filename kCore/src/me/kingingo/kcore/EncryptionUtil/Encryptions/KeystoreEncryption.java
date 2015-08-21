package me.kingingo.kcore.EncryptionUtil.Encryptions;

import java.util.Map;
/**
 * @author Ch4t4r | Daniel Wolf
 * @license Free for commercial use. Do not distribute.
 */
public enum KeystoreEncryption{

	JCEKS("jceks"),
	JKS("jks"),
	PKCS12("pkcs12");
	
	
	protected final String code;
	KeystoreEncryption(String code){
		this.code = code;
	}
	
	KeystoreEncryption(Map<String,Object> map){
		this((String)map.get(map.keySet().toArray(new String[map.size()])[0]));
	}
	
	@Override
	public final String toString(){
		return code;
	}
}
