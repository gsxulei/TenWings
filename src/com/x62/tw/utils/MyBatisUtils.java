package com.x62.tw.utils;

import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MyBatisUtils
{
	private static class Loader
	{
		private static final MyBatisUtils INSTANCE=new MyBatisUtils();
	}

	private SqlSessionFactory factory;

	private MyBatisUtils()
	{
		try
		{
			String resource="mybatis-config.xml";
			InputStream is=Resources.getResourceAsStream(resource);
			factory=new SqlSessionFactoryBuilder().build(is);
			// Configuration config=factory.getConfiguration();
			// config.addMapper(com.x62.tw.dao.DataPluginAccessLogDao.Mapper.class);
			// config.addMapper(com.x62.tw.dao.DataPluginDao.Mapper.class);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static MyBatisUtils getInstance()
	{
		return Loader.INSTANCE;
	}

	public SqlSessionFactory getFactory()
	{
		return factory;
	}

//	public void close()
//	{
//		DataSource ds=factory.getConfiguration().getEnvironment().getDataSource();
//		System.out.println(ds);
//
//		try
//		{
//			while(DriverManager.getDrivers().hasMoreElements())
//			{
//				DriverManager.deregisterDriver(DriverManager.getDrivers().nextElement());
//			}
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
}