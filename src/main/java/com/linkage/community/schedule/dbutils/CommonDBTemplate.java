package com.linkage.community.schedule.dbutils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.ProxyFactory;
import org.apache.commons.dbutils.QueryLoader;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.dbutils.wrappers.SqlNullCheckedResultSet;
import org.apache.commons.dbutils.wrappers.StringTrimmedResultSet;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * 使用apache common-dbutils做二次封装方便JDBC操作 主要针对数据库的常见操作增、删、改、查以及结合事物、批处理操作的封装
 * 主要简化数据库的操作,是轻量级的数据库封装类,可以集成在spring使用
 * 
 * @author wangjie
 * 
 */
@Repository
public class CommonDBTemplate {
	private static final Logger logger = LoggerFactory.getLogger(CommonDBTemplate.class);
	
	private static ThreadLocal<Connection> connMgr = new ThreadLocal<Connection>();
	
	private final DataSource dataSource;
	
	@Value("${app.switch.threadlocal}")
	private String switchOfThreadLocal;
	
	@Autowired
	public CommonDBTemplate(DataSource dataSource) {
		this.dataSource = dataSource;
		try {
			// TODO WJ You can load more properties file here...
			setDataSource(dataSource);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("read properties file error!!!", e);
		}
	}

	// 获取SQL配置文件的加载器
	//public static final QueryLoader loader = QueryLoader.instance();
	// 存放SQL的缓存对象
	public static Map<String, String> load = null;

	public static Map<String, String> load2 = null;

	// 不带连接池的QueryRunner,链接需要自己关闭或者释放
	protected QueryRunner qr = new QueryRunner() {
		@Override
		protected ResultSet wrap(ResultSet rs) {
			ResultSet wrap1 = StringTrimmedResultSet.wrap(rs);
			SqlNullCheckedResultSet wrapper2 = new SqlNullCheckedResultSet(
					wrap1);
			wrapper2.setNullInt(0);
			wrapper2.setNullObject("");
			// TODO WJ :默认空值设置.....
			// wrapper2.setNullInt(0);
			// 更多的值的装饰器可以加载在这里 wrappers
			return ProxyFactory.instance().createResultSet(wrapper2);
		}
	};
	// 带连接池的QueryRunner,连接的关闭或者释放不需要自己维护
	protected QueryRunner qr2 = null;

	/*static {
		try {
			//load = loader.load("/communitysql.properties");
			//load2 = loader.load("/mysql.properties");
			// TODO WJ You can load more properties file here...
			setDataSource(dataSource);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("read properties file error!!!", e);
		}
	}*/

	public void setDataSource(DataSource ds) {
		qr2 = new QueryRunner(ds) {
			@Override
			protected ResultSet wrap(ResultSet rs) {
				// 字符串中含有空字符串的处理器
				ResultSet wrap1 = StringTrimmedResultSet.wrap(rs);
				// 空值处理器
				SqlNullCheckedResultSet wrapper2 = new SqlNullCheckedResultSet(
						wrap1);
				wrapper2.setNullInt(0);
				wrapper2.setNullObject("");
				// TODO WJ :默认空值设置.....
				// wrapper2.setNullInt(0);
				// 更多的值的装饰器可以加载在这里 wrappers
				return ProxyFactory.instance().createResultSet(wrapper2);
			}
		};
	}
	
	
	//获取当前线程中的链接
	public Connection CurrentConnection()
	{
		Connection conn = null;
		try
		{
			conn = connMgr.get();
			//System.out.println(Thread.currentThread().getName()+"is runing");
			if(null==conn)
			{
				conn = this.dataSource.getConnection();
				connMgr.set(conn);//将获取的链接放在当前线程变量中
			}
			
		}
		catch(SQLException e)
		{
			logger.error("获取数据库连接失败！", e);
			throw new RuntimeException("获取数据库连接失败！", e);
		}
		
		return conn;
	}
	
	//开启事物
	public void beginTransaction()
	{
		Connection conn = this.CurrentConnection();
		try
		{
			
			if(conn.getAutoCommit())
			{
				conn.setAutoCommit(false);
			}
		}
		catch(SQLException e)
		{
			logger.error("开启事物失败！", e);
			throw new RuntimeException("开启事物失败！", e);
		}
	}
	
	//关闭事物
	public void commitTransaction()
	{
		Connection conn = connMgr.get();
		try
		{
			if(null!=conn)
			{
				if(!conn.getAutoCommit())
				{
					conn.commit();
				}
			}
		}
		catch(SQLException e)
		{
			logger.error("开启事物失败！", e);
			throw new RuntimeException("开启事物失败！", e);
		}
	}
	
	//回滚事物
	public void rollbackTransaction()
	{
		Connection conn = connMgr.get();
		try
		{
			if(null!=conn)
			{
				if(!conn.getAutoCommit())
				{
					conn.rollback();
				}
			}
		}
		catch(SQLException e)
		{
			logger.error("回滚事物失败！", e);
			throw new RuntimeException("回滚事物失败！", e);
		}
	}
	
	public void recoverTransction() 
	{
		Connection conn = connMgr.get();
		try
		{
			if(null!=conn)
			{
				if(conn.getAutoCommit())
				{
					conn.setAutoCommit(false);
				}
				else
				{
					conn.setAutoCommit(true);
				}
			}
		}
		catch(SQLException e)
		{
			logger.error("复原事物状态失败！", e);
			throw new RuntimeException("复原事物状态失败！", e);
		}
	}
	
	
	public void closeConnection()
	{
		Connection conn = connMgr.get();
		try
		{
			if(null!=conn)
			{
				conn.close();
				conn = null;
			}
		}
		catch(SQLException e)
		{
			logger.error("关闭链接失败！", e);
			throw new RuntimeException("关闭链接失败！", e);
		}
		finally
		{
			connMgr.remove();
		}
	}
	
	
	

	public List<Map<String, Object>> queryMapList(String sql) {
		return queryMapList(sql, (Object[]) null);
	}

	public List<Map<String, Object>> queryMapList(String sql,
			Object[] params) {
		List<Map<String, Object>> mapList = null;
		try {
			if("1".equals(switchOfThreadLocal))
			{
				mapList = qr.query(this.CurrentConnection(), sql, new MapListHandler(
						new CustomRowProcessor()), params);
			}
			else
			{
				mapList = qr2.query(sql, new MapListHandler(
						new CustomRowProcessor()), params);
			}
		} catch (SQLException e) {
			// e.printStackTrace();
			logger.error("查询SQL异常:" + sql, e);
			throw new RuntimeException("查询SQL异常:" + sql, e);
		}
		return mapList;
	}

	public List<Map<String, Object>> queryMapList(Connection conn,
			String sql) {
		return queryMapList(conn, sql, (Object[]) null);
	}

	public List<Map<String, Object>> queryMapList(Connection conn,
			String sql, Object[] params) {
		List<Map<String, Object>> mapList = null;
		try {
			mapList = qr.query(conn, sql, new MapListHandler(
					new CustomRowProcessor()), params);
		} catch (SQLException e) {
			// e.printStackTrace();
			logger.error("查询SQL异常:" + sql, e);
			throw new RuntimeException("查询SQL异常:" + sql, e);
		}
		return mapList;
	}

	public Map<String, Object> queryOneMap(String sql) {
		return queryOneMap(sql, (Object[]) null);
	}

	public Map<String, Object> queryOneMap(String sql, Object[] params) {
		Map<String, Object> map = null;
		try {
			if("1".equals(switchOfThreadLocal))
			{
				map = qr.query(this.CurrentConnection(), sql, new MapHandler(new CustomRowProcessor()),
						params);
			}
			else
			{
				map = qr2.query(sql, new MapHandler(new CustomRowProcessor()),
						params);
			}
		} catch (SQLException e) {
			logger.error("查询SQL异常:" + sql, e);
			throw new RuntimeException("查询SQL异常:" + sql, e);
		}
		if (null == map)
			map = new LinkedHashMap<String, Object>();
		return map;
	}

	public Map<String, Object> queryOneMap(Connection conn, String sql) {
		return queryOneMap(conn, sql, (Object[]) null);
	}

	public Map<String, Object> queryOneMap(Connection conn, String sql,
			Object[] params) {
		Map<String, Object> map = null;
		try {
			map = qr.query(conn, sql, new MapHandler(new CustomRowProcessor()),
					params);
		} catch (SQLException e) {
			logger.error("查询SQL异常:" + sql, e);
			throw new RuntimeException("查询SQL异常:" + sql, e);
		}
		if (null == map)
			map = new LinkedHashMap<String, Object>();
		return map;
	}

	public <T> T queryOneBean(Class<T> type, String sql) {
		return queryOneBean(type, sql, (Object[]) null);
	}

	public <T> T queryOneBean(Class<T> type, String sql, Object[] params) {
		T bean = null;
		try {
			if("1".equals(switchOfThreadLocal))
			{
				bean = qr.query(this.CurrentConnection(), sql, new BeanHandler<T>(type), params);
			}
			else
			{
				bean = qr2.query(sql, new BeanHandler<T>(type), params);
			}
		} catch (SQLException e) {
			logger.error("查询SQL异常:" + sql, e);
			throw new RuntimeException("查询SQL异常:" + sql, e);
		}
		if (null == bean) {
			try {
				bean = type.newInstance();
			} catch (InstantiationException e) {
				throw new RuntimeException("Cannot create " + type.getName()
						+ ": " + e.getMessage());
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Cannot create " + type.getName()
						+ ": " + e.getMessage());
			}
		}
		return bean;
	}

	public <T> T queryOneBean(Connection conn, Class<T> type, String sql) {
		return queryOneBean(conn, type, sql, (Object[]) null);
	}

	public <T> T queryOneBean(Connection conn, Class<T> type,
			String sql, Object[] params) {
		T bean = null;
		try {
			bean = qr.query(conn, sql, new BeanHandler<T>(type), params);
		} catch (SQLException e) {
			logger.error("查询SQL异常:" + sql, e);
			throw new RuntimeException("查询SQL异常:" + sql, e);
		}
		if (null == bean) {
			try {
				bean = type.newInstance();
			} catch (InstantiationException e) {
				throw new RuntimeException("Cannot create " + type.getName()
						+ ": " + e.getMessage());
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Cannot create " + type.getName()
						+ ": " + e.getMessage());
			}
		}
		return bean;
	}

	public <T> List<T> queryBeanList(Class<T> type, String sql) {
		return queryBeanList(type, sql, (Object[]) null);
	}

	public <T> List<T> queryBeanList(Class<T> type, String sql,
			Object[] params) {
		List<T> beanList = null;
		try {
			if("1".equals(switchOfThreadLocal))
			{
				beanList = qr.query(this.CurrentConnection(), sql, new BeanListHandler<T>(type), params);
			}
			else
			{
				beanList = qr2.query(sql, new BeanListHandler<T>(type), params);
			}
		} catch (SQLException e) {
			logger.error("查询SQL异常:" + sql, e);
			throw new RuntimeException("查询SQL异常:" + sql, e);
		}
		return beanList;
	}

	public <T> List<T> queryBeanList(Connection conn, Class<T> type,
			String sql) {
		return queryBeanList(conn, type, sql, (Object[]) null);
	}

	public <T> List<T> queryBeanList(Connection conn, Class<T> type,
			String sql, Object[] params) {
		List<T> beanList = null;
		try {
			beanList = qr
					.query(conn, sql, new BeanListHandler<T>(type), params);
		} catch (SQLException e) {
			logger.error("查询SQL异常:" + sql, e);
			throw new RuntimeException("查询SQL异常:" + sql, e);
		}
		return beanList;
	}

	public <T> List<T> queryColumnList(Connection conn, int columnIndex,
			String sql) {
		return queryColumnList(conn, columnIndex, sql, (Object[]) null);
	}

	public <T> List<T> queryColumnList(Connection conn,
			String columnName, String sql) {
		return queryColumnList(conn, columnName, sql, (Object[]) null);
	}

	public <T> List<T> queryColumnList(int columnIndex, String sql) {
		return queryColumnList(columnIndex, sql, (Object[]) null);
	}

	public <T> List<T> queryColumnList(String columnName, String sql) {
		return queryColumnList(columnName, sql, (Object[]) null);
	}

	public <T> List<T> queryColumnList(String columnName, String sql,
			Object[] params) {
		List<T> columnValueList = null;
		try {
			if("1".equals(switchOfThreadLocal))
			{
				columnValueList = qr.query(this.CurrentConnection(),sql, new ColumnListHandler<T>(
						columnName), params);
			}
			else
			{
				columnValueList = qr2.query(sql, new ColumnListHandler<T>(
						columnName), params);
			}
			
		} catch (SQLException e) {
			logger.error("查询SQL异常:" + sql, e);
			throw new RuntimeException("查询SQL异常:" + sql, e);
		}
		return columnValueList;
	}

	public <T> List<T> queryColumnList(int columnIndex, String sql,
			Object[] params) {
		List<T> columnValueList = null;
		try {
			if("1".equals(switchOfThreadLocal))
			{
				columnValueList = qr.query(this.CurrentConnection(),sql, new ColumnListHandler<T>(
						columnIndex), params);
			}
			else
			{
				columnValueList = qr2.query(sql, new ColumnListHandler<T>(
						columnIndex), params);
			}
		} catch (SQLException e) {
			logger.error("查询SQL异常:" + sql, e);
			throw new RuntimeException("查询SQL异常:" + sql, e);
		}
		return columnValueList;
	}

	public <T> List<T> queryColumnList(Connection conn,
			String columnName, String sql, Object[] params) {
		List<T> columnValueList = null;
		try {
			columnValueList = qr.query(conn, sql, new ColumnListHandler<T>(
					columnName), params);
		} catch (SQLException e) {
			logger.error("查询SQL异常:" + sql, e);
			throw new RuntimeException("查询SQL异常:" + sql, e);
		}
		return columnValueList;
	}

	public <T> List<T> queryColumnList(Connection conn, int columnIndex,
			String sql, Object[] params) {
		List<T> columnValueList = null;
		try {
			columnValueList = qr.query(conn, sql, new ColumnListHandler<T>(
					columnIndex), params);
		} catch (SQLException e) {
			logger.error("查询SQL异常:" + sql, e);
			throw new RuntimeException("查询SQL异常:" + sql, e);
		}
		return columnValueList;
	}

	public <T> T queryObject(int columnIndex, String sql) {
		return queryObject(columnIndex, sql, (Object[]) null);
	}

	public <T> T queryObject(String columnName, String sql) {
		return queryObject(columnName, sql, (Object[]) null);
	}

	public <T> T queryObject(Connection conn, String columnName,
			String sql) {
		return queryObject(conn, columnName, sql, (Object[]) null);
	}

	public <T> T queryObject(Connection conn, int columnIndex, String sql) {
		return queryObject(conn, columnIndex, sql, (Object[]) null);
	}

	public <T> T queryObject(int columnIndex, String sql, Object[] params) {
		T obj = null;
		try {
			if("1".equals(switchOfThreadLocal))
			{
				obj = qr.query(this.CurrentConnection(), sql, new ScalarHandler<T>(columnIndex), params);
			}
			else
			{
				obj = qr2.query(sql, new ScalarHandler<T>(columnIndex), params);
			}
		} catch (SQLException e) {
			logger.error("查询SQL异常:" + sql, e);
			throw new RuntimeException("查询SQL异常:" + sql, e);
		}
		return obj;
	}

	public <T> T queryObject(String columnName, String sql,
			Object[] params) {
		T obj = null;
		try {
			if("1".equals(switchOfThreadLocal))
			{
				obj = qr.query(this.CurrentConnection(), sql, new ScalarHandler<T>(columnName), params);
			}
			else
			{
				obj = qr2.query(sql, new ScalarHandler<T>(columnName), params);
			}
		} catch (SQLException e) {
			logger.error("查询SQL异常:" + sql, e);
			throw new RuntimeException("查询SQL异常:" + sql, e);
		}
		return obj;
	}

	public <T> T queryObject(Connection conn, String columnName,
			String sql, Object[] params) {
		T obj = null;
		try {
			obj = qr.query(conn, sql, new ScalarHandler<T>(columnName), params);
		} catch (SQLException e) {
			logger.error("查询SQL异常:" + sql, e);
			throw new RuntimeException("查询SQL异常:" + sql, e);
		}
		return obj;
	}

	public <T> T queryObject(Connection conn, int columnIndex,
			String sql, Object[] params) {
		T obj = null;
		try {
			obj = qr
					.query(conn, sql, new ScalarHandler<T>(columnIndex), params);
		} catch (SQLException e) {
			logger.error("查询SQL异常:" + sql, e);
			throw new RuntimeException("查询SQL异常:" + sql, e);
		}
		return obj;
	}

	public int queryCount(String sql) {
		return queryCount(sql, (Object[]) null);
	}

	public int queryCount(Connection conn, String sql) {
		return queryCount(conn, sql, (Object[]) null);
	}

	public int queryCount(String sql, Object[] params) {
		Number num = 0;
		try {
			if("1".equals(switchOfThreadLocal))
			{
				num = (Number) qr.query(this.CurrentConnection(), sql, new ScalarHandler<Object>(),
						params);
			}
			else
			{
				num = (Number) qr2.query(sql, new ScalarHandler<Object>(), params);	
			}
		} catch (SQLException e) {
			logger.error("查询SQL异常:" + sql, e);
			throw new RuntimeException("查询SQL异常:" + sql, e);
		}
		return num.intValue();
	}

	public int queryCount(Connection conn, String sql, Object[] params) {
		Number num = 0;
		try {
			num = (Number) qr.query(conn, sql, new ScalarHandler<Object>(),
					params);
		} catch (SQLException e) {
			logger.error("查询SQL异常:" + sql, e);
			throw new RuntimeException("查询SQL异常:" + sql, e);
		}
		return num.intValue();
	}

	public int saveOrUpdateOrDelete(String sql) {
		return saveOrUpdateOrDelete(sql, (Object[]) null);
	}

	public int saveOrUpdateOrDelete(Connection conn, String sql) {
		return saveOrUpdateOrDelete(conn, sql, (Object[]) null);
	}

	public int saveOrUpdateOrDelete(String sql, Object[] params) {
		int rows = 0;
		try {
			if("1".equals(switchOfThreadLocal))
			{
				rows = qr.update(this.CurrentConnection(), sql, params);
			}
			else
			{
				rows = qr2.update(sql, params);
			}
			
		} catch (SQLException e) {
			logger.error("执行SQL异常:" + sql, e);
			throw new RuntimeException("执行SQL异常:" + sql, e);
		}
		return rows;
	}

	public int saveOrUpdateOrDelete(Connection conn, String sql,
			Object[] params) {
		int rows = 0;
		try {
			rows = qr.update(conn, sql, params);
		} catch (SQLException e) {
			logger.error("执行SQL异常:" + sql, e);
			throw new RuntimeException("执行SQL异常:" + sql, e);
		}
		return rows;
	}

	public int[] batchUpdate(Connection conn, String sql) {
		return batchUpdate(conn, sql, new Object[0][0]);
	}

	public int[] batchUpdate(String sql) {
		return batchUpdate(sql, new Object[0][0]);
	}

	public int[] batchUpdate(String sql, Object[][] params) {
		int[] rows = new int[0];
		try {
			if("1".equals(switchOfThreadLocal))
			{
				rows = qr.batch(this.CurrentConnection(), sql, params);
			}
			else
			{
				rows = qr2.batch(sql, params);
			}
			
		} catch (SQLException e) {
			logger.error("执行SQL异常:" + sql, e);
			throw new RuntimeException("执行SQL异常:" + sql, e);
		}
		return rows;
	}

	public int[] batchUpdate(Connection conn, String sql,
			Object[][] params) {
		int[] rows = new int[0];
		try {
			rows = qr.batch(conn, sql, params);
		} catch (SQLException e) {
			logger.error("执行SQL异常:" + sql, e);
			throw new RuntimeException("执行SQL异常:" + sql, e);
		}
		return rows;
	}

	public <T> T insertAndReturnPK(String sql) {
		return insertAndReturnPK(sql, (Object[]) null);
	}

	public <T> T insertAndReturnPK(Connection conn, String sql) {
		return insertAndReturnPK(conn, sql, (Object[]) null);
	}

	public <T> T insertAndReturnPK(String sql, Object[] params) {
		T primarykey = null;
		try {
			if("1".equals(switchOfThreadLocal))
			{
				primarykey = qr.insert(this.CurrentConnection(), sql, new ScalarHandler<T>(), params);
			}
			else
			{
				primarykey = qr2.insert(sql, new ScalarHandler<T>(), params);
			}
		} catch (SQLException e) {
			logger.error("插入SQL异常:" + sql, e);
			throw new RuntimeException("插入SQL异常:" + sql, e);
		}
		return primarykey;
	}

	public <T> T insertAndReturnPK(Connection conn, String sql,
			Object[] params) {
		T primarykey = null;
		try {
			primarykey = qr.insert(conn, sql, new ScalarHandler<T>(), params);
		} catch (SQLException e) {
			logger.error("插入SQL异常:" + sql, e);
			throw new RuntimeException("插入SQL异常:" + sql, e);
		}
		return primarykey;
	}

	public <T> List<T> insertBatch(String sql) {
		return insertBatch(sql, new Object[0][0]);
	}

	public <T> List<T> insertBatch(Connection conn, String sql) {
		return insertBatch(conn, sql, new Object[0][0]);
	}

	public <T> List<T> insertBatch(String sql, Object[][] params) {
		List<T> primaryKeyList = null;
		try {
			if("1".equals(switchOfThreadLocal))
			{
				primaryKeyList = qr.insertBatch(this.CurrentConnection(), sql,
						new ColumnListHandler<T>(), params);
			}
			else
			{
				primaryKeyList = qr.insertBatch(sql, new ColumnListHandler<T>(),
						params);
			}
			
		} catch (SQLException e) {
			logger.error("插入SQL异常:" + sql, e);
			throw new RuntimeException("插入SQL异常:" + sql, e);
		}
		return primaryKeyList;
	}

	public <T> List<T> insertBatch(Connection conn, String sql,
			Object[][] params) {
		List<T> primaryKeyList = null;
		try {
			primaryKeyList = qr.insertBatch(conn, sql,
					new ColumnListHandler<T>(), params);
		} catch (SQLException e) {
			logger.error("插入SQL异常:" + sql, e);
			throw new RuntimeException("插入SQL异常:" + sql, e);
		}
		return primaryKeyList;
	}

	public List<Map<String, Object>> queryMysqlByPage(String sql,
			int beginIndex, int pageSize) {
		sql = sql + " limit ?,?";
		return queryMapList(sql, new Object[] { new Integer(beginIndex),
				new Integer(pageSize) });
	}

	public List<Map<String, Object>> queryMysqlByPage(Connection conn,
			String sql, int beginIndex, int pageSize) {
		sql = sql + " limit ?,?";
		return queryMapList(conn, sql, new Object[] { new Integer(beginIndex),
				new Integer(pageSize) });
	}

	public List<Map<String, Object>> queryMysqlByPage(String sql,
			Object[] params, int beginIndex, int pageSize) {
		sql = sql + " limit ?,?";
		params = ArrayUtils.addAll(params, new Object[] {
				new Integer(beginIndex), new Integer(pageSize) });
		return queryMapList(sql, params);
	}

	public List<Map<String, Object>> queryMysqlByPage(Connection conn,
			String sql, Object[] params, int beginIndex, int pageSize) {
		sql = sql + " limit ?,?";
		params = ArrayUtils.addAll(params, new Object[] {
				new Integer(beginIndex), new Integer(pageSize) });
		return queryMapList(conn, sql, params);
	}

	public <T> List<T> queryMysqlByPage(Class<T> type, String sql,
			int beginIndex, int pageSize) {
		return queryBeanList(type, sql + " limit ?,?", new Object[] {
				new Integer(beginIndex), new Integer(pageSize) });
	}

	public <T> List<T> queryMysqlByPage(Connection conn, Class<T> type,
			String sql, int beginIndex, int pageSize) {
		return queryBeanList(conn, type, sql + " limit ?,?", new Object[] {
				new Integer(beginIndex), new Integer(pageSize) });
	}

	public <T> List<T> queryMysqlByPage(Class<T> type, String sql,
			Object[] params, int beginIndex, int pageSize) {
		return queryBeanList(type, sql + " limit ?,?", ArrayUtils.addAll(
				params, new Object[] { new Integer(beginIndex),
						new Integer(pageSize) }));
	}

	public <T> List<T> queryMysqlByPage(Connection conn, Class<T> type,
			String sql, Object[] params, int beginIndex, int pageSize) {
		return queryBeanList(conn, type, sql + " limit ?,?", ArrayUtils.addAll(
				params, new Object[] { new Integer(beginIndex),
						new Integer(pageSize) }));
	}

	public static Connection open() throws SQLException {
		return DriverManager.getConnection(load2.get("jdbc.url"), load2
				.get("jdbc.username"), load2.get("jdbc.password"));
	}

	public static void main(String[] args) throws Exception {
		/*String sql = load.get("bbs.bbs_form_type");
		logger.info(sql);
		boolean loadDriver = DbUtils.loadDriver(load2
				.get("jdbc.driverClassName"));
		Connection conn = open();*/
		
		/*List<Map<String, Object>> queryMapList = queryMapList(conn, sql);
		for (Map<String, Object> map : queryMapList) {
			System.out.println(map);
		}*/
		/*
		 * System.out.println("--------------------------------------------------"
		 * ); Map<String, Object> queryOneMap = queryOneMap(conn, sql, new
		 * Object[]{1}); System.out.println(queryOneMap);
		 * System.out.println(queryOneMap.get("forum_type_id") instanceof
		 * String);
		 * System.out.println("--------------------------------------------------"
		 * ); BBsForumType bean = queryOneBean(conn, BBsForumType.class, sql,
		 * new Object[]{1}); System.out.println(bean);
		 */
		/*
		 * Long i = insertAndReturnPK(conn,
		 * "insert into bbs_forum_type (forum_type_name,forum_type_state) values (?,?)"
		 * , new Object[]{"测试4",1}); System.out.println(i);
		 */

		/*
		 * int count = saveOrUpdateOrDelete(conn,
		 * "delete from bbs_forum_type where forum_type_id between ? and ?", new
		 * Object[]{8,12}); System.out.println(count);
		 */
		System.out.println("------------------------------------");
		/*List<Map<String, Object>> pageList = queryMysqlByPage(conn,
				"select * from  bbs_forum_type", 10, 5);
		for (Map<String, Object> map : pageList) {
			System.out.println(map);
		}*/
		
		
	}

}
