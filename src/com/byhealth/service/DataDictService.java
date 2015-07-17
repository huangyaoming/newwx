package com.byhealth.service;

import java.util.List;
import java.util.Map;

import com.byhealth.common.utils.Pagination;
import com.byhealth.entity.param.DataDict;

/**
 * 数据字典
 * @author fengjx xd-fjx@qq.com
 * @version DataDictService.java 2014年9月13日
 */
public interface DataDictService {
	
	
	/**
	 * 通过字典组和字典值查询字典信息
	 * @param group_code
	 * @param dict_value
	 * @return
	 */
	public DataDict findDict(String group_code, String dict_value);
	
	/**
	 * 根据字典组，查询字典列表
	 * @param group_code
	 * @return
	 */
	public List<DataDict> findDictList(String group_code);

	/**
	 * 返回已有分组
	 * @return
	 */
	public List<Map<String, String>> findDictGroup();

	/**
	 * 分页查询
	 * @param group_code
	 * @return
	 */
	public Pagination<DataDict> pageList(String group_code);

}
