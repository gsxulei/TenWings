package com.x62.tw.dao.bean;

import com.x62.tw.bean.BaseBean;

public class DataPluginAccessLogBean extends BaseBean
{
	public int id;
	public int dataPluginId;
	public long accessDate;
	public String accessIp;
	public int accessPort;
	public int resultCode;
}