package com.x62.tw.base.db;

import java.io.FileInputStream;
import java.util.List;

import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;

import com.x62.tw.TenWings;

public abstract class TenWingsDao
{
	protected SqlSessionFactory factory;

	public TenWingsDao(String configName)
	{
		DataBaseConfig config=TenWings.getInstance().getOptions(configName);
		factory=MyBatisFactory.get(config);
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
			for(String path:paths)
			{
				FileInputStream fis=new FileInputStream(path);
				XMLMapperBuilder builder=new XMLMapperBuilder(fis,configuration,path,configuration.getSqlFragments());
				builder.parse();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void addMappers()
	{
	}

	public void addLoadedResource()
	{
	}
}