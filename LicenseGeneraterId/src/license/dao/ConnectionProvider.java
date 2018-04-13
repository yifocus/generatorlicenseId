package license.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库连接器
 * 
 * 
 * @author MaiJingFeng
 */
public interface ConnectionProvider {
	
	/**
	 * 启动连接器.
	 * 常用于数据库连接池的启动;
	 * @throws SQLException
	 *
	 */
	public void start() throws SQLException;
	
	/**
	 * 停止连接器.
	 * 常用于数据库连接池的停止
	 *
	 */
	public void shutdown();
	
	/**
	 * 获取数据库连接
	 * @return
	 * @throws SQLException
	 *
	 */
	public Connection getConnection() throws SQLException;
	
	
}
