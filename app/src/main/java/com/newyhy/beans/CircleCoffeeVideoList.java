package com.newyhy.beans;

import com.yhy.common.beans.net.model.discover.UgcInfoResult;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jiervs on 2018/6/27.
 */

public class CircleCoffeeVideoList implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    public int pageNo;

    /**
     * 是否有下一页
     */
    public boolean hasNext;

    /**
     * UGC信息列表
     */
    public List<UgcInfoResult> ugcInfoResultList;
}
