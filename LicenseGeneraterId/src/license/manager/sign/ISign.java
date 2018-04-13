package license.manager.sign;

/**
 *	签名管理
 *
 * @author focus
 * @date 2015年10月8日
 * @time 下午1:46:52
 */
public interface ISign {

	
	/**
	 * 生成签名
	 * @param 生成签名使用的消息
	 * @author focus
	 * @date 2015年10月8日
	 * @time 上午11:23:27
	 */
	public  String createMsgSign(String msg);

}
