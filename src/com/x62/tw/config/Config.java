package com.x62.tw.config;

import java.io.File;

import com.x62.tw.utils.IOUtils;
import com.x62.tw.utils.JsonUtils;

public class Config
{
	private static class Loader
	{
		private static final Config INSTANCE=new Config();
	}

	// private String basePath;
	private File webInf;

	private Configuration configuration;

	private long lastModified=0;

	private Config()
	{
	}

	public static Config getInstance()
	{
		return Loader.INSTANCE;
	}

	// public String getBasePath1()
	// {
	// return basePath;
	// }

	public void setBasePath(String basePath)
	{
		// this.basePath=basePath;
		webInf=new File(basePath,"WEB-INF");
		readConfiguration();
	}

	public String getClassPath()
	{
		File file=new File(webInf,"classes");
		return file.getAbsolutePath();
	}

	private String getPath(String name)
	{
		File file=new File(configuration.appData,name);
		if(file.exists())
		{
			file.mkdirs();
		}
		return file.getAbsolutePath();
	}

	public String getDataBasePath()
	{
		return getPath("db");
	}

	public String getDataPluginsPath()
	{
		return getPath("plugins");
	}

	/**
	 * 获取缓存目录
	 * 
	 * @return ${appData}/cache
	 */
	public String getCachePath()
	{
		return getPath("cache");
	}

	private void readConfiguration()
	{
		File config=new File(getClassPath(),"config.json");
		if(lastModified!=config.lastModified())
		{
			String json=IOUtils.readFile(config.getAbsolutePath());
			configuration=JsonUtils.s2o(json,Configuration.class);
			lastModified=config.lastModified();
		}
	}

	public Configuration getConfiguration()
	{
		readConfiguration();
		return configuration;
	}
}