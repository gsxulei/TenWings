package com.x62.tw.result;

import com.x62.tw.base.BaseBean;

public class BaseResult extends BaseBean
{
	public static transient final int OK=1000;
	public static transient final int ERROR=1001;

	public int code;

	public String msg;

	public BaseResult(int code,String msg)
	{
		this.code=code;
		this.msg=msg;
	}

	public static BaseResult getError(String msg)
	{
		return new BaseResult(ERROR,msg);
	}

	public static BaseResult getSuccess(String msg)
	{
		return new BaseResult(OK,msg);
	}
}