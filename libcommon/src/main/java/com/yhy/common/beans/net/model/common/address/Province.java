package com.yhy.common.beans.net.model.common.address;

import java.util.List;


public class Province {
	public String provinceCode;
	public String provinceName;
	public List<City> mallCityList;
	@Override
	public String toString() {
		return "Province [provinceCode=" + provinceCode + ", provinceName="
				+ provinceName + ", mallCityList=" + mallCityList + "]";
	}
}
