package eu.epicpvp.kcore.EncryptionUtil.Encryptions;

import java.util.Map;
/**
 * @author Ch4t4r | Daniel Wolf
 * @license Free for commercial use. Do not distribute.
 */
public enum AsymmetricEncryption{

	RSA("RSA"),
	DSA("DSA"),
	DiffieHellman("DiffieHellman"),
	Elliptic_Curve("EC");
	
	protected final String code;
	AsymmetricEncryption(String code){
		this.code = code;
	}
	
	AsymmetricEncryption(Map<String,Object> map){
		this((String)map.get(map.keySet().toArray(new String[map.size()])[0]));
	}
	
	@Override
	public final String toString(){
		return code;
	}
	
	/**
	 * @param code A valid String representing an algorithm 
	 * @return Returns an AsymmetricEncryption Object represented by the given code
	 */
	public final static AsymmetricEncryption fromCode(String code){
		switch(code.toLowerCase()){
		case "rsa": return RSA;
		case "dsa": return DSA;
		case "ec": return Elliptic_Curve;
		default: return DiffieHellman;
		}
	}
	
	public final int numberCode(){
		switch(this){
		case RSA: return 1;
		case DSA: return 2;
		case DiffieHellman: return 3;
		default: return 4;
		}
	}
	
	public static AsymmetricEncryption fromNumberCode(int numbercode){
		switch(numbercode){
		case 1: return RSA;
		case 2: return DSA;
		case 3: return DiffieHellman;
		default: return Elliptic_Curve;
		}
	}
}
