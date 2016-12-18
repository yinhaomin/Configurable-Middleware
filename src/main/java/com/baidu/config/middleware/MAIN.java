package com.baidu.config.middleware;

import lombok.extern.log4j.Log4j;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Log4j
public class MAIN {

    private static ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

    /**
     * 生成动态的Class和配置文件的类
     */
    private static RolesGenerator rolesGenerator = ctx.getBean(RolesGenerator.class);

    public static void main(String[] args) {

        // 在启动程序的时候，将各个配置化的文件读取生成相应的Class
        try {
            rolesGenerator.generateRoles();
        } catch (Exception e) {
            String message = String.format("[FATAL ERROR] Failed to generate roles, error msg: %s", e.getMessage());
            log.error(message, e);
            System.exit(1);
        }

    }

}
