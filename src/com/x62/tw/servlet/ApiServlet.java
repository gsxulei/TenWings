package com.x62.tw.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.x62.tw.bean.DataReqParam;
import com.x62.tw.config.Config;
import com.x62.tw.dao.DataPluginAccessLogDao;
import com.x62.tw.dao.DataPluginAccessLogDao.Bean;
import com.x62.tw.dao.DataPluginDao;
import com.x62.tw.pm.DataPluginBean;
import com.x62.tw.pm.DataPluginManager;
import com.x62.tw.utils.IOUtils;
import com.x62.tw.utils.JsonUtils;

@WebServlet("/API")
public class ApiServlet extends HttpServlet
{
	private static final long serialVersionUID=1L;

	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException
	{
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException
	{
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter pw=response.getWriter();

		DataPluginAccessLogDao accessLogDao=new DataPluginAccessLogDao();
		Bean log=new Bean();
		log.accessDate=System.currentTimeMillis();
		log.accessIp=request.getRemoteAddr();
		log.accessPort=request.getRemotePort();

		String param=request.getParameter("param");
		if(param==null||"".equals(param))
		{
			pw.write("{\"code\":\"1002\",\"msg\":\"参数错误\"}");
			IOUtils.close(pw);
			log.resultCode=1002;
			accessLogDao.add(log);
			return;
		}
		Config sysConfig=Config.getInstance();

		DataReqParam reqParam=JsonUtils.s2o(param,DataReqParam.class);
		DataPluginManager dpm=DataPluginManager.getInstance();
		DataPluginBean bean=dpm.get(reqParam.name+"-v"+reqParam.version);

		if(bean==null)
		{
			DataPluginDao dao=new DataPluginDao();
			com.x62.tw.dao.DataPluginDao.Bean obj=dao.find(reqParam.name,reqParam.version);
			if(obj!=null)
			{
				File file=new File(sysConfig.getDataPluginsPath(),obj.path);
				dpm.add(file.getAbsolutePath());
				bean=dpm.get(reqParam.name+"-v"+reqParam.version);
			}
		}

		if(bean==null)
		{
			pw.write("{\"code\":\"1003\",\"msg\":\"接口名或版本错误\"}");
			IOUtils.close(pw);
			log.resultCode=1003;
			accessLogDao.add(log);
			return;
		}

		String result="";
		try
		{
			Class<?> action=bean.loader.loadClass(bean.action);
			Method exec=action.getDeclaredMethod(bean.method,String.class);

			Object obj=action.newInstance();
			Object resObj=exec.invoke(obj,reqParam.param);
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

		pw.write(result);
		IOUtils.close(pw);

		DataPluginDao dao=new DataPluginDao();
		com.x62.tw.dao.DataPluginDao.Bean obj=dao.find(reqParam.name,reqParam.version);
		log.dataPluginId=obj.id;
		log.resultCode=1000;
		accessLogDao.add(log);
	}
}