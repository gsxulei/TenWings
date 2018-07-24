package com.x62.tw.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.x62.tw.base.db.DataSourceFactory;
import com.x62.tw.config.Config;
import com.x62.tw.config.Version;
import com.x62.tw.db.TenWingsDataBase;
import com.x62.tw.pm.DataPluginManager;

@WebListener
public class WebAppListener implements ServletContextListener
{
	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{
		DataSourceFactory.destroy();
		System.out.println("TenWings "+Version.versionName+" 销毁");
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

		new TenWingsDataBase();

		System.out.println("TenWings "+Version.versionName+" 启动");

		// 加载接口插件
		DataPluginManager dpm=DataPluginManager.getInstance();
		dpm.initLoad();
		System.out.println(dpm.get("SystemInfo-v1"));
	}
}