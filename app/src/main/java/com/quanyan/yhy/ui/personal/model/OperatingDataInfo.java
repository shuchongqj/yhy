package com.quanyan.yhy.ui.personal.model;

/**
 * Created by shenwenjie on 2018/2/1.
 */

public class OperatingDataInfo {

    private String operatingName;
    private String operatingUrl;
    private double income;
    private int incomeChange;
    private String digestibility;
    private int digestibilityChange;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOperatingName() {
        return operatingName;
    }

    public void setOperatingName(String operatingName) {
        this.operatingName = operatingName;
    }

    public String getOperatingUrl() {
        return operatingUrl;
    }

    public void setOperatingUrl(String operatingUrl) {
        this.operatingUrl = operatingUrl;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public int getIncomeChange() {
        return incomeChange;
    }

    public void setIncomeChange(int incomeChange) {
        this.incomeChange = incomeChange;
    }

    public String getDigestibility() {
        return digestibility;
    }

    public void setDigestibility(String digestibility) {
        this.digestibility = digestibility;
    }

    public int getDigestibilityChange() {
        return digestibilityChange;
    }

    public void setDigestibilityChange(int digestibilityChange) {
        this.digestibilityChange = digestibilityChange;
    }
}
