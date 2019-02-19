package com.yhy.common.beans.net.model;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:ShareBean
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2016-1-21
 * Time:16:44
 * Version 1.0
 */

public class ShareBean implements Serializable {
    private static final long serialVersionUID = -8387635132593480877L;

    public String dlgTitle;
    public String dlgMessage;
    public String shareTitle;
    public String shareContent;
    public String shareImageURL;
    public String shareImageLocal;
    public String shareWebPage;
    public String shareWebPageThumb;
    public String shareAction;
    public String actionShareResult;
    public boolean isNeedSyncToDynamic = false;

    //打点用，其他不用
    public String pid;
    public String pname;
    public String ptype;
    //分享方式
    public int shareWay = -1;
}
