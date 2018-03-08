package com.x62.tw.pm;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.x62.tw.dao.DataPluginDao;
import com.x62.tw.utils.Config;
import com.x62.tw.utils.IOUtils;
import com.x62.tw.utils.JsonUtils;

public class DataPluginManager
{
	private static class Loader
	{
		private static final DataPluginManager INSTANCE=new DataPluginManager();
	}

	private DataPluginManager()
	{
	}

	public static DataPluginManager getInstance()
	{
		return Loader.INSTANCE;
	}

	private Map<String,DataPluginBean> map=new HashMap<String,DataPluginBean>();
	private Config sysConfig=Config.getInstance();

	public void add(String path)
	{
		File config=new File(path,"config.json");
		String json=IOUtils.readFile(config.getAbsolutePath());
		DataPluginBean bean=JsonUtils.s2o(json,DataPluginBean.class);
		// System.err.println(bean.toString());
		// System.err.println(getClass().getClassLoader());
		// System.err.println(gson.getClass().getClassLoader());
		map.put(bean.name+"-v"+bean.version,bean);

		File libs=new File(path,"libs");
		List<URL> list=new ArrayList<>();
		if(libs.exists())
		{
			for(File f:libs.listFiles())
			{
				try
				{
					list.add(f.toURI().toURL());
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		bean.jarLoader=new URLClassLoader(list.toArray(new URL[0]),getClass().getClassLoader());
		bean.loader=new PluginClassLoader(bean.name+"-v"+bean.version,path,bean.jarLoader);
	}

	public DataPluginBean get(String key)
	{
		return map.get(key);
	}

	public void remove(String key)
	{
		map.remove(key);
	}

	/**
	 * 初始化加载插件
	 */
	public void initLoad()
	{
		DataPluginDao dao=new DataPluginDao();
		List<com.x62.tw.dao.bean.DataPluginBean> list=dao.findAll();
		for(com.x62.tw.dao.bean.DataPluginBean bean:list)
		{
			File file=new File(sysConfig.getDataPluginsPath(),bean.path);
			add(file.getAbsolutePath());
		}
	}
}