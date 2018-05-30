package com.x62.tw.pm;

import java.net.URLClassLoader;

import com.x62.tw.base.BaseBean;

public class DataPluginBean extends BaseBean
{
	public String name;
	public int version;
	public String action;
	public String method="exec";

	public transient PluginClassLoader loader;
	public transient URLClassLoader jarLoader;
}