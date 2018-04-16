package license.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;



import license.entity.LicenseMacAddress;


/**
 * @author focus
 */
public class LicenseMacAddressDao extends BaseDAO {

	private static  class LicenseMacAddressDaoHolder{
		static LicenseMacAddressDao instance = new LicenseMacAddressDao();
	}
	
	public static LicenseMacAddressDao getInstance(){
		return LicenseMacAddressDaoHolder.instance;
	}
	
	public boolean createLicenseRecordTable(){
		String sql = "create table  IF NOT EXISTS  licenseMacAddressTemp  select * from licenseMacAddress;";
		try {
			return update(AppDBManager.getConnection(), sql) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean dropLicenseRecordTable(){
		String sql = "drop table licenseMacAddressTemp";
		try {
			return update(AppDBManager.getConnection(), sql) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<LicenseMacAddress> getLisenceMacAddressAdded(){
		String sql = "select * from licenseMacAddressTemp where macAddress not in (select macAddress from licenseMacAddress )";
		try {
			List<LicenseMacAddress> result =  findList(AppDBManager.getConnection(), LicenseMacAddress.class, sql);
			if(result != null){
				return result;
			}
		} catch (SQLException e) {
//			logger.error("get mac error",e);
		}
		return  new ArrayList<LicenseMacAddress>();
	}
	
	public List<LicenseMacAddress> getAllLicenseMacAddress(){
		String sql = "select * from licenseMacAddressTemp";
		try {
			List<LicenseMacAddress> result =  findList(AppDBManager.getConnection(), LicenseMacAddress.class, sql);
			if(result != null){
				return result;
			}
		} catch (SQLException e) {
//			logger.error("get mac error",e);
		}
		return  new ArrayList<LicenseMacAddress>();
	}

	public List<String> getLisenceMacAddresses(){
		String sql = "select macAddress from licenseMacAddressTemp";

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

		String sql = "select uniqueSerialNumber from licenseMacAddressTemp";

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

		String sql = "select macAddress from licenseMacAddressTemp where macAddress = '"+macAddress + "' and uniqueSerialNumber = '" +uniqueSerialNumber +"'";
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

		String sql = "insert into licenseMacAddressTemp (createDate,ipAddress,macAddress,uniqueSerialNumber) " +
				"values(now(),'"+mac.getIpAddress()+"','"+mac.getMacAddress()+"','" +mac.getUniqueSerialNumber()+"')";
		try {
			insert(AppDBManager.getConnection(),sql);
		} catch (SQLException e) {
//			logger.error("add mac error");
		}
		return 1;
	}
}