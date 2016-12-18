package com.baidu.config.middleware;

import java.util.Random;

public class RandomUtils {

    /**
     * 生成随机的字符串
     * 
     * @param length
     *            表示生成字符串的长度
     * @return
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 生成[min,max)范围的随机数
     * 
     * @param min
     * @param max
     * @return
     */
    public static int getRandomNum(int min, int max) {
        Random random = new Random();
        int randNum = random.nextInt(max - min) + min;
        return randNum;
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            Random random = new Random();
            long a = random.nextInt(100);
            System.out.println(a);
        }
    }

}
