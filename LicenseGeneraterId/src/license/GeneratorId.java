package license;

import java.util.Scanner;

import license.manager.nonce.NonceManager1;
import license.task.NonceInitTask;
import license.util.LicenseUtil;

public class GeneratorId {

	public static void main(String[] args) throws Exception {
		
		NonceInitTask task = new NonceInitTask();
		LicenseService licenseServ = LicenseService.getInstance();
		NonceManager1 nonceManager = new NonceManager1();
		licenseServ.initTable();
		task.run();
		
		System.out.println("已经绑定的机器 ：");
		System.out.println("=============================mac地址列表================================");
		System.out.println();
		System.out.println();
		licenseServ.getAllMacs();
		System.out.println("\nLicenseNonce:   " + nonceManager.getLocalNonce());
		System.out.println();
		System.out.println();
		System.out.println("======================================================================");
		
		Scanner s = new Scanner(System.in);
		
		while(true){
			System.out.println("操作命令:");
			System.out.println("1. 获取LicenseNonce");
			System.out.println("2. 重置");
			System.out.println("3. 已经绑定的机器列表");
			System.out.println("4. 退出");
			System.out.print("请输入：");
			String t = s.nextLine();
			if("1".equalsIgnoreCase(t)){
				System.out.println("======================================================================");
				System.out.println();
				System.out.println();
				System.out.println("\nLicenseNonce:   " + nonceManager.getLocalNonce());
				System.out.println();
				System.out.println();
				System.out.println("======================================================================");
			}else if("2".equals(t)){
				
				licenseServ.dropTable();
				licenseServ.initTable();
				task.run();
				
				System.out.println("======================================================================");
				System.out.println();
				System.out.println();
				System.out.println("初始化license数据成功");
				System.out.println("当前机器       ip: " +  LicenseUtil.getLocalIp() + " mac地址为 : " + LicenseUtil.getLocalMac());
				System.out.println();
				System.out.println();
				System.out.println("======================================================================");
			}else if("3".equals(t)){
				System.out.println("已经绑定的机器列表 ：");
				System.out.println("=============================mac地址列表================================");
				System.out.println();
				System.out.println();
				licenseServ.getAllMacs();
				System.out.println();
				System.out.println();
				System.out.println("======================================================================");
			}else if("404".equals(t)){
				System.out.println("新绑定机器列表 ：");
				System.out.println("=============================mac地址列表================================");
				System.out.println();
				System.out.println();
				licenseServ.getMacsAdd();
				System.out.println();
				System.out.println();
				System.out.println("======================================================================");
			}else if("4".equals(t)){
				System.out.println("操作完成！");
				s.close();
				break;
			}else{
				System.out.println("输入指令错误！");
			}
			
			System.out.println("\r\n\r\n\n");
		}
		
		// 新加入服务器mac地址为：
		// 
	}
}
