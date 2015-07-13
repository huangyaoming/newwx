package com.byhealth.common.utils;

import com.byhealth.common.bean.ApiResult;
import com.byhealth.common.bean.Menu;
import com.byhealth.common.utils.JSONUtil;
import com.byhealth.config.AccessToken;

/**
 * 菜单相关请求客户端
 * Created by fengjx on 2014/11/15 0015.
 */
public class MenuClient extends AbstractClient {


    private static String getMenu = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=";
    private static String createMenu = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";


    /**
     * 查询菜单
     */
    public ApiResult getMenu() {
        AccessToken accessToken = getAccessToken();
        String jsonResult = HttpUtil.get(getMenu + accessToken.getAccessToken());
        return proceResult(jsonResult);
    }

    /**
     * 创建菜单
     */
    public ApiResult createMenu(String jsonStr) {
        AccessToken accessToken = getAccessToken();
        String jsonResult = HttpUtil.post(createMenu + accessToken.getAccessToken(), jsonStr);
        return proceResult(jsonResult);
    }

    /**
     * 创建菜单
     */
    public ApiResult createMenu(Menu menu) {
        String jsonStr = JSONUtil.toJson(menu);
        return createMenu(jsonStr);
    }

}
