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
import com.x62.tw.result.BaseResult;
import com.x62.tw.utils.IOUtils;

@MultipartConfig
@WebServlet("/war")
public class WarServlet extends HttpServlet
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
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=utf-8");

		PrintWriter pw=resp.getWriter();
		Config sysConfig=Config.getInstance();

		String basePath=sysConfig.getBasePath();
		String root=new File(basePath).getParent();
		Part part=req.getPart("file");
		String cd=part.getHeader("Content-Disposition");
		// 截取不同类型的文件需要自行判断
		String name=cd.substring(cd.lastIndexOf("=")+2,cd.length()-1);

		int index=name.lastIndexOf(".");
		String _name=name.substring(0,index);
		String ext=name.substring(index);
		String warPathName=_name+"_"+System.currentTimeMillis();
		File warPath=new File(root,warPathName);
		if(!warPath.exists())
		{
			warPath.mkdirs();
		}

		String warName=warPathName+ext;
		File warFile=new File(root,warName);
		part.write(warFile.getAbsolutePath());

		IOUtils.unzip(warFile.getAbsolutePath(),warPath.getAbsolutePath());

		// conf\Catalina\localhost
		File tomcat=new File(root).getParentFile();
		File conf=new File(tomcat,"conf");
		File catalina=new File(conf,"Catalina");
		File localhost=new File(catalina,"localhost");
		File xml=new File(localhost,"ROOT.xml");

		try
		{
			String content="<Context docBase=\""+warPath.getAbsolutePath()+"\"/>";
			IOUtils.write(xml.getAbsolutePath(),content);
			BaseResult result=BaseResult.getSuccess("TenWings升级成功");
			pw.write(result.toString());
			IOUtils.close(pw);
		}
		catch(Exception e)
		{
			e.printStackTrace();

			BaseResult result=BaseResult.getError("TenWings升级失败");
			pw.write(result.toString());
			IOUtils.close(pw);
		}
	}
}