package com.x62.tw.base;

import com.x62.tw.utils.JsonUtils;

public class BaseBean
{
	@Override
	public String toString()
	{
		return JsonUtils.o2s(this);
	}
}