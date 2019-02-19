package com.quanyan.yhy.ui.personal.model;

/**
 * Created by shenwenjie on 2018/2/1.
 */

public class RankingListInfo {

    private String rankingName;
    private String rankingUrl;
    private String leftMatchName;
    private String rightMatchName;

    private int leftMatchRanking;
    private int rightMatchRanking;
    private int leftRankChange;
    private int rightRankChange;

    private String allRankingListURL;


    public void setLeftRankChange(int leftRankChange) {
        this.leftRankChange = leftRankChange;
    }

    public void setRightRankChange(int rightRankChange) {
        this.rightRankChange = rightRankChange;
    }

    public String getAllRankingListURL() {
        return allRankingListURL;
    }

    public void setAllRankingListURL(String allRankingListURL) {
        this.allRankingListURL = allRankingListURL;
    }

    public String getRankingName() {
        return rankingName;
    }

    public void setRankingName(String rankingName) {
        this.rankingName = rankingName;
    }

    public String getRankingUrl() {
        return rankingUrl;
    }

    public void setRankingUrl(String rankingUrl) {
        this.rankingUrl = rankingUrl;
    }

    public String getLeftMatchName() {
        return leftMatchName;
    }

    public void setLeftMatchName(String leftMatchName) {
        this.leftMatchName = leftMatchName;
    }

    public int getLeftMatchRanking() {
        return leftMatchRanking;
    }

    public void setLeftMatchRanking(int leftMatchRanking) {
        this.leftMatchRanking = leftMatchRanking;
    }

    public int getLeftRankChange() {
        return leftRankChange;
    }


    public String getRightMatchName() {
        return rightMatchName;
    }

    public void setRightMatchName(String rightMatchName) {
        this.rightMatchName = rightMatchName;
    }

    public int getRightMatchRanking() {
        return rightMatchRanking;
    }

    public void setRightMatchRanking(int rightMatchRanking) {
        this.rightMatchRanking = rightMatchRanking;
    }

    public int getRightRankChange() {
        return rightRankChange;
    }


}
