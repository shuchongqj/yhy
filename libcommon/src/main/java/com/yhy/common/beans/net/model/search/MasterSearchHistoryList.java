package com.yhy.common.beans.net.model.search;

import java.io.Serializable;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:MasterSearchHistoryList
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/3/8
 * Time:下午8:07
 * Version 1.0
 */
public class MasterSearchHistoryList implements Serializable{
    private static final long serialVersionUID = -5251009531268387416L;

    public List<BaseHistorySearch> history;
}
