package com.generate.config;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 配置文件
 * @author YI
 * @date 2018-11-15 11:35:41
 */
public class Config {
    /**
     * 项目在硬盘上的基础路径
     */
    public static final String PROJECT_PATH = System.getProperty("user.dir");

    /**
     * java文件路径
     */
    public static final String JAVA_PATH = "\\code";
    /**
     * 资源文件路径
     */
    public static final String RESOURCES_PATH = "\\src\\main\\resources";

    /**
     * 模板位置
     */
    public static final String TEMPLATE_FILE_PATH = PROJECT_PATH + RESOURCES_PATH + "\\generator";

    /**
     * 创建时间
     */
    public static final String DATE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

    /**
     * 数据库驱动
     */
    public static final String JDBC_DIVER_CLASS_NAME = "com.mysql.jdbc.Driver";

    /**
     * Mapper插件基础接口的完全限定名
     */
    public static final String MAPPER_INTERFACE_REFERENCE = "com.generate.config.GeneralMapper";
}
