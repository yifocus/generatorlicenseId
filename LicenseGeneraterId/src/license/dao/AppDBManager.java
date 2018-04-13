package license.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class AppDBManager {

	ConnectionPool pool;
	
	private AppDBManager(){
		pool = new ConnectionPool();
		try {
			pool.start();
		} catch (SQLException e) {
		}
	}
	private static class AppDBManagerHolder{
		public static AppDBManager instance = new AppDBManager();
	}
	
	public static Connection getConnection() throws SQLException {
		return AppDBManagerHolder.instance.pool.getConnection();
	}

}
