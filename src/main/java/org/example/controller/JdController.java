package org.example.controller;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.Constants;
import org.example.entitys.*;
import org.example.networks.HttpClientUtil;
import org.example.utils.HttpMd5;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Controller
public class JdController {
    @RequestMapping("/jd")
    @CrossOrigin
    @ResponseBody
    public String home(String eliteId, String timestamp) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("app_key", Constants.APP_KEY_JD);
        map.put("method", Constants.JD_METHOD);
        map.put("v", "1.0");
        map.put("sign_method", "md5");
        map.put("format", "json");
        map.put("timestamp", timestamp);
        map.put("sortName", "comments");//按照评论数 排序
        TreeMap<String, Object> buy_param_json = new TreeMap<>();
        TreeMap<String, Object> goodsReq = new TreeMap<>();
        goodsReq.put("eliteId", eliteId);
        goodsReq.put("pageIndex", 1);
        goodsReq.put("pageSize", 15);
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
                JdBaseEntity<List<JdMaterialEntity>> jdBaseEntity = new Gson().fromJson(queryResult, new TypeToken<JdBaseEntity<List<JdMaterialEntity>>>() {
                }.getType());
                List<JdMaterialEntity> dataList = jdBaseEntity.getData();
                ArrayList<ResultJdEntiy> resultJdEntiys = new ArrayList<>();
                for (int i = 0; i < dataList.size(); i++) {
                    JdMaterialEntity jdMaterialEntity = dataList.get(i);
                    CommissionInfoEntity commissionInfo = jdMaterialEntity.getCommissionInfo();//佣金信息
                    JdMaterialEntity.CouponInfoContentEntity couponInfo = jdMaterialEntity.getCouponInfo();//优惠券信息
                    ImageInfoContentEntity imageInfo = jdMaterialEntity.getImageInfo();//第一个图片链接为主图链接
                    JdMaterialEntity.PriceInfoEntity priceInfo = jdMaterialEntity.getPriceInfo();//价格信息
                    ShopInfoEntity shopInfo = jdMaterialEntity.getShopInfo();//店铺信息
                    ResultJdEntiy resultJdEntiy = new ResultJdEntiy();
                    resultJdEntiy.setSkuName(jdMaterialEntity.getSkuName());
                    resultJdEntiy.setSkuId(jdMaterialEntity.getSkuId());
                    resultJdEntiy.setShopName(shopInfo.getShopName());

                    if (couponInfo != null && couponInfo.getCouponList() != null && couponInfo.getCouponList().size() > 0) {
                        List<CouponInfoEntity> couponList = couponInfo.getCouponList();
                        for (int j = 0; j < couponList.size(); j++) {
                            int isBest = couponList.get(j).getIsBest();
                            if (isBest == 1) {
                                CouponInfoEntity couponInfoEntity = couponList.get(j);
                                int discount = couponInfoEntity.getDiscount();
                                resultJdEntiy.setDiscount(discount);
                                resultJdEntiy.setCouponUrl(couponInfoEntity.getLink());
                                break;
                            }
                        }
                    }
                    List<ImageInfoContentEntity.ImageInfoEntity> imageList = imageInfo.getImageList();
                    if (imageList != null && imageList.size() > 0) {
                        resultJdEntiy.setImgUrl(imageList.get(0).getUrl());
                    }
                    resultJdEntiy.setMaterialUrl(jdMaterialEntity.getMaterialUrl());
                    resultJdEntiy.setComments(jdMaterialEntity.getComments());
                    resultJdEntiy.setGoodCommentsShare(jdMaterialEntity.getGoodCommentsShare());
                    resultJdEntiy.setHistoryPriceDay(priceInfo.getHistoryPriceDay());
                    resultJdEntiy.setPrice(priceInfo.getPrice());
                    resultJdEntiy.setLowestCouponPrice(priceInfo.getLowestCouponPrice());
                    resultJdEntiy.setLowestPrice(priceInfo.getLowestPrice());
                    resultJdEntiy.setLowestPriceType(priceInfo.getLowestPriceType());
                    resultJdEntiys.add(resultJdEntiy);
                }
                JdBaseEntity jdBaseEntity1 = new JdBaseEntity();
                jdBaseEntity1.setData(resultJdEntiys);
                jdBaseEntity1.setCode(1000);
                queryResult = new Gson().toJson(jdBaseEntity1);
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

    @RequestMapping("/")
    public String home() {
        return "index";
    }
}
