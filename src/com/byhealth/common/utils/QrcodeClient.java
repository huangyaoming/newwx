package com.byhealth.common.utils;


import com.byhealth.common.bean.ApiResult;
import com.byhealth.common.utils.JSONUtil;
import com.byhealth.config.AccessToken;

/**
 * 微信--生成二维码和长短链接切换
 * 
 * @author lizr
 * 
 */
public class QrcodeClient extends AbstractClient {

    private static String shortUrl     = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=";
    private static String createQrcode = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=";
    
    public static String getQrcode = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";

    /**
     * 创建情景二维码
     */
    public ApiResult createQrcode(String jsonStr) {
        AccessToken accessToken = getAccessToken();
        String jsonResult = HttpUtil.post(createQrcode + accessToken.getAccessToken(), jsonStr);
        return proceResult(jsonResult);
    }

    /**
     * 创建菜单
     */
    public ApiResult createQrcode(QrcodeCreate qrcodeCreate) {
        String jsonStr = JSONUtil.toJson(qrcodeCreate);
        return createQrcode(jsonStr);
    }
    
}
