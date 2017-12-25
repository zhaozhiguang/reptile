import com.zhaozhiguang.component.spider.FileParseResolver;
import com.zhaozhiguang.component.spider.SimpleImageParseResolver;
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
            resolver.parse("http://p.usxpic.com/imghost/upload/image/20171216/121609362913.jpg");
        }
        latch.await();
        long end = System.currentTimeMillis();
        System.out.println("end_time:"+end);
        System.out.println("total_time:"+(end-begin));
    }
}
