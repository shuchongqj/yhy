// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.pedometer;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@Table(name = "banner_info")
public class BannerInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1820419857465923448L;

	/**
	 * 广告位id
	 */
	@Column(column = "banner_id")
	@Id
	@NoAutoIncrement
	public long id;

	/**
	 * 广告位名称
	 */
	@Column(column = "name")
	public String name;

	/**
	 * 类别(图文/文字) 取值范围 BANNER_STYLE_WORD(纯文字), BANNER_STYLE_IMG(图片)
	 */
	@Column(column = "style")
	public String style;

	/**
	 * 文字内容
	 */
	@Column(column = "word")
	public String word;

	/**
	 * 类型 取值范围 BANNER_TOPIC(话题), BANNER_ACTIVITY(活动), BANNER_GROUP(圈子)
	 */
	@Column(column = "type")
	public String type;

	/**
	 * 规格
	 */
	@Column(column = "size")
	public String size;

	/**
	 * 图片url
	 */
	@Column(column = "img_url")
	public String imgUrl;

	/**
	 * 描述
	 */
	@Column(column = "desc")
	public String desc;

	/**
	 * 描述在图片中的位置 取值范围 BANNER_DESC_FLOAT(浮动), BANNER_DESC_UNDER(下方),
	 * BANNER_DESC_NONE(不显示)
	 */
	@Column(column = "descLocation")
	public String descLocation;

	/**
	 * 内容类型 取值范围 BANNER_CON_LINK(链接), BANNER_CON_POST(帖子), BANNER_CON_DOCTOR(医生)
	 */
	@Column(column = "content_type")
	public String contentType;

	/**
	 * 内容
	 */
	@Column(column = "content")
	public String content;

	/**
	 * 顺序
	 */
	@Column(column = "banner_order")
	public int order;

	/**
	 * 状态（上架，下架） 取值范围 BANNER_ACTIVE(上架), BANNER_DEACTIVE(下架)
	 */
	@Column(column = "state")
	public String state;

	/**
	 * 反序列化函数，用于从json字符串反序列化本类型实例
	 */
	public static BannerInfo deserialize(String json) throws JSONException {
		if (json != null && !json.isEmpty()) {
			return deserialize(new JSONObject(json));
		}
		return null;
	}

	/**
	 * 反序列化函数，用于从json节点对象反序列化本类型实例
	 */
	public static BannerInfo deserialize(JSONObject json) throws JSONException {
		if (json != null && json != JSONObject.NULL && json.length() > 0) {
			BannerInfo result = new BannerInfo();

			// 广告位id
			result.id = json.optLong("id");
			// 广告位名称

			if (!json.isNull("name")) {
				result.name = json.optString("name", null);
			}
			// 类别(图文/文字) 取值范围 BANNER_STYLE_WORD(纯文字), BANNER_STYLE_IMG(图片)

			if (!json.isNull("style")) {
				result.style = json.optString("style", null);
			}
			// 文字内容

			if (!json.isNull("word")) {
				result.word = json.optString("word", null);
			}
			// 类型 取值范围 BANNER_HOT(热门), BANNER_ACTIVITY(活动), BANNER_GROUP(圈子),
			// BANNER_INQUIRY_HALL(问诊大厅), BANNER_INQUIRY_HALL_WORD(问诊大厅_文字),
			// APP_MAIN_PAGE(首页滑页)

			if (!json.isNull("type")) {
				result.type = json.optString("type", null);
			}
			// 规格

			if (!json.isNull("size")) {
				result.size = json.optString("size", null);
			}
			// 图片url

			if (!json.isNull("imgUrl")) {
				result.imgUrl = json.optString("imgUrl", null);
			}
			// 描述

			if (!json.isNull("desc")) {
				result.desc = json.optString("desc", null);
			}
			// 描述在图片中的位置 取值范围 BANNER_DESC_FLOAT(浮动), BANNER_DESC_UNDER(下方),
			// BANNER_DESC_NONE(不显示)

			if (!json.isNull("descLocation")) {
				result.descLocation = json.optString("descLocation", null);
			}
			// 内容类型 取值范围 BANNER_CON_LINK(链接), BANNER_CON_POST(帖子),
			// BANNER_CON_DOCTOR(医生), BANNER_CON_GROUP_CHAT(群聊)

			if (!json.isNull("contentType")) {
				result.contentType = json.optString("contentType", null);
			}
			// 内容

			if (!json.isNull("content")) {
				result.content = json.optString("content", null);
			}
			// 顺序
			result.order = json.optInt("order");
			// 状态（上架，下架） 取值范围 BANNER_ACTIVE(上架), BANNER_DEACTIVE(下架)

			if (!json.isNull("state")) {
				result.state = json.optString("state", null);
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

		// 广告位id
		json.put("id", this.id);

		// 广告位名称
		if (this.name != null) {
			json.put("name", this.name);
		}

		// 类别(图文/文字) 取值范围 BANNER_STYLE_WORD(纯文字), BANNER_STYLE_IMG(图片)
		if (this.style != null) {
			json.put("style", this.style);
		}

		// 文字内容
		if (this.word != null) {
			json.put("word", this.word);
		}

		// 类型 取值范围 BANNER_HOT(热门), BANNER_ACTIVITY(活动), BANNER_GROUP(圈子),
		// BANNER_INQUIRY_HALL(问诊大厅), BANNER_INQUIRY_HALL_WORD(问诊大厅_文字)
		if (this.type != null) {
			json.put("type", this.type);
		}

		// 规格
		if (this.size != null) {
			json.put("size", this.size);
		}

		// 图片url
		if (this.imgUrl != null) {
			json.put("imgUrl", this.imgUrl);
		}

		// 描述
		if (this.desc != null) {
			json.put("desc", this.desc);
		}

		// 描述在图片中的位置 取值范围 BANNER_DESC_FLOAT(浮动), BANNER_DESC_UNDER(下方),
		// BANNER_DESC_NONE(不显示)
		if (this.descLocation != null) {
			json.put("descLocation", this.descLocation);
		}

		// 内容类型 取值范围 BANNER_CON_LINK(链接), BANNER_CON_POST(帖子),
		// BANNER_CON_DOCTOR(医生), BANNER_CON_GROUP_CHAT(群聊)
		if (this.contentType != null) {
			json.put("contentType", this.contentType);
		}

		// 内容
		if (this.content != null) {
			json.put("content", this.content);
		}

		// 顺序
		json.put("order", this.order);

		// 状态（上架，下架） 取值范围 BANNER_ACTIVE(上架), BANNER_DEACTIVE(下架)
		if (this.state != null) {
			json.put("state", this.state);
		}

		return json;
	}
}
