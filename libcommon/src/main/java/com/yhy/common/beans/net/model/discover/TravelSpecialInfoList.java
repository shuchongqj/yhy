package com.yhy.common.beans.net.model.discover;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoxp on 2015-11-24.
 */
public class TravelSpecialInfoList implements Serializable{

	private static final long serialVersionUID = -4421797076103037488L;
	/**
	 * 当前页码
	 */
	public int pageNo;

	/**
	 * 是否有下一页
	 */
	public boolean hasNext;

	/**
	 * 游记内列表
	 */
	public List<TravelSpecialInfo> travelSpecialInfoList;
	/**
	 * 反序列化函数，用于从json字符串反序列化本类型实例
	 */
	public static TravelSpecialInfoList deserialize(String json) throws JSONException {
		if (json != null && !json.isEmpty()) {
			return deserialize(new JSONObject(json));
		}
		return null;
	}

	/**
	 * 反序列化函数，用于从json节点对象反序列化本类型实例
	 */
	public static TravelSpecialInfoList deserialize(JSONObject json) throws JSONException {
		if (json != null && json != JSONObject.NULL && json.length() > 0) {
			TravelSpecialInfoList result = new TravelSpecialInfoList();

			// 当前页码
			result.pageNo = json.optInt("pageNo");
			// 是否有下一页
			result.hasNext = json.optBoolean("hasNext");
			// 游记内列表
			JSONArray travelSpecialInfoListArray = json.optJSONArray("travelSpecialInfoList");
			if (travelSpecialInfoListArray != null) {
				int len = travelSpecialInfoListArray.length();
				result.travelSpecialInfoList = new ArrayList<TravelSpecialInfo>(len);
				for (int i = 0; i < len; i++) {
					JSONObject jo = travelSpecialInfoListArray.optJSONObject(i);
					if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
						result.travelSpecialInfoList.add(TravelSpecialInfo.deserialize(jo));
					}
				}
			}

			return result;
		}
		return null;
	}

	/*
	 * 序列化函数，用于从对象生成数据字典
	 */
	public JSONObject serialize() throws JSONException {
		JSONObject json = new JSONObject();

		// 当前页码
		json.put("pageNo", this.pageNo);

		// 是否有下一页
		json.put("hasNext", this.hasNext);

		// 游记内列表 
		if (this.travelSpecialInfoList != null) {
			JSONArray travelSpecialInfoListArray = new JSONArray();
			for (TravelSpecialInfo value : this.travelSpecialInfoList)
			{
				if (value != null) {
					travelSpecialInfoListArray.put(value.serialize());
				}
			}
			json.put("travelSpecialInfoList", travelSpecialInfoListArray);
		}

		return json;
	}
}
