package com.x62.tw.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.x62.tw.servlet.ServletUtils;

/**
 * 字符编码过滤
 * 
 * @author GSXL
 *
 */
@WebFilter(urlPatterns={"/*"},asyncSupported=true)
public class CharacterEncodingFilter implements Filter
{
	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
	}

	@Override
	public void doFilter(ServletRequest request,ServletResponse response,
			FilterChain chain) throws IOException,ServletException
	{
		ServletUtils.set((HttpServletRequest)request,(HttpServletResponse)response);
		chain.doFilter(request,response);
	}

	@Override
	public void destroy()
	{
	}
}