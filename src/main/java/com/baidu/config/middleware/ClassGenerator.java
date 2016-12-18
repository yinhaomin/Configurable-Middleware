package com.baidu.config.middleware;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import lombok.extern.log4j.Log4j;

/**
 * 生成Class的类
 * 
 * @author Yin Haomin
 * @date 2016/04/18
 *
 */
@Log4j
@SuppressWarnings("rawtypes")
public class ClassGenerator {

    /**
     * 生成Calss
     * 
     * @param className
     * @param properties
     * @return
     * @throws NotFoundException
     * @throws CannotCompileException
     */
    public static Class generate(String className, Map<String, Class<?>> properties) throws NotFoundException,
            CannotCompileException {

        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.makeClass(className);

        // add this to define a super class to extend
        // cc.setSuperclass(resolveCtClass(MySuperClass.class));

        // add this to define an interface to implement
        cc.addInterface(resolveCtClass(Serializable.class));

        for (Entry<String, Class<?>> entry : properties.entrySet()) {

            cc.addField(new CtField(resolveCtClass(entry.getValue()), entry.getKey(), cc));

            // add getter
            cc.addMethod(generateGetter(cc, entry.getKey(), entry.getValue()));

            // add setter
            cc.addMethod(generateSetter(cc, entry.getKey(), entry.getValue()));
        }

        return cc.toClass();
    }

    /**
     * 生成get方法
     * 
     * @param declaringClass
     * @param fieldName
     * @param fieldClass
     * @return
     * @throws CannotCompileException
     */
    private static CtMethod generateGetter(CtClass declaringClass, String fieldName, Class fieldClass)
            throws CannotCompileException {

        String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

        StringBuffer sb = new StringBuffer();
        sb.append("public ").append(fieldClass.getName()).append(" ").append(getterName).append("(){").append(
                "return this.").append(fieldName).append(";").append("}");
        return CtMethod.make(sb.toString(), declaringClass);
    }

    /**
     * 生成set方法
     * 
     * @param declaringClass
     * @param fieldName
     * @param fieldClass
     * @return
     * @throws CannotCompileException
     */
    private static CtMethod generateSetter(CtClass declaringClass, String fieldName, Class fieldClass)
            throws CannotCompileException {

        String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

        StringBuffer sb = new StringBuffer();
        sb.append("public void ").append(setterName).append("(").append(fieldClass.getName()).append(" ").append(
                fieldName).append(")").append("{").append("this.").append(fieldName).append("=").append(fieldName)
                .append(";").append("}");
        return CtMethod.make(sb.toString(), declaringClass);
    }

    private static CtClass resolveCtClass(Class clazz) throws NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        return pool.get(clazz.getName());
    }

    // /**
    // * 将老的class重命名
    // *
    // * @param clazz
    // */
    // @SuppressWarnings("unused")
    // public static void renameOldClass(Class clazz) {
    // ClassPool pool = ClassPool.getDefault();
    // int length = RandomUtils.getRandomNum(10, 36);
    // String rendomStr = RandomUtils.getRandomString(length);
    // try {
    // CtClass cc = pool.getAndRename(clazz.getName(), clazz.getName() +
    // rendomStr);
    // } catch (Exception e) {
    // log.error("Failed to renane the class, please restart change-dumper");
    // }
    // }

}
