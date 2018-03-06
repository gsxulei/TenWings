package com.x62.tw.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.Flushable;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

/**
 * IO工具
 * 
 * @author 徐雷
 * @since 2017-08-16
 * 
 */
public class IOUtils
{
	/**
	 * 关闭流
	 * 
	 * @param closeables
	 */
	public static void close(Closeable...closeables)
	{
		if(closeables==null)
		{
			return;
		}

		for(Closeable closeable:closeables)
		{
			if(closeable==null)
			{
				continue;
			}

			try
			{
				if(closeable instanceof Flushable)
				{
					((Flushable)closeable).flush();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			try
			{
				closeable.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 解压zip文件,Java自带的解压有问题,有些文件解压出来大小不对<br/>
	 * 
	 * @param from
	 *            zip文件
	 * @param to
	 *            解压路径
	 */
	public static void unzip(String from,String to)
	{
		InputStream is=null;
		OutputStream os=null;
		// zip文件输入流
		ZipArchiveInputStream zais=null;
		ArchiveEntry entry=null;
		try
		{
			is=new FileInputStream(from);
			zais=new ZipArchiveInputStream(is,"UTF-8");

			while(null!=(entry=zais.getNextEntry()))
			{
				String name=entry.getName();
				if(entry.isDirectory())
				{
					File dir=new File(to,entry.getName());
					if(!dir.exists())
					{
						dir.mkdirs();
					}
					continue;
				}
				File file=new File(to,name);
				byte[] buffer=new byte[1024*100];
				os=new FileOutputStream(file);
				int len=-1;
				while((len=zais.read(buffer))!=-1)
				{
					os.write(buffer,0,len);
				}
				os.flush();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(is,os,zais);
		}
		// ZipInputStream zin=null;
		// FileOutputStream fos=null;
		// try
		// {
		// zin=new ZipInputStream(new FileInputStream(from));
		// ZipEntry entry;
		// while((entry=zin.getNextEntry())!=null)
		// {
		// if(entry.isDirectory())
		// {
		// File dir=new File(to,entry.getName());
		// if(!dir.exists())
		// {
		// dir.mkdirs();
		// }
		// continue;
		// }
		//
		// File file=new File(to,entry.getName());
		// fos=new FileOutputStream(file);
		// byte[] b=new byte[102400];
		// int len=0;
		// if((len=zin.read(b))!=-1)
		// {
		// fos.write(b,0,len);
		// }
		// }
		// }
		// catch(Exception e)
		// {
		// e.printStackTrace();
		// }
		// finally
		// {
		// close(zin,fos);
		// }
	}

	public static String readFile(String path)
	{
		StringBuilder result=new StringBuilder();
		BufferedReader reader=null;
		try
		{
			reader=new BufferedReader(new FileReader(path));
			String line="";
			while((line=reader.readLine())!=null)
			{
				result.append(line);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(reader);
		}
		return result.toString();
	}

	public static byte[] readByte(String path)
	{
		InputStream is=null;
		byte[] data=null;
		ByteArrayOutputStream baos=null;
		try
		{
			is=new FileInputStream(path);
			baos=new ByteArrayOutputStream();

			byte[] b=new byte[102400];
			int len=0;
			if((len=is.read(b))!=-1)
			{
				baos.write(b,0,len);
			}
			data=baos.toByteArray();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(is,baos);
		}
		return data;
	}
}