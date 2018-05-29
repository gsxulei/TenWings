package com.x62.tw.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.x62.tw.config.Config;
import com.x62.tw.config.Configuration;
import com.x62.tw.dao.DataPluginDao;
import com.x62.tw.pm.DataPluginManager;
import com.x62.tw.result.BaseResult;
import com.x62.tw.utils.IOUtils;
import com.x62.tw.utils.JsonUtils;

@MultipartConfig
@WebServlet("/plugin")
public class DataPluginUploadServlet extends HttpServlet
{
	private static final long serialVersionUID=1L;

	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException
	{
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException
	{
		PrintWriter pw=response.getWriter();
		Config sysConfig=Config.getInstance();
		String ip=request.getRemoteHost();
		Configuration configuration=sysConfig.getConfiguration();
		if(!configuration.updateWhiteList.contains(ip))
		{
			BaseResult result=BaseResult.getError("非法IP");
			pw.write(result.toString());
			IOUtils.close(pw);
			return;
		}

		// request.getServletContext().getRealPath("WEB-INF/plugins");

		String root=sysConfig.getDataPluginsPath();
		Part part=request.getPart("file");
		String cd=part.getHeader("Content-Disposition");
		// 截取不同类型的文件需要自行判断
		String name=cd.substring(cd.lastIndexOf("=")+2,cd.length()-1);

		String _name=name.substring(0,name.lastIndexOf("."));
		String filename=_name+"_"+UUID.randomUUID().toString().toUpperCase();
		File dir=new File(root,filename);
		if(!dir.exists())
		{
			dir.mkdirs();
		}
		File file=new File(dir,name);
		part.write(file.getAbsolutePath());

		String from=file.getAbsolutePath();
		String to=(new File(dir,_name).getAbsolutePath());
		IOUtils.unzip(from,to);

		File config=new File(to,"config.json");
		com.x62.tw.dao.DataPluginDao.Bean bean=JsonUtils.s2o(config,com.x62.tw.dao.DataPluginDao.Bean.class);
		bean.path=filename+File.separator+_name;

		DataPluginManager dpm=DataPluginManager.getInstance();
		dpm.remove(bean.name+"-v"+bean.version);

		DataPluginDao dao=new DataPluginDao();
		dao.addOrUpdate(bean);
	}
}