package com.x62.tw.mapper;

import org.apache.ibatis.annotations.Insert;

import com.x62.tw.dao.bean.DataPluginAccessLogBean;

public interface DataPluginAccessLogMapper
{
	@Insert("insert into data_plugin_access_log(data_plugin_id,access_date,access_ip,access_port,result_code) values(#{dataPluginId},#{accessDate},#{accessIp},#{accessPort},#{resultCode})")
	void add(DataPluginAccessLogBean bean);
}