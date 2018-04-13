package license.util;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;


public class AESKeyGenerator {
	
	private static final String Algorithm = "AES";
	
	private KeyGenerator keyGenerator;
	
	public AESKeyGenerator(int keySize) throws AesException{
		try {
			keyGenerator = KeyGenerator.getInstance(Algorithm);
			keyGenerator.init(keySize);
		} catch (Exception e) {
			throw new AesException(AesException.GeneratorKeyError);
		}
	}
	
	
	public SecretKey generateAesKey(){
		return keyGenerator.generateKey();
	}
	
	
	public String getEncodingAesKey(){
		byte[] encoed = generateAesKey().getEncoded();
		
		return Base64Utils.encodeFromBytes(encoed);
	}
	
	
}
