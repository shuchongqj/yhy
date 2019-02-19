package com.yhy.common.beans.net.model.common.address;

import java.util.List;

public class City {
	public String cityCode;
	public String cityName;
	public List<Area> mallAreaList;
	@Override
	public String toString() {
		return "City [cityCode=" + cityCode + ", cityName=" + cityName
				+ ", mallAreaList=" + mallAreaList + "]";
	}
}
