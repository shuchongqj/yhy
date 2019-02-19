package com.yhy.common.beans.net.model.trip;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:ChoolseAbilityBean
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:朱道顺
 * Date:2015/12/30
 * Time:19:29
 * Version 1.0
 */
public class ChoolseAbilityBean implements Serializable {
    private static final long serialVersionUID = 6254585055249567440L;

    // 显示title
    String title;
    public long id;

    public ChoolseAbilityBean(String title,long id) {
        this.title = title;
        this.id = id;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }


}

