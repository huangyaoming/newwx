package com.byhealth.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.byhealth.common.utils.CommonUtils;
import com.byhealth.common.utils.FileUtil;
import com.byhealth.common.utils.HttpUtil;
import com.byhealth.common.utils.MyFreemarker;
import com.byhealth.common.utils.Pagination;
import com.byhealth.common.utils.RecordUtil;
import com.byhealth.config.AppConfig;
import com.byhealth.entity.MaterialEntity;
import com.byhealth.entity.SysUserEntity;
import com.byhealth.entity.param.Material;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * 素材管理
 * @author fengjx xd-fjx@qq.com
 * @date 2014年9月12日
 */
public class MaterialServiceImpl {

	public static Pagination<Material> getListPageByType(String type,SysUserEntity sysUser, int pageNumber, int pageSize) {
		String select = "select * ";
		String sql = "from wechat_material m where m.msg_type = ? and m.user_id = ? order by m.in_time desc ";
		Page<Record> p = Db.paginate(pageNumber, pageSize, sql, select, type, sysUser.getId());
		List<Material> list = RecordUtil.getEntityListFromRecordList(p.getList(), Material.class);
		Pagination<Material> page = new Pagination<Material>(list, p.getTotalRow());
		page.setPageNo(p.getPageNumber());
        page.setPageSize(p.getPageSize());
		return page;
	}

	public void saveOrUpdate(MaterialEntity material, List<Map<String, String>> contents, HttpServletRequest request) throws Exception {
		if(null == material){
			throw new Exception("添加素材失败，提交的数据为空");
		}
		String date_str = CommonUtils.date2String(new Date());
		//material.setIn_time(new Date());
		material.set("in_time", new Date());
		String msgType = material.getMsg_type();
		if(null != msgType && msgType.equals("news")){	//图文消息
			if(null != contents && contents.size() > 0 ){
				String basePath = CommonUtils.getFtlHtmlPath(request);
				MyFreemarker freemarker = MyFreemarker.getInstance(basePath);
				String xml_data = material.getXml_data();
				int l = contents.size();
				for(int i = 0; i < l; i++){
					String htmlPath = AppConfig.STATIC_PATH;
					String htmlUrl = "/upload/html/material/" + CommonUtils.getPrimaryKey()+".html";
					//如果不存在则创建文件夹
					FileUtil.makeDirectory(htmlPath + "/upload/html/material/");
					htmlPath = htmlPath + htmlUrl;
					Map<String,String> content = contents.get(i);
					content.put("date", date_str);
					//通过freemarker生成静态页面，并返回URL
					freemarker.createHTML(content, "material.ftl", htmlPath);
					xml_data = xml_data.replaceAll("\\<Url_"+i+">(.*?)\\</Url_"+i+">", 
													"<Url><![CDATA["+AppConfig.STATIC_DOMAIN+htmlUrl+"]]></Url>");
				}
				xml_data.replaceAll("\\<ArticleCount>(.*?)\\</ArticleCount>", "<ArticleCount>"+l+"</ArticleCount>");
				//material.setXml_data(xml_data);
				material.set("xml_data", xml_data);
			}
		}
		if(StringUtils.isBlank(material.getId())){
			String key = CommonUtils.getPrimaryKey();
			material.set("id", key);
			material.save();
			return;
		}
		material.update();
	}
	
	public String loadMaterialContentByUrl(String url) {
		String conten = "error";
		try {
			String path = AppConfig.STATIC_DOMAIN + url;
			conten = HttpUtil.get(path);
			conten = conten.substring(conten.indexOf("<!--###@content@###-->") + 22, conten.lastIndexOf("<!--###@content@###-->"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return conten;
	}
	
}
