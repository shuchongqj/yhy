package com.yhy.common.beans.net.model;



/**
 * Created with Android Studio.
 * Title:
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:吴建淼
 * Date: 2015-10-20
 * Time: 11:01
 * Version 1.0
 */
public class TravelsDetailsBean {
    private String id;
    private String name;
    private String title;
    private String message;
    private String imageUrl;
private String    userimageUrl;
    public String getUserimageUrl() {
        return userimageUrl;
    }

    public void setUserimageUrl(String userimageUrl) {
        this.userimageUrl = userimageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
