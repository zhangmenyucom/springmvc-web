package com.taylor.uuid.loader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.taylor.common.CommonResponse;
import com.taylor.common.utils.HTTPClientUtil;
import com.taylor.common.utils.JsonUtil;
import com.taylor.common.utils.StringUtil;
import com.taylor.uuid.entity.BizIdEntity;
import com.taylor.uuid.exception.UidException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * 从服务器加载maxId
 *
 * @author Michael.Wang
 * @date 2017/5/2
 */
@Slf4j
public class UidLoader {

    private static HttpClient httpClient = new DefaultHttpClient();

    /**
     * 初始化模块内的全部uid（模块一般在web.xml中初始化）
     */
    public static List<BizIdEntity> fetchAll() {
        UidConfig uidConfig = UidConfig.getInstance();
        if (uidConfig == null) {
            return new ArrayList<>();
        }
        String responseContent = null;
        try {
            responseContent = sendSimpleGetRequest(uidConfig.getUrlFetchAll());
            CommonResponse<List<BizIdEntity>> commonResponse = JSON.parseObject(responseContent, new TypeReference<CommonResponse<List<BizIdEntity>>>() {
            }, Feature.IgnoreNotMatch);

            if (commonResponse == null) {
                throw new UidException("Fail to get uid. response of fetAll is null");
            }
            if (commonResponse.getCode() != UidConstant.SUCCESS_CODE) {
                throw new UidException("Fail to fetch all .Response code is : " + commonResponse.getCode());
            }
            return commonResponse.getData();

        } catch (Throwable t) {
            throw new UidException("Fail to get uid.response is:" + responseContent, t);
        }
    }

    public static long fetchNextMaxId(String bizType) {
        UidConfig uidConfig = UidConfig.getInstance();
        if (uidConfig == null) {
            throw new UidException("UidConfig is null. please set server context");
        }
        long nextMaxId;
        try {
            String url = uidConfig.getUrlPrefixUid() + bizType;
            String responseContent = sendSimpleGetRequest(url);
            CommonResponse<String> commonResponse = JsonUtil.fromJson(responseContent, CommonResponse.class);
            nextMaxId = Long.parseLong(commonResponse.getData());
        } catch (Throwable t) {
            throw new UidException("Fail to fetchNextMaxId.Exception is caught.", t);
        }
        return nextMaxId;
    }

    /**
     * 直接获取单个id或编号
     *
     * @param bizTypeWithPostfix 业务类型，如store_order_id,  store_order_no
     * @return
     */
    public static long fetchDirect(String bizTypeWithPostfix) {
        UidConfig uidConfig = UidConfig.getInstance();
        if (uidConfig == null) {
            throw new UidException("UidConfig is null. Please init server context");
        }
        long nextId = 0L;
        try {
            String url = uidConfig.getUrlFetchDirect() + bizTypeWithPostfix;
            String responseContent = sendSimpleGetRequest(url);
            log.debug("response json:{}",responseContent);
            CommonResponse commonResponse = JsonUtil.fromJson(responseContent, CommonResponse.class);
            Object data = commonResponse.getData();
            if (data instanceof Integer) {
                nextId = (Integer) data;
            } else if (data instanceof Long) {
                nextId = (Long) data;
            } else if (data instanceof String) {
                nextId = Long.parseLong((String) data);
            }
        } catch (Throwable t) {
            throw new UidException("Fail to fetchDirect.Exception is caught..", t);
        }
        return nextId;
    }


    /**
     * Send HTTP Get Request
     *
     * @return
     */
    public static String sendSimpleGetRequest(String url) {
        log.info("send request to url: {}", url);
        URI uri = HTTPClientUtil.getURI(url);
        String responseContent = null;

        try {
            HttpGet httpGet = new HttpGet(uri);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                responseContent = EntityUtils.toString(responseEntity);
            }
        } catch (Exception e) {
            throw new UidException("Fail to sendSimpleGetRequest for url:" + url, e);
        }
        if (StringUtil.isEmpty(responseContent)) {
            throw new UidException("Fail to request url Response is null");
        }
        return responseContent;
    }
}
