package com.newyhy.beans;

import java.io.Serializable;
import java.util.List;

/**
 * 圈子 爬取到信息（推荐，动态标签，搜索页面）
 * Created by Jiervs on 2018/6/29.
 */

public class CircleCrawlInfo implements Serializable{

    private static final long serialVersionUID = 2L;

    public String	id;	// 文章id
    public String	title;	// 推荐标题
    public int	top;	// 1:置顶，2:热门
    public String	source;	// 来源
    public int	canComment;	// 是否可以评论，1:是，2:否
    public int	commentNum;	// 评论数
    public String	videoUrl;	// 视频URL
    public String	videoPicUrl;	// 视频缩略图URL
    public int	recommendType;	// 类型：1文章，2视频
    public long publishDate;	// 发布时间
    public int	liveScreenType;	// 默认横屏0 竖屏1
    public long ugcId;	// 动态id
    public long	authorId;// 作者
    public long	videoId;// 视频id

    public List<String> picList;	// 图片集合
    public List<Tag>	tagInfoList;	// 标签信息列表
}
