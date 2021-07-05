
-- DMC dump 1.0.0
-- ------------------------------------------------------
    
-- ----------------------------
-- Table structure for dingdan
-- ----------------------------
    
CREATE TABLE `dingdan` (
  `shangpinid` int(100) NOT NULL COMMENT '商品id',
  `yonghuid` int(100) NOT NULL COMMENT '用户id',
  `dingdanid` int(100) NOT NULL COMMENT '订单id',
  `jianliriqi` varchar(100) NOT NULL COMMENT '建立日期',
  PRIMARY KEY (`dingdanid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单'; 
      
INSERT INTO `dingdan` (`shangpinid`,`yonghuid`,`dingdanid`,`jianliriqi`) VALUES (1,1,1,'202107');
INSERT INTO `dingdan` (`shangpinid`,`yonghuid`,`dingdanid`,`jianliriqi`) VALUES (1,2,2,'202107');
INSERT INTO `dingdan` (`shangpinid`,`yonghuid`,`dingdanid`,`jianliriqi`) VALUES (2,3,3,'202107');
INSERT INTO `dingdan` (`shangpinid`,`yonghuid`,`dingdanid`,`jianliriqi`) VALUES (2,4,4,'202107');
INSERT INTO `dingdan` (`shangpinid`,`yonghuid`,`dingdanid`,`jianliriqi`) VALUES (3,5,5,'202107');
INSERT INTO `dingdan` (`shangpinid`,`yonghuid`,`dingdanid`,`jianliriqi`) VALUES (3,6,6,'202107');
INSERT INTO `dingdan` (`shangpinid`,`yonghuid`,`dingdanid`,`jianliriqi`) VALUES (3,7,7,'202107');
INSERT INTO `dingdan` (`shangpinid`,`yonghuid`,`dingdanid`,`jianliriqi`) VALUES (4,8,8,'202107');
INSERT INTO `dingdan` (`shangpinid`,`yonghuid`,`dingdanid`,`jianliriqi`) VALUES (5,9,9,'202107');
INSERT INTO `dingdan` (`shangpinid`,`yonghuid`,`dingdanid`,`jianliriqi`) VALUES (5,10,10,'202108');
INSERT INTO `dingdan` (`shangpinid`,`yonghuid`,`dingdanid`,`jianliriqi`) VALUES (4,11,11,'202108');
INSERT INTO `dingdan` (`shangpinid`,`yonghuid`,`dingdanid`,`jianliriqi`) VALUES (6,1,12,'202108');
INSERT INTO `dingdan` (`shangpinid`,`yonghuid`,`dingdanid`,`jianliriqi`) VALUES (6,2,13,'202108');
INSERT INTO `dingdan` (`shangpinid`,`yonghuid`,`dingdanid`,`jianliriqi`) VALUES (6,3,14,'202108');
INSERT INTO `dingdan` (`shangpinid`,`yonghuid`,`dingdanid`,`jianliriqi`) VALUES (6,4,15,'202108');
INSERT INTO `dingdan` (`shangpinid`,`yonghuid`,`dingdanid`,`jianliriqi`) VALUES (1,1,16,'202108');
INSERT INTO `dingdan` (`shangpinid`,`yonghuid`,`dingdanid`,`jianliriqi`) VALUES (2,1,17,'202108');
INSERT INTO `dingdan` (`shangpinid`,`yonghuid`,`dingdanid`,`jianliriqi`) VALUES (3,2,18,'202108');
INSERT INTO `dingdan` (`shangpinid`,`yonghuid`,`dingdanid`,`jianliriqi`) VALUES (4,2,19,'202108');
INSERT INTO `dingdan` (`shangpinid`,`yonghuid`,`dingdanid`,`jianliriqi`) VALUES (5,11,20,'202108');
INSERT INTO `dingdan` (`shangpinid`,`yonghuid`,`dingdanid`,`jianliriqi`) VALUES (4,9,21,'202108');
INSERT INTO `dingdan` (`shangpinid`,`yonghuid`,`dingdanid`,`jianliriqi`) VALUES (6,9,22,'202107');
-- ----------------------------
-- Table structure for shangjia
-- ----------------------------
    
CREATE TABLE `shangjia` (
  `shangjiamingchen` varchar(100) DEFAULT NULL COMMENT '商家名称',
  `shangjiaid` int(100) NOT NULL COMMENT '商家id',
  PRIMARY KEY (`shangjiaid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商家'; 
      
INSERT INTO `shangjia` (`shangjiamingchen`,`shangjiaid`) VALUES ('惠普',3);
INSERT INTO `shangjia` (`shangjiamingchen`,`shangjiaid`) VALUES ('飞利浦',4);
INSERT INTO `shangjia` (`shangjiamingchen`,`shangjiaid`) VALUES ('华为',5);
-- ----------------------------
-- Table structure for shangpin
-- ----------------------------
    
CREATE TABLE `shangpin` (
  `shangpinid` int(100) NOT NULL COMMENT '商品id',
  `shangpinmingchen` varchar(100) NOT NULL COMMENT '商品名称',
  `shangpinleibieid` int(11) NOT NULL COMMENT '商品类别id',
  `suoshushangjia` int(100) NOT NULL COMMENT '所属商家',
  `jiage` varchar(100) DEFAULT NULL COMMENT '价格',
  PRIMARY KEY (`shangpinid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品'; 
      
INSERT INTO `shangpin` (`shangpinid`,`shangpinmingchen`,`shangpinleibieid`,`suoshushangjia`,`jiage`) VALUES (1,'笔记本电脑',1,3,'2000');
INSERT INTO `shangpin` (`shangpinid`,`shangpinmingchen`,`shangpinleibieid`,`suoshushangjia`,`jiage`) VALUES (2,'鼠标',1,3,'50');
INSERT INTO `shangpin` (`shangpinid`,`shangpinmingchen`,`shangpinleibieid`,`suoshushangjia`,`jiage`) VALUES (3,'显示器',1,3,'800');
INSERT INTO `shangpin` (`shangpinid`,`shangpinmingchen`,`shangpinleibieid`,`suoshushangjia`,`jiage`) VALUES (4,'手机',1,5,'1000');
INSERT INTO `shangpin` (`shangpinid`,`shangpinmingchen`,`shangpinleibieid`,`suoshushangjia`,`jiage`) VALUES (5,'平板',1,5,'1500');
INSERT INTO `shangpin` (`shangpinid`,`shangpinmingchen`,`shangpinleibieid`,`suoshushangjia`,`jiage`) VALUES (6,'剃须刀',1,4,'80');
-- ----------------------------
-- Table structure for shangpinleibie
-- ----------------------------
    
CREATE TABLE `shangpinleibie` (
  `leibieid` int(100) NOT NULL COMMENT '类别id',
  `leibiemingchen` varchar(100) NOT NULL COMMENT '类别名称',
  PRIMARY KEY (`leibieid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品类别'; 
      
INSERT INTO `shangpinleibie` (`leibieid`,`leibiemingchen`) VALUES (1,'电子产品');
INSERT INTO `shangpinleibie` (`leibieid`,`leibiemingchen`) VALUES (2,'零食');
INSERT INTO `shangpinleibie` (`leibieid`,`leibiemingchen`) VALUES (3,'办公用品');
-- ----------------------------
-- Table structure for yonghuxinxi
-- ----------------------------
    
CREATE TABLE `yonghuxinxi` (
  `shouhuochengshi` varchar(100) NOT NULL COMMENT '收货城市',
  `xingbie` bit(1) NOT NULL COMMENT '性别',
  `yonghuming` varchar(100) NOT NULL COMMENT '用户名',
  `yonghuid` int(100) NOT NULL COMMENT '用户id',
  `shoujihaoma` varchar(100) NOT NULL COMMENT '手机号码',
  `zhuceshijian` varchar(100) DEFAULT NULL COMMENT '注册时间',
  `youjiandizhi` varchar(100) DEFAULT NULL COMMENT '邮件地址',
  `nianling` int(100) DEFAULT NULL COMMENT '年龄',
  PRIMARY KEY (`yonghuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户'; 
      
INSERT INTO `yonghuxinxi` (`shouhuochengshi`,`xingbie`,`yonghuming`,`yonghuid`,`shoujihaoma`,`zhuceshijian`,`youjiandizhi`,`nianling`) VALUES ('广州',CHAR(1),'创客小镇',1,'13425295950','202106','250614271@qq.com',2);
INSERT INTO `yonghuxinxi` (`shouhuochengshi`,`xingbie`,`yonghuming`,`yonghuid`,`shoujihaoma`,`zhuceshijian`,`youjiandizhi`,`nianling`) VALUES ('广州',CHAR(0),'榕树下',2,'15089694509','202105','250614271qq.com',16);
INSERT INTO `yonghuxinxi` (`shouhuochengshi`,`xingbie`,`yonghuming`,`yonghuid`,`shoujihaoma`,`zhuceshijian`,`youjiandizhi`,`nianling`) VALUES ('广州',CHAR(1),'二重奏',3,'13435832113','202107','@qq.com',18);
INSERT INTO `yonghuxinxi` (`shouhuochengshi`,`xingbie`,`yonghuming`,`yonghuid`,`shoujihaoma`,`zhuceshijian`,`youjiandizhi`,`nianling`) VALUES ('东莞',CHAR(1),'潘庆',4,'1342424025','202105','250614271@.qq.com',18);
INSERT INTO `yonghuxinxi` (`shouhuochengshi`,`xingbie`,`yonghuming`,`yonghuid`,`shoujihaoma`,`zhuceshijian`,`youjiandizhi`,`nianling`) VALUES ('东莞',CHAR(1),'泡泡龙',5,'13760031302','202107','250614271@qq.c',22);
INSERT INTO `yonghuxinxi` (`shouhuochengshi`,`xingbie`,`yonghuming`,`yonghuid`,`shoujihaoma`,`zhuceshijian`,`youjiandizhi`,`nianling`) VALUES ('东莞',CHAR(1),'风驰电掣',6,'13432424025','202106','250614271@qq.com',22);
INSERT INTO `yonghuxinxi` (`shouhuochengshi`,`xingbie`,`yonghuming`,`yonghuid`,`shoujihaoma`,`zhuceshijian`,`youjiandizhi`,`nianling`) VALUES ('成都',CHAR(0),'全智贤',7,'13250214271','202104','250614271@qq.com',19);
INSERT INTO `yonghuxinxi` (`shouhuochengshi`,`xingbie`,`yonghuming`,`yonghuid`,`shoujihaoma`,`zhuceshijian`,`youjiandizhi`,`nianling`) VALUES ('成都',CHAR(0),'韧豆腐',8,'177078520223','202107','250614271@qq.com',22);
INSERT INTO `yonghuxinxi` (`shouhuochengshi`,`xingbie`,`yonghuming`,`yonghuid`,`shoujihaoma`,`zhuceshijian`,`youjiandizhi`,`nianling`) VALUES ('北京',CHAR(0),'扁平线',9,'24242424','202106','250614271@qq.com',22);
INSERT INTO `yonghuxinxi` (`shouhuochengshi`,`xingbie`,`yonghuming`,`yonghuid`,`shoujihaoma`,`zhuceshijian`,`youjiandizhi`,`nianling`) VALUES ('北京',CHAR(1),'苏堤春晓',10,'5221','202106','250614271@qq.com',22);
INSERT INTO `yonghuxinxi` (`shouhuochengshi`,`xingbie`,`yonghuming`,`yonghuid`,`shoujihaoma`,`zhuceshijian`,`youjiandizhi`,`nianling`) VALUES ('上海',CHAR(0),'多线程',11,'89612022521\n','202106','250614271@qq.com',20);
-- ----------------------------
-- Table structure for yuechengshixiaoshoue
-- ----------------------------
    
CREATE TABLE `yuechengshixiaoshoue` (
  `riqi` int(100) NOT NULL COMMENT '日期',
  `chengshi` varchar(100) DEFAULT NULL COMMENT '城市',
  `xiaoshoue` int(100) DEFAULT NULL COMMENT '销售额',
  PRIMARY KEY (`riqi`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='月城市销售额'; 
      

-- ----------------------------
-- Table structure for yueshi
-- ----------------------------
    
CREATE TABLE `yueshi` (
  `riqi` int(100) NOT NULL COMMENT '日期',
  `chengshi` varchar(100) DEFAULT NULL COMMENT '城市',
  `xiaoshoue` int(100) DEFAULT NULL COMMENT '销售额',
  `shangpinleibie` int(100) DEFAULT NULL COMMENT '商品类别',
  PRIMARY KEY (`riqi`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='月市商品类别销售额'; 
      

-- ----------------------------
-- Table structure for yuexb
-- ----------------------------
    
CREATE TABLE `yuexb` (
  `riqi` int(100) NOT NULL COMMENT '日期',
  `xingbie` bit(1) DEFAULT NULL COMMENT '性别',
  `shangpinleibie` varchar(100) DEFAULT NULL COMMENT '商品类别',
  `xiaoshoue` varchar(100) DEFAULT NULL COMMENT '销售额',
  PRIMARY KEY (`riqi`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='月性别商品类别销售额'; 
      

-- ----------------------------
-- Table structure for yuexiaoshoue
-- ----------------------------
    
CREATE TABLE `yuexiaoshoue` (
  `riqi` int(100) NOT NULL COMMENT '日期',
  `xiaoshoue` int(100) DEFAULT NULL COMMENT '销售额',
  PRIMARY KEY (`riqi`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='月销售额'; 
      

-- ----------------------------
-- Table structure for yuezhuceyonghu
-- ----------------------------
    
CREATE TABLE `yuezhuceyonghu` (
  `riqi` int(100) NOT NULL COMMENT '日期',
  `yonghushuliang` int(100) DEFAULT NULL COMMENT '用户数量',
  PRIMARY KEY (`riqi`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='月注册用户'; 
      
