package com.zhaozhiguang.component.parse;

import com.zhaozhiguang.component.common.ScannerUtils;
import com.zhaozhiguang.component.parse.xp1024.ListModel;
import com.zhaozhiguang.component.parse.xp1024.NetFriendFakeInfoResolver;
import com.zhaozhiguang.component.parse.xp1024.NetFriendFakeListResolver;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        String url = "http://w2.aqu1024.com/pw/thread.php?fid=15";
        if(args!=null&&args.length>0) url = args[0];
        AbstractHtmlParseResolver resolver = new NetFriendFakeListResolver();
        resolver.setIgnoreUrls(ScannerUtils.scanUrl());
        resolver.setCallback(listModels -> {
            for(ListModel model:(List<ListModel>)listModels){
                if(model!=null){
                    AbstractHtmlParseResolver resolver1 = new NetFriendFakeInfoResolver();
                    resolver1.setCallback(urls -> {
                        for (String ul : (List<String>)urls){
                            SimpleImageParseResolver fileParseResolver = new SimpleImageParseResolver();
                            fileParseResolver.parse(new ListModel(ul, model.getTitle()));
                        }
                    });
                    resolver1.parse(model.getUrl());
                }
            }
        });
        resolver.parse(url);
    }
}
