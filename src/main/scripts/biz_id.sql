
DROP TABLE IF EXISTS biz_id;
CREATE TABLE biz_id (
  id int(11) NOT NULL AUTO_INCREMENT,
  biz_type varchar(30) DEFAULT NULL,
  current_max int(11) not null DEFAULT 0,
  step int NOT NULL DEFAULT 1000,
  update_time timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  module VARCHAR(30)  DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO `biz_id` VALUES (null, 'STORE_ORDER_ID', 0, 1000, '2017-05-11 21:31:15', 'order_center');
INSERT INTO `biz_id` VALUES (null, 'STORE_ORDER_NO', 0, 1000, '2017-05-11 21:31:15', 'order_center');
INSERT INTO `biz_id` VALUES (null, 'PAY_ORDER_RECORD_ID', 0, 1000, '2017-05-11 21:29:57', 'pay_center');
INSERT INTO `biz_id` VALUES (null, 'PAY_ORDER_RECORD_NO', 0, 1000, '2017-05-11 21:29:57', 'pay_center');
INSERT INTO `biz_id` VALUES (null, 'CUSTOMER_ID', 0, 1000, '2017-05-11 21:19:35', 'scrm_center');
INSERT INTO `biz_id` VALUES (null, 'RECIPIENT_ACCOUNT_ID', 0, 1000, '2017-05-11 21:29:57', 'pay_center');