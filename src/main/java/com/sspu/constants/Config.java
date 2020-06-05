package com.sspu.constants;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author ymj
 * @Date： 2020/4/28 16:29
 */
public class Config {
    /**
     * 读取 配置文件
     * @param filePath 文件路径
     * @param key key
     * @return value
     */
    public static String GetValueByKey(String filePath, String key)  {
        Properties pps = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            pps.load(in); //所有的K-V对都加载了
            String value = pps.getProperty(key);
            return value;
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
