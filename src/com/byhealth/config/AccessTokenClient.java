package com.byhealth.config;

import com.byhealth.common.utils.AbstractClient;
import com.byhealth.common.utils.HttpUtil;
import com.byhealth.common.utils.ParaMap;

import java.util.Map;

/**
 * 获取全局access_token
 * Created by fengjx on 2014/11/15 0015.
 */
public class AccessTokenClient extends AbstractClient {


    private String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";

    public AccessToken getAccessToken() {
        final String appId = apiConfig.getAppId();
        final String appSecret = apiConfig.getAppSecret();
        Map<String, String> queryParas = ParaMap.create("appid", appId).put("secret", appSecret).getData();
        String json = HttpUtil.get(url, queryParas);
        return new AccessToken(json);
    }

}
