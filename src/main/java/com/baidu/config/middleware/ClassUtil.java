package com.baidu.config.middleware;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Maps;

import lombok.extern.log4j.Log4j;

/**
 * 根据配置文件生成动态的Class.
 *
 * @author Yin Haomin
 * @date 2016/04/15
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Log4j
public class ClassUtil {

    /**
     * 根据传入的folderPat，读取出来所有的配置文件，按照配置文件的配置生成类，并将需要的信息写入到相应的map中
     *
     * @param folderPath        存放类的配置文件的路径
     * @param objectPropertyMap 存放配置文件的property的map
     * @param objectMap         存放object的map
     *
     * @throws Exception
     */
    public static void generateDynamicClass(Map<Integer, Map<String, Integer>> objectPropertyMap,
                                            Map<Integer, Class> classMap) throws Exception {

        URL path = ClassUtil.class.getClassLoader().getResource("");
        log.info("class path: " + path.getPath());
        File filePath = new File(path.getPath());
        File[] roleFiles = filePath.listFiles();

        for (int k = 0; k < roleFiles.length; k++) {
            String name = roleFiles[k].getName();
            Pattern pattern = Pattern.compile("[\\d+]\\.properties");
            Matcher matcher = pattern.matcher(name);
            if (matcher.matches()) {
                try {
                    // 文件名称即为CDP文件的type信息，这里的sequence等于CDP文件的type信息
                    // String fileName = roleFiles[k].getName();
                    String sequenceStr = name.split("\\.")[0];
                    int sequence = Integer.parseInt(sequenceStr);
                    HashMap returnMap = new HashMap();
                    HashMap typeMap = new HashMap();
                    // 读取配置文件
                    Properties prop = new OrderedProperties();
                    InputStream inputStream = ClassUtil.class.getClassLoader().getResourceAsStream(name);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                    prop.load(bufferedReader);
                    inputStream.close();
                    Set<String> keylist = prop.stringPropertyNames();
                    BeanInfo beanInfo = Introspector.getBeanInfo(Role.class);
                    PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

                    for (int i = 0; i < propertyDescriptors.length; i++) {
                        PropertyDescriptor descriptor = propertyDescriptors[i];
                        String propertyName = descriptor.getName();
                        if (!propertyName.equals("class")) {
                            Method readMethod = descriptor.getReadMethod();
                            Object result = readMethod.invoke(new Role(), new Object[0]);
                            if (result != null) {
                                returnMap.put(propertyName, result);
                            } else {
                                returnMap.put(propertyName, "");
                            }
                            typeMap.put(propertyName, descriptor.getPropertyType());
                        }
                    }
                    // 加载配置文件中的属性
                    Iterator<String> iterator = keylist.iterator();
                    Map<String, Integer> propertiesMap = Maps.newHashMap();
                    int propertySequence = 1;
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        propertiesMap.put(key, propertySequence);
                        propertySequence++;
                        returnMap.put(key, prop.getProperty(key));
                        typeMap.put(key, Class.forName(returnMap.get(key).toString()));
                    }

                    int length = RandomUtils.getRandomNum(10, 36);
                    String rendomStr = RandomUtils.getRandomString(length);

                    Class<?> clazz = ClassGenerator.generate("net.javaforge.blog.javassist.Pojo$Generated" + sequence
                            + rendomStr, typeMap);

                    Object obj = clazz.newInstance();

                    System.out.println("Clazz: " + clazz);
                    System.out.println("Object: " + obj);
                    System.out.println("Serializable? " + (obj instanceof Serializable));

                    for (final Method method : clazz.getDeclaredMethods()) {
                        System.out.println(method);
                    }

                    objectPropertyMap.put(sequence, propertiesMap);
                    classMap.put(sequence, clazz);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 为不同的类型返回不同的数据
     *
     * @param target
     * @param s
     *
     * @return
     */
    public static Object convert(Class<?> target, String s) {
        if (target == Object.class || target == String.class || s == null) {
            return s;
        }
        if (target == Character.class || target == char.class) {
            return s.charAt(0);
        }
        if (target == Byte.class || target == byte.class) {
            return Byte.parseByte(s);
        }
        if (target == Short.class || target == short.class) {
            return Short.parseShort(s);
        }
        if (target == Integer.class || target == int.class) {
            return Integer.parseInt(s);
        }
        if (target == Long.class || target == long.class) {
            return Long.parseLong(s);
        }
        if (target == Float.class || target == float.class) {
            return Float.parseFloat(s);
        }
        if (target == Double.class || target == double.class) {
            return Double.parseDouble(s);
        }
        if (target == Boolean.class || target == boolean.class) {
            return Boolean.parseBoolean(s);
        }
        throw new IllegalArgumentException("Don't know how to convert to " + target);
    }

}
