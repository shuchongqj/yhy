package com.ymanalyseslibrary.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:2/22/16
 * Time:10:10
 * Version 1.0
 */
public class MapValueWrapper {

    private String mEventName;
    private Map<String, Object> mEventValues;

    public MapValueWrapper(String eventName, Map<String, Object> eventValues){
        this.mEventName = eventName;
        this.mEventValues = eventValues;
    }

    private void setEventName(String eventName){
        this.mEventName = eventName;
    }

    public void setEventValues(Map<String, Object> eventValues){
        this.mEventValues = eventValues;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();

        if(mEventValues != null){
            mEventValues.put("time", System.currentTimeMillis());
        }

        JSONObject content = new JSONObject();
        Map<String, Object> hashMap = new HashMap(mEventValues);
        Iterator iterator = hashMap.entrySet().iterator();
        try {
            while (iterator.hasNext()) {
                Map.Entry set = (Map.Entry) iterator.next();
                Object object = set.getValue();
                if (object instanceof String) {
                    content.putOpt((String) set.getKey(), (String) set.getValue());
                } else if (object instanceof Long) {
                    content.putOpt((String) set.getKey(), (long) set.getValue());
                } else if (object instanceof Integer) {
                    content.putOpt((String) set.getKey(), (int) set.getValue());
                } else if (object instanceof Float) {
                    content.putOpt((String) set.getKey(), (float) set.getValue());
                } else if (object instanceof Double) {
                    content.putOpt((String) set.getKey(), (double) set.getValue());
                } else if (object instanceof Collection) {
                    content.putOpt((String) set.getKey(), new JSONArray((Collection) set.getValue()));
                } else{
                    content.putOpt((String) set.getKey(), wrappObject(object));
                }
            }
            jsonObject.putOpt(mEventName, content);
        } catch (JSONException e) {
           return null;
        }

        return jsonObject.toString();
    }

    private JSONObject wrappObject(Object object){
        Field[] fields = object.getClass().getDeclaredFields();
        JSONObject object1 = new JSONObject();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Object o = field.get(object);
                if (object instanceof String) {
                    object1.putOpt(field.getName(), (String) o);
                } else if (object instanceof Long) {
                    object1.putOpt(field.getName(), (long) o);
                } else if (object instanceof Integer) {
                    object1.putOpt(field.getName(), (int) o);
                } else if (object instanceof Float) {
                    object1.putOpt(field.getName(), (float) o);
                } else if (object instanceof Double) {
                    object1.putOpt(field.getName(), (double) o);
                } else if (object instanceof Collection) {
                    object1.putOpt(field.getName(), (Collection) o);
                } else {
                    object1.putOpt(field.getName(), wrappObject(o));
                }
            }
        }catch (JSONException e) {
            return object1;
        } catch (IllegalAccessException e) {
            return object1;
        }
        return object1;
    }
}
