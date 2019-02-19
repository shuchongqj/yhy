package com.yhy.common.beans.net.model.discover;

import com.yhy.common.beans.net.model.club.POIInfo;
import com.yhy.common.beans.net.model.user.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoxp on 2015-11-24.
 */
public class TravelSpecialInfo implements Serializable{

	private static final long serialVersionUID = -4917452818193235538L;
	/**
	 * 游记id
	 */
	public long id;

	/**
	 * 游记标题
	 */
	public String title;

	/**
	 * 游记背景图
	 */
	public String backImg;

	/**
	 * 拍摄地点
	 */
	public String poiContent;

	/**
	 * 简介
	 */
	public String preface;

	/**
	 * 游记内容
	 */
	public String imgContentJson;

	/**
	 * 游记内容数组
	 */
	public List<TravelJsonInfo> travelContentJsonList;
	/**
	 * 游记创建时间
	 */
	public long gmtCreated;

	/**
	 * 用户信息
	 */
	public UserInfo userInfo;

	/**
	 * 地理位置信息
	 */
	public POIInfo poiInfo;

	/**
	 * 是否点赞
	 */
	public String isSupport;

	/**
	 * 点赞数
	 */
	public int supportNum;

	/**
	 * 阅读数
	 */
	public int redNum;

	/**
	 * 反序列化函数，用于从json字符串反序列化本类型实例
	 */
	public static TravelSpecialInfo deserialize(String json) throws JSONException {
		if (json != null && !json.isEmpty()) {
			return deserialize(new JSONObject(json));
		}
		return null;
	}

	/**
	 * 反序列化函数，用于从json节点对象反序列化本类型实例
	 */
	public static TravelSpecialInfo deserialize(JSONObject json) throws JSONException {
		if (json != null && json != JSONObject.NULL && json.length() > 0) {
			TravelSpecialInfo result = new TravelSpecialInfo();

			// 游记id
			result.id = json.optLong("id");
			// 游记标题

			if(!json.isNull("title")){
				result.title = json.optString("title", null);
			}
			// 游记背景图

			if(!json.isNull("backImg")){
				result.backImg = json.optString("backImg", null);
			}
			// 拍摄地点

			if(!json.isNull("poiContent")){
				result.poiContent = json.optString("poiContent", null);
			}
			// 简介

			if(!json.isNull("preface")){
				result.preface = json.optString("preface", null);
			}
			// 游记内容

			if(!json.isNull("imgContentJson")){
				result.imgContentJson = json.optString("imgContentJson", null);
			}
			// 游记内容数组
			JSONArray travelContentJsonListArray = json.optJSONArray("travelContentJsonList");
			if (travelContentJsonListArray != null) {
				int len = travelContentJsonListArray.length();
				result.travelContentJsonList = new ArrayList<TravelJsonInfo>(len);
				for (int i = 0; i < len; i++) {
					JSONObject jo = travelContentJsonListArray.optJSONObject(i);
					if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
						result.travelContentJsonList.add(TravelJsonInfo.deserialize(jo));
					}
				}
			}

			// 游记创建时间
			result.gmtCreated = json.optLong("gmtCreated");
			// 用户信息
			result.userInfo = UserInfo.deserialize(json.optJSONObject("userInfo"));
			// 地理位置信息
			result.poiInfo = POIInfo.deserialize(json.optJSONObject("poiInfo"));
			// 是否点赞

			if(!json.isNull("isSupport")){
				result.isSupport = json.optString("isSupport", null);
			}
			// 点赞数
			result.supportNum = json.optInt("supportNum");
			// 阅读数
			result.redNum = json.optInt("redNum");
			return result;
		}
		return null;
	}

	/*
     * 序列化函数，用于从对象生成数据字典
     */
	public JSONObject serialize() throws JSONException {
		JSONObject json = new JSONObject();

		// 游记id
		json.put("id", this.id);

		// 游记标题
		if(this.title != null) { json.put("title", this.title); }

		// 游记背景图
		if(this.backImg != null) { json.put("backImg", this.backImg); }

		// 拍摄地点
		if(this.poiContent != null) { json.put("poiContent", this.poiContent); }

		// 简介
		if(this.preface != null) { json.put("preface", this.preface); }

		// 游记内容
		if(this.imgContentJson != null) { json.put("imgContentJson", this.imgContentJson); }

		// 游记内容数组 
		if (this.travelContentJsonList != null) {
			JSONArray travelContentJsonListArray = new JSONArray();
			for (TravelJsonInfo value : this.travelContentJsonList)
			{
				if (value != null) {
					travelContentJsonListArray.put(value.serialize());
				}
			}
			json.put("travelContentJsonList", travelContentJsonListArray);
		}

		// 游记创建时间
		json.put("gmtCreated", this.gmtCreated);

		// 用户信息
		if (this.userInfo != null) { json.put("userInfo", this.userInfo.serialize()); }

		// 地理位置信息
		if (this.poiInfo != null) { json.put("poiInfo", this.poiInfo.serialize()); }

		// 是否点赞
		if(this.isSupport != null) { json.put("isSupport", this.isSupport); }

		// 点赞数
		json.put("supportNum", this.supportNum);

		// 阅读数
		json.put("redNum", this.redNum);

		return json;
	}
}
