package com.x62.tw.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.x62.tw.pm.DataPluginManager;
import com.x62.tw.utils.Config;
import com.x62.tw.utils.MyBatisUtils;

@WebListener
public class StartInitListener implements ServletContextListener
{
	@Override
	public void contextDestroyed(ServletContextEvent event)
	{
		MyBatisUtils mbu=MyBatisUtils.getInstance();
		mbu.close();
	}

	/**
	 * 应用启动时初始化工作
	 */
	@Override
	public void contextInitialized(ServletContextEvent event)
	{
		String basePath=event.getServletContext().getRealPath("");
		Config config=Config.getInstance();
		config.setBasePath(basePath);

		//加载接口插件
		DataPluginManager dpm=DataPluginManager.getInstance();
		dpm.initLoad();
	}
}