package license.entity;

import java.util.Date;

/**
 * mac 地址
 *	
 *
 * @author focus
 * @date 2015年10月28日
 * @time 下午5:46:46
 */
public class LicenseMacAddress {

	private int id;
	private String macAddress;
	private String ipAddress;
	private String uniqueSerialNumber;
	private Date createDate;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getUniqueSerialNumber() {
		return uniqueSerialNumber;
	}
	public void setUniqueSerialNumber(String uniqueSerialNumber) {
		this.uniqueSerialNumber = uniqueSerialNumber;
	}
	@Override
	public String toString() {
		return "LicenseMacAddress [macAddress=" + macAddress
				+ ", uniqueSerialNumber=" + uniqueSerialNumber
				+ ", createDate=" + createDate + "]";
	}
	
}
