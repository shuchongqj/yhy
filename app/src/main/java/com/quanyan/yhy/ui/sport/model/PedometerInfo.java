package com.quanyan.yhy.ui.sport.model;

/**
 * Created by shenwenjie on 2018/2/5.
 */

public class PedometerInfo {

    private int stepNum;// 运动页运动步数
    private long bannerId;// banner id
    private String bannerCover;// banner封面
    private String bannerType;// 展位类别
    private String url;// banner URL
    private String bannerLeftCover;

    public String getBannerLeftCover() {
        return bannerLeftCover;
    }

    public void setBannerLeftCover(String bannerLeftCover) {
        this.bannerLeftCover = bannerLeftCover;
    }

    public int getStepNum() {
        return stepNum;
    }

    public void setStepNum(int stepNum) {
        this.stepNum = stepNum;
    }

    public long getBannerId() {
        return bannerId;
    }

    public void setBannerId(long bannerId) {
        this.bannerId = bannerId;
    }

    public String getBannerCover() {
        return bannerCover;
    }

    public void setBannerCover(String bannerCover) {
        this.bannerCover = bannerCover;
    }

    public String getBannerType() {
        return bannerType;
    }

    public void setBannerType(String bannerType) {
        this.bannerType = bannerType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
