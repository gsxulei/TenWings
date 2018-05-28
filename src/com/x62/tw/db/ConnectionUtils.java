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
	public static Connection getConnection(Options options,boolean database)
	{
		Connection conn=null;
		try
		{
			String url="jdbc:mysql://"+options.ip+":"+options.port+"/";
			if(database)
			{
				url+=options.dbName+"?useUnicode=true&characterEncoding=utf-8";
			}
			Class.forName(options.driver);
			conn=DriverManager.getConnection(url,options.username,options.password);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return conn;
	}

	public static Connection getConnection(Options options)
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

	public static class Options
	{
		/**
		 * 数据库驱动字符串,默认为MySQL
		 */
		public String driver="com.mysql.jdbc.Driver";

		/**
		 * 数据库连接URL
		 */
		// public String url;
		public String ip;

		public String port="3306";

		/**
		 * 数据库用户名
		 */
		public String username;

		/**
		 * 数据库密码
		 */
		public String password;

		public String dbName;
	}
}