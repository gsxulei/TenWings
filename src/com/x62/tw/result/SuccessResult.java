package com.x62.tw.result;

public class SuccessResult<T>extends BaseResult
{
	public T data;

	public SuccessResult(int code,String msg)
	{
		super(code,msg);
	}

	public void setData(T data)
	{
		this.data=data;
	}
}