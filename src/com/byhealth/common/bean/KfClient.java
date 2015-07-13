package com.byhealth.common.bean;

import java.io.File;

import com.byhealth.common.utils.AbstractClient;
import com.byhealth.common.utils.BeanUtil;
import com.byhealth.common.utils.HttpUtil;
import com.byhealth.common.utils.JSONUtil;
import com.byhealth.config.AccessToken;
import com.byhealth.config.ApiConfig;
import com.byhealth.config.ApiConfigContext;

/**
 * 客服相关请求客户端
 * Created by fengjx on 2014/11/15 0015.
 */
public class KfClient extends AbstractClient {


    private static String getKfAccountList  = "https://api.weixin.qq.com/cgi-bin/customservice/getkflist?access_token=" ;
    
    
    private static String addKfAccount   	 = "https://api.weixin.qq.com/customservice/kfaccount/add?access_token=" ;
    private static String updateKfAccount = "https://api.weixin.qq.com/customservice/kfaccount/update?access_token=" ;
    private static String uploadHeading 	 = "http://api.weixin.qq.com/customservice/kfaccount/uploadheadimg?access_token=" ;
    private static String delKfAccount    	 = "https://api.weixin.qq.com/customservice/kfaccount/del?access_token=" ;

    public <T extends AbstractClient> T init(ApiConfig apiConfig) {
        //从缓存读取ApiConfig
        ApiConfig temp = ApiConfigContext.getApiConfigByKey(apiConfig.createKey());
        //缓存中没有相关配置
        if(BeanUtil.isNull(temp)){
            this.apiConfig = apiConfig;
        }else{
            //配置缓存里的配置
            this.apiConfig = temp;
        }
        return (T)this;
    }
    
    /**
     * 查询菜单
     */
    public ApiResult getKfAccountList() {
        AccessToken accessToken = getAccessToken();
        String jsonResult = HttpUtil.get(getKfAccountList + accessToken.getAccessToken());
        return proceResult(jsonResult);
    }

    /**
     * 创建客服账户
     */
    public ApiResult addKfAccount(String jsonStr) {
        AccessToken accessToken = getAccessToken();
        String jsonResult = HttpUtil.post(addKfAccount + accessToken.getAccessToken(), jsonStr);
        return proceResult(jsonResult);
    }

    /**
     * 创建客服账户
     */
    public ApiResult addKfAccount(KfAccount account) {
        String jsonStr = JSONUtil.toJson(account);
        return addKfAccount(jsonStr);
    }
    
    
    /**
     * 设置客服账户
     */
    public ApiResult updateKfAccount(String jsonStr) {
        AccessToken accessToken = getAccessToken();
        String jsonResult = HttpUtil.post(updateKfAccount + accessToken.getAccessToken(), jsonStr);
        return proceResult(jsonResult);
    }

    /**
     * 设置客服账户
     */
    public ApiResult updateKfAccount(KfAccount account) {
        String jsonStr = JSONUtil.toJson(account);
        return updateKfAccount(jsonStr);
    }
    
    
    /**
     * 设置客服账户
     */
    public ApiResult uploadHeading(String kf_account , File media) {
//        AccessToken accessToken = getAccessToken();
//        
//        String jsonResult = HttpUtil.post(uploadHeading + accessToken.getAccessToken(), jsonStr);
//        return proceResult(jsonResult);
    		return null ;
    }
    
    /**
     * 删除客服账户
     */
    public ApiResult delKfAccount(String kf_account) {
        AccessToken accessToken = getAccessToken();
        String jsonResult = HttpUtil.get(delKfAccount + accessToken.getAccessToken() +"&kf_account=" + kf_account );
        return proceResult(jsonResult);
    }

}
