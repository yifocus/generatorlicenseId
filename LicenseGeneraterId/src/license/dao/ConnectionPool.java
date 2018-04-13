package license.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 数据连接池
 * @author focus
 * @date 2015年12月1日
 * @time 下午2:52:08
 */
public class ConnectionPool  implements ConnectionProvider{

	ComboPooledDataSource dataSource;
	
	private boolean isStared = false;
	
	public ConnectionPool(){
	}
	
	@Override
	public synchronized void start() throws SQLException {
		
		if(isStared) return;
		
		ConnectionPoolConfig config = ConnectionPoolConfig.getInstance();
		
		dataSource = new ComboPooledDataSource();
		dataSource.setJdbcUrl(config.getConnectionURL());
		dataSource.setUser(config.getUsername());
		dataSource.setPassword(config.getPassword());
		dataSource.setCheckoutTimeout(config.getConnectionTimeoutSeconds() * 100);
		
		dataSource.setMaxPoolSize(5000);
		dataSource.setMinPoolSize(10);
		dataSource.setAcquireIncrement(5);
		
		dataSource.setMaxIdleTime(config.getConnectionMaxIdleTimeSeconds());
		dataSource.setMaxIdleTimeExcessConnections(20);
		dataSource.setMaxConnectionAge(20);
		dataSource.setIdleConnectionTestPeriod(config.getConnectionIdleTestIntervalSeconds());
		
		dataSource.setPreferredTestQuery("select 1");
		dataSource.setTestConnectionOnCheckin(true);
		dataSource.setTestConnectionOnCheckout(true);
		isStared = true;
	}

	@Override
	public synchronized void shutdown() {
		// TODO Auto-generated method stub
		
		if(dataSource != null){
			dataSource.close();
		}
		dataSource = null;
		
		isStared = false;
	}

	@Override
	public Connection getConnection() throws SQLException {
		// TODO Auto-generated method stub
		if(!isStared){
			throw new SQLException("the connection provider is not start...");
		}
		
		return dataSource.getConnection();
	}

	
	public static void main(String[] args) throws SQLException {
		final ConnectionPool pool = new ConnectionPool();
		pool.start();
		for(int i = 0; i < 2; i++){
			new Thread(){
				public void run(){
					try {
						Connection conn = pool.getConnection();
						yield();
						Statement stmt = conn.createStatement();
						System.out.println("stmt :" + stmt);
						stmt.close();
						conn.close();
						System.out.println(conn);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
				}
			}.start();
		}
		
		
	}
}

