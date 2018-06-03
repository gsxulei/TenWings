/*建表*/
CREATE DATABASE IF NOT EXISTS ten_wings DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

USE ten_wings;

/*接口详情表*/
CREATE TABLE data_plugin
(
	id INT AUTO_INCREMENT PRIMARY KEY,
	plugin_name VARCHAR(30) COMMENT '数据接口名称',
	plugin_version INT COMMENT '版本',
	path VARCHAR(250) COMMENT '路径'
)

CREATE TABLE data_plugin_access_log
(
	id INT AUTO_INCREMENT PRIMARY KEY,
	data_plugin_id INT COMMENT '接口id',
	access_date BIGINT COMMENT '访问时间',
	access_ip VARCHAR(50) COMMENT '访问者IP',
	access_port INT COMMENT '访问者端口',
	result_code INT COMMENT '访问结果码'
)