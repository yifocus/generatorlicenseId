package license.dao;

import java.io.FileInputStream;
import java.util.Properties;

import com.google.common.base.Strings;

public class JiveGlobals {
	
	static Properties p = new Properties();
	
	private static Properties properties(){

		if(p.isEmpty()){
			synchronized (JiveGlobals.class) {
				
				if(p.isEmpty()){
					FileInputStream read;
					try {
						read = new FileInputStream(".././config/Test.properties");
						p.load(read);
						read.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return p;
	}

	public static void setProperty(String key,
			String value) {
		properties().setProperty(key, value);
	}

	public static String getProperty(String key) {
		return properties().getProperty(key);
	}

	public static String getProperty(String key,
			String defaultValue) {
		String value = properties().getProperty(key);
		if(Strings.isNullOrEmpty(value)){
			return defaultValue;
		}
		
		return value;
	}

	public static int getIntProperty(
			String key,
			int defaultValue) {
		
		try {
			return Integer.valueOf(getProperty(key,String.valueOf(defaultValue)));
		} catch (Exception e) {
		}
		return defaultValue;
	}

}
