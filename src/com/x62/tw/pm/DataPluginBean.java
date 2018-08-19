package com.x62.tw.pm;

import java.net.URLClassLoader;

import com.x62.tw.base.BaseBean;
import com.x62.tw.utils.Utils;

public class DataPluginBean extends BaseBean
{
	public String name;
	public int version;
	public String action;
	public String method="exec";
	public String path;

	public transient PluginClassLoader loader;
	public transient URLClassLoader jarLoader;

	@Override
	public boolean equals(Object obj)
	{
		if(obj==null)
		{
			return false;
		}
		if(obj instanceof DataPluginBean)
		{
			DataPluginBean target=(DataPluginBean)obj;
			if(Utils.isEmpty(name)||Utils.isEmpty(target.name))
			{
				return false;
			}
			return name.equals(target.name)&&version==target.version;
		}
		return false;
	}

	public String getKey()
	{
		return name+"-v"+version;
	}
}