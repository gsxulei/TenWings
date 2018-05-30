package com.x62.tw.base.db;

import org.apache.ibatis.binding.MapperRegistry;
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
	}

	private void addMappers()
	{
		MapperRegistry registry=factory.getConfiguration().getMapperRegistry();
		Class<?>[] mappers=getMappers();
		for(Class<?> mapper:mappers)
		{
			if(!registry.hasMapper(mapper))
			{
				registry.addMapper(mapper);
			}
		}
	}

	public abstract Class<?>[] getMappers();
}