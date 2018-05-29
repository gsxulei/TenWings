package com.x62.tw.db;

public abstract class SQLHelper
{
	private final int version;
	private int oldVersion=1;
	private DataBase db;
	private DataBaseConfig options;

	public SQLHelper(int version)
	{
		this.version=version;
		options=getOptions();
		db=new DataBase(options);
		checkVersion();
	}

	public void checkVersion()
	{
		if(!db.exists("show databases like '"+options.dbName+"'"))
		{
			db.createDataBase();
			db.execSQL("CREATE TABLE config(id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',config_key VARCHAR(50) COMMENT 'key',config_value VARCHAR(255) COMMENT 'value')");
			db.execSQL("insert into config(config_key,config_value) values('version','1')");
			onCreate(db);
		}

		oldVersion=db.getVersion();
		if(version>oldVersion)
		{
			db.execSQL("USE "+options.dbName+";");
			onUpgrade(db,oldVersion);
			db.execSQL("update config set config_value='"+version+"' where config_key='version'");
		}
	}

	protected DataBase getDataBase()
	{
		return db;
	}

	/**
	 * 当且仅当version=1时调用
	 * 
	 * @param db
	 */
	public abstract void onCreate(DataBase db);

	/**
	 * 当version>1时调用
	 * 
	 * @param db
	 * @param oldVersion
	 *            旧版本号
	 * @param newVersion
	 *            新版本号
	 */
	public abstract void onUpgrade(DataBase db,int oldVersion);

	public abstract DataBaseConfig getOptions();
}