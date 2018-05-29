package com.x62.tw.utils;

import java.security.MessageDigest;

public class Utils
{
	public static String digest(byte[] bytes,String algorithm)
	{
		try
		{
			MessageDigest md=MessageDigest.getInstance(algorithm);
			byte[] result=md.digest(bytes);
			StringBuffer buffer=new StringBuffer();
			for(byte b:result)
			{
				int number=b&0xff;
				String str=Integer.toHexString(number);
				if(str.length()==1)
				{
					buffer.append("0");
				}
				buffer.append(str);
			}
			return buffer.toString();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "";
		}
	}

	public static String md5(byte[] bytes)
	{
		return digest(bytes,"md5");
	}

	/**
	 * 返回32位16进制(128位bit)
	 * 
	 * @param text
	 * @return
	 */
	public static String md5(String text)
	{
		if(text==null||text.length()==0)
		{
			return "";
		}
		return md5(text.getBytes());
	}

	public static String hash1(byte[] bytes)
	{
		return digest(bytes,"SHA1");
	}

	/**
	 * 返回40位16进制(160位bit)
	 * 
	 * @param text
	 * @return
	 */
	public static String hash1(String text)
	{
		if(text==null||text.length()==0)
		{
			return "";
		}
		return hash1(text.getBytes());
	}

	public static String hash256(byte[] bytes)
	{
		return digest(bytes,"SHA-256");
	}

	/**
	 * 返回64位16进制(256位bit)
	 * 
	 * @param text
	 * @return
	 */
	public static String hash256(String text)
	{
		if(text==null||text.length()==0)
		{
			return "";
		}
		return hash256(text.getBytes());
	}
}