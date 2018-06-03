package com.x62.tw.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils
{
	public static String getDate(String pattern)
	{
		SimpleDateFormat sdf=new SimpleDateFormat(pattern);
		Date date=new Date();
		return sdf.format(date);
	}

	public static String getFileName()
	{
		return getDate("yyyyMMddHHmmss");
	}
}