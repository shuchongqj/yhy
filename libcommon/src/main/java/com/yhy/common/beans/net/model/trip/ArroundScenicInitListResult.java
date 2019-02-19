package com.yhy.common.beans.net.model.trip;

import com.yhy.common.beans.net.model.master.QueryTerm;
import com.yhy.common.beans.net.model.user.Destination;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:ArroundScenicInitListResult
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/5/30
 * Time:下午9:03
 * Version 1.1.0
 */
public class ArroundScenicInitListResult implements Serializable{

    private static final long serialVersionUID = 1998320315284406394L;

    public QueryTerm filtes;

    public ScenicInfoResult result;

    public Destination destination;
}
