package com.happymall.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by onegx
 */
public class PropertiesUtil {

   private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    private static Properties pros;

    static{
        String filename = "mmall.properties";
        pros = new Properties();
        try {
            pros.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(filename)));
        } catch (IOException e) {
            logger.error("参数错误",e);
        }
    }

    public static String getProperty(String key){
        String value = pros.getProperty(key);
        if(value == null){
            return null;
        }
        return value.trim();
    }

    public static String getProperty(String key,String dafeultValue){
        String value = pros.getProperty(key);
        if(value == null){
            value = dafeultValue;
        }
        return value.trim();
    }


}
