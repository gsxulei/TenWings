package com.x62.tw.config;

import java.util.ArrayList;
import java.util.List;

import com.x62.tw.bean.BaseBean;

/**
 * 配置,对应于config.json文件
 * 
 * @author GSXL
 *
 */
public class Configuration extends BaseBean
{
	/**
	 * TenWings插件存放目录
	 */
	public String appData;

	/**
	 * 中央管理服务器地址,用户下载初始化数据(插件)
	 */
	public String centerManager;

	/**
	 * 自动升级白名单
	 */
	public List<String> updateWhiteList=new ArrayList<>();

	/**
	 * 数据(统计数据)访问白名单
	 */
	public List<String> dataAccessWhiteList=new ArrayList<>();

	/**
	 * 负载均衡
	 */
	public List<String> group=new ArrayList<>();
}