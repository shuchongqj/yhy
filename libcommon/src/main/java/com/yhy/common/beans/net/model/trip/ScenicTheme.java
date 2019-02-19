package com.yhy.common.beans.net.model.trip;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:ScenicTheme
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2015-11-24
 * Time:15:17
 * Version 1.0
 * Description:
 */
public class ScenicTheme implements Serializable{

    private static final long serialVersionUID = 4535021271439307762L;
    private boolean isSelect;

    private ItemSubjectInfo subjectInfo;

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public ItemSubjectInfo getSubjectInfo() {
        if(subjectInfo == null){
            return new ItemSubjectInfo();
        }
        return subjectInfo;
    }

    public void setSubjectInfo(ItemSubjectInfo subjectInfo) {
        this.subjectInfo = subjectInfo;
    }
}
