import com.zhaozhiguang.component.spider.FileParseResolver;
import com.zhaozhiguang.component.spider.SimpleImageParseResolver;
import org.junit.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;

public class SimpleImageParseResolverTest {

    private CountDownLatch latch = new CountDownLatch(9);

    @Test
    public void testImageParse() throws InterruptedException {
        BlockingQueue<String> queue = new LinkedBlockingDeque<>(10);
        queue.put("http://p.usxpic.com/imghost/upload/image/20171216/121609362913.jpg");
        queue.put("http://p.usxpic.com/imghost/upload/image/20171216/121609362913.jpg");
        queue.put("http://p.usxpic.com/imghost/upload/image/20171216/121609362913.jpg");
        queue.put("http://p.usxpic.com/imghost/upload/image/20171216/121609362913.jpg");
        queue.put("http://p.usxpic.com/imghost/upload/image/20171216/121609362913.jpg");
        queue.put("http://p.usxpic.com/imghost/upload/image/20171216/121609362913.jpg");
        queue.put("http://p.usxpic.com/imghost/upload/image/20171216/121609362913.jpg");
        queue.put("http://p.usxpic.com/imghost/upload/image/20171216/121609362913.jpg");
        queue.put("http://p.usxpic.com/imghost/upload/image/20171216/121609362913.jpg");
        FileParseResolver resolver = new SimpleImageParseResolver(queue,latch);
        latch.await();
    }
}
