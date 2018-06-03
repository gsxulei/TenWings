package com.x62.tw.base.db;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;

import com.mysql.jdbc.AbandonedConnectionCleanupThread;
import com.x62.tw.config.Config;

public class DataSourceFactory
{
	private static Map<String,PooledDataSource> ds=new HashMap<>();
	private static final String CHARSET="?useUnicode=true&characterEncoding=utf-8";

	public static DataSource get(DataBaseConfig config)
	{
		PooledDataSource dataSource=ds.get(config.id+config.dbName);
		if(dataSource!=null)
		{
			return dataSource;
		}

		String url="jdbc:mysql://"+config.ip+":"+config.port+"/";
		if(config.dbName!=null&&!"".equals(config.dbName))
		{
			url+=config.dbName;
		}
		url+=CHARSET;
		dataSource=new PooledDataSource(config.driver,url,config.username,config.password);
		ds.put(config.id+config.dbName,dataSource);

		return dataSource;
	}

	public static DataSource getLocalMySQL()
	{
		DataBaseConfig config=Config.getInstance().getConfiguration().getConfig("LOCAL_MySQL");
		PooledDataSource dataSource=ds.get(config.id+config.dbName);
		if(dataSource!=null)
		{
			return dataSource;
		}

		String url="jdbc:mysql://"+config.ip+":"+config.port+"/"+CHARSET;
		dataSource=new PooledDataSource(config.driver,url,config.username,config.password);
		ds.put(config.id+config.dbName,dataSource);

		return dataSource;
	}

	/**
	 * 关闭数据库连接池,注销驱动
	 */
	public static void destroy()
	{
		//关闭MySQL清理线程
		try
		{
			AbandonedConnectionCleanupThread.checkedShutdown();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		//关闭连接池
		for(Map.Entry<String,PooledDataSource> entry:ds.entrySet())
		{
			PooledDataSource dataSource=entry.getValue();
			dataSource.forceCloseAll();
		}

		// 注销JDBC驱动时不会影响其他Web应用,getDrivers是根据ClassLoader返回驱动的
		Enumeration<Driver> drivers=DriverManager.getDrivers();
		while(drivers.hasMoreElements())
		{
			try
			{
				Driver driver=drivers.nextElement();
				DriverManager.deregisterDriver(driver);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}