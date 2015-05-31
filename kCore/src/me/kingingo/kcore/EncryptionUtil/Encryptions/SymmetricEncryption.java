package me.kingingo.kcore.EncryptionUtil.Encryptions;

/**
 * @author Ch4t4r | Daniel Wolf
 * @license Free for commercial use. Do not distribute.
 */
public enum SymmetricEncryption{

	AES("AES","AES",16),
	RC4("ARCFOUR","ARCFOUR",1),
	Blowfish("Blowfish/ECB/PKCS5Padding","Blowfish",8),
	DES("DES","DES",8),
	TRIPEL_DES("DESede","DESede",8),
	RC2("RC2","RC2",8);
	
	protected final String code;
	protected final String cipher;
	protected final int blockSize;
	SymmetricEncryption(String code,String cipherCode,int blockSize){
		this.code = code;
		cipher = cipherCode;
		this.blockSize = blockSize;
	}

	@Override
	public final String toString(){
		return cipher;
	}
	
	public int getBlocksize(){
		return blockSize;
	}
	
	/**
	 * @param code A valid String representing an algorithm 
	 * @return Returns an SymmetricEncryption Object represented by the given code
	 */
	public final static SymmetricEncryption fromCode(String code){
		switch(code.toLowerCase()){
		case "aes": return AES;
		case "rc4": return RC4;
		case "blowfish": return Blowfish;
		case "des": return DES;
		case "rc2": return RC2;
		default: return TRIPEL_DES;
		}
	}

	public final String getCipherCode(){
		return cipher;
	}
	
	public final int numberCode(){
		switch(this){
		case AES: return 5;
		case RC4: return 6;
		case Blowfish: return 7;
		case DES: return 8;
		case TRIPEL_DES: return 9;
		default: return 10;
		}
	}
	
	public static final SymmetricEncryption fromNumberCode(int numCode){
		switch(numCode){
		case 5: return AES;
		case 6: return RC4;
		case 7: return Blowfish;
		case 8: return DES;
		case 9:return TRIPEL_DES;
		default: return RC2;
		}
	}
}
