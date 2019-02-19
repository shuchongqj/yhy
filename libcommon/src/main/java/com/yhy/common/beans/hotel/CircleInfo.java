package com.yhy.common.beans.hotel;


public class CircleInfo {
	private float circleSize;
	
	private int annulusColor;
	
	private int circleColor;

	public CircleInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CircleInfo(float circleSize, int annulusColor, int circleColor) {
		super();
		this.circleSize = circleSize;
		this.annulusColor = annulusColor;
		this.circleColor = circleColor;
	}

	public float getCircleSize() {
		return circleSize;
	}

	public void setCorcleSize(float circleSize) {
		this.circleSize = circleSize;
	}

	public int getAnnulusColor() {
		return annulusColor;
	}

	public void setAnnulusColor(int annulusColor) {
		this.annulusColor = annulusColor;
	}

	public int getCircleColor() {
		return circleColor;
	}

	public void setCorcleColor(int circleColor) {
		this.circleColor = circleColor;
	}
	
}
