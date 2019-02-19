package com.yhy.common.beans.net.model.tm;
import com.yhy.common.beans.calender.PickSku;
import com.yhy.common.beans.net.model.trip.ItemVO;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created with Android Studio.
 * Title:SkuListWithStartDate
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:和平必胜、正义必胜、人民必胜
 * Author:炎黄子孙
 * Date:15/12/27
 * Time:下午12:48
 * Version 1.0
 */
public class SkuListWithStartDate implements Serializable {
    private static final long serialVersionUID = 5180441039886301547L;

    public TmCreateOrderContext tmCreateOrderContext;

    public ItemVO itemVO;

    public HashMap<String, PickSku> skuMap;
    public long endDate;

}
