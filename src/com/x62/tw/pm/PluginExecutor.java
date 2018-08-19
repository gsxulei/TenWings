package com.x62.tw.pm;

import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.AsyncContext;

import com.x62.tw.dao.DataPluginAccessLogDao;
import com.x62.tw.dao.DataPluginDao;
import com.x62.tw.dao.DataPluginAccessLogDao.Bean;
import com.x62.tw.utils.JsonUtils;

public class PluginExecutor implements Runnable
{
	private DataPluginBean bean;
	private PrintWriter pw;
	private String param;
	private AsyncContext asyncContext;

	public PluginExecutor(DataPluginBean bean,AsyncContext asyncContext,String param)
	{
		this.bean=bean;
		this.param=param;
	}

	@Override
	public void run()
	{
		String result="";
		try
		{
			pw=asyncContext.getResponse().getWriter();
			Class<?> action=bean.loader.loadClass(bean.action);
			Method exec=action.getDeclaredMethod(bean.method,String.class);

			Object obj=action.newInstance();
			Object resObj=exec.invoke(obj,param);
			if(resObj instanceof String)
			{
				result=(String)resObj;
			}
			else
			{
				result=JsonUtils.o2s(resObj);
			}

			// result=(String)exec.invoke(obj,reqParam.param);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		System.err.println("pw->"+pw);
		System.err.println("result->"+result);
		pw.write(result);
		//IOUtils.close(pw);

		Bean log=new Bean();
		DataPluginDao dao=new DataPluginDao();
		com.x62.tw.dao.DataPluginDao.Bean obj=dao.find(bean.name,bean.version);

		DataPluginAccessLogDao accessLogDao=new DataPluginAccessLogDao();
		log.dataPluginId=obj.id;
		log.resultCode=1000;
		accessLogDao.add(log);
	}
}