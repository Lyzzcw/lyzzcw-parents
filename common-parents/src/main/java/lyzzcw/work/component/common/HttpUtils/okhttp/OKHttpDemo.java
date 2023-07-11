package lyzzcw.work.component.common.HttpUtils.okhttp;

import com.alibaba.fastjson.JSONObject;
import io.github.admin4j.http.HttpRequest;
import io.github.admin4j.http.core.HttpHeaderKey;
import io.github.admin4j.http.core.Pair;
import io.github.admin4j.http.util.HttpJsonUtil;
import io.github.admin4j.http.util.HttpUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: lzy
 * @version: 1.0
 * Date: 2022/12/5 14:01
 * Description: No Description
 */
public class OKHttpDemo {

    public static final OkHttpClient client = new OkHttpClient()
            .newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    @Test
    public void get(){
        Response response = HttpUtil.get("https://github.com/search", Pair.of("q", "okhttp"));
        System.out.println("response = " + response);

        //返回格式为JSON的 可以使用 HttpJsonUtil 自动返回JsonObject
        JSONObject object= HttpJsonUtil.get("https://github.com/search",
                Pair.of("q","http"),
                Pair.of("username","agonie201218"));
        System.out.println("object = "+object);

        //HttpRequest 链式请求
        Response response1= HttpRequest.get("https://search.gitee.com/?skin=rec&type=repository")
                .queryMap("q","admin4j")
                .header(HttpHeaderKey.USER_AGENT,"admin4j")
                .execute();
        System.out.println("response = "+response1);
    }

    @Test
    public void post(){
        // JSON 格式的body
        Response post = HttpUtil.post("https://oapi.dingtalk.com/robot/send?access_token=27f5954ab60ea8b2e431ae9101b1289c138e85aa6eb6e3940c35ee13ff8b6335", "{\"msgtype\": \"text\",\"text\": {\"content\":\"【反馈提醒】我就是我, 是不一样的烟火\"}}");
        System.out.println("post = " + post);
    }

    @Test
    public void Form(){
        // form 请求
        Map<String, Object> formParams = new HashMap<>(16);
        formParams.put("username", "admin");
        formParams.put("password", "admin123");
        Response response = HttpUtil.postForm("http://192.168.1.13:9100/auth/login",
                formParams
        );
        System.out.println("response = " + response);

        // post form
        Response response1=HttpRequest.get("http://192.168.1.13:9100/auth/login")
                .queryMap("q","admin4j")
                .header(HttpHeaderKey.USER_AGENT,"admin4j")
                .form("username","admin")
                .form("password","admin123")
                .execute();
        System.out.println("response = "+response1);
    }

    @Test
    public void fileUpLoad(){
        File file=new File("C:\\Users\\andanyang\\Downloads\\Sql.txt");
        Map<String, Object> formParams=new HashMap<>();
        formParams.put("key","test");
        formParams.put("file",file);
        formParams.put("token","WXyUseb-D4sCum-EvTIDYL-mEehwDtrSBg-Zca7t:qgOcR2gUoKmxt-VnsNb657Oatzo=:eyJzY29wZSI6InpoYW56aGkiLCJkZWFkbGluZSI6MTY2NTMwNzUxNH0=");
        Response response=HttpUtil.upload("https://upload.qiniup.com/",formParams);
        System.out.println(response);
    }

    @Test
    public void fileDownLoad() throws IOException {
        HttpUtil.down("https://gitee.com/admin4j/common-http","path/");
    }

    @Test
    public void customization() throws IOException {
        Request request = new Request
                .Builder()
                .get()
                .url("")
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(response);


        EbayInventoryClient ebayInventoryClient=new EbayInventoryClient(1L);
        JSONObject jsonObject=ebayInventoryClient.inventoryItem(0,10);

    }
}
