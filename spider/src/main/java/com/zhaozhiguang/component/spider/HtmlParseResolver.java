package com.zhaozhiguang.component.spider;

/**
 * html文本解析处理
 */
public interface HtmlParseResolver {

    /**
     * 根据文本处理文档
     * @param text
     */
    void parse(String text);
}
