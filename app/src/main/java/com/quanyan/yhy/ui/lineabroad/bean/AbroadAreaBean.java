package com.quanyan.yhy.ui.lineabroad.bean;

import com.yhy.common.beans.net.model.user.Destination;

import java.io.Serializable;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:AbroadAreaBean
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-6-1
 * Time:10:20
 * Version 1.1.0
 */

public class AbroadAreaBean implements Serializable {
    private static final long serialVersionUID = 2953207435718191393L;
    private Destination destination;
    private String simpleName;
    private List<Destination> childDestinations;

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public List<Destination> getChildDestinations() {
        return childDestinations;
    }

    public void setChildDestinations(List<Destination> childDestinations) {
        this.childDestinations = childDestinations;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }
}
