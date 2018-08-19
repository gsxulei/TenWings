package com.x62.tw.pm;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.x62.tw.config.Config;
import com.x62.tw.utils.IOUtils;
import com.x62.tw.utils.JsonUtils;
import com.x62.tw.utils.Utils;

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

	private List<DataPluginBean> all=new ArrayList<>();

	private Map<String,DataPluginBean> map=new HashMap<String,DataPluginBean>();
	private Config sysConfig=Config.getInstance();
	File plugin=new File(sysConfig.getConfiguration().appData,"plugin.json");

	public void addOrUpdate(DataPluginBean bean)
	{
		boolean flag=true;
		for(DataPluginBean b:all)
		{
			if(b.equals(bean))
			{
				b.action=bean.action;
				b.method=bean.method;
				b.path=bean.method;
				flag=false;
				break;
			}
		}
		if(flag)
		{
			all.add(bean);
		}
		remove(bean.getKey());
		JsonUtils.o2f(plugin,all);
	}

	private void load(String path)
	{
		File file=new File(sysConfig.getDataPluginsPath(),path);
		File config=new File(file.getAbsolutePath(),"config.json");
		String json=IOUtils.readFile(config.getAbsolutePath());
		if(Utils.isEmpty(json))
		{
			return;
		}
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
		bean.loader=new PluginClassLoader(bean.getKey(),file.getAbsolutePath(),bean.jarLoader);
	}

	public DataPluginBean get(String key)
	{
		DataPluginBean bean=map.get(key);
		if(bean!=null)
		{
			return bean;
		}
		for(DataPluginBean b:all)
		{
			if(b.getKey().equals(key))
			{
				bean=b;
				break;
			}
		}
		if(bean!=null)
		{
			load(bean.path);
		}
		return map.get(key);
	}

	public void remove(String key)
	{
		map.remove(key);
	}

	/**
	 * 初始化加载插件
	 */
	public void init()
	{
		// DataPluginDao dao=new DataPluginDao();
		// List<Bean> list=dao.findAll();
		// for(Bean bean:list)
		// {
		// File file=new File(sysConfig.getDataPluginsPath(),bean.path);
		// add(file.getAbsolutePath());
		// }
		// Bean b=dao.find("SystemInfo",1);
		// System.out.println("b->"+b);
		DataPluginBean[] beans=JsonUtils.f2o(plugin,DataPluginBean[].class);
		if(beans==null)
		{
			return;
		}
		for(DataPluginBean bean:beans)
		{
			all.add(bean);
			load(bean.path);
		}
	}
}