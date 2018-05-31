package com.x62.tw.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.x62.tw.base.db.DataSourceFactory;
import com.x62.tw.config.Config;
import com.x62.tw.pm.DataPluginManager;

@WebListener
public class StartInitListener implements ServletContextListener
{
	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{
		DataSourceFactory.onDestroyed();
		System.err.println("contextDestroyed");
	}

	/**
	 * 应用启动时初始化工作
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		String basePath=sce.getServletContext().getRealPath("");
		Config config=Config.getInstance();
		config.setBasePath(basePath);

		System.out.println("TenWings 1.1");

		// 加载接口插件
		DataPluginManager dpm=DataPluginManager.getInstance();
		dpm.initLoad();
	}
}