// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;
    
import com.yhy.common.beans.net.model.tm.TmValueVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SalesPropertyVO implements Serializable{

    private static final long serialVersionUID = 641467728757244797L;
    /**
     * id
     */
    public long id;
      
    /**
     * 属性名
     */
    public String text;

    /**
     * 属性类型 TEXT:普通文本/DATE:普通日期/PERSON_TYPE:人员类型/START_DATE:出发日期/LINE_PACKAGE:套餐/ACTIVITY_TIME:活动时间/ACTIVITY_CONTENT:活动内容
     */
    public String type;
      
    /**
     * 属性值是否可多选
     */
    public boolean isMultiSelect;
      
    /**
     * 展示顺序 数字越小越靠上
     */
    public int order;

    /**
     * 属性值列表
     */
    public List<TmValueVO> valueVOList;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static SalesPropertyVO deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static SalesPropertyVO deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            SalesPropertyVO result = new SalesPropertyVO();
            
            // id
            result.id = json.optLong("id");
            // 属性名
            
              if(!json.isNull("text")){
                  result.text = json.optString("text", null);
              }
            // 属性类型 DATE：日期/PERSON_TYPE：出行人类别
            
              if(!json.isNull("type")){
                  result.type = json.optString("type", null);
              }
            // 属性值是否可多选
            result.isMultiSelect = json.optBoolean("isMultiSelect");
            // 展示顺序 数字越小越靠上
            result.order = json.optInt("order");

            // 属性值列表
            JSONArray valueVOListArray = json.optJSONArray("valueVOList");
            if (valueVOListArray != null) {
                int len = valueVOListArray.length();
                result.valueVOList = new ArrayList<TmValueVO>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = valueVOListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.valueVOList.add(TmValueVO.deserialize(jo));
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
        
        // id
        json.put("id", this.id);
          
        // 属性名
        if(this.text != null) { json.put("text", this.text); }
          
        // 属性类型 DATE：日期/PERSON_TYPE：出行人类别
        if(this.type != null) { json.put("type", this.type); }
          
        // 属性值是否可多选
        json.put("isMultiSelect", this.isMultiSelect);
          
        // 展示顺序 数字越小越靠上
        json.put("order", this.order);

        // 属性值列表
        if (this.valueVOList != null) {
            JSONArray valueVOListArray = new JSONArray();
            for (TmValueVO value : this.valueVOList)
            {
                if (value != null) {
                    valueVOListArray.put(value.serialize());
                }
            }
            json.put("valueVOList", valueVOListArray);
        }

        return json;
    }
}
  