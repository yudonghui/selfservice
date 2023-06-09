package org.example.controller;

import com.alibaba.fastjson.JSON;
import org.example.Constants;
import org.example.networks.HttpClientUtil;
import org.example.utils.HttpMd5;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.TreeMap;

@RestController
public class JdController {
    @RequestMapping("/jd")
    String home(String eliteId, String timestamp) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("app_key", Constants.APP_KEY_JD);
        map.put("method", Constants.JD_METHOD);
        map.put("v", "1.0");
        map.put("sign_method", "md5");
        map.put("format", "json");
        map.put("timestamp", timestamp);

        TreeMap<String, Object> buy_param_json = new TreeMap<>();
        TreeMap<String, Object> goodsReq = new TreeMap<>();
        goodsReq.put("eliteId", eliteId);
        goodsReq.put("pageIndex", 1);
        goodsReq.put("pageSize", 10);
        buy_param_json.put("goodsReq", goodsReq);
        map.put("360buy_param_json", JSON.toJSONString(buy_param_json));

        String sign = HttpMd5.buildSignJd(map);
        map.put("sign", sign);
        String request = HttpClientUtil.getRequest(Constants.JD_URL, map);
        String queryResult = "";
        try {
            JSONObject jsonObject = new JSONObject(request);
            if (!request.contains("error_response")) {
                JSONObject jd_union_open_goods_jingfen_query_responce = jsonObject.getJSONObject("jd_union_open_goods_jingfen_query_responce");
                String code = jd_union_open_goods_jingfen_query_responce.getString("code");
                queryResult = jd_union_open_goods_jingfen_query_responce.getString("queryResult");
            } else {
                JSONObject error_response = jsonObject.getJSONObject("error_response");
                String code = error_response.getString("code");
                String zh_desc = error_response.getString("zh_desc");
                queryResult = "{\"code\":" + code + ",\"data\":null,\"message\":" + zh_desc + "}";
            }

        } catch (Exception e) {
            queryResult = "{\"code\":2000,\"data\":null,\"message\":\"系统错误\"}";
            e.printStackTrace();
        }
        return queryResult;
    }
}
