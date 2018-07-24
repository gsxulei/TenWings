package com.x62.tw.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.SqlSession;

import com.x62.tw.base.BaseBean;
import com.x62.tw.base.db.MapperUtils;
import com.x62.tw.base.db.TenWingsDao;
import com.x62.tw.utils.IOUtils;

public class DataPluginDao extends TenWingsDao
{
	private Mapper mapper;

	public DataPluginDao()
	{
		super("twdb");
		mapper=MapperUtils.getMapper(Mapper.class,"twdb");
	}

	@Override
	public void addMappers()
	{
		List<Class<?>> mappers=new ArrayList<>();
		mappers.add(Mapper.class);
		addMappers(mappers);
	}

	public boolean addOrUpdate(Bean bean)
	{
		boolean result=true;
		try
		{
			Bean obj=find(bean.name,bean.version);
			if(obj==null)
			{
				result=add(bean);
			}
			else
			{
				result=update(bean.path,bean.name,bean.version);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			result=false;
		}
		return result;
	}

	public boolean add(Bean bean)
	{
		boolean result=true;
		SqlSession session=factory.openSession();
		try
		{
			Mapper mapper=session.getMapper(Mapper.class);
			mapper.add(bean);
			session.commit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			result=false;
		}
		finally
		{
			IOUtils.close(session);
		}
		return result;
	}

	public boolean update(String path,String name,int version)
	{
		boolean result=true;
		SqlSession session=factory.openSession();
		try
		{
			Mapper mapper=session.getMapper(Mapper.class);
			mapper.updatePath(path,name,version);
			session.commit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			result=false;
		}
		finally
		{
			IOUtils.close(session);
		}
		return result;
	}

	public Bean find(String name,int version)
	{
		Bean bean=null;
		// SqlSession session=factory.openSession();
		// try
		// {
		// Mapper mapper=session.getMapper(Mapper.class);
		// List<Bean> list=mapper.find(name,version);
		// if(list!=null&&list.size()>0)
		// {
		// bean=list.get(0);
		// }
		// }
		// catch(Exception e)
		// {
		// e.printStackTrace();
		// }
		// finally
		// {
		// IOUtils.close(session);
		// }
		List<Bean> list=mapper.find(name,version);
		if(list!=null&&list.size()>0)
		{
			bean=list.get(0);
		}
		return bean;
	}

	public List<Bean> findAll()
	{
		List<Bean> list=mapper.findAll();
		if(list==null)
		{
			list=new ArrayList<Bean>();
		}
		// Mapper mapper=MapperUtils.getMapper(Mapper.class,"twdb");

		// SqlSession session=factory.openSession();
		// try
		// {
		// Mapper mapper=session.getMapper(Mapper.class);
		// list=mapper.findAll();
		// }
		// catch(Exception e)
		// {
		// e.printStackTrace();
		// }
		// finally
		// {
		// IOUtils.close(session);
		// }
		return list;
	}

	public static class Bean extends BaseBean
	{
		public int id;
		public String name;
		public int version;
		public String path;
	}

	public interface Mapper
	{
		@Insert("insert into data_plugin(plugin_name,plugin_version,path) values(#{name},#{version},#{path})")
		void add(Bean bean);

		@Update("update data_plugin set path=#{path} where plugin_name=#{name} and plugin_version=#{version}")
		void updatePath(@Param("path") String path,@Param("name") String name,@Param("version") int version);

		@Select("select * from data_plugin")
		@Results({@Result(property="name",column="plugin_name"),@Result(property="version",column="plugin_version")})
		List<Bean> findAll();

		@Select("select * from data_plugin where plugin_name=#{name} and plugin_version=#{version}")
		@Results({@Result(property="name",column="plugin_name"),@Result(property="version",column="plugin_version")})
		List<Bean> find(@Param("name") String name,@Param("version") int version);
	}
}