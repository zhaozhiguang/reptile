package com.zhaozhiguang.component.parse.xp1024;


import com.zhaozhiguang.component.http.HttpUtils;
import com.zhaozhiguang.component.parse.AbstractHtmlParseResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetFriendFakeInfoResolver extends AbstractHtmlParseResolver {

    private static final Logger logger = LoggerFactory.getLogger(NetFriendFakeInfoResolver.class);

    Pattern pattern = Pattern.compile("><img src=\"(.+?)\" border=\"0\" onclick=\"if\\(this.width>=1024\\) window.open\\('(.+?)'\\);\" onload=\"if\\(this.width>'1024'\\)this.width='1024';\" >");

    public NetFriendFakeInfoResolver(CountDownLatch latch) {
        this.latch = latch;
    }

    public NetFriendFakeInfoResolver(){

    }

    @Override
    protected void task(java.lang.String url) throws Exception {
        java.lang.String s = HttpUtils.get(url, null);
        Matcher matcher = pattern.matcher(s);
        while(matcher.find()){
            results.add(matcher.group(1));
        }
    }

}
