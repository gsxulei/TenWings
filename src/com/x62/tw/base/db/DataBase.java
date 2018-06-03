package com.x62.tw.base.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import com.x62.tw.utils.IOUtils;

public class DataBase
{
	private DataBaseConfig config;
	private DataBaseConfig mysql;

	private DataSource ds;

	public DataBase(DataBaseConfig config)
	{
		this.config=config;
		mysql=getMySQL();
		ds=DataSourceFactory.get(config);
	}

	/**
	 * 执行SQL语句
	 * 
	 * @param sql
	 */
	public void execSQL(String sql)
	{
		Connection conn=null;
		Statement statement=null;
		try
		{
			conn=ds.getConnection();
			statement=conn.createStatement();
			statement.executeUpdate(new String(sql.getBytes(),"utf-8"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			IOUtils.close(statement,conn);
		}
	}

	public boolean dbExists()
	{
		Connection conn=null;
		Statement statement=null;
		ResultSet rs=null;
		boolean result=false;
		try
		{
			conn=DataSourceFactory.get(mysql).getConnection();
			statement=conn.createStatement();
			rs=statement.executeQuery("show databases like '"+config.dbName+"'");
			result=rs.next();
		}
		catch(Exception e)
		{
			result=true;
			e.printStackTrace();
		}
		finally
		{
			IOUtils.close(rs,statement,conn);
		}
		return result;
	}

	public void createDataBase()
	{
		// execSQL("CREATE DATABASE IF NOT EXISTS "+config.dbName+" DEFAULT
		// CHARSET utf8 COLLATE utf8_general_ci;");
		Connection conn=null;
		Statement statement=null;
		try
		{
			conn=DataSourceFactory.get(mysql).getConnection();
			statement=conn.createStatement();
			statement.executeUpdate(
					"CREATE DATABASE IF NOT EXISTS "+config.dbName+" DEFAULT CHARSET utf8 COLLATE utf8_general_ci;");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			IOUtils.close(statement,conn);
		}
	}

	public int getVersion()
	{
		Connection conn=null;
		Statement statement=null;
		ResultSet rs=null;
		int result=1;
		try
		{
			conn=ds.getConnection();
			statement=conn.createStatement();
			rs=statement.executeQuery("select config_value from config where config_key='version'");
			rs.next();
			result=rs.getInt(1);
		}
		catch(Exception e)
		{
			result=1;
			e.printStackTrace();
		}
		finally
		{
			IOUtils.close(rs,statement,conn);
		}
		return result;
	}

	private DataBaseConfig getMySQL()
	{
		DataBaseConfig mysql=new DataBaseConfig();
		mysql.driver=config.driver;
		mysql.ip=config.ip;
		mysql.port=config.port;
		mysql.username=config.username;
		mysql.password=config.password;
		mysql.id=config.id;
		return mysql;
	}
}