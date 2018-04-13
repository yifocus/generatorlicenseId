package license.manager.sign;

import java.nio.charset.Charset;

import license.util.BaseCoder;

public abstract class AbstractSign extends BaseCoder implements ISign{

	protected void info(String msg){
		System.out.println(msg);
	}
	protected void error(String err,Throwable e){
		System.out.println(err);
	}
	
	public static Charset CHARSET = Charset.forName("UTF-8");
	// 默认私钥
	public static String default_private_key = ""; 
	
	static {
		try {
			default_private_key = encryptBASE64(encryptMD5("diyiqixin".getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
