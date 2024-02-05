//package lyzzcw.work.component.common.http.okhttp;
//
//import io.github.admin4j.http.ApiJsonClient;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author: lzy
// * @version: 1.0
// * Date: 2023/1/11 10:39
// * Description: No Description
// */
//public class EbayClient extends ApiJsonClient {
//    /**
//     * 店铺配置
//     *
//     * @param storeId
//     */
//    public EbayClient(Long storeId) {
//
//        //TODO 获取店铺相关配置
//        Map<String, String> config = new HashMap<>();
//
//        basePath = "https://api.ebay.com";
//        defaultHeaderMap.put("Authorization", "Bearer " + config.get("accessToken"));
//        defaultHeaderMap.put("X-EBAY-C-MARKETPLACE-ID", config.get("marketplaceId"));
//    }
//}
