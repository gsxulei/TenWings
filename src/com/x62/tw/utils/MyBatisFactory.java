package com.x62.tw.utils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import com.x62.tw.db.DataBaseConfig;

public class MyBatisFactory
{
	private static Map<String,SqlSessionFactory> sessions=new HashMap<>();

	public static SqlSessionFactory get(DataBaseConfig config)
	{
		SqlSessionFactory factory=sessions.get(config.configName);
		if(factory!=null)
		{
			return factory;
		}
		String url="jdbc:mysql://"+config.ip+":"+config.port+"/"+config.dbName
				+"?useUnicode=true&characterEncoding=utf-8";
		DataSource dataSource=new PooledDataSource(config.driver,url,config.username,config.password);
		TransactionFactory transactionFactory=new JdbcTransactionFactory();
		Environment environment=new Environment("development",transactionFactory,dataSource);
		Configuration configuration=new Configuration(environment);
		factory=new SqlSessionFactoryBuilder().build(configuration);
		sessions.put(config.configName,factory);
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