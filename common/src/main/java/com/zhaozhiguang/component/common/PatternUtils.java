package com.zhaozhiguang.component.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则工具类
 * @author zhaozhiguang
 */
public class PatternUtils {

    /**
     * 返回url的后缀
     * @param url
     * @return
     */
    public static String getSuffixtoUrl(String url){
        String suffixes="avi|mpeg|3gp|mp3|mp4|wav|jpeg|gif|jpg|png|apk|exe|pdf|rar|zip|docx|doc";
        Pattern pat= Pattern.compile("[\\w]+[\\.]("+suffixes+")");//正则判断
        Matcher mc=pat.matcher(url);//条件匹配
        while(mc.find()){
            return mc.group();//截取文件名后缀名
        }
        return "";
    }
}
