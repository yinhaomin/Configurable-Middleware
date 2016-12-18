package com.baidu.config.middleware;

import java.util.Map;

import lombok.extern.log4j.Log4j;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

/**
 * 产生Roles的class.
 *
 * @author Yin Haomin
 * @date 2016/04/15
 */
@Log4j
@Component
@SuppressWarnings({ "rawtypes" })
public class RolesGenerator {

    /**
     * 存放生成class的配置的顺序的map
     */
    public static Map<Integer, Map<String, Integer>> objectPropertyMap = Maps.newHashMap();

    /**
     * 存放生成的class的map
     */
    public static Map<Integer, Class> classMap = Maps.newHashMap();

    /**
     * 根据传入的roles的文件夹的相对路径生成相应的Class和Class的配置
     *
     * @param rolesFolderPath
     *
     * @throws Exception
     */
    public void generateRoles() throws Exception {
        ClassUtil.generateDynamicClass(objectPropertyMap, classMap);
    }

    /**
     * 重新load动态class
     *
     * @throws Exception
     */
    public void reload() throws Exception {
        // for (Integer key : classMap.keySet()) {
        // ClassGenerator.renameOldClass(classMap.get(key));
        // }
        log.info("start reload properties file");
        ClassUtil.generateDynamicClass(objectPropertyMap, classMap);

    }

}
