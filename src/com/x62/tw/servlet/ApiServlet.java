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
import com.x62.tw.dao.DataPluginDao;
import com.x62.tw.pm.DataPluginBean;
import com.x62.tw.pm.DataPluginManager;
import com.x62.tw.utils.Config;
import com.x62.tw.utils.IOUtils;
import com.x62.tw.utils.JsonUtils;
import com.x62.tw.utils.MyBatisUtils;

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
		String param=request.getParameter("param");
		Config sysConfig=Config.getInstance();

		DataReqParam reqParam=JsonUtils.s2o(param,DataReqParam.class);
		DataPluginManager dpm=DataPluginManager.getInstance();
		DataPluginBean bean=dpm.get(reqParam.name+"-v"+reqParam.version);

		if(bean==null)
		{
			MyBatisUtils myBatisUtils=MyBatisUtils.getInstance();
			DataPluginDao dao=new DataPluginDao(myBatisUtils.getFactory());
			com.x62.tw.dao.bean.DataPluginBean obj=dao.find(reqParam.name,reqParam.version);
			if(obj!=null)
			{
				File file=new File(sysConfig.getDataPluginsPath(),obj.path);
				dpm.add(file.getAbsolutePath());
				bean=dpm.get(reqParam.name+"-v"+reqParam.version);
			}
		}

		if(bean==null)
		{
			PrintWriter pw=response.getWriter();
			pw.write("{\"msg\":\"接口名或版本错误\"}");
			IOUtils.close(pw);
			return;
		}

		String result="";
		try
		{
			Class<?> action=bean.loader.loadClass(bean.action);
			Method exec=action.getDeclaredMethod(bean.method,String.class);

			Object obj=action.newInstance();
			result=(String)exec.invoke(obj,reqParam.param);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		PrintWriter pw=response.getWriter();
		pw.write(result);
		IOUtils.close(pw);
	}
}