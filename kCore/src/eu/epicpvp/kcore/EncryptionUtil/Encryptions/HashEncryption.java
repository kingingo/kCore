package eu.epicpvp.kcore.EncryptionUtil.Encryptions;


public enum HashEncryption {

	MD5("MD5");

	protected final String code;
	HashEncryption(String code){
		this.code = code;
	}
	
	
	@Override
	public final String toString(){
		return code;
	}
	
	/**
	 * @param code A valid String representing an algorithm 
	 * @return Returns an SignatureEncryption Object represented by the given code
	 */
	public final  static HashEncryption fromCode(String code){
		switch(code.toLowerCase()){
			case "md5": return MD5;
			default: return null;
		}
	}
}
