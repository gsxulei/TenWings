package com.x62.tw.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.x62.tw.config.Config;
import com.x62.tw.config.Configuration;

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
		String ip=req.getRemoteHost();
		Configuration config=Config.getInstance().getConfiguration();
		if(config.updateWhiteList.contains(ip))
		{
			System.out.println("合法IP");
		}
		else
		{
			System.out.println("非法IP");
		}

		System.err.println(ip);
	}
}