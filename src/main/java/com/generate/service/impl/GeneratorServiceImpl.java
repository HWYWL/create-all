package com.generate.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.blade.ioc.annotation.Bean;
import com.generate.model.GeneratorModel;
import com.generate.service.GeneratorService;
import freemarker.template.TemplateExceptionHandler;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.generate.config.Config.*;

/**
 * 生成代码各层
 * @author YI
 * @date 2018-11-15 22:13:01
 */
@Bean
public class GeneratorServiceImpl implements GeneratorService {
    public static final Logger LOGGER = LoggerFactory.getLogger(GeneratorServiceImpl.class);

    @Override
    public void getModelAndMapper(GeneratorModel model) {
        Context context = new Context(ModelType.FLAT);
        context.setId("Potato");
        context.setTargetRuntime("MyBatis3Simple");
        context.addProperty(PropertyRegistry.CONTEXT_BEGINNING_DELIMITER, "`");
        context.addProperty(PropertyRegistry.CONTEXT_ENDING_DELIMITER, "`");

        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
        jdbcConnectionConfiguration.setConnectionURL(getConnectionURL(model));
        jdbcConnectionConfiguration.setUserId(model.getJdbcUserName());
        jdbcConnectionConfiguration.setPassword(model.getJdbcPassWord());
        jdbcConnectionConfiguration.setDriverClass(JDBC_DIVER_CLASS_NAME);
        context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);

        PluginConfiguration pluginConfiguration = new PluginConfiguration();
        pluginConfiguration.setConfigurationType("tk.mybatis.mapper.generator.MapperPlugin");
        pluginConfiguration.addProperty("mappers", model.basePackagePath + NEW_CORE_PACKAGE + MAPPER_INTERFACE_REFERENCE);
        context.addPluginConfiguration(pluginConfiguration);

        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();
        javaModelGeneratorConfiguration.setTargetProject(PROJECT_PATH + JAVA_PATH);
        javaModelGeneratorConfiguration.setTargetPackage(getBasicPackage(model, MODEL_PACKAGE));
        context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);

        SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
        sqlMapGeneratorConfiguration.setTargetProject(PROJECT_PATH + RESOURCES_PATH);
        sqlMapGeneratorConfiguration.setTargetPackage("mybatis/mapper");
        context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);

        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
        javaClientGeneratorConfiguration.setTargetProject(PROJECT_PATH + JAVA_PATH);
        javaClientGeneratorConfiguration.setTargetPackage(getBasicPackage(model, MAPPER_PACKAGE));
        javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
        context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);

        TableConfiguration tableConfiguration = new TableConfiguration(context);
        tableConfiguration.setTableName(model.getTableName());
        tableConfiguration.setGeneratedKey(new GeneratedKey("id", "Mysql", true, null));
        context.addTableConfiguration(tableConfiguration);

        List<String> warnings;
        MyBatisGenerator generator;
        try {
            Configuration config = new Configuration();
            config.addContext(context);
            config.validate();

            boolean overwrite = true;
            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            warnings = new ArrayList<>();
            generator = new MyBatisGenerator(config, callback, warnings);
            generator.generate(null);
        } catch (Exception e) {
            throw new RuntimeException("生成Model和Mapper失败", e);
        }

        if (generator.getGeneratedJavaFiles().isEmpty() || generator.getGeneratedXmlFiles().isEmpty()) {
            throw new RuntimeException("生成Model和Mapper失败：" + warnings);
        }
    }

    @Override
    public void getService(GeneratorModel model) {
        try {
            freemarker.template.Configuration cfg = getConfiguration();

            Map<String, Object> data = new HashMap<>(16);
            data.put("date", DATE);
            data.put("author", model.getAuthor());
            String modelNameUpperCamel = tableNameConvertUpperCamel(model.getTableName());
            data.put("modelNameUpperCamel", modelNameUpperCamel);
            data.put("modelNameLowerCamel", tableNameConvertLowerCamel(model.getTableName()));
            data.put("basePackage", model.getBasePackagePath());

            File file = FileUtil.file(PROJECT_PATH + JAVA_PATH + getPackagePath(model.getPackagePathService()) + modelNameUpperCamel + "Service.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("service.ftl").process(data, new FileWriter(file));
            System.out.println(modelNameUpperCamel + "Service.java 生成成功");

            File file1 = FileUtil.file(PROJECT_PATH + JAVA_PATH + getPackagePath(model.getPackagePathServiceImpl()) + modelNameUpperCamel + "ServiceImpl.java");
            if (!file1.getParentFile().exists()) {
                file1.getParentFile().mkdirs();
            }
            cfg.getTemplate("service-impl.ftl").process(data, new FileWriter(file1));
            System.out.println(modelNameUpperCamel + "ServiceImpl.java 生成成功");
        } catch (Exception e) {
            throw new RuntimeException("生成Service失败", e);
        }
    }

    @Override
    public void getController(GeneratorModel model) {
        try {
            freemarker.template.Configuration cfg = getConfiguration();

            Map<String, Object> data = new HashMap<>(16);
            data.put("date", DATE);
            data.put("author", model.getAuthor());
            data.put("baseRequestMapping", tableNameConvertMappingPath(model.getTableName()));
            String modelNameUpperCamel = tableNameConvertUpperCamel(model.getTableName());
            data.put("modelNameUpperCamel", modelNameUpperCamel);
            data.put("modelNameLowerCamel", tableNameConvertLowerCamel(model.getTableName()));
            data.put("basePackage", model.getBasePackagePath());

            File file = FileUtil.file(PROJECT_PATH + JAVA_PATH + getPackagePath(model.getPackagePathController()) + modelNameUpperCamel + "Controller.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            cfg.getTemplate("controller.ftl").process(data, new FileWriter(file));

            System.out.println(modelNameUpperCamel + "Controller.java 生成成功");
        } catch (Exception e) {
            throw new RuntimeException("生成Controller失败", e);
        }

    }

    /**
     * 核心类库操作
     * @param model
     */
    @Override
    public void coreAdapt(GeneratorModel model) {
        File src = FileUtil.file(PROJECT_PATH + JAVA_PATH + getPackagePath(CORE_PACKAGE));
        File dest = FileUtil.file(PROJECT_PATH + JAVA_PATH + getPackagePath(model.getBasePackagePath()));
        FileUtil.copy(src, dest, true);

        List<File> files = FileUtil.loopFiles(PROJECT_PATH + JAVA_PATH + getPackagePath(model.getBasePackagePath() + NEW_CORE_PACKAGE));

        files.forEach(file -> {
            System.out.println("文件名 === " + FileUtil.getName(file));
            List<String> strings = FileUtil.readUtf8Lines(file);
            strings.set(0, strings.get(0).replace(CORE_PACKAGE, model.getBasePackagePath() + NEW_CORE_PACKAGE));
            FileUtil.del(file);
            FileUtil.appendUtf8Lines(strings, file);
        });

        FileUtil.copy(dest, FileUtil.mkParentDirs(CODE_PATH + JAVA_PATH + getPackagePath(model.getBasePackagePath())), true);
        FileUtil.copy(FileUtil.file(PROJECT_PATH + RESOURCES_PATH + "\\mybatis"), FileUtil.mkParentDirs(CODE_PATH + RESOURCES_PATH), true);

        // 删除已经复制的文件
        System.gc();
        dest.delete();
        dest.deleteOnExit();

        FileUtil.del(dest);
        FileUtil.del(PROJECT_PATH + RESOURCES_PATH + "\\mybatis");

        File zip = ZipUtil.zip(FileUtil.newFile(CODE_PATH));

        String url = PROJECT_PATH + RESOURCES_PATH + "/static/temp/GenerateCode.zip";
        FileUtil.move(zip, FileUtil.newFile(url), true);

        model.setDownloadPath("/static/temp/GenerateCode.zip");
    }

    private static freemarker.template.Configuration getConfiguration() throws IOException {
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_28);
        cfg.setDirectoryForTemplateLoading(FileUtil.file(TEMPLATE_FILE_PATH));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        return cfg;
    }

    private static String tableNameConvertLowerCamel(String tableName) {
        StringBuilder result = new StringBuilder();
        if (tableName != null && tableName.length() > 0) {
            boolean flag = false;
            for (int i = 0; i < tableName.length(); i++) {
                char ch = tableName.charAt(i);
                if ("_".charAt(0) == ch) {
                    flag = true;
                } else {
                    if (flag) {
                        result.append(Character.toUpperCase(ch));
                        flag = false;
                    } else {
                        result.append(ch);
                    }
                }
            }
        }
        return result.toString();
    }

    private static String tableNameConvertUpperCamel(String tableName) {
        String camel = tableNameConvertLowerCamel(tableName);
        return camel.substring(0, 1).toUpperCase() + camel.substring(1);

    }

    private static String tableNameConvertMappingPath(String tableName) {
        return StrUtil.SLASH + (tableName.contains(StrUtil.UNDERLINE) ? tableName.replaceAll(StrUtil.UNDERLINE, StrUtil.SLASH) : tableName);
    }

    private static String getConnectionURL(GeneratorModel model) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("jdbc:mysql://");
        buffer.append(model.getJdbcUrl());
        buffer.append(CharUtil.COLON);
        buffer.append(model.getPort());
        buffer.append(CharUtil.SLASH);
        buffer.append(model.getDatabaseName());
        buffer.append("?serverTimezone=GMT%2B8");

        return buffer.toString();
    }

    private static String getBasicPackage(GeneratorModel model, String packagePath) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(model.getBasePackagePath());
        buffer.append(packagePath);


        return buffer.toString();
    }

    private static String getPackagePath(String path) {
        StringBuffer buffer = new StringBuffer();

        String replace = path.replace(StrUtil.DOT, StrUtil.BACKSLASH);

        buffer.append(StrUtil.BACKSLASH);
        buffer.append(replace);
        buffer.append(StrUtil.BACKSLASH);


        return buffer.toString();
    }
}
