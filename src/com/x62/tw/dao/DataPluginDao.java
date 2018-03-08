package com.x62.tw.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.x62.tw.dao.bean.DataPluginBean;
import com.x62.tw.mapper.DataPluginMapper;
import com.x62.tw.utils.IOUtils;
import com.x62.tw.utils.MyBatisUtils;

public class DataPluginDao
{
	private SqlSessionFactory factory;

	public DataPluginDao()
	{
		//this.factory=factory;
		this.factory=MyBatisUtils.getInstance().getFactory();
	}

	public boolean addOrUpdate(DataPluginBean bean)
	{
		boolean result=true;
		try
		{
			DataPluginBean obj=find(bean.name,bean.version);
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

	public boolean add(DataPluginBean bean)
	{
		boolean result=true;
		SqlSession session=factory.openSession();
		try
		{
			DataPluginMapper mapper=session.getMapper(DataPluginMapper.class);
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
			DataPluginMapper mapper=session.getMapper(DataPluginMapper.class);
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

	public DataPluginBean find(String name,int version)
	{
		DataPluginBean bean=null;
		SqlSession session=factory.openSession();
		try
		{
			DataPluginMapper mapper=session.getMapper(DataPluginMapper.class);
			List<DataPluginBean> list=mapper.find(name,version);
			if(list!=null&&list.size()>0)
			{
				bean=list.get(0);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			IOUtils.close(session);
		}
		return bean;
	}

	public List<DataPluginBean> findAll()
	{
		List<DataPluginBean> list=new ArrayList<DataPluginBean>();
		SqlSession session=factory.openSession();
		try
		{
			DataPluginMapper mapper=session.getMapper(DataPluginMapper.class);
			list=mapper.findAll();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			IOUtils.close(session);
		}
		return list;
	}
}