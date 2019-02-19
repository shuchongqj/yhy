package com.yhy.common.beans.net.model;


import com.yhy.common.beans.net.model.user.NativeDataBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:NativeBean
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-3-9
 * Time:19:19
 * Version 1.0
 * Description:
 */
public class NativeBean implements Serializable{

    private static final long serialVersionUID = 8559850086910908665L;
    private String TYPE;
    private String OPERATION;
    private NativeDataBean DATA;

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getOPERATION() {
        return OPERATION;
    }

    public void setOPERATION(String OPERATION) {
        this.OPERATION = OPERATION;
    }

    public NativeDataBean getData() {
        return DATA;
    }

    public void setData(NativeDataBean data) {
        this.DATA = data;
    }

    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        if(this.TYPE != null){
            json.put("TYPE", this.TYPE);
        }

        if(this.OPERATION != null){
            json.put("OPERATION", this.OPERATION);
        }

        if(this.DATA != null){
            json.put("DATA", this.DATA.serialize());
        }
        return json;
    }


}




