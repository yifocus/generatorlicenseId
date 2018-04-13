package license.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import license.util.AppUtil;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

/**
 * apache dbutils操作数据库工具类
 * 1、包括查询单个、查询集合、查询总数、更新单个、批量更新方法
 * 2、单个update方法默认自动commit
 * 3、批量updateBatch方法根据需要传入commit提交
 * 4、所有更新方法默认不关闭连接conn，由外层去关闭，便于事物的控制
 * （若是由数据源datasource的方式创建连接，则QueryRunner会主动去关闭连接，但对于事物控制不友好）
 * 
 * @author liaofei
 * @date 2015年8月31日
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class BaseDAO {
	
//	private static Logger LOG = LoggerFactory
//			.getLogger(BaseDAO.class);
	
	protected QueryRunner runner = new QueryRunner();

	
	//批量执行的记录数量的阀值
	public static final int THRESHOLD = 500; 

	/**
	 * 查找多个对象集合
	 * @param conn
	 * @param clazz
	 * @param sql
	 * @param params
	 * @param isClose
	 * @return list
	 * @author liaofei
	 * @throws SQLException 
	 * @date 2015年8月31日
	 */
	public <T>List<T> findList(Connection conn, Class<T> clazz, String sql, Object[] params, boolean isClose) throws SQLException {
    	List<T> list = null;
    	try{
    		list = (List<T>)runner.query(conn, sql, new BeanListHandler(clazz), params);
    	}finally{
    		if(isClose)
    			DbUtils.close(conn);
    	}
    	return list;
    }
	
	/**
	 * 查询第一列
	 * @author focus
	 * @date 2016年3月1日
	 * @time 下午4:46:08
	 */
	public <T>List<T> findListByOneColumn(Connection conn,String sql,final Class<T> clazz,String columnName)throws SQLException{
		List<T> list = null;
    	try{
    		list = (List<T>)runner.query(conn, sql, new ColumnListHandler<T>(columnName), new Object[]{});
    	}finally{
    		DbUtils.close(conn);
    	}
    	return list;
	}
	
	public <T>List<T> findList(Connection conn, Class<T> clazz, String sql, Object[] params) throws SQLException {
		return findList(conn, clazz, sql, params, true);
	}
	
	public <T>List<T> findList(Connection conn, Class<T> clazz, String sql) throws SQLException {
		return findList(conn, clazz, sql, null);
	}
	
    /**
     * 查找单个对象
     * @param conn
     * @param clazz
     * @param sql
     * @param params
     * @param isClose
     * @return Object
     * @author liaofei
     * @throws SQLException 
     * @date 2015年8月31日
     */
	public <T>T findOne(Connection conn, Class<T> clazz, String sql, Object[] params, boolean isClose) throws SQLException {
    	T object = null;
    	try{
    		object = (T) runner.query(conn, sql, new BeanHandler(clazz), params);
    	}finally{
    		if(isClose)
    			DbUtils.close(conn);
    	}
    	return object;
    }
	
	public <T>T findOne(Connection conn, Class<T> clazz, String sql) throws SQLException {
		return findOne(conn, clazz, sql, null, true);
	}
	
    
	/**
	 * 查询数据是否存在
	 * @param conn
	 * @param sql
	 * @param params
	 * @return Boolean
	 * @throws SQLException
	 * @author liaofei
	 * @date 2015年8月25日
	 */
	public Boolean isExist(Connection conn, String sql, boolean isClose, Object... params) throws SQLException {//Object[] params
		boolean hasCount = false;
		try{
			hasCount = runner.query(conn, sql, new ResultSetHandler<Boolean>() {
				@Override
				public Boolean handle(ResultSet results) throws SQLException {
					if(results.next()){
						return true;
					}
					return false;
				}
			}, params);
		}finally{
    		if(isClose)
    			DbUtils.close(conn);
		}
		return hasCount;
	}
	/**
	 * 新增、修改、删除数据，针对单操作
	 * @param conn
	 * @param sql
	 * @param params
	 * @param isClose
	 * @return int
	 * @author liaofei
	 * @throws SQLException 
	 * @date 2015年8月31日
	 */
	public int update(Connection conn, String sql, Object[] params, boolean isClose) throws SQLException{
		try{
			return runner.update(conn, sql, params);
		}finally{
    		if(isClose)
    			DbUtils.close(conn);
    	}
	}

	/**
	 * 更新数据
	 * @author focus
	 * @date 2016年2月24日
	 * @time 下午5:10:35
	 */
	public int update(Connection conn, String sql) throws SQLException{
		return update(conn, sql, null);
	}
	
	/**
	 * 更新数据
	 * @author focus
	 * @date 2016年2月24日
	 * @time 下午5:10:35
	 */
	public int update(Connection conn, String sql, Object[] params) throws SQLException{
		return update(conn, sql, params, true);
	}
	
	/**
	 * 添加数据
	 * @author focus
	 * @date 2016年2月24日
	 * @time 下午5:00:39
	 */
	public Long insert(Connection conn, String sql, boolean isClose, Object[] params) throws SQLException{
		try {
				return runner.insert(conn, sql, new ScalarHandler<Long>(), params);
		} finally{
    		if(isClose)
    			DbUtils.close(conn);
    	}
	}
	
	/**
	 * 添加数据
	 *
	 * @author focus
	 * @date 2016年2月24日
	 * @time 下午5:09:56
	 */
	public Long insert(Connection conn, String sql) throws SQLException{
		return insert(conn, sql, true, null);
	}
	
	/**
	 * 添加数据
	 *
	 * @author focus
	 * @date 2016年2月24日
	 * @time 下午5:09:56
	 */
	public Long insert(Connection conn, String sql, Object[] params) throws SQLException{
		return insert(conn, sql, true, params);
	}
	
	
	public <T>int insertBatch(Connection conn,String tableName,List<T> paramList) throws Exception{
		return insertBatch(conn, tableName, paramList,true);
	}
	
	public <T> int deleteBatch(Connection conn,String tableName,List<T> paramList) throws Exception{
		return deleteBatch(conn, tableName, paramList,true);
	}
	
	public <T> int deleteBatch(Connection conn,String tableName,List<T> paramList,boolean isAutoCommit) throws Exception{
		if(AppUtil.isEmpty(paramList)){
			// 若参数为空则
			return 0;
		}
		
		T t = paramList.get(0);
		List<String> columnNames = getFieldNames(t, true);
		List<String> conditions = new ArrayList<String>();
		for(int i = 0,len = columnNames.size(); i < len; i ++){
			String columnName = columnNames.get(i);
			conditions.add(columnName + " = ? and");
		}
		
		// 防止没有条件，和防止条件中的最后一个and 没有延续
		conditions.add(" 1 = 1");
		
		String sql = "delete from " + tableName + " where " + parseListToStr(conditions).replaceAll(",", "");
		Object[][] params = new Object[paramList.size()][columnNames.size()];
		for(int i = 0,len = paramList.size(); i < len; i ++){
			T t1 = paramList.get(i);
			if(t1 == null) continue;
			for(int j = 0,size = columnNames.size(); j < size; j ++){
				params[i][j] = AppUtil.getFieldValue(columnNames.get(j), t1);
			}
		}
		
		return updateBatch(conn, sql, params, isAutoCommit);
	}
	
	/***
	 * 批量添加
	 * @author focus
	 * @date 2016年2月25日
	 * @time 下午5:55:20
	 */
	public <T>int insertBatch(Connection conn,String tableName,List<T> paramList,boolean isAutoCommit) throws Exception{
		
		if(AppUtil.isEmpty(paramList)){
			// 若参数为空则
			return 0;
		}
		
		T t = paramList.get(0);
		List<String> columnNames = getFieldNames(t, false);
		List<String> conditions = new ArrayList<String>();
		for(int i = 0,len = columnNames.size(); i < len; i ++){
			conditions.add("?");
		}
		
		String sql = "insert ignore into " + tableName + " (" +parseListToStr(columnNames) +")values(" +parseListToStr(conditions)+ ")";
		Object[][] params = new Object[paramList.size()][columnNames.size()];
		for(int i = 0,len = paramList.size(); i < len; i ++){
			T t1 = paramList.get(i);
			if(t1 == null) continue;
			for(int j = 0,size = columnNames.size(); j < size; j ++){
				params[i][j] = AppUtil.getFieldValue(columnNames.get(j), t1);
			}
		}
		
		return updateBatch(conn, sql, params, isAutoCommit);
	}
	/***
	 * 批量添加，对空数据的column也插入数据
	 * @author focus
	 * @date 2016年2月25日
	 * @time 下午5:55:20
	 */
	public <T>int insertBatchAll(Connection conn,String tableName,List<T> paramList,boolean isAutoCommit) throws Exception{
		
		if(AppUtil.isEmpty(paramList)){
			// 若参数为空则
			return 0;
		}
		
		T t = paramList.get(0);
		List<String> columnNames = getFieldNames(t, false);
		List<String> conditions = new ArrayList<String>();
		for(int i = 0,len = columnNames.size(); i < len; i ++){
			conditions.add("?");
		}
		
		String sql = "insert ignore into " + tableName + " (" +parseListToStr(columnNames) +")values(" +parseListToStr(conditions)+ ")";
		Object[][] params = new Object[paramList.size()][columnNames.size()];
		for(int i = 0,len = paramList.size(); i < len; i ++){
			T t1 = paramList.get(i);
			if(t1 == null) continue;
			for(int j = 0,size = columnNames.size(); j < size; j ++){
				params[i][j] = AppUtil.getFieldValue(columnNames.get(j), t1);
			}
		}
		
		return updateBatch(conn, sql, params, isAutoCommit);
	}
	
	private String parseListToStr(List<String> list){
		return list.toString().replaceAll("\\[|\\]", "");
	}

	private <T>List<String> getFieldNames(T t, boolean isIgnoreNull) throws Exception {
		
		List<String> fieldNames = new ArrayList<String>();
		List<Field> fields = AppUtil.getAllFields(t.getClass());
		for(Field field : fields){
			
			field.setAccessible(true);
			Object value = field.get(t);
			if(isIgnoreNull && AppUtil.isEmpty(value)) continue;
			
			fieldNames.add(field.getName());
		}
		return fieldNames;
	}
	
	/**
	 * 属性列的类型
	 * @author focus
	 * @date 2016年2月25日
	 * @time 下午7:09:18
	 */
	enum FieldType{
		string, // 字符型
		number, // 数值型
		object; // 非基本类型
	}
	
	/**
	 * 批量更新方法，外面组装二维数据参数
	 * @param conn
	 * @param sql
	 * @param params
	 * @param isCommit
	 * @return int
	 * @throws SQLException
	 * @author liaofei
	 * @date 2015年8月31日
	 */
	public int updateBatch(Connection conn, String sql, Object[][] params, boolean isCommit) throws SQLException{
		int resultCount = 0;
		int size = params.length;
		try {
			//关闭自动提交，批量执行
			conn.setAutoCommit(false);
			for (int i = 0; i < size;) {

				int fromIndex = i;
				i = i < size - THRESHOLD ? i += THRESHOLD : size;
				int toIndex = i;

				//二维数组的复制截取
				Object[][] copyArray = new Object[toIndex-fromIndex][];		
				for(int j = fromIndex; j < toIndex; j++){
					copyArray[j-fromIndex]=new Object[params[j].length];
					System.arraycopy(params[j], 0, copyArray[j-fromIndex], 0, params[j].length);
				}
				
				resultCount += runner.batch(conn, sql, copyArray).length;
			}
			if(isCommit){
				conn.commit();
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			conn.setAutoCommit(true);
			if(isCommit){
				DbUtils.close(conn);
			}
		}
//		LOG.info("[updateBatch]resultCount==" + resultCount);
		return resultCount;
	}
	
	public static void main(String[] args) throws Exception {
		
		
	}
//	private static void printArray(Object[][] array){
//		for(int i=0;i<array.length;i++){
//			for(int j=0;j<array[i].length;j++){
//				System.out.print(array[i][j]+"  ");
//			}
//			System.out.println();
//		}
//	}
    
}
