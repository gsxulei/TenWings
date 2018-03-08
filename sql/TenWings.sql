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

CREATE TABLE data_plugin_access_log
(
	id INT AUTO_INCREMENT PRIMARY KEY,
	data_plugin_id INT,/*接口id*/
	access_date BIGINT,/*访问时间*/
	access_ip VARCHAR(50),/*访问者IP*/
	access_port INT,/*访问者端口*/
	result_code INT/*访问结果码*/
)