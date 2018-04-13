package license.util;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密/解密器;
 * 采用AES/CBC/NoPadding;
 * 
 * <p>
 * <ul>
 * <li>字符串AES加密后经base64转码输出;
 * <li>密钥为base64编码密钥;
 * 
 * @author MaiJingFeng
 */
public class AesBase64Cipher {
	private static final String Algorithm = "AES/CBC/NoPadding";
	private static Charset CHARSET = Charset.forName("UTF-8");
	
	public AesBase64Cipher(){
		
	}
	
	
	/**
	 * 加密
	 * @param msg 原始消息
	 * @param encodingAesKey 密钥
	 * @return 输出base64编码后的加密消息体
	 * @throws AesException
	 *
	 */
	public static String encrypt(String msg, String encodingAesKey) throws AesException{
		try {
			byte[] key = createSecretKeyBytes(encodingAesKey);
			Cipher cipher = Cipher.getInstance(Algorithm);
			SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
			IvParameterSpec iv = new IvParameterSpec(key, 0, 16);
			cipher.init(Cipher.ENCRYPT_MODE, keySpec,iv);
			
			byte[] source = msg.getBytes(CHARSET);
			byte[] padBytes = PKCS7Encoder.encode(source.length);
			
			//进行补位
			ByteBuffer buf = ByteBuffer.allocate(source.length + padBytes.length);
			buf.put(source).put(padBytes);
			
			byte[] resultBytes = cipher.doFinal(buf.array());
			
			String encode_encrypt_msg = Base64Utils.encodeFromBytes(resultBytes);
			
			return encode_encrypt_msg;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AesException(AesException.EncryptAESError);
		} 
	}
	
	/**
	 * 解密
	 * @param encode_encrypt_msg base64编码的加密消息体
	 * @param encodingAesKey 密钥
	 * @return 返回原始消息体
	 * @throws AesException
	 *
	 */
	public static String decrypt(String encode_encrypt_msg, String encodingAesKey) throws AesException{
		try {
			
			byte[] raw = createSecretKeyBytes(encodingAesKey);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");  
            Cipher c = Cipher.getInstance(Algorithm);  
            IvParameterSpec iv = new IvParameterSpec(raw, 0, 16);  
            c.init(Cipher.DECRYPT_MODE, skeySpec,iv);
			
			byte[] decode_encrypt_msg = Base64Utils.decodeToBytes(encode_encrypt_msg);
			byte[] resultBytes = c.doFinal(decode_encrypt_msg);
			
			byte[] removedPadBytes = PKCS7Encoder.decode(resultBytes);
			
			
			return new String(removedPadBytes, CHARSET);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AesException(AesException.DecryptAESError);
		} 
	}
	
	/**
	 * 对密钥进行Base64解码
	 * @param encodingAesKey
	 * @return
	 * @throws AesException
	 *
	 */
	private static byte[] createSecretKeyBytes(String encodingAesKey) throws AesException{
		return Base64Utils.decodeToBytes(encodingAesKey);
	}
	
	
	/**
	 * 返回经Base64编码的AES随机密钥;
	 * @param count 密钥长度
	 * @return
	 *
	 */
	public static String getRandomEncodingAesKey(){
		return Base64Utils.encode(getRandomStr());
	}
	
	
	// 随机生成16位字符串
	private static String getRandomStr() {
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 16; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
	
	
	public static void main(String[] args) throws Exception {
		String privateKey = getRandomEncodingAesKey();
		System.out.println("encodingAESKey: " + privateKey);
		String sourceStr = "focus23424";
		System.out.println("Source String: " + sourceStr);
		String encrypt = encrypt(sourceStr, privateKey);
		System.out.println("Encrypted String: " + encrypt);
		
		String decrypt = decrypt(encrypt, privateKey);
		System.out.println("Decrypted String: " + decrypt);
	}
}
