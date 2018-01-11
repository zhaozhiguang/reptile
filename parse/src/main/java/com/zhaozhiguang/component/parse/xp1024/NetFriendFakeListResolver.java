package com.zhaozhiguang.component.parse.xp1024;


import com.zhaozhiguang.component.http.HttpUtils;
import com.zhaozhiguang.component.parse.AbstractHtmlParseResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetFriendFakeListResolver extends AbstractHtmlParseResolver {

    private static final Logger logger = LoggerFactory.getLogger(NetFriendFakeListResolver.class);

    Pattern pattern = Pattern.compile("<h3><a href=\"([^r].+?)\" id=\"(.+?)\">(.+?)</a></h3>");

    public NetFriendFakeListResolver(CountDownLatch latch){
        this.latch = latch;
    }

    public NetFriendFakeListResolver(){

    }

    @Override
    protected void task(String url) throws Exception {
        String s = HttpUtils.get(url, null);
        Matcher matcher = pattern.matcher(s);
        while(matcher.find()){
            results.add(new ListModel("http://w2.aqu1024.com/pw/"+matcher.group(1),matcher.group(3)));
        }
    }

}
