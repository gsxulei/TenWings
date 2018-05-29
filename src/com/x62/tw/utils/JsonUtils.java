package com.x62.tw.utils;

import java.io.File;

import com.google.gson.Gson;

/**
 * JSON工具
 * 
 * @author GSXL
 *
 */
public class JsonUtils
{
	/**
	 * 对象转字符串
	 * 
	 * @param t
	 *            JSON对象
	 * @return
	 */
	public static <T> String o2s(T t)
	{
		Gson gson=new Gson();
		return gson.toJson(t);
	}

	public static <T> T s2o(String json,Class<T> t)
	{
		Gson gson=new Gson();
		// gson.fromJson(json,String.class);
		return gson.fromJson(json,t);
	}

	public static <T> T s2oFromFile(String path,Class<T> t)
	{
		String json=IOUtils.readFile(path);
		return s2o(json,t);
	}

	public static <T> T s2o(File file,Class<T> t)
	{
		return s2oFromFile(file.getAbsolutePath(),t);
	}
}