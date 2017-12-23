import com.zhaozhiguang.component.http.HttpUtils;
import org.junit.Test;

import java.io.IOException;


public class HttpUtilsTest {

    @Test
    public void testRequest(){
        try {
            HttpUtils.get("http://w2.aqu1024.com/pw/thread.php?fid=15&page=13",null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
