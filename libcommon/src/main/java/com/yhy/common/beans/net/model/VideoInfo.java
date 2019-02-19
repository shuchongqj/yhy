package com.yhy.common.beans.net.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:VideoInfo
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/5/17
 * Time:上午11:34
 * Version 1.1.0
 */
@Table(name = "videos")
public class VideoInfo implements Serializable{
    private static final long serialVersionUID = 3143551570677609540L;
    @Id
    @NoAutoIncrement
    @Column(column = "id")
    public long id;
    @Column(column = "local_path")
    public String videoLocalPath;
    @Column(column = "url")
    public String videoUrl;
    @Column(column = "create_date")
    public long createDate;
    @Column(column = "duration")
    public long duration;
    @Column(column = "thumb_local_path")
    public String videoThumbLocalPath;
    @Column(column = "thumb_url")
    public String videoThumbUrl;
}
