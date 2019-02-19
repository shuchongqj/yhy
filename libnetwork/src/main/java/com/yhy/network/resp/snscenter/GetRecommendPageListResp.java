package com.yhy.network.resp.snscenter;


import java.util.List;

public class GetRecommendPageListResp {

    public String	traceId;	// 用于追踪日志的UUID
    public boolean	hasNext;	// 是否有下一页
    public List<RecommendResult> list;	// 选项
    public int	opType;	// 1:上滑，2:下拉
    public long	minSeqNo;	// 最小序号
    public long	maxSeqNo;	// 最大序号

    public static class RecommendResult{

        public long	id;	// 文章id
        public String	title;	// 标题
        public int	top;	// 1:置顶，2:热门
        public List<String>	picList;	// 图片集合
        public List<TagInfo>	tagInfoList;	// 标签信息列表
        public String	source;	// 来源
        public int	canComment;	// 是否可以评论，1:是，2:否
        public int	commentNum;	// 评论数
        public int	recommendType;	// 类型：1文章，2视频
        public long	publishDate;	// 发布时间
        public long	ugcId;	// 动态id
        public long	authorId;	// 作者
        public long	videoId;	// 视频id，推荐视频使用
        public String	videoUrl;	// 视频URL
        public String	videoPicUrl;	// 视频缩略图URL
        public String	liveScreenType;	// 直播横竖屏类型,横屏:HORIZONTAL, 竖屏:VERTICAL
        public int	type;	// 关注类型 0:未关注 1:单向关注 2:双向关注
        public int	viewCount;	// 观看次数
        public String	authorName;	// 作者名字

        public static class TagInfo {
            public long	id;	// 标签ID
            public String	name;	// 标签名称
            public int	type;	// 1:文章标签 2:固定标签 3:屏蔽来源
            public boolean isSelected;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RecommendResult result = (RecommendResult) o;

            if (id != result.id) return false;
            if (videoId != result.videoId) return false;
            return title != null ? title.equals(result.title) : result.title == null;
        }

        @Override
        public int hashCode() {
            int result = (int) (id ^ (id >>> 32));
            result = 31 * result + (title != null ? title.hashCode() : 0);
            result = 31 * result + (int) (videoId ^ (videoId >>> 32));
            return result;
        }
    }
}
