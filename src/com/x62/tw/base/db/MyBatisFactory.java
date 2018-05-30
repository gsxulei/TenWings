package com.x62.tw.base.db;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

public class MyBatisFactory
{
	private static Map<String,SqlSessionFactory> sessions=new HashMap<>();

	public static SqlSessionFactory get(DataBaseConfig config)
	{
		SqlSessionFactory factory=sessions.get(config.id);
		if(factory!=null)
		{
			return factory;
		}
		DataSource dataSource=DataSourceFactory.get(config);
		TransactionFactory transactionFactory=new JdbcTransactionFactory();
		Environment environment=new Environment("development",transactionFactory,dataSource);
		Configuration configuration=new Configuration(environment);
		factory=new SqlSessionFactoryBuilder().build(configuration);
		sessions.put(config.id,factory);
		return factory;
	}

	public static SqlSessionFactory get(String resource)
	{
		SqlSessionFactory factory=sessions.get(resource);
		if(factory!=null)
		{
			return factory;
		}

		try
		{
			InputStream is=Resources.getResourceAsStream(resource);
			factory=new SqlSessionFactoryBuilder().build(is);
			sessions.put(resource,factory);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return factory;
	}
}