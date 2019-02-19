package com.quanyan.yhy.ui.personal.model;

/**
 * Created by shenwenjie on 2018/2/1.
 */

public class ClubInfo {

    private String clubName;
    private String clubUrl;
    private int exerciseTime;
    private int exerciseTimeMonth;
    private String clubFund;
    private String moreClubURL;

    public String getMoreClubURL() {
        return moreClubURL;
    }

    public void setMoreClubURL(String moreClubURL) {
        this.moreClubURL = moreClubURL;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getClubUrl() {
        return clubUrl;
    }

    public void setClubUrl(String clubUrl) {
        this.clubUrl = clubUrl;
    }

    public int getExerciseTime() {
        return exerciseTime;
    }

    public int getExerciseTimeMonth() {
        return exerciseTimeMonth;
    }

    public void setExerciseTimeMonth(int exerciseTimeMonth) {
        this.exerciseTimeMonth = exerciseTimeMonth;
    }

    public void setExerciseTime(int exerciseTime) {
        this.exerciseTime = exerciseTime;
    }

    public String getClubFund() {
        return clubFund;
    }

    public void setClubFund(String clubFund) {
        this.clubFund = clubFund;
    }
}
