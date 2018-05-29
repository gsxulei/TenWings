package com.x62.tw.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionUtils
{
	/**
	 * 获取数据库连接Connection
	 * 
	 * @return Connection
	 */
	public static Connection getConnection(DataBaseConfig config,boolean database)
	{
		Connection conn=null;
		try
		{
			String url="jdbc:mysql://"+config.ip+":"+config.port+"/";
			if(database)
			{
				url+=config.dbName+"?useUnicode=true&characterEncoding=utf-8";
			}
			Class.forName(config.driver);
			conn=DriverManager.getConnection(url,config.username,config.password);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return conn;
	}

	public static Connection getConnection(DataBaseConfig options)
	{
		return getConnection(options,true);
	}

	// private static String Driver="com.mysql.jdbc.Driver";
	//
	// private static String URL="";
	//
	// private static String Name="";
	//
	// private static String Pass="";

	// /**
	// * 读取数据库连接配置
	// */
	// static
	// {
	// Properties properties=new Properties();
	// try
	// {
	// // String
	// // path=ConnectionUtil.class.getResource("").toURI().getPath();
	// // InputStream inStream=new
	// // FileInputStream(path+"dbConfig.properties");
	// InputStream
	// is=ConnectionUtil.class.getResourceAsStream("dbConfig.properties");
	// // String
	// //
	// path=ConnectionUtil.class.getClassLoader().getResource("com/hws/dao/dbConfig.properties").toURI().getPath();
	// // InputStream inStream=new FileInputStream(path);
	// properties.load(is);
	// // is.r
	// // properties.
	// URL=properties.getProperty("URL");
	// Name=properties.getProperty("Name");
	// Pass=properties.getProperty("Pass");
	// is.close();
	// // Class.forName(Driver);
	//
	// /*
	// * try { Class.forName(Driver);
	// * conn=DriverManager.getConnection(URL,Name,Pass); }
	// * catch(Exception e) { e.printStackTrace(); }
	// */
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }
}