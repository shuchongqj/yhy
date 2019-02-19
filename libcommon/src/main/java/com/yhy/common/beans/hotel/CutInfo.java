package com.yhy.common.beans.hotel;


public class CutInfo {
	private String cutMsg;
	private int msgColor;
	private float msgSize;
	public CutInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CutInfo(String cutMsg, int msgColor, float msgSize) {
		super();
		this.cutMsg = cutMsg;
		this.msgColor = msgColor;
		this.msgSize = msgSize;
	}
	public String getCutMsg() {
		return cutMsg;
	}
	public void setCutMsg(String cutMsg) {
		this.cutMsg = cutMsg;
	}
	public int getMsgColor() {
		return msgColor;
	}
	public void setMsgColor(int msgColor) {
		this.msgColor = msgColor;
	}
	public float getMsgSize() {
		return msgSize;
	}
	public void setMsgSize(float msgSize) {
		this.msgSize = msgSize;
	}

	
}
