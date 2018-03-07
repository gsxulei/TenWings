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
			InputStream inputStream=Resources.getResourceAsStream(resource);
			factory=new SqlSessionFactoryBuilder().build(inputStream);
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
}