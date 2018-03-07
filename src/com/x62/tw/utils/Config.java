package com.x62.tw.utils;

import java.io.File;

public class Config
{
	private static class Loader
	{
		private static final Config INSTANCE=new Config();
	}

	private String basePath;
	private File webInf;

	private Config()
	{
	}

	public static Config getInstance()
	{
		return Loader.INSTANCE;
	}

	public String getBasePath()
	{
		return basePath;
	}

	public void setBasePath(String basePath)
	{
		this.basePath=basePath;
		webInf=new File(basePath,"WEB-INF");
	}

	public String getDataPluginsPath()
	{
		File file=new File(webInf,"plugins");
		if(!file.exists())
		{
			file.mkdirs();
		}
		return file.getAbsolutePath();
	}
}