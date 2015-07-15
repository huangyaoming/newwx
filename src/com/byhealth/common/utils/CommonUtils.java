package com.byhealth.common.utils;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author peng
 */
public final class CommonUtils {

	private static Logger logger = Logger.getLogger(CommonUtils.class);

	public static final String DATA_FORMAT_ALL = "yyyy-MM-dd HH:mm:ss";
	public static final String DATA_FORMAT_DD = "yyyy-MM-dd";

	/**
	 * 生成1-9位随机数
	 *
	 * @param count
	 *            位数
	 * @return 返回count位随机数
	 */
	public static String getRandomNum(int count) {
		return RandomStringUtils.randomNumeric(count);
	}

	/**
	 * 主键生成器，基于时间戳+机器地址的
	 *
	 * @return 主键
	 */
	public static String getPrimaryKey() {
		EthernetAddress nic = EthernetAddress.fromInterface();
		TimeBasedGenerator uuidGenerator = Generators.timeBasedGenerator(nic);
		UUID uuid = uuidGenerator.generate();
		return uuid.toString().replaceAll("-", "").toLowerCase();
	}

	/**
	 * @param date
	 * @return
	 */
	public static String date2String(Date date) {
		return date2String(date, DATA_FORMAT_ALL);
	}

	/**
	 * @param date
	 * @param dataFormat
	 *            默认为yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String date2String(Date date, String dataFormat) {
		if (null == date) {
			return null;
		}
		String dateFmt = DATA_FORMAT_ALL;
		if (StringUtils.isNotBlank(dataFormat)) {
			dateFmt = dataFormat;
		}
		DateFormat fmt = new SimpleDateFormat(dateFmt);
		return fmt.format(date);
	}

	/**
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date string2Date(String date) {
		return string2Date(date, DATA_FORMAT_ALL);
	}

	/**
	 * @param date
	 * @param dataFormat
	 *            默认为yyyy-MM-dd
	 * @return
	 * @throws ParseException
	 */
	public static Date string2Date(String date, String dataFormat) {
		String dateFmt = DATA_FORMAT_ALL;
		if (StringUtils.isNotBlank(dataFormat)) {
			dateFmt = dataFormat;
		}
		DateFormat fmt = new SimpleDateFormat(dateFmt);
		try {
			return fmt.parse(date);
		} catch (ParseException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 获得项目classPath
	 *
	 * @return
	 */
	public static String getClassPath() {
		String classPath = Thread.currentThread().getContextClassLoader()
				.getResource("/").getPath();
		if (SystemUtils.IS_OS_WINDOWS) {
			classPath = classPath.substring(1, classPath.length());
		}
		return classPath;
	}

	/**
	 * 获得项目classPath
	 *
	 * @return
	 */
	public static String getClassPath(String path) {
		String classPath = Thread.currentThread().getContextClassLoader()
				.getResource(path).getPath();
		if (SystemUtils.IS_OS_WINDOWS) {
			classPath = classPath.substring(1, classPath.length());
		}
		return classPath;
	}
	
	public static Map<String, String> retFailure() {
        return retFailure("操作失败");
    }
    
    public static Map<String, String> retFailure(String msg) {
        Map<String, String> res = new HashMap<String, String>();
        res.put("code", "0");
        res.put("msg", msg);
        return res;
    }
    
    public static Map<String, String> retSuccess() {
        return retSuccess("操作成功");
    }
    
    public static Map<String, String> retSuccess(String msg) {
        Map<String, String> res = new HashMap<String, String>();
        res.put("code", "1");
        res.put("msg", msg);
        return res;
    }

}
