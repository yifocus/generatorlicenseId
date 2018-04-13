package license.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import license.conf.SignStrategy;

import com.google.common.io.BaseEncoding;

@SuppressWarnings("rawtypes")
public class LicenseUtil {


	/**
	 * 判断对象是否为空，对象适用于：集合，字符序列，Map，一维数组，通用对象
	 * 
	 * @param obj
	 * @return
	 * @author focus
	 * @date 2015年9月2日
	 * @time 上午11:06:19
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null)
			return true;
		if (obj instanceof Collection) {
			return ((Collection) obj).isEmpty();
		} else if (obj instanceof CharSequence) {
			return ((CharSequence) obj).toString().trim().length() == 0
					|| obj.toString().trim().equalsIgnoreCase("null")
					|| "".equals(obj.toString().trim());
		} else if (obj instanceof Map) {
			return ((Map) obj).isEmpty();
		} else if (obj instanceof Object[]) {
			return ((Object[]) obj).length == 0;
		}
		return false;
	}

	/**
	 * 判断对象是否为空
	 * 
	 * @param objs
	 * @return
	 * @author focus
	 * @date 2015年9月2日
	 * @time 上午11:07:25
	 */
	public static boolean isEmpty(Object... objs) {
		for (Object obj : objs) {
			if (isEmpty(obj) == false)
				return false;
		}
		return true;
	}

	/**
	 * 判断对象是否不为空
	 * 
	 * @param obj
	 * @return
	 * @author focus
	 * @date 2015年9月2日
	 * @time 上午11:07:34
	 */
	public static boolean isNotEmpty(Object obj) {
		return isEmpty(obj) == false;
	}

	/**
	 * 判断对象是否不为空，必须所有元素都不为空
	 * 
	 * @param objs
	 * @return
	 * @author focus
	 * @date 2015年9月2日
	 * @time 上午11:07:41
	 */
	public static boolean isNotEmpty(Object... objs) {
		for (Object obj : objs) {
			if (isEmpty(obj))
				return false;
		}
		return true;
	}

	/**
	 * 时间开始
	 * 
	 * @return
	 * @author focus
	 * @date 2015年9月8日
	 * @time 下午2:30:43
	 */
	public static long getstartTime() {
		return System.currentTimeMillis();
	}

	/**
	 * 耗时
	 * 
	 * @param startTime
	 * @return
	 * @author focus
	 * @date 2015年9月8日
	 * @time 下午2:30:37
	 */
	public static String elapse(long startTime) {
		return " 耗时: " + (getstartTime() - startTime) + " 毫秒  ";
	}


	/**
	 * 转换Map 为指定的Bean
	 * 
	 * @param clzz
	 * @param map
	 * @return
	 * @author focus
	 * @date 2015年10月9日
	 * @time 下午2:08:56
	 */
	public static <T> T parseMapToBean(Class<T> clzz, Map<String, Object> map) {

		T t = null;
		try {
			t = clzz.newInstance();
			Field[] fields = clzz.getDeclaredFields();
			for (Field field : fields) {

				field.setAccessible(true);
				String fieldName = field.getName();
				Class fieldType = field.getType();

				if (map.containsKey(fieldName)) {
					Object value = map.get(fieldName);
					if (value.getClass().equals(fieldType)) {
						field.set(t, value);
					}
				}
			}

		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}
		return t;
	}

	/**
	 * 获得本地的mac地址
	 * 
	 * @author focus
	 * @date 2015年10月9日
	 * @time 下午2:48:45
	 */
	public static String getLocalMac() {
		return MacAddressAPI.getMACAddress();
	}


	/**
	 * 获取nonce摘要 md5(mac1 + mac2 ....macn)
	 *
	 * @author focus
	 * @date 2015年11月2日
	 * @time 下午1:52:51
	 */
	public static String getNonceDigest(Object macAddresses) {
		String digest = SignStrategy.MD5.getSignManager().createMsgSign(
				macAddresses.toString());
		return getPre(digest, 8);
	}

	public static String getUniqueMarkByNonceFile() {

		String mainBoardSerial = getMainBoardSerialNum();
		// 获取文件中的唯一标识
		// 其实是获取nonceFile 隐藏文件的文件最后一次修改时间的加密作为唯一标识
		File file = getNonceFile();
		if (file == null) {
			return mainBoardSerial;
		}
		return String.valueOf(file.lastModified()) + mainBoardSerial;
	}

	/**
	 * 若在指定目录中没有文件则直接创建文件
	 * 
	 * @author focus
	 * @throws FileNotFoundException
	 * @date 2016年1月25日
	 * @time 下午4:10:40
	 */
	private static File writeNonceFile(File file) {
		try {
			PrintWriter out = new PrintWriter(file);
			String head = "此文件为nonce生成的唯一文件，请勿操作修改或者删除";
			out.println(head);
			try {
				out.println(encryptBASE64(("解密License文件出来了吧，" + " 二逼了吧，"
						+ " 傻了吧，" + " 其实不是这个密码,哈哈哈哈！").getBytes()));
			} catch (Exception e) {
			}
			out.flush();
			out.close();
		} catch (FileNotFoundException e1) {
			throw new RuntimeException(e1);
		}
		return null;
	}

	/**
	 * get main board Serial number
	 * @author focus
	 * @date 2016年2月17日
	 * @time 上午9:57:23
	 */
	public static String getMainBoardSerialNum() {
		String productUUID = "";
		BufferedReader bufferedReader = null;
		BufferedReader errorReader = null;
		Process process = null;
		try {
			// linux下的命令，获取主板id
			process = Runtime.getRuntime().exec("sudo cat /sys/class/dmi/id/product_uuid");
			bufferedReader = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			
			
			while ((productUUID = bufferedReader.readLine()) != null) {
				return productUUID;
			}
			
			errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			
		} catch (Exception e) {
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if(errorReader != null){
					errorReader.close();
				}
			} catch (IOException e1) {
			}
			
			bufferedReader = null;
			process = null;
			bufferedReader = null;
		}
		return productUUID;
	}

	private static boolean isWinSystem() {
		try {
			String os = System.getProperty("os.name");
			return os != null && os.startsWith("Windows");
		} catch (Throwable e) {

		}
		return false;
	}

	private static File getNonceFile() {
		try {
			File file = null;
			if (isWinSystem()) {
				// windows
				file = new File("C:/.nonce");
			} else {
				// unix
				file = new File("/etc", ".nonce");
			}
			operationNonceFile(file);
			return file;
		} catch (IOException e) {
		}
		return null;
	}

	private static void operationNonceFile(File file) throws IOException {
		if (file.exists() == false) {
			file.createNewFile();
			writeNonceFile(file);
			// 设置文件权限
			file.setExecutable(false);
			file.setWritable(false);
			file.setReadable(false);
		}
	}

	/**
	 * 获取nonce 内容 (mac1{4} + mac2{4} + mac3{4}.....macn{4})<br>
	 * {4}表示 字符串的后四位
	 * 
	 * @author focus
	 * @date 2015年11月2日
	 * @time 下午1:53:01
	 */
	public static String getNonceBody(List<String> macAddresses) {
		StringBuilder result = new StringBuilder(macAddresses.size() * 4);
		for (String macAddress : macAddresses) {
			result.append(getSuffix(macAddress, 4));
		}
		return result.toString();
	}

	/**
	 * 写文件
	 * 
	 * @param fileContent
	 * @param file
	 * @author focus
	 * @date 2015年10月10日
	 * @time 上午10:40:13
	 */
	public static void writeFile(String fileContent, File file) {

		PrintWriter write = null;
		try {
			write = new PrintWriter(file);
			write.print(fileContent);
		} catch (FileNotFoundException e) {
		} finally {
			write.flush();
			write.close();
		}
	}

	public static File getFile() {
		File dir = new File("/license");
		File file = new File(dir, "lisence.dat");
		if (file.exists() == false) {
			try {
				if (dir.exists() == false) {
					dir.mkdir();
				}
				file.createNewFile();
			} catch (IOException e) {
			}
		}
		System.out.println(" 文件路径为： " + file.getAbsolutePath());
		return file;
	}



	/**
	 * 读文件
	 * 
	 * @param file
	 * @return
	 * @author focus
	 * @date 2015年10月10日
	 * @time 上午10:40:05
	 */
	public static StringBuffer readFile(File file) {

		StringBuffer buf = new StringBuffer();
		BufferedReader read = null;
		try {
			read = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));
			String str = null;
			while ((str = read.readLine()) != null) {
				buf.append(str);
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e1) {
		} finally {
			try {
				read.close();
			} catch (IOException e) {
			}
		}
		return buf;
	}

	/**
	 * BASE64解密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptBASE64(String key) throws Exception {
		return BaseEncoding.base64().decode(key);
	}

	/**
	 * BASE64加密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptBASE64(byte[] key) throws Exception {
		return BaseEncoding.base64().encode(key);
	}

	public static void main(String[] args) throws Exception {
		// String str = "focus12312312142343333333333333333333333333";
		// String encode = encryptBASE64(str.getBytes());
		// String deCode = new String(decryptBASE64(encode));
		// System.out.println(encode);
		// System.out.println(deCode);
		//
		// System.out.println(Integer.toBinaryString(31));
		// System.out.println(Integer.toBinaryString(129));
		//
		//
		// for(int i = 64; i < 128; i ++){
		// System.out.println( " 第 " + i + " 个字符是:" +(char)(i));
		// }
		//
		// String s = "";
		// System.out.println(s.getBytes()[0]);
		//
		// String str1 = "1234567";
		// System.out.println(getSuffix(str1, 3));

		System.out.println(getUniqueMarkByNonceFile());

		String os = System.getProperty("os.name");
		String osUser = System.getProperty("user.name");

		System.out.println(os);
		System.out.println(osUser);
	}

	/**
	 * 获取字符串的后几位字符
	 *
	 * @author focus
	 * @date 2015年11月2日
	 * @time 下午1:26:28
	 */
	public static String getSuffix(String str, int lastIndex) {
		if (str == null)
			return str;
		if (str.length() < lastIndex) {
			return str;
		}
		return str.substring(str.length() - lastIndex);
	}

	/**
	 * 获取字符串的前面几位
	 *
	 * @author focus
	 * @date 2015年11月2日
	 * @time 下午2:04:58
	 */
	public static String getPre(String str, int preIndex) {
		if (str == null)
			return str;
		if (str.length() < preIndex) {
			return str;
		}
		return str.substring(0, preIndex);
	}


	public static String getLocalIp() {
		if (isWinSystem()) {
			return getWindowIp();
		}
		return getLinuxLocalIp();
	}

	public static String getWindowIp() {
		try {
			InetAddress address = InetAddress.getLocalHost();
			return address.getHostAddress();

		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 获取Linux下的IP地址
	 *
	 * @return IP地址
	 * @throws SocketException
	 */
	private static String getLinuxLocalIp() {

		String ip = "";
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				String name = intf.getName();
				if (!name.contains("docker") && !name.contains("lo")) {
					for (Enumeration<InetAddress> enumIpAddr = intf
							.getInetAddresses(); enumIpAddr.hasMoreElements();) {
						InetAddress inetAddress = enumIpAddr.nextElement();
						if (!inetAddress.isLoopbackAddress()) {
							String ipaddress = inetAddress.getHostAddress()
									.toString();
							if (!ipaddress.contains("::")
									&& !ipaddress.contains("0:0:")
									&& !ipaddress.contains("fe80")) {
								ip = ipaddress;
							}
						}
					}
				}
			}
		} catch (SocketException ex) {
			ip = "127.0.0.1";
		}
		return ip;
	}


	public static String getUnixMACAddress() {
		String mac = null;
		BufferedReader bufferedReader = null;
		Process process = null;
		try {
			// linux下的命令，一般取eth0作为本地主网卡
			process = Runtime.getRuntime().exec("ifconfig");
			// 显示信息中包含有mac地址信息
			bufferedReader = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String line = null;
			int index = -1;
			while ((line = bufferedReader.readLine()) != null) {
				// 寻找标示字符串[hwaddr]
				index = line.toLowerCase().indexOf("hwaddr");
				if (index >= 0) {// 找到了
					// 取出mac地址并去除2边空格
					mac = line.substring(index + "hwaddr".length() + 1).trim();
					break;
				}
			}
		} catch (IOException e) {
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			bufferedReader = null;
			process = null;
		}
		return mac;
	}
}
