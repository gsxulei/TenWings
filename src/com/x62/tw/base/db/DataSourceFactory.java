package com.x62.tw.base.db;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;

import com.x62.tw.config.Config;

public class DataSourceFactory
{
	private static Map<String,DataSource> ds=new HashMap<>();
	private static final String CHARSET="?useUnicode=true&characterEncoding=utf-8";

	public static DataSource get(DataBaseConfig config)
	{
		DataSource dataSource=ds.get(config.id+config.dbName);
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
		DataSource dataSource=ds.get(config.id+config.dbName);
		if(dataSource!=null)
		{
			return dataSource;
		}

		String url="jdbc:mysql://"+config.ip+":"+config.port+"/"+CHARSET;
		dataSource=new PooledDataSource(config.driver,url,config.username,config.password);
		ds.put(config.id+config.dbName,dataSource);

		return dataSource;
	}
}