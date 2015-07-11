package com.byhealth.common.bean;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.jfinal.plugin.activerecord.Model;

/**
 * 字符串toString基类
 * 
 * @author huangym
 */
public abstract class ToStringBase extends Model<ToStringBase> implements
		Serializable {

	/** 序列号ID */
	private static final long serialVersionUID = 1704932592395633947L;

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public abstract String getTableName();

}
