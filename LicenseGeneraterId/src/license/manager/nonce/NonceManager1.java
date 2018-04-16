package license.manager.nonce;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import license.dao.LicenseMacAddressDao;
import license.util.LicenseUtil;


/**
 * 解决在集群，或者分机的时候：利用license<p>
 * <strong>解决方案：</strong><br>
 * 
 *	<li>	1    首先取各个机器的mac地址保存到数据库
 *	<li>    2   根据所有的mac地址生成一个唯一的nonce
 *	<li>	3   在验证nonce时， 首先验证当前的mac地址是否在数据库中存在的mac地址
 *	<li>
 * @author focus
 * @date 2015年10月28日
 * @time 下午5:54:00
 */
public class NonceManager1 {

	
	LicenseMacAddressDao lisenceMacAddressMapper = new license.dao.LicenseMacAddressDao();
	/**
	 * 比较 License 中的nonce 和本地的nonce进行比较
	 * @author focus
	 * @date 2015年10月29日
	 * @time 下午1:34:07
	 */
	public boolean compareNonce(String licenseNonce){
		
		if(licenseNonce == null){
			return false;
		}
		
		// mac验证   判断本地额mac地址是否是在允许的范围之内
		String localMac = LicenseUtil.getLocalMac();
		String	localMacSuffix = LicenseUtil.getSuffix(localMac,4);
		List<String> allMacs = deCryptNonceToMacAddresses(licenseNonce);
		if(allMacs == null || 
				allMacs.contains(localMacSuffix) == false) {
			return false;
		}
		
		String localUniquneSerial = LicenseUtil
				.getNonceDigest(localMac
						+ LicenseUtil.getUniqueMarkByNonceFile());
		
		// 序列号验证
		String macAddress = lisenceMacAddressMapper.
				getMacAddressAnduniqueSerialNumber(localMac, localUniquneSerial);
		if(macAddress == null){
			return false;
		}
		return licenseNonce.equals(getLocalNonce());
	}
	
	/**
	 * 取得本地的nonce: 组成格式，  本地md5取8位为摘要 + mac地址的后四位 如果有多个<br>
	 * 如： md5(mac1 + mac2)后八位 + mac1后四位 + mac2 后四位
	 * @author focus
	 * @date 2015年10月29日
	 * @time 下午1:34:56
	 */
	public String getLocalNonce(){
		
		List<String> macAddresses = lisenceMacAddressMapper.getLisenceMacAddresses();
		List<String> serials = lisenceMacAddressMapper.getUniqueSerialNumbers();
		
		if(macAddresses == null || macAddresses.isEmpty()){
			// 如果没有数据， 则取本地的数据中的mac地址
			macAddresses = new ArrayList<String>();
			macAddresses.add(LicenseUtil.getLocalMac());
		}
		
		// 去掉重复数据， 避免多线程数据插入到数据库
		LinkedHashSet<String> dif = new LinkedHashSet<String>();
		for(String macAddress : macAddresses){
			if(LicenseUtil.isNotEmpty(macAddress)){
				dif.add(macAddress);
			}
		}
		
		// 如果是集群， 这是由 集群的服务器组的mac 地址组成
		// 排序一次， 防止在多线程插入的时候
		macAddresses = new ArrayList<String>(dif);
		Collections.sort(macAddresses);
		
		// 预防多线程插入重复数据
		LinkedHashSet<String> tempResult = new LinkedHashSet<String>();
		serials.addAll(macAddresses);
		for(String serial : serials){
			if(LicenseUtil.isNotEmpty(serial)){
				tempResult.add(serial);
			}
		}
		
		List<String> result = new ArrayList<String>(tempResult);
		Collections.sort(result);
		
		return LicenseUtil.getNonceDigest(tempResult) +
				LicenseUtil.getNonceBody(macAddresses);
	}
	
	/**
	 * 解析nonce 中的macAddress 转换成为集合
	 * @author focus
	 * @date 2015年10月29日
	 * @time 下午2:04:20
	 */
	public List<String> deCryptNonceToMacAddresses(String licenseNonce){
		
		//  nonce： md5(mac1 + mac2)后八位 + mac1后四位 + mac2 后四位
		List<String> macs = new ArrayList<String>();
		if(licenseNonce == null) return macs;
		
		try {
			
			if(licenseNonce.length() >= 12){
				
				// 最少位数为 12位  ：md5(mac1) 后八位 + mac1 后四位
				String macStr = licenseNonce.substring(8); // 去掉摘要字符串
				for(int i = 0 ,len = macStr.length(); i < len;){
					
					int startIndex = i;
					int endIndex = i + 4;
					i += 4;
					
					if(endIndex > len) break;
					
					macs.add(macStr.substring(startIndex, endIndex));
					
				}
			}
		} catch (Exception e) {
		}
		
		return macs;
	}
	
	public static void main(String[] args) {
		String licenseNonce = "acdsfdsa12345678012344440022";
		NonceManager1 n = new NonceManager1();
		List<String> result = n.deCryptNonceToMacAddresses(licenseNonce);
		System.out.println(result);
	}
	
}
