package com.x62.tw.db;

import com.x62.tw.base.db.DataBase;
import com.x62.tw.base.db.DataBaseConfig;
import com.x62.tw.base.db.DataBaseUpgradeHelper;
import com.x62.tw.config.Config;
import com.x62.tw.config.Configuration;

public class TenWingsDataBase extends DataBaseUpgradeHelper
{
	private static final int version=1;

	public TenWingsDataBase()
	{
		super(version);
	}

	@Override
	public void onCreate(DataBase db)
	{
		// 接口详情表
		StringBuilder data_plugin=new StringBuilder();
		data_plugin.append("CREATE TABLE data_plugin(");
		data_plugin.append("id INT AUTO_INCREMENT PRIMARY KEY,");
		data_plugin.append("plugin_name VARCHAR(30) COMMENT '数据接口名称',");
		data_plugin.append("plugin_version INT COMMENT '版本',");
		data_plugin.append("path VARCHAR(250) COMMENT '路径')");
		data_plugin.append("");
		data_plugin.append("");
		data_plugin.append("");

		StringBuilder data_plugin_access_log=new StringBuilder();
		data_plugin_access_log.append("CREATE TABLE data_plugin_access_log(");
		data_plugin_access_log.append("id INT AUTO_INCREMENT PRIMARY KEY,");
		data_plugin_access_log.append("data_plugin_id INT COMMENT '接口id',");
		data_plugin_access_log.append("access_date BIGINT COMMENT '访问时间',");
		data_plugin_access_log.append("access_ip VARCHAR(50) COMMENT '访问者IP',");
		data_plugin_access_log.append("access_port INT COMMENT '访问者端口',");
		data_plugin_access_log.append("result_code INT COMMENT '访问结果码')");
		data_plugin_access_log.append("");

		db.execSQL(data_plugin.toString());
		db.execSQL(data_plugin_access_log.toString());
	}

	@Override
	public void onUpgrade(DataBase db,int oldVersion)
	{
	}

	@Override
	public DataBaseConfig getConfig()
	{
		Configuration configuration=Config.getInstance().getConfiguration();
		return configuration.getConfig("twdb");
	}

}