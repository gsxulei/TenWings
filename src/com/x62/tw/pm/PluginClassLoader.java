package com.x62.tw.pm;

import java.io.File;

import com.x62.tw.utils.IOUtils;

public class PluginClassLoader extends ClassLoader
{
	private String name;
	private String path;
	private final String fileType=".class";

	public PluginClassLoader(String name,String path)
	{
		super();
		this.name=name;
		this.path=path;
	}

	public PluginClassLoader(String name,String path,ClassLoader parent)
	{
		super(parent);
		this.name=name;
		this.path=path;
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException
	{
		String filename=name.replace('.',File.separatorChar);
		File file=new File(path,filename+fileType);
		byte[] data=IOUtils.readByte(file.getAbsolutePath());
		return defineClass(name,data,0,data.length);
	}

	@Override
	public String toString()
	{
		return name;
	}
}