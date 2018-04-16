package license;

import java.util.List;

import license.dao.LicenseMacAddressDao;
import license.entity.LicenseMacAddress;

public class LicenseService {

	private static class LicenseServiceHolder {
		public static LicenseService instance = new LicenseService();
	}
	
	public static LicenseService getInstance(){
		return LicenseServiceHolder.instance;
	}
	
	
	public void initTable(){
		LicenseMacAddressDao.getInstance().createLicenseRecordTable();
	}
	
	public void dropTable(){
		LicenseMacAddressDao.getInstance().dropLicenseRecordTable();
	}
	
	public void getAllMacs(){
		LicenseMacAddressDao dao = LicenseMacAddressDao.getInstance();
		List<LicenseMacAddress> macs = dao.getAllLicenseMacAddress();
		printMacs(macs);
	}
	
	
	public void getMacsAdd(){
		LicenseMacAddressDao dao = LicenseMacAddressDao.getInstance();
		List<LicenseMacAddress> macs = dao.getLisenceMacAddressAdded();
		
		printMacs(macs);
	}


	private void printMacs(List<LicenseMacAddress> macs) {
		
		LicenseMacAddressDao dao = LicenseMacAddressDao.getInstance();
		List<LicenseMacAddress> addMacs = dao.getLisenceMacAddressAdded();
		
		int i = 0;
		System.out.println("\r\n            ip                 fromEim                    mac");
		for(LicenseMacAddress mac : macs){
			i ++; 
			String m = mac.getMacAddress();
			String ip = mac.getIpAddress();
			printLine(i,ip,m,isFromEim(m, addMacs));
		}
		
		System.out.println("\r\n \r\n" );
		System.out.println("机器绑定总数为： " + macs.size());
	}

	private void printLine(int i, String ip, String m, String fromEim) {
		StringBuilder result = new StringBuilder();
		
		result.append("\n").append(i).append(appendR(1)).
			   append(ip).append(appendR(2)).
			   append(fromEim).append(appendR(3)).
			   append(m);
		System.out.println(result.toString());
	}
	
	private String appendR(int count){
		StringBuilder b = new StringBuilder();
		for(int i = 0; i < count; i ++){
			b.append("\t");
		}
		return b.toString();
	}


	private String isFromEim(String mac, List<LicenseMacAddress> addMacs){
		boolean isfromEim = true;
		
		for(LicenseMacAddress addMac : addMacs){
			if(mac.equals(addMac.getMacAddress())){
				isfromEim = false;
				break;
			}
		}
		return isfromEim ? "y" : "n";
	}
	
	public static void main(String[] args) {
		
		StringBuilder b = new StringBuilder();
		b.append("1");
		for(int i = 0 ; i < 10; i++){
			b.append(" ");
		}
		
		b.append("2");
		
		System.out.println(b.toString());
		System.out.println(b.length());
		
	}
	
	
	
	
}
