package lyzzcw.work.component.common.HttpUtils.okhttp;

import com.alibaba.fastjson.JSONObject;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: lzy
 * @version: 1.0
 * Date: 2023/1/11 10:41
 * Description: 订单相关api
 */
public class EbayOrderClient extends EbayClient {
    /**
     * 店铺配置
     *
     * @param storeId
     */
    public EbayOrderClient(Long storeId) {
        super(storeId);
    }

    /**
     * 订单列表
     *
     * @param beginTime
     * @param endTime
     * @param limit
     * @param offset
     * @return
     */
    public JSONObject orders(String beginTime, String endTime, int limit, int offset) {

        final String path = "/sell/fulfillment/v1/order";

        String filter = MessageFormat.format("lastmodifieddate:[{0}..{1}]", beginTime, endTime);

        //
        Map<String, Object> queryMap = new HashMap<>(8);
        queryMap.put("filter", filter);
        queryMap.put("limit", limit);
        queryMap.put("offset", offset);

        return get("/sell/inventory/v1/inventory_item", queryMap);
    }
}
