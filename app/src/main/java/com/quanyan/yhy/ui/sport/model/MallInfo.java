package com.quanyan.yhy.ui.sport.model;

/**
 * Created by shenwenjie on 2018/1/30.
 */

public class MallInfo {

    private long itemId ;//item id
    private String cover;//item封面
    private String itemTitle;//积分商品标题
    private int scorePrice;// 积分价格

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public int getScorePrice() {
        return scorePrice;
    }

    public void setScorePrice(int scorePrice) {
        this.scorePrice = scorePrice;
    }
}
