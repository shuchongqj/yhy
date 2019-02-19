package com.quanyan.yhy.ui.discovery;



import java.io.Serializable;


public class PoiLocation implements Serializable {
	private String address;
//	private LatLonPoint lat;
	private String title;
	private  double longitude;
	private  double latitude;


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

//	public LatLonPoint getLat() {
//		return lat;
//	}
//
//	public void setLat(LatLonPoint lat) {
//		this.lat = lat;
//	}
}
