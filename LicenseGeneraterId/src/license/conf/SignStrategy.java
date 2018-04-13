package license.conf;

import license.manager.sign.ISign;
import license.manager.sign.source.SignMD5;


/**
 * 签名的策略
 *
 * @author focus
 * @date 2015年10月8日
 * @time 下午2:40:10
 */
public enum SignStrategy {

	MD5(SignMD5.class);
	private ISign signManager;
	private SignStrategy(Class<? extends ISign> clzz){
		try {
			signManager = clzz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public ISign getSignManager(){
		return signManager;
	}
	
}
