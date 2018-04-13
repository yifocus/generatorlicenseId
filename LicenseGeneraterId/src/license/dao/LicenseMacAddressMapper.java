package license.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


import license.entity.LicenseMacAddress;


/**
 * @author focus
 */
public class LicenseMacAddressMapper extends BaseDAO {

//	Logger logger = LoggerFactory.getLogger(getClass());
	public String getLisenceMacAddress(String macAddress){
		String sql = "select macAddress from licenseMacAddress where macAddress = '"+macAddress+"'";
		try {
			LicenseMacAddress address = findOne(AppDBManager.getConnection(), LicenseMacAddress.class, sql);
			if(address != null){
				return address.getMacAddress();
			}
		} catch (SQLException e) {
//			logger.error("get mac error",e);
		}
		return null;
	}

	public List<String> getLisenceMacAddresses(){
		String sql = "select macAddress from licenseMacAddress";

		try {
			List<LicenseMacAddress> addresses =  findList(AppDBManager.getConnection(), LicenseMacAddress.class, sql);
			List<String> results = new CopyOnWriteArrayList<>();
			for(LicenseMacAddress address : addresses){
				results.add(address.getMacAddress());
			}

			return results;
		} catch (SQLException e) {
//			logger.error("get mac error",e);
		}
		return null;
	}
	public List<String> getUniqueSerialNumbers(){

		String sql = "select uniqueSerialNumber from licenseMacAddress";

		try {
			List<LicenseMacAddress> addresses =  findList(AppDBManager.getConnection(), LicenseMacAddress.class, sql);
			List<String> results = new CopyOnWriteArrayList<>();
			for(LicenseMacAddress address : addresses){
				results.add(address.getUniqueSerialNumber());
			}
			return results;
		} catch (SQLException e) {
//			logger.error("get uniqueSerialNumber error",e);
		}
		return null;
 	}
	public String getMacAddressAnduniqueSerialNumber(
			String macAddress,
			 String uniqueSerialNumber){

		String sql = "select macAddress from licenseMacAddress where macAddress = '"+macAddress + "' and uniqueSerialNumber = '" +uniqueSerialNumber +"'";
		try {
			LicenseMacAddress address = findOne(AppDBManager.getConnection(), LicenseMacAddress.class, sql);
			if(address != null){
				return address.getMacAddress();
			}
		} catch (SQLException e) {
//			logger.error("get mac error",e);
		}
		return null;
	}

	public int insert(LicenseMacAddress mac){

		String sql = "insert into licenseMacAddress (createDate,ipAddress,macAddress,uniqueSerialNumber) " +
				"values(now(),'"+mac.getIpAddress()+"','"+mac.getMacAddress()+"','" +mac.getUniqueSerialNumber()+"')";
		try {
			insert(AppDBManager.getConnection(),sql);
		} catch (SQLException e) {
//			logger.error("add mac error");
		}
		return 1;
	}
}