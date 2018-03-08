package com.x62.tw.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.x62.tw.dao.bean.DataPluginAccessLogBean;
import com.x62.tw.mapper.DataPluginAccessLogMapper;
import com.x62.tw.utils.IOUtils;
import com.x62.tw.utils.MyBatisUtils;

public class DataPluginAccessLogDao
{
	private SqlSessionFactory factory;

	public DataPluginAccessLogDao()
	{
		this.factory=MyBatisUtils.getInstance().getFactory();
	}
	
	
	public boolean add(DataPluginAccessLogBean bean)
	{
		boolean result=true;
		SqlSession session=factory.openSession();
		try
		{
			DataPluginAccessLogMapper mapper=session.getMapper(DataPluginAccessLogMapper.class);
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
}
