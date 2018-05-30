package com.x62.tw.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.session.SqlSession;

import com.x62.tw.base.BaseBean;
import com.x62.tw.base.db.TenWingsDao;
import com.x62.tw.utils.IOUtils;

public class DataPluginAccessLogDao extends TenWingsDao
{
	public DataPluginAccessLogDao()
	{
		super("twdb");
	}

	@Override
	public Class<?>[] getMappers()
	{
		return new Class<?>[]{Mapper.class};
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

	public static class Bean extends BaseBean
	{
		public int id;
		public int dataPluginId;
		public long accessDate;
		public String accessIp;
		public int accessPort;
		public int resultCode;
	}

	public static interface Mapper
	{
		@Insert("insert into data_plugin_access_log(data_plugin_id,access_date,access_ip,"
				+"access_port,result_code) values(#{dataPluginId},#{accessDate},#{accessIp}"
				+",#{accessPort},#{resultCode})")
		void add(Bean bean);
	}
}