package license.manager.sign.source;

import license.manager.sign.AbstractSign;


/**
 * MD5 加密
 * @author focus
 * @date 2015年10月13日
 * @time 下午3:30:57
 */
public class SignMD5 extends AbstractSign{
	
	@Override
	public String createMsgSign(String msg) {
		
		try {
			return encryptBASE64(encryptMD5(encryptMD5(msg.getBytes(CHARSET))));
		} catch (Exception e) {
			error("MD5加密失败", e);
		}
		return null;
	}
	
}
