package com.yhy.sport.view;


/**
 * Create by nandy on 2018/6/8
 */
public interface StepView extends IView{
    void setStepStatus(String status);

    void setStepButtonText(String text);

    void setTodayStep(int step);

    void setYesterdayStep(int step);

    void setBeforeYesterdayStep(int step);

    void setStepRunningPeriod(String period);

    void setSensorType(int type);

    void setCurLocation(String location);
}
