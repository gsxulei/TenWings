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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.x62.tw.config.Config;
import com.x62.tw.config.Configuration;
import com.x62.tw.pm.PluginClassLoader;
import com.x62.tw.result.BaseResult;
import com.x62.tw.utils.IOUtils;

@MultipartConfig
@WebServlet("/db")
public class DataBaseServlet extends HttpServlet
{
	private static final long serialVersionUID=1L;

	@Override
	protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException
	{
		doPost(req,resp);
	}

	@Override
	protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException
	{
		ServletUtils.set(req,resp);

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

		String root=sysConfig.getDataBasePath();
		Part part=req.getPart("file");
		String cd=part.getHeader("Content-Disposition");
		// 截取不同类型的文件需要自行判断
		String name=cd.substring(cd.lastIndexOf("=")+2,cd.length()-1);

		String _name=name.substring(0,name.lastIndexOf("."));
		// String filename=_name+"_"+UUID.randomUUID().toString().toUpperCase();
		String filename=_name+"_"+System.currentTimeMillis();
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
		String json=IOUtils.readFile(config.getAbsolutePath());
		JsonParser jsonParser=new JsonParser();

		try
		{
			JsonObject obj=jsonParser.parse(json).getAsJsonObject();
			String action=obj.get("action").getAsString();
			PluginClassLoader loader=new PluginClassLoader("db",to,getClass().getClassLoader());
			loader.loadClass(action).newInstance();

			BaseResult result=BaseResult.getSuccess("数据库升级插件上传成功");
			pw.write(result.toString());
			IOUtils.close(pw);
		}
		catch(Exception e)
		{
			e.printStackTrace();

			BaseResult result=BaseResult.getError("数据库升级插件上传失败");
			pw.write(result.toString());
			IOUtils.close(pw);
		}
	}
}