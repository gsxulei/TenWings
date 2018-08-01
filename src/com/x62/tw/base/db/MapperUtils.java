package com.x62.tw.base.db;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.x62.tw.utils.IOUtils;

public class MapperUtils
{
	public static class ProxyHandler<T> implements InvocationHandler
	{
		private Class<T> clazz;
		private SqlSessionFactory factory;

		public ProxyHandler(Class<T> clazz,SqlSessionFactory factory)
		{
			this.clazz=clazz;
			this.factory=factory;
		}

		@Override
		public Object invoke(Object proxy,Method method,Object[] args) throws Throwable
		{
			if(Object.class.equals(method.getDeclaringClass()))
			{
				try
				{
					return method.invoke(this,args);
				}
				catch(Throwable t)
				{
					t.printStackTrace();
				}
			}
			SqlSession session=null;
			Object result=null;
			try
			{
				session=factory.openSession();
				Object object=session.getMapper(clazz);
				// System.err.println("-----");
				result=method.invoke(object,args);
				session.commit();
				// System.err.println("commit");
				// if(result==null&&method.getReturnType().isAssignableFrom(List.class))
				// {
				// result=new ArrayList<>();
				// }
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				IOUtils.close(session);
			}
			return result;
		}
	};

	// @SuppressWarnings("unchecked")
	// public static <T> T getMapper(Class<T> clazz,String configName)
	// {
	// DataBaseConfig config=TenWings.getInstance().getOptions(configName);
	// SqlSessionFactory factory=MyBatisFactory.get(config);
	//
	// ClassLoader loader=MapperUtils.class.getClassLoader();
	// Class<?>[] interfaces=new Class[]{clazz};
	// ProxyHandler<?> handler=new ProxyHandler<>(clazz,factory);
	// return (T)Proxy.newProxyInstance(loader,interfaces,handler);
	// }
	//
	// @SuppressWarnings("unchecked")
	// public static <T> T getMapper(Class<T> clazz,DataBaseConfig config)
	// {
	// SqlSessionFactory factory=MyBatisFactory.get(config);
	//
	// ClassLoader loader=MapperUtils.class.getClassLoader();
	// Class<?>[] interfaces=new Class[]{clazz};
	// ProxyHandler<?> handler=new ProxyHandler<>(clazz,factory);
	// return (T)Proxy.newProxyInstance(loader,interfaces,handler);
	// }

	@SuppressWarnings("unchecked")
	public static <T> T getMapper(Class<T> clazz,ClassLoader loader,SqlSessionFactory factory)
	{
		// ClassLoader loader=MapperUtils.class.getClassLoader();
		Class<?>[] interfaces=new Class[]{clazz};
		ProxyHandler<?> handler=new ProxyHandler<>(clazz,factory);
		return (T)Proxy.newProxyInstance(loader,interfaces,handler);
	}
}