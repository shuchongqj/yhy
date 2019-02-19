package com.newyhy.beans;

import java.io.Serializable;

/**
 * 标签信息
 * Created by Jiervs on 2018/6/29.
 */

public class Tag implements Serializable{

    private static final long serialVersionUID = 3L;

    public long id;
    public String name;
    public int type;
    public boolean isSelected = false;
}
