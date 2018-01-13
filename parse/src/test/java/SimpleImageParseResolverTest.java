import com.zhaozhiguang.component.common.LoadPropertiesUtils;
import com.zhaozhiguang.component.common.ScannerUtils;
import com.zhaozhiguang.component.parse.AbstractHtmlParseResolver;
import com.zhaozhiguang.component.parse.FileParseResolver;
import com.zhaozhiguang.component.parse.SimpleImageParseResolver;
import com.zhaozhiguang.component.parse.xp1024.ListModel;
import com.zhaozhiguang.component.parse.xp1024.NetFriendFakeInfoResolver;
import com.zhaozhiguang.component.parse.xp1024.NetFriendFakeListResolver;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;

public class SimpleImageParseResolverTest {

    private CountDownLatch latch = new CountDownLatch(1);

    @Test
    public void testImageParse() throws InterruptedException {
        SimpleImageParseResolver resolver = new SimpleImageParseResolver(latch);
        long begin = System.currentTimeMillis();
        System.out.println("begin_time:"+begin);
        for(int i=0;i<1;i++){
            resolver.parse(new ListModel("http://p.usxpic.com/imghost/upload/image/20171221/122107534856.jpg","default"));
        }
        latch.await();
        long end = System.currentTimeMillis();
        System.out.println("end_time:"+end);
        System.out.println("total_time:"+(end-begin));
    }

    @Test
    public void testNetFriendFakeListResolver() throws InterruptedException {
        AbstractHtmlParseResolver resolver = new NetFriendFakeListResolver(latch);
        resolver.parse("http://w2.aqu1024.com/pw/thread.php?fid=15");
        latch.await();
    }

    @Test
    public void testNetFriendFakeInfoResolver() throws InterruptedException {
        AbstractHtmlParseResolver resolver = new NetFriendFakeInfoResolver(latch);
        resolver.parse("http://w2.aqu1024.com/pw/htm_data/15/1801/957737.html");
        latch.await();
    }

    public static void main(String[] args) throws InterruptedException {
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
