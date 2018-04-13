package license;

import java.util.Scanner;

import license.manager.nonce.NonceManager1;
import license.task.NonceInitTask;

public class GeneratorId {

	public static void main(String[] args) throws Exception {
		
		new NonceInitTask().run();
		System.out.println("获取LicenseNonce 请输入 y/n");
		
		Scanner s = new Scanner(System.in);
		String t = s.nextLine();
		if("Y".equalsIgnoreCase(t)){
			System.out.println("\r\n\r\n\r\nLicenseNonce 为: " + new NonceManager1().getLocalNonce());
		}else if("2".equals(t)){
			
			new NonceInitTask().run();
			System.out.println(" 生成 LicenseNonce成功， 当前LicenseNocne 为: " + new NonceManager1().getLocalNonce());
		}
		
		System.out.println("\r\n\r\n\n");
		s.close();
	}
}
