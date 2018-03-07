/*建表*/
CREATE DATABASE IF NOT EXISTS ten_wings DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

USE ten_wings;

/*接口详情表*/
CREATE TABLE data_plugin
(
	id INT AUTO_INCREMENT PRIMARY KEY,
	plugin_name VARCHAR(30),/*数据接口名称*/
	plugin_version INT,/*版本*/
	path VARCHAR(100)/*路径*/
)