package com.x62.tw.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.x62.tw.pm.DataPluginManager;
import com.x62.tw.utils.Config;

@WebListener
public class StartInitListener implements ServletContextListener
{
	@Override
	public void contextDestroyed(ServletContextEvent event)
	{
	}

	@Override
	public void contextInitialized(ServletContextEvent event)
	{
		String basePath=event.getServletContext().getRealPath("");
		Config config=Config.getInstance();
		config.setBasePath(basePath);

		DataPluginManager dpm=DataPluginManager.getInstance();
		dpm.initLoad();
	}
}