package com.byhealth.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.byhealth.common.bean.TulingApiClient;
import com.byhealth.common.bean.resp.Article;
import com.byhealth.common.bean.resp.Music;
import com.byhealth.common.bean.resp.RespMusicMessage;
import com.byhealth.common.bean.resp.RespNewsMessage;
import com.byhealth.common.bean.resp.RespTextMessage;
import com.byhealth.common.bean.restful.BaiduMusicServiceApi;
import com.byhealth.common.bean.restful.BaiduTranslateServiceApi;
import com.byhealth.common.bean.restful.DreamServiceApi;
import com.byhealth.common.bean.restful.LuckServiceApi;
import com.byhealth.common.bean.restful.WeatherServiceApi;
import com.byhealth.common.bean.restful.YoukuVideoServiceApi;
import com.byhealth.common.bean.tuling.vo.req.RequestBean;
import com.byhealth.common.constants.WechatReqMsgtypeConstants;
import com.byhealth.common.constants.WechatRespMsgtypeConstants;
import com.byhealth.common.utils.MessageUtil;
import com.byhealth.context.WechatContext;
import com.byhealth.service.TextExtService;

import org.apache.log4j.Logger;

/**
 * 核心服务类
 * @author fengjx
 * @date 
 */
public class MyTextExtService implements TextExtService {
	
	private static final Logger logger = Logger.getLogger(MyTextExtService.class);

	@Override
	public String execute() {
		String respMessage = null;
		try {
			Map<String, String> requestMap = WechatContext.getWechatPostMap();

			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");

			RespTextMessage textMessage = new RespTextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(WechatRespMsgtypeConstants.RESP_MESSAGE_TYPE_TEXT);
			textMessage.setFuncFlag(0);
			
			logger.info("消息类型："+msgType);
			// 文本消息
			if (msgType.equals(WechatReqMsgtypeConstants.REQ_MSG_TYPE_TEXT)) {
				// 接收用户发送的文本消息内容
				String content = requestMap.get("Content");
				// 创建图文消息
				RespNewsMessage newsMessage = new RespNewsMessage();
				newsMessage.setToUserName(fromUserName);
				newsMessage.setFromUserName(toUserName);
				newsMessage.setCreateTime(new Date().getTime());
				newsMessage.setMsgType(WechatRespMsgtypeConstants.RESP_MESSAGE_TYPE_NEWS);
				newsMessage.setFuncFlag(0);

				List<Article> articleList = new ArrayList<Article>();
				if (content.startsWith("歌曲")) {
					// 文本消息内容  
			        String respContent = null;
                    // 将歌曲2个字及歌曲后面的+、空格、-等特殊符号去掉  
                    String keyWord = content.replaceAll("^歌曲[\\+ ~!@#%^-_=]?", "").trim();  
                    // 如果歌曲名称为空  
                    if ("".equals(keyWord)) {  
                        respContent = BaiduMusicServiceApi.getMusicUsage();  
                    } else {  
                        String[] kwArr = keyWord.split("@");  
                        // 歌曲名称  
                        String musicTitle = kwArr[0];  
                        // 演唱者默认为空  
                        String musicAuthor = "";  
                        if (2 == kwArr.length){
                        	musicAuthor = kwArr[1];  
                        }
                        // 搜索音乐  
                        Music music = BaiduMusicServiceApi.searchMusic(musicTitle, musicAuthor);
                        // 未搜索到音乐  
                        if (null == music) {  
                            respContent = "对不起，没有找到你想听的歌曲<" + musicTitle + ">。";  
                        } else {  
                            // 音乐消息  
                            RespMusicMessage musicMessage = new RespMusicMessage();
                            musicMessage.setToUserName(fromUserName);  
                            musicMessage.setFromUserName(toUserName);  
                            musicMessage.setCreateTime(new Date().getTime());  
                            musicMessage.setMsgType(WechatRespMsgtypeConstants.RESP_MESSAGE_TYPE_MUSIC);  
                            musicMessage.setMusic(music);  
                            respMessage = MessageUtil.musicMessageToXml(musicMessage);
                        }
                        //没有搜索结果
                        if (null != respContent){
                        	textMessage.setContent(respContent);  
                            respMessage = MessageUtil.textMessageToXml(textMessage);  
                        }
                    }  
                }
				//翻译
				else if (content.startsWith("翻译")) {
                    String keyWord = content.replaceAll("^翻译", "").trim();
                    logger.info("翻译："+keyWord);
                    if ("".equals(keyWord)) {
                    	//返回使用指南
                        textMessage.setContent(BaiduTranslateServiceApi.getTranslateUsage());  
                    } else {  
                        textMessage.setContent(BaiduTranslateServiceApi.translate(keyWord));  
                    }
                    respMessage = MessageUtil.textMessageToXml(textMessage);
                }
				//搜索视频（优酷）
				else if (content.startsWith("视频")) {
                    String keyWord = content.replaceAll("^视频", "").trim();
                    logger.debug("搜索视频："+keyWord);
                    String textContent = null;
                    if ("".equals(keyWord)) {  
                    	textContent = YoukuVideoServiceApi.getVideoUsage();
                    } else {
                    	articleList = YoukuVideoServiceApi.searchVideo(keyWord);
                    	if(null != articleList && articleList.size() > 0){
                    		newsMessage.setArticleCount(articleList.size());
        					newsMessage.setArticles(articleList);
        					respMessage = MessageUtil.newsMessageToXml(newsMessage);
                    	}else{
                    		textContent = "没有搜索结果...";
                    	}
                    }
                    //没有搜索结果
                    if (null != textContent){
                    	textMessage.setContent(textContent);  
                        respMessage = MessageUtil.textMessageToXml(textMessage);  
                    }
                }
				//星座运势
				else if (content.startsWith("运势")) {
                    String keyWord = content.replaceAll("^运势", "").trim();
                    logger.info("星座："+keyWord);
                    if ("".equals(keyWord)) {
                    	//返回使用指南
                        textMessage.setContent(LuckServiceApi.getLuckUsage());  
                    } else {  
                        textMessage.setContent(LuckServiceApi.luckyDay(keyWord));  
                    }
                    respMessage = MessageUtil.textMessageToXml(textMessage);
                }
				//天气预报
				else if (content.startsWith("天气")) {
                    String keyWord = content.replaceAll("^天气", "").trim();
                    logger.info("天气查询："+keyWord);
                    if ("".equals(keyWord)) {
                    	//返回使用指南
                        textMessage.setContent(WeatherServiceApi.getWeatherUsage());  
                    } else {
                        textMessage.setContent(WeatherServiceApi.queryhWeather(keyWord));  
                    }
                    respMessage = MessageUtil.textMessageToXml(textMessage);
                }
				//周公解梦
				else if (content.startsWith("解梦")) {
                    String keyWord = content.replaceAll("^解梦", "").trim();
                    logger.info("解梦："+keyWord);
                    if ("".equals(keyWord)) {
                    	//返回使用指南
                        textMessage.setContent(DreamServiceApi.getDreamUsage());  
                    } else {  
                        textMessage.setContent(DreamServiceApi.explain(keyWord));  
                    }
                    respMessage = MessageUtil.textMessageToXml(textMessage);
                }
            	else if (content.startsWith("留言")) {
                    //textMessage.setContent("<a href=\"http://fengjxblog.sinaapp.com/?page_id=31\">留言</a>");  
                    textMessage.setContent("<a href=\"\">留言</a>");  
                    respMessage = MessageUtil.textMessageToXml(textMessage);
                }
            	else if("game".equals(content)) {
					//textMessage.setContent("进入<a href=\"http://3.fengjianxin.sinaapp.com/wechat/games/fishjoy/index.html\">捕鱼达人</a>");  
					textMessage.setContent("进入<a href=\"\">捕鱼达人</a>");  
                    respMessage = MessageUtil.textMessageToXml(textMessage);  
				}else{
                    //没有匹配规则的消息，交给图灵机器人处理
                    RequestBean req = new RequestBean();
                    req.setInfo(content);
                    req.setUserid(fromUserName);
                    return MessageUtil.replaceMsgByReg(TulingApiClient.call2WechatMsg(req),requestMap);
                }
			}
		} catch (Exception e) {
			logger.error("执行自定义文本消息扩展处理发生异常",e);
		}
		return respMessage;
	}
	
}