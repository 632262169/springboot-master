## farmblog 系统简介

个人博客系统，基于Springboot、SpringMVC、Mybatis-plus、thymeleaf、ApacheShiro等，后台主题使用AdminLTE，前端主题基于amazeUI响应式设计

## 功能说明

1. 系统管理，包括菜单管理、角色管理、用户管理、修改个人信息、修改个人密码
2. 内容管理，包括文章分类管理、标签管理和文章管理
3. 前端展示，分类导航、标签导航、文章列表和文章详情等

## 部署说明

1. 创建数据库，导入初始化sql文件，即/resources/db/blog.sql
2. 修改数据库连接，/resources/application.yml
3. 图片库使用阿里云oss，可自行申请阿里云账号，oss有免费使用额度，并修改配置applicationi.properties
4. 直接运行application.java，前端访问：[http://localhost](http://http://localhost)，后台访问:[http://localhost/admin](http://http://localhost/admin)，账号admin，密码admin

## 技术选型

1. 这里是列表文本、后端

* JDK：1.8
* 核心框架：Springboot 1.5、SpringMVC
* 安全框架：Apache Shiro 1.3.2
* 数据库连接池：HikariCP 2.7.0
* 缓存框架：Ehcache
* 日志管理：logback
* 工具类：Apache Commons-lang3 3.6、fastjson 1.2.38
* 搜索框架：lucene-core 6.4.1

2. 这里是列表文本、前端

* 模板引擎：thymeleaf
* UI框架：bootstrap3.3.7、adminLTE、amazeUI
* JS框架：jquery-3.2.1
* 表格插件：Bootstrap Table
* 表单验证插件：jquery.validate
* 弹层组件：layer
* 树形表格：Bootstrap TreeTable
* 树形插件：zTree

## 截图
![输入图片说明](https://gitee.com/uploads/images/2018/0313/100725_dd32fbcc_331488.jpeg "TIM截图20180313100623.jpg")
![输入图片说明](https://gitee.com/uploads/images/2018/0313/100739_3ea6497c_331488.jpeg "TIM截图20180313100536.jpg")