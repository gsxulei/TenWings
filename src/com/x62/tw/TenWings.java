package com.x62.tw;

import com.x62.tw.config.Config;
import com.x62.tw.config.Configuration;
import com.x62.tw.db.DataBaseConfig;

public class TenWings
{
	private Config config;
	private Configuration configuration;

	private static class Loader
	{
		private static TenWings INSTANCE=new TenWings();
	}

	private TenWings()
	{
		config=Config.getInstance();
		configuration=config.getConfiguration();
	}

	public String getClassPath()
	{
		return config.getClassPath();
	}

	public DataBaseConfig getOptions(String configName)
	{
		return configuration.getOptions(configName);
	}

	public static TenWings getInstance()
	{
		if(isWeb())
		{
			return Loader.INSTANCE;
		}
		return null;
	}

	public static boolean isWeb()
	{
		boolean isWeb=true;
		try
		{
			TenWings.class.getClassLoader().loadClass("com.x62.tw.config.Config");
		}
		catch(Exception e)
		{
			isWeb=false;
		}
		return isWeb;
	}
}