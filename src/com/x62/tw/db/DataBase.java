package com.x62.tw.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.x62.tw.utils.IOUtils;

public class DataBase
{
	private DataBaseConfig options;

	public DataBase(DataBaseConfig options)
	{
		this.options=options;
	}

	/**
	 * 执行SQL语句
	 * 
	 * @param sql
	 */
	public void execSQL(String sql)
	{
		execSQL(sql,true);
	}

	public void execSQL(String sql,boolean database)
	{
		Connection conn=null;
		Statement statement=null;
		try
		{
			conn=ConnectionUtils.getConnection(options,database);
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

	public boolean exists(String sql)
	{
		Connection conn=null;
		Statement statement=null;
		ResultSet rs=null;
		boolean result=false;
		try
		{
			conn=ConnectionUtils.getConnection(options,false);
			statement=conn.createStatement();
			rs=statement.executeQuery(sql);
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
		execSQL("CREATE DATABASE IF NOT EXISTS "+options.dbName+" DEFAULT CHARSET utf8 COLLATE utf8_general_ci;",false);
	}

	public int getVersion()
	{
		Connection conn=null;
		Statement statement=null;
		ResultSet rs=null;
		int result=1;
		try
		{
			conn=ConnectionUtils.getConnection(options);
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
		
		
//		try
//		{
//			//Connection conn=ConnectionUtil.getConnection();
//			//Connection conn=JdbcUtils_DBCP.getConnection();
//			Connection conn=ConnPool.getConnection();
//			//System.err.println(conn.getClass());
//			Statement s=conn.createStatement();
//			ResultSet rs=s.executeQuery(sql);
//			ResultSetMetaData rsmd=rs.getMetaData();
//			int colCount=rsmd.getColumnCount();
//			if(isFull)
//			{
//				String[] rowNames=new String[colCount];
//				for(int i=0;i<colCount;i++)
//				{
//					rowNames[i]=rsmd.getColumnName(i+1);
//				}
//				result.add(rowNames);
//			}
//			while(rs.next())
//			{
//				String[] row=new String[colCount];
//				for(int i=0;i<colCount;i++)
//				{
//					row[i]=rs.getString(i+1);
//				}
//				result.add(row);
//			}
//			//System.out.println(conn);
//			ConnPool.release(conn,s,rs);
//			//JdbcUtils_DBCP.release(conn,s,rs);
//			//rs.close();
//			//s.close();
//			//conn.close();
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
	}
}