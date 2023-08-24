package lyzzcw.work.component.common.HttpUtils.okhttp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
import java.util.List;
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
    public void get() {
        Response response = HttpUtil.get("https://github.com/search", Pair.of("q", "okhttp"));
        System.out.println("response = " + response);

        //返回格式为JSON的 可以使用 HttpJsonUtil 自动返回JsonObject
        JSONObject object = HttpJsonUtil.get("https://github.com/search",
                Pair.of("q", "http"),
                Pair.of("username", "agonie201218"));
        System.out.println("object = " + object);

        //HttpRequest 链式请求
        Response response1 = HttpRequest.get("https://search.gitee.com/?skin=rec&type=repository")
                .queryMap("q", "admin4j")
                .header(HttpHeaderKey.USER_AGENT, "admin4j")
                .execute();
        System.out.println("response = " + response1);
    }

    @Test
    public void post() {
        // JSON 格式的body
        Response post = HttpUtil.post("https://oapi.dingtalk.com/robot/send?access_token=27f5954ab60ea8b2e431ae9101b1289c138e85aa6eb6e3940c35ee13ff8b6335", "{\"msgtype\": \"text\",\"text\": {\"content\":\"【反馈提醒】我就是我, 是不一样的烟火\"}}");
        System.out.println("post = " + post);
    }

    @Test
    public void Form() {
        // form 请求
        Map<String, Object> formParams = new HashMap<>(16);
        formParams.put("username", "admin");
        formParams.put("password", "admin123");
        Response response = HttpUtil.postForm("http://192.168.1.13:9100/auth/login",
                formParams
        );
        System.out.println("response = " + response);

        // post form
        Response response1 = HttpRequest.get("http://192.168.1.13:9100/auth/login")
                .queryMap("q", "admin4j")
                .header(HttpHeaderKey.USER_AGENT, "admin4j")
                .form("username", "admin")
                .form("password", "admin123")
                .execute();
        System.out.println("response = " + response1);
    }

    @Test
    public void fileUpLoad() {
        File file = new File("C:\\Users\\andanyang\\Downloads\\Sql.txt");
        Map<String, Object> formParams = new HashMap<>();
        formParams.put("key", "test");
        formParams.put("file", file);
        formParams.put("token", "WXyUseb-D4sCum-EvTIDYL-mEehwDtrSBg-Zca7t:qgOcR2gUoKmxt-VnsNb657Oatzo=:eyJzY29wZSI6InpoYW56aGkiLCJkZWFkbGluZSI6MTY2NTMwNzUxNH0=");
        Response response = HttpUtil.upload("https://upload.qiniup.com/", formParams);
        System.out.println(response);
    }

    @Test
    public void fileDownLoad() throws IOException {
        HttpUtil.down("https://gitee.com/admin4j/common-http", "path/");
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


        EbayInventoryClient ebayInventoryClient = new EbayInventoryClient(1L);
        JSONObject jsonObject = ebayInventoryClient.inventoryItem(0, 10);

    }

    @Test
    public void getRegionInfo() {

        //返回格式为JSON的 可以使用 HttpJsonUtil 自动返回JsonObject
//        JSONObject object = HttpJsonUtil.get("https://restapi.amap.com/v3/config/district",
//                Pair.of("key", "7b36dbc1792987808a13bdc45b2b479a"),
//                Pair.of("keywords", "河北省"),
//                Pair.of("subdistrict", "2"),
//                Pair.of("size", "1"),
//                Pair.of("offset", Integer.MAX_VALUE),
//                Pair.of("output", "JSON"));
//        System.out.println("object = " + object);


        String str = "{\n" +
                "  \"status\": \"1\",\n" +
                "  \"info\": \"OK\",\n" +
                "  \"infocode\": \"10000\",\n" +
                "  \"count\": \"1\",\n" +
                "  \"suggestion\": {\n" +
                "    \"keywords\": [ ],\n" +
                "    \"cities\": [ ]\n" +
                "  },\n" +
                "  \"districts\": [\n" +
                "    {\n" +
                "      \"citycode\": [ ],\n" +
                "      \"adcode\": \"130000\",\n" +
                "      \"name\": \"河北省\",\n" +
                "      \"center\": \"114.530399,38.037707\",\n" +
                "      \"level\": \"province\",\n" +
                "      \"districts\": [\n" +
                "        {\n" +
                "          \"citycode\": \"0315\",\n" +
                "          \"adcode\": \"130200\",\n" +
                "          \"name\": \"唐山市\",\n" +
                "          \"center\": \"118.180149,39.63068\",\n" +
                "          \"level\": \"city\",\n" +
                "          \"districts\": [\n" +
                "            {\n" +
                "              \"citycode\": \"0315\",\n" +
                "              \"adcode\": \"130283\",\n" +
                "              \"name\": \"迁安市\",\n" +
                "              \"center\": \"118.701021,39.998861\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0315\",\n" +
                "              \"adcode\": \"130225\",\n" +
                "              \"name\": \"乐亭县\",\n" +
                "              \"center\": \"118.91245,39.425748\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0315\",\n" +
                "              \"adcode\": \"130229\",\n" +
                "              \"name\": \"玉田县\",\n" +
                "              \"center\": \"117.738196,39.901827\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0315\",\n" +
                "              \"adcode\": \"130202\",\n" +
                "              \"name\": \"路南区\",\n" +
                "              \"center\": \"118.154348,39.624988\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0315\",\n" +
                "              \"adcode\": \"130209\",\n" +
                "              \"name\": \"曹妃甸区\",\n" +
                "              \"center\": \"118.460197,39.273528\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0315\",\n" +
                "              \"adcode\": \"130224\",\n" +
                "              \"name\": \"滦南县\",\n" +
                "              \"center\": \"118.68363,39.520272\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0315\",\n" +
                "              \"adcode\": \"130205\",\n" +
                "              \"name\": \"开平区\",\n" +
                "              \"center\": \"118.262246,39.671634\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0315\",\n" +
                "              \"adcode\": \"130204\",\n" +
                "              \"name\": \"古冶区\",\n" +
                "              \"center\": \"118.447134,39.73392\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0315\",\n" +
                "              \"adcode\": \"130227\",\n" +
                "              \"name\": \"迁西县\",\n" +
                "              \"center\": \"118.31467,40.141486\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0315\",\n" +
                "              \"adcode\": \"130284\",\n" +
                "              \"name\": \"滦州市\",\n" +
                "              \"center\": \"118.70301,39.740963\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0315\",\n" +
                "              \"adcode\": \"130281\",\n" +
                "              \"name\": \"遵化市\",\n" +
                "              \"center\": \"117.965878,40.189119\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0315\",\n" +
                "              \"adcode\": \"130207\",\n" +
                "              \"name\": \"丰南区\",\n" +
                "              \"center\": \"118.085169,39.576031\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0315\",\n" +
                "              \"adcode\": \"130208\",\n" +
                "              \"name\": \"丰润区\",\n" +
                "              \"center\": \"118.162426,39.832919\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0315\",\n" +
                "              \"adcode\": \"130203\",\n" +
                "              \"name\": \"路北区\",\n" +
                "              \"center\": \"118.201085,39.625079\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"citycode\": \"0335\",\n" +
                "          \"adcode\": \"130300\",\n" +
                "          \"name\": \"秦皇岛市\",\n" +
                "          \"center\": \"119.52022,39.888243\",\n" +
                "          \"level\": \"city\",\n" +
                "          \"districts\": [\n" +
                "            {\n" +
                "              \"citycode\": \"0335\",\n" +
                "              \"adcode\": \"130324\",\n" +
                "              \"name\": \"卢龙县\",\n" +
                "              \"center\": \"118.891931,39.892564\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0335\",\n" +
                "              \"adcode\": \"130303\",\n" +
                "              \"name\": \"山海关区\",\n" +
                "              \"center\": \"119.775187,39.978882\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0335\",\n" +
                "              \"adcode\": \"130302\",\n" +
                "              \"name\": \"海港区\",\n" +
                "              \"center\": \"119.564962,39.94756\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0335\",\n" +
                "              \"adcode\": \"130306\",\n" +
                "              \"name\": \"抚宁区\",\n" +
                "              \"center\": \"119.244847,39.876253\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0335\",\n" +
                "              \"adcode\": \"130322\",\n" +
                "              \"name\": \"昌黎县\",\n" +
                "              \"center\": \"119.199846,39.699677\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0335\",\n" +
                "              \"adcode\": \"130304\",\n" +
                "              \"name\": \"北戴河区\",\n" +
                "              \"center\": \"119.48449,39.834912\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0335\",\n" +
                "              \"adcode\": \"130321\",\n" +
                "              \"name\": \"青龙满族自治县\",\n" +
                "              \"center\": \"118.94985,40.407473\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"citycode\": \"0314\",\n" +
                "          \"adcode\": \"130800\",\n" +
                "          \"name\": \"承德市\",\n" +
                "          \"center\": \"117.962749,40.952942\",\n" +
                "          \"level\": \"city\",\n" +
                "          \"districts\": [\n" +
                "            {\n" +
                "              \"citycode\": \"0314\",\n" +
                "              \"adcode\": \"130828\",\n" +
                "              \"name\": \"围场满族蒙古族自治县\",\n" +
                "              \"center\": \"117.75934,41.938372\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0314\",\n" +
                "              \"adcode\": \"130826\",\n" +
                "              \"name\": \"丰宁满族自治县\",\n" +
                "              \"center\": \"116.645798,41.209951\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0314\",\n" +
                "              \"adcode\": \"130825\",\n" +
                "              \"name\": \"隆化县\",\n" +
                "              \"center\": \"117.739026,41.314402\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0314\",\n" +
                "              \"adcode\": \"130827\",\n" +
                "              \"name\": \"宽城满族自治县\",\n" +
                "              \"center\": \"118.485472,40.611333\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0314\",\n" +
                "              \"adcode\": \"130804\",\n" +
                "              \"name\": \"鹰手营子矿区\",\n" +
                "              \"center\": \"117.659341,40.546424\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0314\",\n" +
                "              \"adcode\": \"130824\",\n" +
                "              \"name\": \"滦平县\",\n" +
                "              \"center\": \"117.332652,40.941644\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0314\",\n" +
                "              \"adcode\": \"130803\",\n" +
                "              \"name\": \"双滦区\",\n" +
                "              \"center\": \"117.799588,40.959426\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0314\",\n" +
                "              \"adcode\": \"130821\",\n" +
                "              \"name\": \"承德县\",\n" +
                "              \"center\": \"118.174166,40.768082\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0314\",\n" +
                "              \"adcode\": \"130802\",\n" +
                "              \"name\": \"双桥区\",\n" +
                "              \"center\": \"117.943121,40.974679\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0314\",\n" +
                "              \"adcode\": \"130822\",\n" +
                "              \"name\": \"兴隆县\",\n" +
                "              \"center\": \"117.500558,40.417358\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0314\",\n" +
                "              \"adcode\": \"130881\",\n" +
                "              \"name\": \"平泉市\",\n" +
                "              \"center\": \"118.702032,41.018482\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"citycode\": \"0311\",\n" +
                "          \"adcode\": \"130100\",\n" +
                "          \"name\": \"石家庄市\",\n" +
                "          \"center\": \"114.514976,38.042007\",\n" +
                "          \"level\": \"city\",\n" +
                "          \"districts\": [\n" +
                "            {\n" +
                "              \"citycode\": \"0311\",\n" +
                "              \"adcode\": \"130126\",\n" +
                "              \"name\": \"灵寿县\",\n" +
                "              \"center\": \"114.383013,38.307908\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0311\",\n" +
                "              \"adcode\": \"130183\",\n" +
                "              \"name\": \"晋州市\",\n" +
                "              \"center\": \"115.044141,38.033937\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0311\",\n" +
                "              \"adcode\": \"130132\",\n" +
                "              \"name\": \"元氏县\",\n" +
                "              \"center\": \"114.525508,37.767332\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0311\",\n" +
                "              \"adcode\": \"130127\",\n" +
                "              \"name\": \"高邑县\",\n" +
                "              \"center\": \"114.611659,37.615905\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0311\",\n" +
                "              \"adcode\": \"130123\",\n" +
                "              \"name\": \"正定县\",\n" +
                "              \"center\": \"114.57043,38.14699\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0311\",\n" +
                "              \"adcode\": \"130184\",\n" +
                "              \"name\": \"新乐市\",\n" +
                "              \"center\": \"114.683745,38.343952\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0311\",\n" +
                "              \"adcode\": \"130181\",\n" +
                "              \"name\": \"辛集市\",\n" +
                "              \"center\": \"115.217626,37.943239\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0311\",\n" +
                "              \"adcode\": \"130121\",\n" +
                "              \"name\": \"井陉县\",\n" +
                "              \"center\": \"114.145669,38.032366\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0311\",\n" +
                "              \"adcode\": \"130107\",\n" +
                "              \"name\": \"井陉矿区\",\n" +
                "              \"center\": \"114.062258,38.065446\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0311\",\n" +
                "              \"adcode\": \"130128\",\n" +
                "              \"name\": \"深泽县\",\n" +
                "              \"center\": \"115.20089,38.184572\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0311\",\n" +
                "              \"adcode\": \"130133\",\n" +
                "              \"name\": \"赵县\",\n" +
                "              \"center\": \"114.775914,37.756935\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0311\",\n" +
                "              \"adcode\": \"130125\",\n" +
                "              \"name\": \"行唐县\",\n" +
                "              \"center\": \"114.553044,38.437535\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0311\",\n" +
                "              \"adcode\": \"130105\",\n" +
                "              \"name\": \"新华区\",\n" +
                "              \"center\": \"114.463904,38.050749\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0311\",\n" +
                "              \"adcode\": \"130110\",\n" +
                "              \"name\": \"鹿泉区\",\n" +
                "              \"center\": \"114.313559,38.086536\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0311\",\n" +
                "              \"adcode\": \"130131\",\n" +
                "              \"name\": \"平山县\",\n" +
                "              \"center\": \"114.186007,38.260288\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0311\",\n" +
                "              \"adcode\": \"130104\",\n" +
                "              \"name\": \"桥西区\",\n" +
                "              \"center\": \"114.45004,38.025245\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0311\",\n" +
                "              \"adcode\": \"130111\",\n" +
                "              \"name\": \"栾城区\",\n" +
                "              \"center\": \"114.647922,37.900915\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0311\",\n" +
                "              \"adcode\": \"130108\",\n" +
                "              \"name\": \"裕华区\",\n" +
                "              \"center\": \"114.531599,38.007002\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0311\",\n" +
                "              \"adcode\": \"130109\",\n" +
                "              \"name\": \"藁城区\",\n" +
                "              \"center\": \"114.846562,38.022177\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0311\",\n" +
                "              \"adcode\": \"130102\",\n" +
                "              \"name\": \"长安区\",\n" +
                "              \"center\": \"114.538955,38.03682\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0311\",\n" +
                "              \"adcode\": \"130130\",\n" +
                "              \"name\": \"无极县\",\n" +
                "              \"center\": \"114.976256,38.178852\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0311\",\n" +
                "              \"adcode\": \"130129\",\n" +
                "              \"name\": \"赞皇县\",\n" +
                "              \"center\": \"114.386114,37.666549\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"citycode\": \"0310\",\n" +
                "          \"adcode\": \"130400\",\n" +
                "          \"name\": \"邯郸市\",\n" +
                "          \"center\": \"114.53915,36.625849\",\n" +
                "          \"level\": \"city\",\n" +
                "          \"districts\": [\n" +
                "            {\n" +
                "              \"citycode\": \"0310\",\n" +
                "              \"adcode\": \"130407\",\n" +
                "              \"name\": \"肥乡区\",\n" +
                "              \"center\": \"114.800199,36.548545\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0310\",\n" +
                "              \"adcode\": \"130431\",\n" +
                "              \"name\": \"鸡泽县\",\n" +
                "              \"center\": \"114.889951,36.911525\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0310\",\n" +
                "              \"adcode\": \"130408\",\n" +
                "              \"name\": \"永年区\",\n" +
                "              \"center\": \"114.536626,36.74188\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0310\",\n" +
                "              \"adcode\": \"130433\",\n" +
                "              \"name\": \"馆陶县\",\n" +
                "              \"center\": \"115.281818,36.548295\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0310\",\n" +
                "              \"adcode\": \"130423\",\n" +
                "              \"name\": \"临漳县\",\n" +
                "              \"center\": \"114.585514,36.322504\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0310\",\n" +
                "              \"adcode\": \"130434\",\n" +
                "              \"name\": \"魏县\",\n" +
                "              \"center\": \"114.939217,36.35926\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0310\",\n" +
                "              \"adcode\": \"130406\",\n" +
                "              \"name\": \"峰峰矿区\",\n" +
                "              \"center\": \"114.212571,36.419298\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0310\",\n" +
                "              \"adcode\": \"130427\",\n" +
                "              \"name\": \"磁县\",\n" +
                "              \"center\": \"114.373965,36.37507\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0310\",\n" +
                "              \"adcode\": \"130402\",\n" +
                "              \"name\": \"邯山区\",\n" +
                "              \"center\": \"114.531062,36.59457\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0310\",\n" +
                "              \"adcode\": \"130425\",\n" +
                "              \"name\": \"大名县\",\n" +
                "              \"center\": \"115.147985,36.286406\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0310\",\n" +
                "              \"adcode\": \"130403\",\n" +
                "              \"name\": \"丛台区\",\n" +
                "              \"center\": \"114.492875,36.636434\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0310\",\n" +
                "              \"adcode\": \"130404\",\n" +
                "              \"name\": \"复兴区\",\n" +
                "              \"center\": \"114.462581,36.638879\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0310\",\n" +
                "              \"adcode\": \"130435\",\n" +
                "              \"name\": \"曲周县\",\n" +
                "              \"center\": \"114.957549,36.766347\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0310\",\n" +
                "              \"adcode\": \"130430\",\n" +
                "              \"name\": \"邱县\",\n" +
                "              \"center\": \"115.200049,36.811783\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0310\",\n" +
                "              \"adcode\": \"130481\",\n" +
                "              \"name\": \"武安市\",\n" +
                "              \"center\": \"114.203685,36.69759\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0310\",\n" +
                "              \"adcode\": \"130426\",\n" +
                "              \"name\": \"涉县\",\n" +
                "              \"center\": \"113.692157,36.586073\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0310\",\n" +
                "              \"adcode\": \"130424\",\n" +
                "              \"name\": \"成安县\",\n" +
                "              \"center\": \"114.670094,36.445331\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0310\",\n" +
                "              \"adcode\": \"130432\",\n" +
                "              \"name\": \"广平县\",\n" +
                "              \"center\": \"114.921185,36.476727\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"citycode\": \"0316\",\n" +
                "          \"adcode\": \"131000\",\n" +
                "          \"name\": \"廊坊市\",\n" +
                "          \"center\": \"116.683546,39.538304\",\n" +
                "          \"level\": \"city\",\n" +
                "          \"districts\": [\n" +
                "            {\n" +
                "              \"citycode\": \"0316\",\n" +
                "              \"adcode\": \"131028\",\n" +
                "              \"name\": \"大厂回族自治县\",\n" +
                "              \"center\": \"116.98961,39.886569\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0316\",\n" +
                "              \"adcode\": \"131026\",\n" +
                "              \"name\": \"文安县\",\n" +
                "              \"center\": \"116.457628,38.873185\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0316\",\n" +
                "              \"adcode\": \"131082\",\n" +
                "              \"name\": \"三河市\",\n" +
                "              \"center\": \"117.078269,39.982933\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0316\",\n" +
                "              \"adcode\": \"131081\",\n" +
                "              \"name\": \"霸州市\",\n" +
                "              \"center\": \"116.391488,39.125238\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0316\",\n" +
                "              \"adcode\": \"131022\",\n" +
                "              \"name\": \"固安县\",\n" +
                "              \"center\": \"116.298696,39.438797\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0316\",\n" +
                "              \"adcode\": \"131025\",\n" +
                "              \"name\": \"大城县\",\n" +
                "              \"center\": \"116.653917,38.705232\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0316\",\n" +
                "              \"adcode\": \"131023\",\n" +
                "              \"name\": \"永清县\",\n" +
                "              \"center\": \"116.50608,39.330983\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0316\",\n" +
                "              \"adcode\": \"131002\",\n" +
                "              \"name\": \"安次区\",\n" +
                "              \"center\": \"116.6945443,39.50256863\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0316\",\n" +
                "              \"adcode\": \"131024\",\n" +
                "              \"name\": \"香河县\",\n" +
                "              \"center\": \"117.006072,39.763772\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0316\",\n" +
                "              \"adcode\": \"131003\",\n" +
                "              \"name\": \"广阳区\",\n" +
                "              \"center\": \"116.710667,39.52343\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"citycode\": \"0318\",\n" +
                "          \"adcode\": \"131100\",\n" +
                "          \"name\": \"衡水市\",\n" +
                "          \"center\": \"115.668987,37.739367\",\n" +
                "          \"level\": \"city\",\n" +
                "          \"districts\": [\n" +
                "            {\n" +
                "              \"citycode\": \"0318\",\n" +
                "              \"adcode\": \"131102\",\n" +
                "              \"name\": \"桃城区\",\n" +
                "              \"center\": \"115.675208,37.735152\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0318\",\n" +
                "              \"adcode\": \"131182\",\n" +
                "              \"name\": \"深州市\",\n" +
                "              \"center\": \"115.559576,38.001535\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0318\",\n" +
                "              \"adcode\": \"131128\",\n" +
                "              \"name\": \"阜城县\",\n" +
                "              \"center\": \"116.175424,37.862984\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0318\",\n" +
                "              \"adcode\": \"131103\",\n" +
                "              \"name\": \"冀州区\",\n" +
                "              \"center\": \"115.579392,37.550922\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0318\",\n" +
                "              \"adcode\": \"131126\",\n" +
                "              \"name\": \"故城县\",\n" +
                "              \"center\": \"115.965877,37.347873\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0318\",\n" +
                "              \"adcode\": \"131122\",\n" +
                "              \"name\": \"武邑县\",\n" +
                "              \"center\": \"115.887498,37.802036\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0318\",\n" +
                "              \"adcode\": \"131123\",\n" +
                "              \"name\": \"武强县\",\n" +
                "              \"center\": \"115.982119,38.041447\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0318\",\n" +
                "              \"adcode\": \"131121\",\n" +
                "              \"name\": \"枣强县\",\n" +
                "              \"center\": \"115.724365,37.514217\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0318\",\n" +
                "              \"adcode\": \"131127\",\n" +
                "              \"name\": \"景县\",\n" +
                "              \"center\": \"116.270558,37.692831\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0318\",\n" +
                "              \"adcode\": \"131124\",\n" +
                "              \"name\": \"饶阳县\",\n" +
                "              \"center\": \"115.725898,38.235313\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0318\",\n" +
                "              \"adcode\": \"131125\",\n" +
                "              \"name\": \"安平县\",\n" +
                "              \"center\": \"115.518918,38.234769\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"citycode\": \"0317\",\n" +
                "          \"adcode\": \"130900\",\n" +
                "          \"name\": \"沧州市\",\n" +
                "          \"center\": \"116.838715,38.304676\",\n" +
                "          \"level\": \"city\",\n" +
                "          \"districts\": [\n" +
                "            {\n" +
                "              \"citycode\": \"0317\",\n" +
                "              \"adcode\": \"130982\",\n" +
                "              \"name\": \"任丘市\",\n" +
                "              \"center\": \"116.084412,38.685325\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0317\",\n" +
                "              \"adcode\": \"130924\",\n" +
                "              \"name\": \"海兴县\",\n" +
                "              \"center\": \"117.497545,38.143308\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0317\",\n" +
                "              \"adcode\": \"130923\",\n" +
                "              \"name\": \"东光县\",\n" +
                "              \"center\": \"116.537138,37.888844\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0317\",\n" +
                "              \"adcode\": \"130928\",\n" +
                "              \"name\": \"吴桥县\",\n" +
                "              \"center\": \"116.391557,37.628225\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0317\",\n" +
                "              \"adcode\": \"130983\",\n" +
                "              \"name\": \"黄骅市\",\n" +
                "              \"center\": \"117.330043,38.372266\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0317\",\n" +
                "              \"adcode\": \"130902\",\n" +
                "              \"name\": \"新华区\",\n" +
                "              \"center\": \"116.866309,38.314094\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0317\",\n" +
                "              \"adcode\": \"130921\",\n" +
                "              \"name\": \"沧县\",\n" +
                "              \"center\": \"117.007478,38.21985569\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0317\",\n" +
                "              \"adcode\": \"130903\",\n" +
                "              \"name\": \"运河区\",\n" +
                "              \"center\": \"116.842964,38.283456\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0317\",\n" +
                "              \"adcode\": \"130925\",\n" +
                "              \"name\": \"盐山县\",\n" +
                "              \"center\": \"117.230681,38.058074\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0317\",\n" +
                "              \"adcode\": \"130930\",\n" +
                "              \"name\": \"孟村回族自治县\",\n" +
                "              \"center\": \"117.104514,38.053438\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0317\",\n" +
                "              \"adcode\": \"130927\",\n" +
                "              \"name\": \"南皮县\",\n" +
                "              \"center\": \"116.708603,38.038761\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0317\",\n" +
                "              \"adcode\": \"130922\",\n" +
                "              \"name\": \"青县\",\n" +
                "              \"center\": \"116.804137,38.583657\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0317\",\n" +
                "              \"adcode\": \"130926\",\n" +
                "              \"name\": \"肃宁县\",\n" +
                "              \"center\": \"115.829619,38.423044\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0317\",\n" +
                "              \"adcode\": \"130984\",\n" +
                "              \"name\": \"河间市\",\n" +
                "              \"center\": \"116.099362,38.446656\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0317\",\n" +
                "              \"adcode\": \"130981\",\n" +
                "              \"name\": \"泊头市\",\n" +
                "              \"center\": \"116.578322,38.084262\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0317\",\n" +
                "              \"adcode\": \"130929\",\n" +
                "              \"name\": \"献县\",\n" +
                "              \"center\": \"116.122767,38.189924\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"citycode\": \"0313\",\n" +
                "          \"adcode\": \"130700\",\n" +
                "          \"name\": \"张家口市\",\n" +
                "          \"center\": \"114.885895,40.768931\",\n" +
                "          \"level\": \"city\",\n" +
                "          \"districts\": [\n" +
                "            {\n" +
                "              \"citycode\": \"0313\",\n" +
                "              \"adcode\": \"130723\",\n" +
                "              \"name\": \"康保县\",\n" +
                "              \"center\": \"114.60018,41.853016\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0313\",\n" +
                "              \"adcode\": \"130732\",\n" +
                "              \"name\": \"赤城县\",\n" +
                "              \"center\": \"115.831256,40.913348\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0313\",\n" +
                "              \"adcode\": \"130706\",\n" +
                "              \"name\": \"下花园区\",\n" +
                "              \"center\": \"115.287127,40.502628\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0313\",\n" +
                "              \"adcode\": \"130709\",\n" +
                "              \"name\": \"崇礼区\",\n" +
                "              \"center\": \"115.282345,40.974741\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0313\",\n" +
                "              \"adcode\": \"130731\",\n" +
                "              \"name\": \"涿鹿县\",\n" +
                "              \"center\": \"115.196835,40.382681\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0313\",\n" +
                "              \"adcode\": \"130702\",\n" +
                "              \"name\": \"桥东区\",\n" +
                "              \"center\": \"114.894114,40.788472\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0313\",\n" +
                "              \"adcode\": \"130705\",\n" +
                "              \"name\": \"宣化区\",\n" +
                "              \"center\": \"115.099515,40.609444\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0313\",\n" +
                "              \"adcode\": \"130726\",\n" +
                "              \"name\": \"蔚县\",\n" +
                "              \"center\": \"114.589136,39.840154\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0313\",\n" +
                "              \"adcode\": \"130728\",\n" +
                "              \"name\": \"怀安县\",\n" +
                "              \"center\": \"114.386488,40.67527\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0313\",\n" +
                "              \"adcode\": \"130703\",\n" +
                "              \"name\": \"桥西区\",\n" +
                "              \"center\": \"114.868604,40.819553\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0313\",\n" +
                "              \"adcode\": \"130725\",\n" +
                "              \"name\": \"尚义县\",\n" +
                "              \"center\": \"113.968763,41.076588\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0313\",\n" +
                "              \"adcode\": \"130708\",\n" +
                "              \"name\": \"万全区\",\n" +
                "              \"center\": \"114.740584,40.767377\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0313\",\n" +
                "              \"adcode\": \"130722\",\n" +
                "              \"name\": \"张北县\",\n" +
                "              \"center\": \"114.719927,41.159039\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0313\",\n" +
                "              \"adcode\": \"130730\",\n" +
                "              \"name\": \"怀来县\",\n" +
                "              \"center\": \"115.517868,40.415625\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0313\",\n" +
                "              \"adcode\": \"130727\",\n" +
                "              \"name\": \"阳原县\",\n" +
                "              \"center\": \"114.150267,40.104303\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0313\",\n" +
                "              \"adcode\": \"130724\",\n" +
                "              \"name\": \"沽源县\",\n" +
                "              \"center\": \"115.688544,41.670497\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"citycode\": \"0312\",\n" +
                "          \"adcode\": \"130600\",\n" +
                "          \"name\": \"保定市\",\n" +
                "          \"center\": \"115.464523,38.874476\",\n" +
                "          \"level\": \"city\",\n" +
                "          \"districts\": [\n" +
                "            {\n" +
                "              \"citycode\": \"0312\",\n" +
                "              \"adcode\": \"130636\",\n" +
                "              \"name\": \"顺平县\",\n" +
                "              \"center\": \"115.135133,38.837988\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0312\",\n" +
                "              \"adcode\": \"130632\",\n" +
                "              \"name\": \"安新县\",\n" +
                "              \"center\": \"115.935688,38.936102\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0312\",\n" +
                "              \"adcode\": \"130683\",\n" +
                "              \"name\": \"安国市\",\n" +
                "              \"center\": \"115.327088,38.418985\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0312\",\n" +
                "              \"adcode\": \"130623\",\n" +
                "              \"name\": \"涞水县\",\n" +
                "              \"center\": \"115.713651,39.394305\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0312\",\n" +
                "              \"adcode\": \"130624\",\n" +
                "              \"name\": \"阜平县\",\n" +
                "              \"center\": \"114.195118,38.849221\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0312\",\n" +
                "              \"adcode\": \"130631\",\n" +
                "              \"name\": \"望都县\",\n" +
                "              \"center\": \"115.15542,38.696221\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0312\",\n" +
                "              \"adcode\": \"130627\",\n" +
                "              \"name\": \"唐县\",\n" +
                "              \"center\": \"114.982968,38.748477\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0312\",\n" +
                "              \"adcode\": \"130684\",\n" +
                "              \"name\": \"高碑店市\",\n" +
                "              \"center\": \"115.873612,39.327233\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0312\",\n" +
                "              \"adcode\": \"130626\",\n" +
                "              \"name\": \"定兴县\",\n" +
                "              \"center\": \"115.808183,39.263219\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0312\",\n" +
                "              \"adcode\": \"130629\",\n" +
                "              \"name\": \"容城县\",\n" +
                "              \"center\": \"115.861635,39.043321\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0312\",\n" +
                "              \"adcode\": \"130638\",\n" +
                "              \"name\": \"雄县\",\n" +
                "              \"center\": \"116.108624,38.994825\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0312\",\n" +
                "              \"adcode\": \"130637\",\n" +
                "              \"name\": \"博野县\",\n" +
                "              \"center\": \"115.464295,38.458048\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0312\",\n" +
                "              \"adcode\": \"130634\",\n" +
                "              \"name\": \"曲阳县\",\n" +
                "              \"center\": \"114.740476,38.614409\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0312\",\n" +
                "              \"adcode\": \"130635\",\n" +
                "              \"name\": \"蠡县\",\n" +
                "              \"center\": \"115.583701,38.488064\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0312\",\n" +
                "              \"adcode\": \"130628\",\n" +
                "              \"name\": \"高阳县\",\n" +
                "              \"center\": \"115.779149,38.700846\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0312\",\n" +
                "              \"adcode\": \"130681\",\n" +
                "              \"name\": \"涿州市\",\n" +
                "              \"center\": \"115.97444,39.485684\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0312\",\n" +
                "              \"adcode\": \"130630\",\n" +
                "              \"name\": \"涞源县\",\n" +
                "              \"center\": \"114.694416,39.360622\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0312\",\n" +
                "              \"adcode\": \"130607\",\n" +
                "              \"name\": \"满城区\",\n" +
                "              \"center\": \"115.322246,38.949732\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0312\",\n" +
                "              \"adcode\": \"130602\",\n" +
                "              \"name\": \"竞秀区\",\n" +
                "              \"center\": \"115.458671,38.877318\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0312\",\n" +
                "              \"adcode\": \"130606\",\n" +
                "              \"name\": \"莲池区\",\n" +
                "              \"center\": \"115.497153,38.883528\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0312\",\n" +
                "              \"adcode\": \"130608\",\n" +
                "              \"name\": \"清苑区\",\n" +
                "              \"center\": \"115.48988,38.76527\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0312\",\n" +
                "              \"adcode\": \"130682\",\n" +
                "              \"name\": \"定州市\",\n" +
                "              \"center\": \"114.990321,38.516746\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0312\",\n" +
                "              \"adcode\": \"130609\",\n" +
                "              \"name\": \"徐水区\",\n" +
                "              \"center\": \"115.655772,39.018781\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0312\",\n" +
                "              \"adcode\": \"130633\",\n" +
                "              \"name\": \"易县\",\n" +
                "              \"center\": \"115.497487,39.350219\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"citycode\": \"0319\",\n" +
                "          \"adcode\": \"130500\",\n" +
                "          \"name\": \"邢台市\",\n" +
                "          \"center\": \"114.49742,37.060227\",\n" +
                "          \"level\": \"city\",\n" +
                "          \"districts\": [\n" +
                "            {\n" +
                "              \"citycode\": \"0319\",\n" +
                "              \"adcode\": \"130524\",\n" +
                "              \"name\": \"柏乡县\",\n" +
                "              \"center\": \"114.693447,37.48288\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0319\",\n" +
                "              \"adcode\": \"130581\",\n" +
                "              \"name\": \"南宫市\",\n" +
                "              \"center\": \"115.40866,37.358907\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0319\",\n" +
                "              \"adcode\": \"130530\",\n" +
                "              \"name\": \"新河县\",\n" +
                "              \"center\": \"115.251005,37.520891\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0319\",\n" +
                "              \"adcode\": \"130531\",\n" +
                "              \"name\": \"广宗县\",\n" +
                "              \"center\": \"115.142766,37.074795\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0319\",\n" +
                "              \"adcode\": \"130534\",\n" +
                "              \"name\": \"清河县\",\n" +
                "              \"center\": \"115.665081,37.0451\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0319\",\n" +
                "              \"adcode\": \"130506\",\n" +
                "              \"name\": \"南和区\",\n" +
                "              \"center\": \"114.683683,37.005626\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0319\",\n" +
                "              \"adcode\": \"130528\",\n" +
                "              \"name\": \"宁晋县\",\n" +
                "              \"center\": \"114.940006,37.624524\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0319\",\n" +
                "              \"adcode\": \"130533\",\n" +
                "              \"name\": \"威县\",\n" +
                "              \"center\": \"115.266829,36.975164\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0319\",\n" +
                "              \"adcode\": \"130535\",\n" +
                "              \"name\": \"临西县\",\n" +
                "              \"center\": \"115.501258,36.871312\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0319\",\n" +
                "              \"adcode\": \"130582\",\n" +
                "              \"name\": \"沙河市\",\n" +
                "              \"center\": \"114.503023,36.855548\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0319\",\n" +
                "              \"adcode\": \"130523\",\n" +
                "              \"name\": \"内丘县\",\n" +
                "              \"center\": \"114.512226,37.287612\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0319\",\n" +
                "              \"adcode\": \"130502\",\n" +
                "              \"name\": \"襄都区\",\n" +
                "              \"center\": \"114.507443,37.071314\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0319\",\n" +
                "              \"adcode\": \"130505\",\n" +
                "              \"name\": \"任泽区\",\n" +
                "              \"center\": \"114.671339,37.121958\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0319\",\n" +
                "              \"adcode\": \"130503\",\n" +
                "              \"name\": \"信都区\",\n" +
                "              \"center\": \"114.468229,37.093798\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0319\",\n" +
                "              \"adcode\": \"130522\",\n" +
                "              \"name\": \"临城县\",\n" +
                "              \"center\": \"114.498651,37.444512\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0319\",\n" +
                "              \"adcode\": \"130532\",\n" +
                "              \"name\": \"平乡县\",\n" +
                "              \"center\": \"115.03008,37.063771\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0319\",\n" +
                "              \"adcode\": \"130529\",\n" +
                "              \"name\": \"巨鹿县\",\n" +
                "              \"center\": \"115.037884,37.221293\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"citycode\": \"0319\",\n" +
                "              \"adcode\": \"130525\",\n" +
                "              \"name\": \"隆尧县\",\n" +
                "              \"center\": \"114.770509,37.351232\",\n" +
                "              \"level\": \"district\",\n" +
                "              \"districts\": [ ]\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        Map<String, List<String>> map = Maps.newHashMap();
        JSONObject json = JSON.parseObject(str);
        JSONArray cityDistricts = json.getJSONArray("districts").getJSONObject(0).getJSONArray("districts");
        for(int i = 0; i < cityDistricts.size(); i++){
            JSONArray countyDistricts = cityDistricts.getJSONObject(i).getJSONArray("districts");
            List<String> countys = Lists.newArrayList();
            for(int j = 0; j < countyDistricts.size(); j++){
                String name = countyDistricts.getJSONObject(j).getString("name");
                countys.add(name);
            }
            String city = cityDistricts.getJSONObject(i).getString("name");
            map.put(city,countys);
        }
        System.out.println(map);

        String citys = "{衡水市=[桃城区, 深州市, 阜城县, 冀州区, 故城县, 武邑县, 武强县, 枣强县, 景县, 饶阳县, 安平县], 保定市=[顺平县, 安新县, 安国市, 涞水县, 阜平县, 望都县, 唐县, 高碑店市, 定兴县, 容城县, 雄县, 博野县, 曲阳县, 蠡县, 高阳县, 涿州市, 涞源县, 满城区, 竞秀区, 莲池区, 清苑区, 定州市, 徐水区, 易县], 张家口市=[康保县, 赤城县, 下花园区, 崇礼区, 涿鹿县, 桥东区, 宣化区, 蔚县, 怀安县, 桥西区, 尚义县, 万全区, 张北县, 怀来县, 阳原县, 沽源县], 承德市=[围场满族蒙古族自治县, 丰宁满族自治县, 隆化县, 宽城满族自治县, 鹰手营子矿区, 滦平县, 双滦区, 承德县, 双桥区, 兴隆县, 平泉市], 邯郸市=[肥乡区, 鸡泽县, 永年区, 馆陶县, 临漳县, 魏县, 峰峰矿区, 磁县, 邯山区, 大名县, 丛台区, 复兴区, 曲周县, 邱县, 武安市, 涉县, 成安县, 广平县], 沧州市=[任丘市, 海兴县, 东光县, 吴桥县, 黄骅市, 新华区, 沧县, 运河区, 盐山县, 孟村回族自治县, 南皮县, 青县, 肃宁县, 河间市, 泊头市, 献县], 秦皇岛市=[卢龙县, 山海关区, 海港区, 抚宁区, 昌黎县, 北戴河区, 青龙满族自治县], 唐山市=[迁安市, 乐亭县, 玉田县, 路南区, 曹妃甸区, 滦南县, 开平区, 古冶区, 迁西县, 滦州市, 遵化市, 丰南区, 丰润区, 路北区], 石家庄市=[灵寿县, 晋州市, 元氏县, 高邑县, 正定县, 新乐市, 辛集市, 井陉县, 井陉矿区, 深泽县, 赵县, 行唐县, 新华区, 鹿泉区, 平山县, 桥西区, 栾城区, 裕华区, 藁城区, 长安区, 无极县, 赞皇县], 廊坊市=[大厂回族自治县, 文安县, 三河市, 霸州市, 固安县, 大城县, 永清县, 安次区, 香河县, 广阳区], 邢台市=[柏乡县, 南宫市, 新河县, 广宗县, 清河县, 南和区, 宁晋县, 威县, 临西县, 沙河市, 内丘县, 襄都区, 任泽区, 信都区, 临城县, 平乡县, 巨鹿县, 隆尧县]}";
    }
}
