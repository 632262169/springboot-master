package com.wangbowen.common.gen;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.wangbowen.common.mapper.JsonMapper;
import com.wangbowen.common.utils.StringUtils;

/**
 * Mybatis-Plus代码生成器
 * Created by Administrator on 2017/8/8.
 */
public class CodeGeneratorUtils {
    public static String generate(String tableNames, String moduleName) {
        Map<String, String> resultMap = new HashMap<>();

        try {
            if (StringUtils.isBlank(tableNames) || StringUtils.isBlank(moduleName)) {
                throw new Exception("请输入表名称和模块名称");
            }

            String[] tables = tableNames.split(",");
            // 代码生成器
            AutoGenerator mpg = new AutoGenerator().setGlobalConfig(
                    // 全局配置
                    new GlobalConfig()
                            .setOutputDir("D:\\Tools\\mybatis-plus-blog\\sys")//输出目录
                            .setFileOverride(true)// 是否覆盖文件
                            .setActiveRecord(true)// 开启 activeRecord 模式
                            .setEnableCache(false)// XML 二级缓存
                            .setBaseResultMap(true)// XML ResultMap
                            .setBaseColumnList(true)// XML columList
                            .setAuthor("luckyxz999")
                    // 自定义文件命名，注意 %s 会自动填充表实体属性！
                    // .setMapperName("%sDao")
                    // .setXmlName("%sDao")
                    // .setServiceName("MP%sService")
                    // .setServiceImplName("%sServiceDiy")
                    // .setControllerName("%sAction")
            ).setDataSource(
                    // 数据源配置
                    new DataSourceConfig()
                            .setDbType(DbType.MYSQL)// 数据库类型
                            .setTypeConvert(new MySqlTypeConvert() {
                                // 自定义数据库表字段类型转换【可选】
                                @Override
                                public DbColumnType processTypeConvert(String fieldType) {
                                    System.out.println("转换类型：" + fieldType);
                                    // if ( fieldType.toLowerCase().contains( "tinyint" ) ) {
                                    //    return DbColumnType.BOOLEAN;
                                    // }
                                    return super.processTypeConvert(fieldType);
                                }
                            })
                            .setDriverName("com.mysql.jdbc.Driver")
                            .setUsername("root")
                            .setPassword("IhEqV1AONHx8")
                            .setUrl("jdbc:mysql://106.14.121.154:3306/blog?useUnicode=true&characterEncoding=utf-8")
            ).setStrategy(
                    // 策略配置
                    new StrategyConfig()
                            // .setCapitalMode(true)// 全局大写命名
                            // .setDbColumnUnderline(true)//全局下划线命名
//                        .setTablePrefix(new String[]{"bmd_", "mp_"})// 此处可以修改为您的表前缀
                            .setNaming(NamingStrategy.underline_to_camel)// 表名生成策略
                            .setInclude(tables) // 需要生成的表
                    // .setExclude(new String[]{"test"}) // 排除生成的表
                    // 自定义实体父类
                            .setSuperEntityClass("com.wangbowen.common.entity.SuperEntity")
                    // 自定义实体，公共字段
                            .setSuperEntityColumns(new String[]{"create_by", "create_date", "update_by",
                                    "update_date", "del_flag"})
                    //.setTableFillList(tableFillList)
                    // 自定义 mapper 父类
                    // .setSuperMapperClass("com.baomidou.demo.TestMapper")
                    // 自定义 service 父类
                    // .setSuperServiceClass("com.baomidou.demo.TestService")
                    // 自定义 service 实现类父类
                    // .setSuperServiceImplClass("com.baomidou.demo.TestServiceImpl")
                    // 自定义 controller 父类
                    // .setSuperControllerClass("com.baomidou.demo.TestController")
                    // 【实体】是否生成字段常量（默认 false）
                    // public static final String ID = "test_id";
                    // .setEntityColumnConstant(true)
                    // 【实体】是否为构建者模型（默认 false）
                    // public User setName(String name) {this.name = name; return this;}
                    // .setEntityBuilderModel(true)
                    // 【实体】是否为lombok模型（默认 false）<a href="https://projectlombok.org/">document</a>
                    // .setEntityLombokModel(true)
                    // Boolean类型字段是否移除is前缀处理
                    .setEntityBooleanColumnRemoveIsPrefix(false)
                    // .setRestControllerStyle(true)
                    // .setControllerMappingHyphenStyle(true)
            ).setPackageInfo(
                    // 包配置
                    new PackageConfig()
                            .setModuleName(moduleName)
                            .setParent("com.wangbowen.modules")// 自定义包路径
                            .setController("controller")// 这里是控制器包名，默认 web
            ).setCfg(
                    // 注入自定义配置，可以在 VM 中使用 cfg.abc 设置的值
                    new InjectionConfig() {
                        @Override
                        public void initMap() {
                            Map<String, Object> map = new HashMap<>();
                            map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                            this.setMap(map);
                        }
                    }.setFileOutConfigList(Collections.<FileOutConfig>singletonList(new FileOutConfig("/templates/mapper.xml.vm") {
                        // 自定义输出文件目录
                        @Override
                        public String outputFile(TableInfo tableInfo) {
                            return "D:\\Tools\\mybatis-plus-blog\\sys\\xml\\" + tableInfo.getEntityName() + "Mapper.xml";
                        }
                    }))
            ).setTemplate(
                    // 关闭默认 xml 生成，调整生成 至 根目录
                    new TemplateConfig().setXml(null)
                    // 自定义模板配置，模板可以参考源码 /mybatis-plus/src/main/resources/template 使用 copy
                    // 至您项目 src/main/resources/template 目录下，模板名称也可自定义如下配置：
                    // .setController("...");
                    // .setEntity("...");
                    // .setMapper("...");
                    // .setXml("...");
                    // .setService("...");
                    // .setServiceImpl("...");
            );

            // 执行生成
            mpg.execute();

            // 打印注入设置，这里演示模板里面怎么获取注入内容【可无】
            System.err.println(mpg.getCfg().getMap().get("abc"));
            resultMap.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", "fail");
            resultMap.put("errorMsg", e.getMessage());
        }

        return JsonMapper.toJsonString(resultMap);
    }


    public static void main(String[] args) {
//        //sys-用户角色权限模块
//        generate("sys_user,sys_role,sys_menu,sys_user_role,sys_role_menu", "sys");

//        //cms-日志模块
//        generate("cms_article,cms_category,cms_comment", "cms");
    }
}

