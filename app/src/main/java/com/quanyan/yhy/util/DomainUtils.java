package com.quanyan.yhy.util;


import com.yhy.common.config.ContextHelper;

public class DomainUtils {
	/**
	 * 获取Domain
	 * @return
     */
	public static String getDomain() {
		return ContextHelper.getDefaultDomain();
	}
}
