package license.util;

import com.google.common.base.Charsets;
import com.google.common.io.BaseEncoding;

/**
 * Base64编码工具类
 * 
 * 
 * @author MaiJingFeng
 */
public class Base64Utils {

	
	/**
	 * 将字符串进行URL安全的Base64编码(RFC 4648 section 5 or RFC 3548.)
	 * @param decodedString
	 * @return 若decodedString=null,则返回null
	 *
	 */
	public static String encodeURLSafe(String decodedString){
		if(decodedString == null) return null;
		try{
			return encodeURLSafeFromBytes(decodedString.getBytes(Charsets.UTF_8));
		}catch(Exception e){
			return null;
		}
	}
	
	
	/**
	 * 进行URL安全的Base64解码
	 * @param encodedString
	 * @return if the input is not a valid encoded string according to this
	 *         encoding, will return null
	 *
	 */
	public static String decodeURLSafe(String encodedString){
		if(encodedString == null) return null;
		try{
			return new String(decodeURLSafeToBytes(encodedString),Charsets.UTF_8);
		}catch(Exception e){
			return null;
		}
	}
	
	
	public static byte[] decodeURLSafeToBytes(String encodedString){
		return BaseEncoding.base64Url()
				.decode(encodedString);
	}
	
	
	public static String encodeURLSafeFromBytes(byte[] decodedBytes){
		return BaseEncoding.base64Url().encode(decodedBytes);
	}
	
	
	/**
	 * 将字符串进行一般的Base64编码(RFC 4648 section 4 or RFC 3548.)
	 * @param decodedString
	 * @return 若decodedString=null,则返回null
	 *
	 */
	public static String encode(String decodedString){
		if(decodedString == null) return null;
		try{
			return encodeFromBytes(decodedString.getBytes(Charsets.UTF_8));
		}catch(Exception e){
			return null;
		}
	}
	
	
	/**
	 * 进行一般Base64解码
	 * @param encodedString
	 * @return if the input is not a valid encoded string according to this
	 *         encoding, will return null
	 *
	 */
	public static String decode(String encodedString){
		if(encodedString == null) return null;
		try{
			return new String(decodeToBytes(encodedString),Charsets.UTF_8);
		}catch(Exception e){
			return null;
		}
	}
	

	public static byte[] decodeToBytes(String encodedString){
		return BaseEncoding.base64()
				.decode(encodedString);
	}
	
	
	public static String encodeFromBytes(byte[] decodedBytes){
		return BaseEncoding.base64().encode(decodedBytes);
	}
	
}
