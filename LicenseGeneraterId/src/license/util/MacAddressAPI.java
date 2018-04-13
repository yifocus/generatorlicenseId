package license.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import license.constant.LicenseConstant;

import com.google.common.base.Strings;

public class MacAddressAPI {
	
	private static HashSet<String> exceptSelfMacs = new HashSet<String>();
	static Pattern p = Pattern.compile("[a-f|A-F|0-9]{2}:[a-f|A-F|0-9]{2}:[a-f|A-F|0-9]{2}:[a-f|A-F|0-9]{2}:[a-f|A-F|0-9]{2}:[a-f|A-F|0-9]{2}");
	static{
		Collections.addAll(exceptSelfMacs, "ff:ff:ff:ff:ff:ff","00:00:00:00:00:00");
	}
	    /**
	    * 获取当前操作系统名称. return 操作系统名称 例如:windows xp,linux 等.
	    */
	    public static String getOSName() {
	        return System.getProperty("os.name").toLowerCase();
	    }

	    /**
	    * 获取unix网卡的mac地址. 非windows的系统默认调用本方法获取. 如果有特殊系统请继续扩充新的取mac地址方法.
	    * 
	    * @return mac地址
	    */
	    public static String getUnixMACAddress() {
	    	String networkCard = "ifconfig eth0";
	        String macAddress = getUnixMACAddress(networkCard);
	        if(Strings.isNullOrEmpty(macAddress)){
	        	networkCard = "ip a";
	        	return getUnixMACAddress(networkCard);
	        }
	        return macAddress;
	    }

		private static String getUnixMACAddress(String networkCard) {
			String mac = null;
	        BufferedReader bufferedReader = null;
	        Process process = null;
	        try {
	            // linux下的命令，一般取eth0作为本地主网卡
	            process = Runtime.getRuntime().exec(networkCard);
	            // 显示信息中包含有mac地址信息
	            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	            String line = null;
	            while ((line = bufferedReader.readLine()) != null) {
	            	// 通过正则表达式获取
	                Matcher matcher = p.matcher(line);
	                while(matcher.find()){
	                	mac = matcher.group();
	                	if(!exceptSelfMacs.contains(mac)){
	                		break;
	                	}
	                }
	            }
	        }
	        catch (IOException e) {
	        }
	        finally {
	            try {
	                if (bufferedReader != null) {
	                    bufferedReader.close();
	                }
	            }
	            catch (IOException e1) {
	                e1.printStackTrace();
	            }
	            bufferedReader = null;
	            process = null;
	        }
	        
	        if(mac != null){
	        	mac = mac.replaceAll(":", ""); // 过滤mac地址间隔符
	        }
	        return mac;
		}
	    
	    
	    

	    /**
	    * 获取widnows网卡的mac地址.
	    * 
	    * @return mac地址
	    */
	    public static String getWindowsMACAddress() {
	        String mac = null;
	        BufferedReader bufferedReader = null;
	        Process process = null;
	        try {
	            // windows下的命令，显示信息中包含有mac地址信息
	            process = Runtime.getRuntime().exec("ipconfig /all");
	            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	            String line = null;
	            int index = -1;
	            while ((line = bufferedReader.readLine()) != null) {
	                // 寻找标示字符串[physical
	                index = line.toLowerCase().indexOf("physical address");
	                if (index >= 0) {// 找到了
	                    index = line.indexOf(":");// 寻找":"的位置
	                    if (index >= 0) {
	                        // 取出mac地址并去除2边空格
	                        mac = line.substring(index + 1).trim();
	                    }
	                    break;
	                }
	            }
	        }
	        catch (IOException e) {
	        }
	        finally {
	            try {
	                if (bufferedReader != null) {
	                    bufferedReader.close();
	                }
	            }
	            catch (IOException e1) {
	                e1.printStackTrace();
	            }
	            bufferedReader = null;
	            process = null;
	        }
	        return mac;
	    }

	    /**
	    * windows 7 专用 获取MAC地址
	    * 
	    * @return
	    * @throws Exception
	    */
	    public static String getWindows7MACAddress() {
	        StringBuffer sb = new StringBuffer();
	        try {
	            // 获取本地IP对象
	            InetAddress ia = InetAddress.getLocalHost();
	            // 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
	            byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
	            // 下面代码是把mac地址拼装成String
	            for (int i = 0; i < mac.length; i++) {
	                // mac[i] & 0xFF 是为了把byte转化为正整数
	                String s = Integer.toHexString(mac[i] & 0xFF);
	                sb.append(s.length() == 1 ? 0 + s : s);
	            }
	        }
	        catch (Exception ex) {
	        }
	        return sb.toString();
	    }

	    /**
	    * 获取MAC地址
	    * 
	    * @param argc
	    *            运行参数.
	    * @throws Exception
	    */
	    public static String getMACAddress() {
	        // windows
	        String mac = getWindowsMACAddress();
	        if(!isNull(mac)){
	        }
	        // windows7
	        if (isNull(mac)) {
	            mac = getWindows7MACAddress();
	        }
	        // unix
	        if (isNull(mac)) {
	            mac = getUnixMACAddress();
	        }

	        if (!isNull(mac)) {
	            mac = mac.replace("-", "");
	        }
	        else {
	            mac = LicenseConstant.DEFAULT_MAC_ADDRESS;
	        }
	        return mac.toLowerCase();
	    }

	    public static boolean isNull(Object strData) {
	        if (strData == null || String.valueOf(strData).trim().equals("")) {
	            return true;
	        }
	        return false;
	    }

	    public static void main(String[] args) {
	    	
	    	String ipa = " link/ether 94:de:80:8b:87:78 brd ff:ff:ff:ff:ff:ff";
	    	
	    	String reg_name="[a-f|A-F|0-9]{2}:[a-f|A-F|0-9]{2}:[a-f|A-F|0-9]{2}:[a-f|A-F|0-9]{2}:[a-f|A-F|0-9]{2}:[a-f|A-F|0-9]{2}";
	    	
	    	Pattern p = Pattern.compile(reg_name);
	    	Matcher matcher = p.matcher(ipa);
	    	System.out.println(matcher.find());
	    	System.out.println(matcher.group());
	    	
	    }

}
