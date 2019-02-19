package com.yhy.network.resp.snscenter;


import java.util.List;

public class GetTagInfoListByTypeResp {
    private List<TagResult> tagResultList;

    public List<TagResult> getTagResultList() {
        return tagResultList;
    }

    public void setTagResultList(List<TagResult> tagResultList) {
        this.tagResultList = tagResultList;
    }

    public static class TagResult {
        private long	id;	// id
        private String	code;	// code
        private String	description;	// 描述
        private int	type;	// 类型 1:运动标签 2：用途标签 3：tab标签
        private int	tabType;	// tab标签类型 1：固定标签 2：动态标签
        private String	iconUrl;	// 图标url
        private String	url;	// 列表url
        private int	isIndex;	// 是否是默认显示tab 0：否 1：是
        private List<String>tagList;	// 标签列表

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getTabType() {
            return tabType;
        }

        public void setTabType(int tabType) {
            this.tabType = tabType;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getIsIndex() {
            return isIndex;
        }

        public void setIsIndex(int isIndex) {
            this.isIndex = isIndex;
        }

        public List<String> getTagList() {
            return tagList;
        }

        public void setTagList(List<String> tagList) {
            this.tagList = tagList;
        }
    }
}



