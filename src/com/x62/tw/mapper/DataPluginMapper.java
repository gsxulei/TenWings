package com.x62.tw.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.x62.tw.dao.bean.DataPluginBean;

public interface DataPluginMapper
{
	@Insert("insert into data_plugin(plugin_name,plugin_version,path) values(#{name},#{version},#{path})")
	void add(DataPluginBean bean);

	@Update("update data_plugin set path=#{path} where plugin_name=#{name} and plugin_version=#{version}")
	void updatePath(@Param("path") String path,@Param("name") String name,@Param("version") int version);

	@Select("select * from data_plugin")
	@Results({@Result(property="name",column="plugin_name"),@Result(property="version",column="plugin_version")})
	List<DataPluginBean> findAll();

	@Select("select * from data_plugin where plugin_name=#{name} and plugin_version=#{version}")
	@Results({@Result(property="name",column="plugin_name"),@Result(property="version",column="plugin_version")})
	List<DataPluginBean> find(@Param("name") String name,@Param("version") int version);
}