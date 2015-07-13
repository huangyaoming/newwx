package com.byhealth.service.executor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 服务执行器工厂
 * @author jie.hua@alipay.com
 * @version $Id: InServiceExecutorFactory.java, v 0.1 2014-1-6 下午9:46:12 jie.hua Exp $
 */
public class InServiceExecutorFactory {
	
	private static final Logger logger = Logger.getLogger(InServiceExecutorFactory.class);
	
	/**
     * 服务映射
     */
    private static final Map<String, InServiceExecutor> executorMaps;
    
	static {
		executorMaps = new HashMap<String, InServiceExecutor>();
		// 商户 文本消息处理器
		InServiceExecutor ise = new InWechatTextMsgExecutor();
		executorMaps.put(ise.getExecutorName(), ise);
		// 商户 用户关注消息处理器
		ise = new InWechatEventSubscribeMsgExecutor();
		executorMaps.put(ise.getExecutorName(), ise);
		// 商户 用户取消关注消息处理器
		ise = new InWechatEventUnSubscribeMsgExecutor();
		executorMaps.put(ise.getExecutorName(), ise);
		// 商户 扫描二维码消息处理器
		ise = new InWechatEventScanMsgExecutor();
		executorMaps.put(ise.getExecutorName(), ise);
		// 商户 图片消息处理器
		ise = new InWechatImageMsgExecutor();
		executorMaps.put(ise.getExecutorName(), ise);
		// 商户 菜单点击事件消息处理器
		ise = new InWechatEventClickMsgExecutor();
		executorMaps.put(ise.getExecutorName(), ise);
		// 商户 验证码消息验证处理器
		ise = new InWechatValidMsgExecutor();
		executorMaps.put(ise.getExecutorName(), ise);
		// 商户 地理位置消息处理器
		ise = new InWechatLocationMsgExecutor();
		executorMaps.put(ise.getExecutorName(), ise);
	}
	
    /**
     * 根据name查询服务执行器
     * 
     * @param name
     * @return
     */
    public InServiceExecutor getExecutorByName(String name) {
        return executorMaps.get(name);

    }


    /**
     * 根据name查询服务执行器
     * @param requestMap
     * @return
     */
    public InServiceExecutor getExecutorByName(Map<String, String> requestMap) {
    	String executorName = Dispatcher.getExecutorName(requestMap);
    	logger.info("executorName = "+executorName);
        return executorMaps.get(executorName);
    }

    /**
     *  设置服务执行器
     * @param executors
     */
    public void setExecutorList(List<InServiceExecutor> executors) {
        if (executors == null || executors.isEmpty()) {
            return;
        }
        for (InServiceExecutor executor : executors) {
            executorMaps.put(executor.getExecutorName(), executor);
        }
    }

}
