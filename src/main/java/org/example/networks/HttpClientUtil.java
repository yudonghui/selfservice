package org.example.networks;


import com.alibaba.fastjson.JSON;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;
import org.example.commons.Constants;
import org.example.utils.DateFormtUtils;
import org.example.utils.HttpMd5;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;


public class HttpClientUtil {
    private static Log logger = LogFactory.getLog(HttpClientUtil.class);

    public static String getRequest(String url) {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        String body = "";
        try {
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response1 = httpclient.execute(httpGet);
            // The underlying HTTP connection is still held by the response object
            // to allow the response content to be streamed directly from the network socket.
            // In order to ensure correct deallocation of system resources
            // the user MUST call CloseableHttpResponse#close() from a finally clause.
            // Please note that if response content is not fully consumed the underlying
            // connection cannot be safely re-used and will be shut down and discarded
            // by the connection manager.

            try {
                System.out.println(response1.getStatusLine());
                HttpEntity entity1 = response1.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed

                body = EntityUtils.toString(entity1);
                System.out.println(body);
                EntityUtils.consume(entity1);
            } finally {
                response1.close();
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return body;
    }


    /**
     * get请求带日志
     *
     * @param url
     * @return
     */
    public static String getRequestNoTry(String url) {
        return HttpClientUtil.getRequestNoTry(url, null);
    }

    public static String getRequestNoTry(String url, Integer timeout) {


        CloseableHttpClient httpclient = HttpClients.createDefault();
        String body = "";
        try {
            HttpGet httpGet = new HttpGet(url);
            if (timeout != null) {
                RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).setConnectionRequestTimeout(timeout).setStaleConnectionCheckEnabled(true).build();
                httpGet.setConfig(requestConfig);
            }
            CloseableHttpResponse response1 = httpclient.execute(httpGet);

            // The underlying HTTP connection is still held by the response object
            // to allow the response content to be streamed directly from the network socket.
            // In order to ensure correct deallocation of system resources
            // the user MUST call CloseableHttpResponse#close() from a finally clause.
            // Please note that if response content is not fully consumed the underlying
            // connection cannot be safely re-used and will be shut down and discarded
            // by the connection manager.
            try {
                System.out.println(response1.getStatusLine());
                HttpEntity entity1 = response1.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed

                body = EntityUtils.toString(entity1);
                System.out.println(body);
                EntityUtils.consume(entity1);
            } catch (Exception e) {

            } finally {
                response1.close();
            }
        } catch (ClientProtocolException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return body;
    }

    /**
     * get请求带编码
     *
     * @param url
     * @param paramMap 参数
     */
    public static String getRequest(String url, Map<String, String> paramMap) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        StringBuffer urlSB = new StringBuffer(url);
        if (paramMap != null && paramMap.size() > 0) {
            for (String key : paramMap.keySet()) {
                String value = paramMap.get(key);
                if (TextUtils.isEmpty(value)) {
                    continue;
                }
                try {
                    if (urlSB.indexOf("?") > -1) {
                        urlSB.append("&");
                        urlSB.append(key);
                        urlSB.append("=");
                        urlSB.append(URLEncoder.encode(value, "UTF-8"));
                    } else {
                        urlSB.append("?");
                        urlSB.append(key);
                        urlSB.append("=");
                        urlSB.append(URLEncoder.encode(value, "UTF-8"));
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        logger.info(urlSB.toString());
        return HttpClientUtil.getRequest(urlSB.toString());

    }

    /**
     * get请求带编码
     *
     * @param url
     * @param paramMap 参数
     */
    public static String getRequestO(String url, Map<String, Object> paramMap) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        StringBuffer urlSB = new StringBuffer(url);
        if (paramMap != null && paramMap.size() > 0) {
            for (String key : paramMap.keySet()) {
                Object value = paramMap.get(key);
                if (ObjectUtils.isEmpty(value)) {
                    continue;
                }
                try {
                    if (urlSB.indexOf("?") > -1) {
                        urlSB.append("&");
                        urlSB.append(key);
                        urlSB.append("=");
                        if (value instanceof String) {
                            urlSB.append(URLEncoder.encode((String) value, "UTF-8"));
                        } else {
                            urlSB.append(value);
                        }
                    } else {
                        urlSB.append("?");
                        urlSB.append(key);
                        urlSB.append("=");
                        if (value instanceof String) {
                            urlSB.append(URLEncoder.encode((String) value, "UTF-8"));
                        } else {
                            urlSB.append(value);
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        logger.info(urlSB.toString());
        return HttpClientUtil.getRequest(urlSB.toString());

    }

    public static String postRequest(String url, String jsonParam) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        logger.info(":::URL:" + url + ";JsonParam:" + jsonParam);
        String body = "{}";
//		 String str= post("https://localhost:443/ssl/test.shtml","name=12&page=34","application/x-www-form-urlencoded", "UTF-8", 10000, 10000);
        try {
            HttpPost httpPost = new HttpPost(url);
            HttpEntity entity = new StringEntity(jsonParam, ContentType.create("application/x-www-form-urlencoded", "UTF-8"));
            httpPost.setEntity(entity);
            CloseableHttpResponse response2 = httpclient.execute(httpPost);

            try {
                System.out.println(response2.getStatusLine());
                HttpEntity entity2 = response2.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
                body = EntityUtils.toString(entity2);
                System.out.println(body);
                EntityUtils.consume(entity2);

            } catch (Exception e) {
                logger.error(":::Post Request Error.Url:" + url + ";JsonParam:" + jsonParam, e);
            } finally {
                response2.close();
            }
        } catch (Exception e) {
            logger.error(":::Post Request Error.Url:" + url + ";JsonParam:" + jsonParam, e);
        } finally {
            httpclient.close();
        }
        return body;
    }

    public static String postRequest(String url, Map<String, String> param, Map<String, String> headers) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String body = "{}";
        try {

            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            Set<String> key = param.keySet();
            for (Iterator<String> it = key.iterator(); it.hasNext(); ) {
                String k = it.next();
                nvps.add(new BasicNameValuePair(k, param.get(k)));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            CloseableHttpResponse response2 = httpclient.execute(httpPost);

            try {
                System.out.println(response2.getStatusLine());
                HttpEntity entity2 = response2.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
                body = EntityUtils.toString(entity2);
                System.out.println(body);
                EntityUtils.consume(entity2);

            } catch (Exception e) {
                logger.error(":::Post Request Error.Url:" + url + ";Param:" + param, e);
            } finally {
                response2.close();
            }
        } catch (Exception e) {
            logger.error(":::Post Request Error.Url:" + url + ";Param:" + param, e);
        } finally {
            httpclient.close();
        }
        return body;
    }

    public static String postRequest(String url, Map<String, String> param) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String body = "{}";
        try {
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            Set<String> key = param.keySet();
            for (Iterator<String> it = key.iterator(); it.hasNext(); ) {
                String k = (String) it.next();
                nvps.add(new BasicNameValuePair(k, param.get(k)));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            CloseableHttpResponse response2 = httpclient.execute(httpPost);

            try {
                System.out.println(response2.getStatusLine());
                HttpEntity entity2 = response2.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
                body = EntityUtils.toString(entity2);
                System.out.println(body);
                EntityUtils.consume(entity2);

            } catch (Exception e) {
                logger.error(":::Post Request Error.Url:" + url + ";Param:" + param, e);
            } finally {
                response2.close();
            }
        } catch (Exception e) {
            logger.error(":::Post Request Error.Url:" + url + ";Param:" + param, e);
        } finally {
            httpclient.close();
        }
        return body;
    }


    /**
     * @param url
     * @param jsonParam
     * @return
     * @throws Exception
     */
    public static String postRequestByJsonStr(String url, String jsonParam, Map<String, String> header) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        logger.info(":::URL:" + url + ";JsonParam:" + jsonParam);
        String body = "{}";
        try {
            HttpPost httpPost = new HttpPost(url);
            HttpEntity entity = new StringEntity(jsonParam, ContentType.create("application/json", "UTF-8"));
            httpPost.setEntity(entity);
            //设置header
            if (null != header && header.keySet().size() > 0) {
                header.keySet().forEach(item -> {
                    httpPost.addHeader(item, header.get(item));

                });
            }
            CloseableHttpResponse response2 = httpclient.execute(httpPost);
            try {
                System.out.println(response2.getStatusLine());
                HttpEntity entity2 = response2.getEntity();
                body = EntityUtils.toString(entity2);
                System.out.println(body);
                EntityUtils.consume(entity2);

            } catch (Exception e) {
                logger.error(":::Post Request Error.Url:" + url + ";JsonParam:" + jsonParam, e);
            } finally {
                response2.close();
            }
        } catch (Exception e) {
            logger.error(":::Post Request Error.Url:" + url + ";JsonParam:" + jsonParam, e);
        } finally {
            httpclient.close();
        }
        return body;
    }

    /**
     * @param url
     * @param jsonParam
     * @return
     * @throws Exception
     */
    public static String postRequestByFormData(String url, String jsonParam) {
        // 创建HttpClient实例
        HttpClient httpClient = HttpClientBuilder.create().build();

        // 创建HttpPost实例
        HttpPost httpPost = new HttpPost(url);


        // 将请求实体设置到请求中
        httpPost.setEntity(new StringEntity(jsonParam, ContentType.MULTIPART_FORM_DATA));

        // 设置请求头
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "multipart/form-data");
        String responseBody = "";
        try {
            // 发送POST请求并获取响应
            HttpResponse response = httpClient.execute(httpPost);
            // 打印响应内容
            responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
            System.out.println(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseBody;
    }

    /**
     * 提供调用集团管控接口使用
     *
     * @param url
     * @param reqParam
     * @return
     * @throws Exception
     */
    public static String postRequestGroup(String url, String reqParam, String token) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        logger.info(":::URL:" + url + ";JsonParam:" + reqParam + ";token:" + token);
        String body = "{}";
        try {
            HttpPost httpPost = new HttpPost(url);
            HttpEntity entity = new StringEntity(reqParam, ContentType.create("application/json", "UTF-8"));
            httpPost.setEntity(entity);
            httpPost.addHeader("Authorization", token);
            CloseableHttpResponse response2 = httpclient.execute(httpPost);
            try {
                HttpEntity entity2 = response2.getEntity();
                body = EntityUtils.toString(entity2);
                EntityUtils.consume(entity2);

            } catch (Exception e) {
                logger.error(":::Post Request Error.Url:" + url + ";JsonParam:" + reqParam, e);
            } finally {
                response2.close();
            }
        } catch (Exception e) {
            logger.error(":::Post Request Error.Url:" + url + ";JsonParam:" + reqParam, e);
        } finally {
            httpclient.close();
        }
        return body;
    }


    public static void main(String[] args) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("app_key", Constants.APP_KEY_JD);
        map.put("method", Constants.JD_METHOD);
        map.put("v", "1.0");
        map.put("sign_method", "md5");
        map.put("format", "json");
        map.put("timestamp", DateFormtUtils.getCurrentDate(DateFormtUtils.YMD_HMS));

        TreeMap<String, Object> buy_param_json = new TreeMap<>();
        TreeMap<String, Object> goodsReq = new TreeMap<>();
        goodsReq.put("eliteId", "2");
        goodsReq.put("pageIndex", 1);
        goodsReq.put("pageSize", 10);
        buy_param_json.put("goodsReq", goodsReq);
        map.put("360buy_param_json", JSON.toJSONString(buy_param_json));

        String sign = HttpMd5.buildSignJd(map);
        map.put("sign", sign);
        getRequest(Constants.JD_URL, map);
    }
}
