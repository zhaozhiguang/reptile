import com.zhaozhiguang.component.parse.AbstractHtmlParseResolver;
import com.zhaozhiguang.component.parse.FileParseResolver;
import com.zhaozhiguang.component.parse.SimpleImageParseResolver;
import com.zhaozhiguang.component.parse.xp1024.NetFriendFakeListResolver;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class SimpleImageParseResolverTest {

    private CountDownLatch latch = new CountDownLatch(1);

    @Test
    public void testImageParse() throws InterruptedException {
        FileParseResolver resolver = new SimpleImageParseResolver(latch);
        long begin = System.currentTimeMillis();
        System.out.println("begin_time:"+begin);
        for(int i=0;i<1;i++){
            resolver.parse("http://p.usxpic.com/imghost/upload/image/20171221/122107534856.jpg");
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
}
