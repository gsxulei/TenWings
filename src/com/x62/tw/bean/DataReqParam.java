package com.x62.tw.bean;

import com.x62.tw.base.BaseBean;

public class DataReqParam extends BaseBean
{
	public String name;
	public int version;
	public String param;
	public String sign="{}";

	public String getKey()
	{
		return name+"-v"+version;
	}
}