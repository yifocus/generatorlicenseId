package license.dao;

import license.constant.DBConstant;


public class ConnectionPoolConfig {
	
	private static ConnectionPoolConfig config = new ConnectionPoolConfig();
	
	//数据库用户
	public static final String KEY_STATISTIC_DB_USERNAME = DBConstant.DB_USERNAME;
	
	/**
	 * 数据库加密密码
	 */
	public static final String KEY_STATISTIC_DB_PASSWORD = DBConstant.DB_PASSWORD;
	
	
	public static final String KEY_STATISTIC_DB_POOL_MAX_CONNECTIONS = "plugin.eim.statistics.db.maxConnections";
	public static final String KEY_STATISTIC_DB_POOL_MIN_CONNECTIONS = "plugin.eim.statistics.db.minConnections";
	public static final String KEY_STATISTIC_DB_POOL_CONNECTION_TIMEOUT_SECONDS = "plugin.eim.statistics.db.connectionTimeout.seconds";
	public static final String KEY_STATISTIC_DB_POOL_CONNECTION_MAX_IDLE_SECONDS = "plugin.eim.statistics.db.connection.maxIdle.seconds";
	public static final String KEY_STATISTIC_DB_POOL_CONNECTION_IDLE_TEST_INTERVAL_SECONDS = "plugin.eim.statistics.db.connection.test.interval.seconds";
	
	//STATISTIC数据库默认连接属性
	private String default_db_user="root";
    private String default_db_password="";
      
	private int default_db_minConnections = 5;
	private int default_db_maxConnections = 60;
	
	private int default_db_connectionTimeout_seconds = 20;
	private int default_db_connection_max_idle_seconds = 30 * 60;
	private int default_db_connection_test_idle_interval_seconds = 60;
	
	private String default_db_connection_url = "jdbc:mysql://127.0.0.1:3306/eim?rewriteBatchedStatements=true&useUnicode=true&characterEncoding=UTF-8&characterSetResults=UTF-8";
	private ConnectionPoolConfig(){
	}
	
	public static ConnectionPoolConfig getInstance(){
		return config;
	}
	
	/**
	 * 配置连接STATISTIC数据库的明文密码
	 * @param originalPwd
	 */
	public void setPassword(String originalPwd){
		if(originalPwd != null){
			JiveGlobals.setProperty(KEY_STATISTIC_DB_PASSWORD, default_db_password);
		}
	}
	
	
	
	/**
	 * 获取STATISTIC连接明文密码
	 * @return
	 */
	public String getPassword(){
		//明文密码
		String pwdProperty =JiveGlobals.getProperty(KEY_STATISTIC_DB_PASSWORD);
		
		//若系统当前没有配置该项,则配置为默认值
		if(pwdProperty == null){
			pwdProperty = default_db_password;
		}
		
		return pwdProperty;
		
	}
	
	
	/**
	 * 配置STATISTIC数据库的用户名
	 * @param username
	 */
	public void setUsername(String username){
		if(username != null && !username.isEmpty()){
			JiveGlobals.setProperty(KEY_STATISTIC_DB_USERNAME,username);
		}
	}
	
	
	/**
	 * 获取当前访问STATISTIC数据库的用户名
	 * @return
	 */
	public String getUsername(){
		String username = JiveGlobals.getProperty(KEY_STATISTIC_DB_USERNAME);
		
		//若系统当前没有配置该项,则配置为默认值
		if(username == null){
			//JiveGlobals.setProperty(KEY_STATISTIC_DB_USERNAME, default_db_user);
			username = default_db_user;
		}
		return username;
	}
	
	
	/**
	 * 获取当前连接STATISTIC数据库所使用的访问连接URL, 用于mysql
	 * @return
	 */
	public String getConnectionURL(){
		return JiveGlobals.getProperty(DBConstant.DB_URL,default_db_connection_url);
	}
	
	
	/**
	 * 最大连接数
	 * @return
	 *
	 */
	public int getConnectionMaxNum(){
		int max = default_db_maxConnections;
		String maxNum = JiveGlobals.getProperty(KEY_STATISTIC_DB_POOL_MAX_CONNECTIONS);
		
		if(maxNum != null){
			try{
				max = Integer.parseInt(maxNum);
			}catch(Exception e){
				//ignore;
			}
		}
		
		return max;
	}
	
	
	public void setConnectionMaxNum(int count){
		if(count < 0) return ;
		JiveGlobals.setProperty(KEY_STATISTIC_DB_POOL_MAX_CONNECTIONS, Integer.toString(count));
	}
	
	/**
	 * 初始连接数
	 * @return
	 *
	 */
	public int getConnectionMinNum(){
		int min = default_db_minConnections;
		String minNum = JiveGlobals.getProperty(KEY_STATISTIC_DB_POOL_MIN_CONNECTIONS);
		
		if(minNum != null){
			try{
				min = Integer.parseInt(minNum);
			}catch(Exception e){
				//ignore;
			}
		}
		
		return min;
	}
	
	public void setConnectionMinNum(int count){
		if(count < 0) return ;
		JiveGlobals.setProperty(KEY_STATISTIC_DB_POOL_MIN_CONNECTIONS, Integer.toString(count));
	}
	
	
	/**
	 * Connection连接超时时间
	 * @return SQL连接超时的秒数
	 *
	 */
	public int getConnectionTimeoutSeconds(){
		int seconds = default_db_connectionTimeout_seconds;
		int timeoutSeconds = JiveGlobals.getIntProperty(KEY_STATISTIC_DB_POOL_CONNECTION_TIMEOUT_SECONDS, seconds);
		return timeoutSeconds;
	}
	
	
	public void setConnectionTimeoutSeconds(int timeout){
		if(timeout < 0) return ;
		JiveGlobals.setProperty(KEY_STATISTIC_DB_POOL_CONNECTION_TIMEOUT_SECONDS, Integer.toString(timeout));
	}
	
	
	public int getConnectionMaxIdleTimeSeconds(){
		
		return JiveGlobals.getIntProperty(KEY_STATISTIC_DB_POOL_CONNECTION_MAX_IDLE_SECONDS, default_db_connection_max_idle_seconds);
	}
	
	
	public void setConnectionMaxIdleTimeSeconds(int seconds){
		if(seconds < 0) return ;
		
		JiveGlobals.setProperty(KEY_STATISTIC_DB_POOL_CONNECTION_MAX_IDLE_SECONDS, Integer.toString(seconds));
	}
	
	public int getConnectionIdleTestIntervalSeconds(){
		return JiveGlobals.getIntProperty(KEY_STATISTIC_DB_POOL_CONNECTION_IDLE_TEST_INTERVAL_SECONDS, default_db_connection_test_idle_interval_seconds);
	}
	
	
	public void setConnectionIdleTestIntervalSeconds(int seconds){
		if(seconds < 0) return ;
		
		JiveGlobals.setProperty(KEY_STATISTIC_DB_POOL_CONNECTION_IDLE_TEST_INTERVAL_SECONDS, Integer.toString(seconds));
	}
}
