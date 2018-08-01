package com.x62.tw.base.db;

import java.io.FileInputStream;
import java.lang.reflect.Field;

import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;

import com.x62.tw.TenWings;
import com.x62.tw.base.annotations.MapperMark;
import com.x62.tw.utils.Utils;

public abstract class TenWingsDao
{
	protected SqlSessionFactory factory;
	private ClassLoader classLoader;

	public TenWingsDao()
	{
	}

	public TenWingsDao(String configName)
	{
		DataBaseConfig config=TenWings.getInstance().getOptions(configName);
		init(config);
	}

	public TenWingsDao(DataBaseConfig config)
	{
		init(config);
		// System.out.println("TenWingsDao->"+this);
		// System.out.println("TenWingsDao->"+Thread.currentThread().getContextClassLoader());
		// System.out.println("classLoader->"+classLoader);
	}

	private void init(DataBaseConfig config)
	{
		classLoader=getClass().getClassLoader();
		factory=MyBatisFactory.get(classLoader,config);
		// addMappers();
		// addLoadedResource();
		System.out.println("TenWingsDao->"+this);
		System.out.println("factory->"+factory);
		bindMapper();
	}

	// protected void addMappers(List<Class<?>> mappers)
	// {
	// if(mappers==null||mappers.size()==0)
	// {
	// return;
	// }
	// MapperRegistry registry=factory.getConfiguration().getMapperRegistry();
	// for(Class<?> mapper:mappers)
	// {
	// if(!registry.hasMapper(mapper))
	// {
	// registry.addMapper(mapper);
	// }
	// }
	// }

	// protected synchronized void addLoadedResource(List<String> paths)
	// {
	// if(paths==null||paths.size()==0)
	// {
	// return;
	// }
	// Configuration configuration=factory.getConfiguration();
	// try
	// {
	// ClassLoader loader=Resources.getDefaultClassLoader();
	// System.out.println("loader->"+loader);
	// // Resources.setDefaultClassLoader(classLoader);
	// for(String path:paths)
	// {
	// FileInputStream fis=new FileInputStream(path);
	// XMLMapperBuilder builder=new
	// XMLMapperBuilder(fis,configuration,path,configuration.getSqlFragments());
	// builder.parse();
	// }
	// Resources.setDefaultClassLoader(loader);
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	// public <T> T getMapper(Class<T> clazz)
	// {
	// return MapperUtils.getMapper(clazz,classLoader,factory);
	// }

	// public void addMappers()
	// {
	// }
	//
	// public void addLoadedResource()
	// {
	// }

	/**
	 * 绑定Mapper
	 */
	private void bindMapper()
	{
		Field[] fields=getClass().getDeclaredFields();
		MapperRegistry registry=factory.getConfiguration().getMapperRegistry();
		Configuration configuration=factory.getConfiguration();
		for(Field field:fields)
		{
			MapperMark mark=field.getAnnotation(MapperMark.class);
			if(mark==null)
			{
				continue;
			}
			String path=mark.resource();
			if(Utils.isEmpty(path))
			{
				if(!registry.hasMapper(field.getType()))
				{
					registry.addMapper(field.getType());
				}
			}
			else
			{
				try
				{
					ClassLoader loader=Resources.getDefaultClassLoader();
					System.out.println("loader->"+loader);
					FileInputStream fis=new FileInputStream(path);
					XMLMapperBuilder builder=new XMLMapperBuilder(fis,configuration,path,
							configuration.getSqlFragments());
					builder.parse();
					Resources.setDefaultClassLoader(loader);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}

			try
			{
				Object value=MapperUtils.getMapper(field.getType(),classLoader,factory);
				field.setAccessible(true);
				field.set(this,value);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}