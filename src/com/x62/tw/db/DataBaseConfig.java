package com.x62.tw.db;

public class DataBaseConfig
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

	/**
	 * 数据库名称
	 */
	public String dbName;

	public String configName;
}