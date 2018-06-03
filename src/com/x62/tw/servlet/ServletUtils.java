package com.x62.tw.servlet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.x62.tw.bean.TempFileBean;
import com.x62.tw.config.Config;
import com.x62.tw.utils.IOUtils;
import com.x62.tw.utils.Utils;

/**
 * Servlet工具类
 * 
 * @author GSXL
 *
 */
public class ServletUtils
{
	/**
	 * 设置Request和Response编码为UTF-8<br/>
	 * 设置Response的ContentType为"text/html;charset=utf-8"
	 * 
	 * @param req
	 * @param resp
	 */
	public static void set(HttpServletRequest req,HttpServletResponse resp)
	{
		try
		{
			req.setCharacterEncoding("utf-8");
			resp.setCharacterEncoding("utf-8");
			resp.setContentType("text/html;charset=utf-8");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 获取请求客户端的真实IP(Nginx代理)
	 * 
	 * @param req
	 * @return
	 */
	public static String getRealIP(HttpServletRequest req)
	{
		String ip=req.getHeader("X-real-ip");// nginx服务代理
		if(Utils.isEmpty(ip)||"unknown".equalsIgnoreCase(ip))
		{
			ip=req.getHeader("x-forwarded-for");// Squid 服务代理
		}
		if(Utils.isEmpty(ip)||"unknown".equalsIgnoreCase(ip))
		{
			ip=req.getHeader("Proxy-Client-IP");// apache 服务代理
		}
		if(Utils.isEmpty(ip)||"unknown".equalsIgnoreCase(ip))
		{
			ip=req.getHeader("WL-Proxy-Client-IP");// weblogic 服务代理
		}
		if(Utils.isEmpty(ip)||"unknown".equalsIgnoreCase(ip))
		{
			ip=req.getRemoteAddr();
		}

		// 有些网络通过多层代理,那么获取到的ip就会有多个,一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
		if(Utils.notEmpty(ip))
		{
			ip=ip.split(",")[0].trim();
		}
		return ip;
	}

	public static List<TempFileBean> writeFiles(HttpServletRequest req)
	{
		List<TempFileBean> list=new ArrayList<>();

		return list;
	}

	public static TempFileBean writeFile(HttpServletRequest req)
	{
		Config config=Config.getInstance();
		String tempPath=config.getTempPath();
		TempFileBean temp=null;
		try
		{
			Part part=req.getPart("file");
			String cd=part.getHeader("Content-Disposition");
			// 截取不同类型的文件需要自行判断
			String originalName=cd.substring(cd.lastIndexOf("=")+2,cd.length()-1);

			// String _name=fileName.substring(0,index);
			String fileExt=IOUtils.getFileExt(originalName);

			String fileNewName=UUID.randomUUID().toString().toUpperCase()+fileExt;
			temp=new TempFileBean();
			temp.file=new File(tempPath,fileNewName);
			temp.originalName=originalName;
			part.write(temp.file.getAbsolutePath());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			temp=null;
		}
		return temp;
	}
}