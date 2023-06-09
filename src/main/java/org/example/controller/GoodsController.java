package org.example.controller;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.util.TextUtils;
import org.example.commons.Constants;
import org.example.commons.CommonUtils;
import org.example.entitys.*;
import org.example.networks.HttpClientUtil;
import org.example.networks.MsgCode;
import org.example.utils.DateFormtUtils;
import org.example.utils.HttpMd5;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestController
public class GoodsController {
    @RequestMapping(value = "/post",method = RequestMethod.POST)
    @CrossOrigin//解决跨域问题
    public String post(@RequestBody Map<String,Object> map) {
        return map.get("name")+""+map.get("age");
    }

    @RequestMapping("/jd")
    @CrossOrigin//解决跨域问题
    public String home(String eliteId, String timestamp) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("app_key", Constants.APP_KEY_JD);
        map.put("method", Constants.JD_METHOD);
        map.put("v", "1.0");
        map.put("sign_method", "md5");
        map.put("format", "json");
        map.put("timestamp", timestamp);
        TreeMap<String, Object> buy_param_json = new TreeMap<>();
        TreeMap<String, Object> goodsReq = new TreeMap<>();
        goodsReq.put("eliteId", TextUtils.isEmpty(eliteId) ? CommonUtils.randomJdCode() : eliteId);
        goodsReq.put("pageIndex", 1);
        goodsReq.put("pageSize", 50);
        goodsReq.put("sortName", getSortName());//按照评论数 排序
        //goodsReq.put("fields", "hotWords");//支持出参数据筛选，逗号','分隔，目前可用：videoInfo(视频信息),hotWords(热词),similar(相似推荐商品),documentInfo(段子信息)，skuLabelInfo（商品标签），promotionLabelInfo（商品促销标签）,companyType（小店标识）,purchasePriceInfo（到手价）
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
                ArrayList<ResultJdEntiy> resultJdOrigin = new ArrayList<>();
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
                    long comments = getLong(jdMaterialEntity.getComments());
                    String commentStr;
                    if (comments > 10000000) {
                        commentStr = comments / 10000000 + "千万+";
                    } else if (comments > 1000000) {
                        commentStr = comments / 1000000 + "00万+";
                    } else if (comments > 100000) {
                        commentStr = comments / 100000 + "0万+";
                    } else if (comments > 10000) {
                        commentStr = comments / 10000 + "万+";
                    } else {
                        commentStr = comments + "";
                    }
                    resultJdEntiy.setComments(commentStr);
                    resultJdEntiy.setGoodCommentsShare(jdMaterialEntity.getGoodCommentsShare());
                    resultJdEntiy.setHistoryPriceDay(priceInfo.getHistoryPriceDay());
                    resultJdEntiy.setPrice(priceInfo.getPrice());
                    resultJdEntiy.setLowestCouponPrice(priceInfo.getLowestCouponPrice());
                    resultJdEntiy.setLowestPrice(priceInfo.getLowestPrice());
                    resultJdEntiy.setLowestPriceType(priceInfo.getLowestPriceType());
                    resultJdOrigin.add(resultJdEntiy);
                    if (resultJdEntiy.getDiscount() >= 1) {//优惠券不为空
                        resultJdEntiys.add(resultJdEntiy);
                    }
                }
                if (resultJdEntiys.size() < 5 && resultJdOrigin.size() > 10) {
                    for (int i = 0; i < 10; i++) {
                        resultJdEntiys.add(resultJdOrigin.get(i));
                    }
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

    /**
     *
     */
    @RequestMapping("/tb")
    @CrossOrigin
    public String homeTb(String materialId) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("method", Constants.TB_METHOD);
        map.put("app_key", Constants.APP_KEY_TB);
        map.put("timestamp", DateFormtUtils.getCurrentDate(DateFormtUtils.YMD_HMS));
        map.put("sign_method", "md5");
        map.put("format", "json");
        map.put("adzone_id", Constants.ADZONE_ID);
        map.put("material_id", TextUtils.isEmpty(materialId) ? CommonUtils.randomTbCode() : materialId);
        map.put("v", "2.0");
        map.put("simplify", true);
        map.put("page_no", "1");
        map.put("page_size", "20");
        String sign = HttpMd5.buildSignTb(map);
        map.put("sign", sign);
        String request = HttpClientUtil.getRequestO(Constants.TB_URL, map);
        String queryResult = "";
        try {
            JSONObject jsonObject = new JSONObject(request);
            if (!request.contains("error_response")) {
                TbBaseEntity<List<MaterialEntity>> tbBaseEntity = new Gson().fromJson(request, new TypeToken<TbBaseEntity<List<MaterialEntity>>>() {
                }.getType());
                List<MaterialEntity> result_list = tbBaseEntity.getResult_list();
                TbBaseEntity tbBaseEntity1 = new TbBaseEntity();
                tbBaseEntity1.setCode(1000);
                tbBaseEntity1.setResult_list(result_list);
                queryResult = new Gson().toJson(tbBaseEntity1);
            } else {
                JSONObject error_response = jsonObject.getJSONObject("error_response");
                int code = error_response.getInt("code");
                queryResult = "{\"code\":" + code + ",\"data\":null,\"message\":" + MsgCode.getStrByCode(code) + "}";
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

    public static long getLong(String string) {
        if (TextUtils.isEmpty(string)) return 0;
        try {
            long anInt = Long.parseLong(string);
            return anInt;
        } catch (Exception e) {
            return 0;
        }
    }

    public static String getSortName() {
        // 排序字段(price：单价, commissionShare：佣金比例, commission：佣金， inOrderCount30DaysSku：sku维度30天引单量，comments：评论数，goodComments：好评数)
        String[] sortNames = {"price", "commissionShare", "commission", "inOrderCount30DaysSku", "comments", "goodComments"};
        int num = (int) (Math.random() * 6);
        return sortNames[num];
    }

    public ArrayList<ResultJdEntiy> getJdList(int pageIndex, int pageSize) {
        return null;
    }
}
