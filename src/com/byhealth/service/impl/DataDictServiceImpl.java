package com.byhealth.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.byhealth.common.utils.Pagination;
import com.byhealth.common.utils.RecordUtil;
import com.byhealth.entity.param.DataDict;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * 数据字典
 * @author fengjx xd-fjx@qq.com
 * @version DataDictServiceImpl.java 2014年9月13日
 */
public class DataDictServiceImpl {

	public static DataDict findDict(String group_code, String dict_value) {
		String sql = "select * from wechat_data_dict d where d.group_code = ? and d.dict_value = ?";
		return RecordUtil.getFirstEntity(DataDict.class, sql, group_code, dict_value);
	}

	public static List<DataDict> findDictList(String group_code) {
		if(StringUtils.isBlank(group_code)){
			throw new IllegalArgumentException("group_code不能为空");
		}
		String sql = "select * from wechat_data_dict d where d.group_code = ? order by d.order_num";
		return RecordUtil.getEntityList(DataDict.class, sql, group_code);
	}

	public static List<Map<String, Object>> findDictGroup() {
		String sql = " select distinct d.group_code as group_code, d.group_name as group_name from wechat_data_dict d ";
		List<Record> list = Db.find(sql);
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		if (list != null) {
			for (Record record : list) {
				result.add(record.getColumns());
			}
		}
		return result;
	}

	/**
	 * 分页查询
	 * @param group_code
	 * @return
	 */
	public static Pagination<DataDict> pageList(String group_code, int pageNumber, int pageSize) {
		String sql = "from wechat_data_dict d where d.group_code = ? order by d.group_code, d.order_num";
		Page<Record> p = null;
		if(StringUtils.isBlank(group_code)){
			sql = "from wechat_data_dict d order by d.group_code, d.order_num";
			p = Db.paginate(pageNumber, pageSize, "select * ", sql);
		} else {
			p = Db.paginate(pageNumber, pageSize, "select * ", sql, group_code);
		}
		List<DataDict> list = RecordUtil.getEntityListFromRecordList(p.getList(), DataDict.class);
		Pagination<DataDict> page = new Pagination<DataDict>(list, p.getTotalRow());
		page.setPageNo(p.getPageNumber());
		page.setPageSize(p.getPageSize());
		return page;
	}


}
