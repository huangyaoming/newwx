package com.byhealth.scheduler;

import com.byhealth.config.ApiConfig;
import com.byhealth.config.ApiConfigContext;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Iterator;
import java.util.Map;

/**
 * 定时获取微信access_token的线程
 * 
 * @author fengjx
 * @date 2014年5月10日
 */
public class AccessTokenTask implements Job {

    private static final Logger logger = Logger.getLogger(AccessTokenTask.class);

    private void doTask() {
    	System.out.println("AccessTokenTask启动，定时清理过期的accesstoken");
        logger.debug("AccessTokenTask启动，定时清理过期的accesstoken");
        Map<String, ApiConfig> map = ApiConfigContext.getApiConfigMap();
        Iterator<Map.Entry<String, ApiConfig>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, ApiConfig> entry = it.next();
            ApiConfig config = entry.getValue();
			boolean available = config.getAccessToken().isAvailable();
			if(!available){
                logger.info("accesstoken清理：删除ApiConfigContext key=" + entry.getKey());
                ApiConfigContext.remove(entry.getKey());
			}
        }
    }

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		doTask();
	}
}
