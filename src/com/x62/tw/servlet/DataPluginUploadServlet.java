package com.x62.tw.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

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
import com.x62.tw.dao.DataPluginDao.Bean;
import com.x62.tw.pm.DataPluginManager;
import com.x62.tw.result.BaseResult;
import com.x62.tw.utils.DateUtils;
import com.x62.tw.utils.IOUtils;
import com.x62.tw.utils.JsonUtils;

@MultipartConfig
@WebServlet("/plugin")
public class DataPluginUploadServlet extends HttpServlet
{
	private static final long serialVersionUID=1L;

	protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException
	{
		doPost(req,resp);
	}

	protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException
	{
		//ServletUtils.set(req,resp);

		PrintWriter pw=resp.getWriter();
		Config sysConfig=Config.getInstance();
		Configuration configuration=sysConfig.getConfiguration();
		String ip=req.getRemoteHost();
		if(!configuration.updateWhiteList.contains(ip))
		{
			BaseResult result=BaseResult.getError("非法IP");
			pw.write(result.toString());
			IOUtils.close(pw);
			return;
		}

		// request.getServletContext().getRealPath("WEB-INF/plugins");

		String pluginsRoot=sysConfig.getDataPluginsPath();

		Part part=req.getPart("file");
		String cd=part.getHeader("Content-Disposition");
		// 截取不同类型的文件需要自行判断
		String originalName=cd.substring(cd.lastIndexOf("=")+2,cd.length()-1);

		String _name=originalName.substring(0,originalName.lastIndexOf("."));
		// String filename=_name+"_"+UUID.randomUUID().toString().toUpperCase();
		String time=DateUtils.getFileName();
		String filename=_name+"_"+time;
		File pluginsDir=new File(pluginsRoot,filename);
		if(!pluginsDir.exists())
		{
			pluginsDir.mkdirs();
		}
		File file=new File(pluginsDir,originalName);
		part.write(file.getAbsolutePath());

		String json=IOUtils.readConfigFileFromZip(file.getAbsolutePath());
		Bean bean=JsonUtils.s2o(json,Bean.class);

		String key=bean.name+"-v"+bean.version;
		// String from=file.getAbsolutePath();
		File to=new File(pluginsDir,key);
		IOUtils.unzip(file.getAbsolutePath(),to.getAbsolutePath());

		// File config=new File(to,"config.json");
		// com.x62.tw.dao.DataPluginDao.Bean
		// bean=JsonUtils.f2o(config,com.x62.tw.dao.DataPluginDao.Bean.class);
		File newFile=new File(pluginsRoot,key+"_"+time);
		pluginsDir.renameTo(newFile);
		bean.path=newFile.getName()+File.separator+to.getName();

		DataPluginManager dpm=DataPluginManager.getInstance();
		dpm.remove(key);

		DataPluginDao dao=new DataPluginDao();
		dao.addOrUpdate(bean);

		BaseResult result=BaseResult.getSuccess("接口插件上传成功");
		pw.write(result.toString());
		IOUtils.close(pw);
	}
}