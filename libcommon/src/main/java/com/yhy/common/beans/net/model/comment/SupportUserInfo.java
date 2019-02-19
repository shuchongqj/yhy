package com.yhy.common.beans.net.model.comment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by zhaoxp on 2015-11-20.
 */
public class SupportUserInfo implements Serializable {

	private static final long serialVersionUID = 6433006274262964458L;
	/**
	 * 用户ID
	 */
	public long userId;

	/**
	 * 头像
	 */
	public String avatar;

	/**
	 * 性别FEMALE女 MALE男 INVALID_GENDER未知
	 */
	public String gender;

	/**
	 * 昵称
	 */
	public String nick;

	/**
	 * 年龄
	 */
	public int age;

	/**
	 * 年龄日期
	 */
	public long birthday;

	/**
	 * 用户角色
	 */
	public long options;

	/**
	 * 反序列化函数，用于从json字符串反序列化本类型实例
	 */
	public static SupportUserInfo deserialize(String json) throws JSONException {
		if (json != null && !json.isEmpty()) {
			return deserialize(new JSONObject(json));
		}
		return null;
	}

	/**
	 * 反序列化函数，用于从json节点对象反序列化本类型实例
	 */
	public static SupportUserInfo deserialize(JSONObject json) throws JSONException {
		if (json != null && json != JSONObject.NULL && json.length() > 0) {
			SupportUserInfo result = new SupportUserInfo();

			// 用户ID
			result.userId = json.optLong("userId");
			// 头像

			if(!json.isNull("avatar")){
				result.avatar = json.optString("avatar", null);
			}
			// 性别FEMALE女 MALE男 INVALID_GENDER未知

			if(!json.isNull("gender")){
				result.gender = json.optString("gender", null);
			}
			// 昵称

			if(!json.isNull("nick")){
				result.nick = json.optString("nick", null);
			}
			// 年龄
			result.age = json.optInt("age");
			// 年龄日期
			result.birthday = json.optLong("birthday");

			// 用户角色
			result.options = json.optLong("options");
			return result;
		}
		return null;
	}

	/*
	 * 序列化函数，用于从对象生成数据字典
	 */
	public JSONObject serialize() throws JSONException {
		JSONObject json = new JSONObject();

		// 用户ID
		json.put("userId", this.userId);

		// 头像
		if(this.avatar != null) { json.put("avatar", this.avatar); }

		// 性别FEMALE女 MALE男 INVALID_GENDER未知
		if(this.gender != null) { json.put("gender", this.gender); }

		// 昵称
		if(this.nick != null) { json.put("nick", this.nick); }

		// 年龄
		json.put("age", this.age);

		// 年龄日期
		json.put("birthday", this.birthday);

		// 用户角色
		json.put("options", this.options);
		return json;
	}
}
