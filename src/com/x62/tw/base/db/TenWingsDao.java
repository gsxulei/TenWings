package com.x62.tw.base.db;

import java.io.FileInputStream;
import java.util.List;

import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;

import com.x62.tw.TenWings;

public abstract class TenWingsDao
{
	protected SqlSessionFactory factory;
	private ClassLoader classLoader;

	public TenWingsDao()
	{
	}

	public TenWingsDao(String configName)
	{
		this.classLoader=this.getClass().getClassLoader();
		DataBaseConfig config=TenWings.getInstance().getOptions(configName);
		init(config);
	}

	public TenWingsDao(DataBaseConfig config)
	{
		classLoader=getClass().getClassLoader();
		init(config);
		System.out.println("TenWingsDao->"+this);
		//System.out.println("TenWingsDao->"+Thread.currentThread().getContextClassLoader());
		System.out.println("classLoader->"+classLoader);
	}

	private void init(DataBaseConfig config)
	{
		factory=MyBatisFactory.get(classLoader,config);
		addMappers();
		addLoadedResource();
	}

	protected void addMappers(List<Class<?>> mappers)
	{
		if(mappers==null||mappers.size()==0)
		{
			return;
		}
		MapperRegistry registry=factory.getConfiguration().getMapperRegistry();
		for(Class<?> mapper:mappers)
		{
			if(!registry.hasMapper(mapper))
			{
				registry.addMapper(mapper);
			}
		}
	}

	protected void addLoadedResource(List<String> paths)
	{
		if(paths==null||paths.size()==0)
		{
			return;
		}
		Configuration configuration=factory.getConfiguration();
		try
		{
			ClassLoader loader=Resources.getDefaultClassLoader();
			System.out.println("loader->"+loader);
			Resources.setDefaultClassLoader(classLoader);
			for(String path:paths)
			{
				FileInputStream fis=new FileInputStream(path);
				XMLMapperBuilder builder=new XMLMapperBuilder(fis,configuration,path,configuration.getSqlFragments());
				builder.parse();
			}
			Resources.setDefaultClassLoader(loader);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public <T> T getMapper(Class<T> clazz)
	{
		return MapperUtils.getMapper(clazz,classLoader,factory);
	}

	public void addMappers()
	{
	}

	public void addLoadedResource()
	{
	}
}