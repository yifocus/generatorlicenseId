package license.task;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import license.constant.LicenseConstant;
import license.dao.LicenseMacAddressDao;
import license.entity.LicenseMacAddress;
import license.util.LicenseUtil;

import com.google.common.base.Strings;

/**
 * 解决在集群，或者分机的时候：利用license
 * <p>
 * <strong>解决方案：</strong><br>
 * 
 * <li>1 首先取各个机器的mac地址保存到数据库
 * <li>2 根据所有的mac地址生成一个唯一的nonce
 * <li>3 在验证nonce时， 首先验证当前的mac地址是否在数据库中存在的mac地址
 * 
 * @author focus
 * @date 2015年10月28日
 * @time 下午5:54:00
 */
public class NonceInitTask extends Thread {

	private AtomicBoolean flag = new AtomicBoolean(true);

	@Override
	public void run() {
		LicenseMacAddressDao dao = LicenseMacAddressDao.getInstance();
		try {
			synchronized (this) {
				String localMac = LicenseUtil.getLocalMac();
				String localUniquneSerial = LicenseUtil
						.getNonceDigest(localMac
								+ LicenseUtil.getUniqueMarkByNonceFile());
				String dbMacAddress = dao
						.getMacAddressAnduniqueSerialNumber(localMac,
								localUniquneSerial);
				if (dbMacAddress != null) {
					flag.set(false);
				} else {
					
					// 主板信息获取失败
					String mainBoardSerialNum = LicenseUtil.getMainBoardSerialNum();
					// 获取mac地址失败
					String macAddress = LicenseUtil.getLocalMac();
					
					if(LicenseConstant.DEFAULT_MAC_ADDRESS.toLowerCase().equals(macAddress) ||
							Strings.isNullOrEmpty(mainBoardSerialNum)){
						
						// 获取硬件信息失败，此时不记录数据信息到数据库进行匹配验证
						flag.set(true);
						return;
					}
					
					// 标记新添加的eim 服务器部署
					LicenseMacAddress mac = new LicenseMacAddress();
					mac.setCreateDate(new Date());
					mac.setIpAddress(LicenseUtil.getLocalIp());
					mac.setMacAddress(localMac);
					mac.setUniqueSerialNumber(localUniquneSerial);
					int result = dao.insert(mac);
					flag.set(true);
				}
			}
		} catch (Exception e) {
			try {
				sleep(1000 * 60 * 5);
			} catch (InterruptedException e1) {
			}
			flag.set(true);
		}
	}
}
