package com.yhy.common.beans.net.model.trip;

import java.io.Serializable;

/**
 * Author：By wangjian on 2015-11-23 17:21.
 */
public class ScenicTicketsDialogList implements Serializable{

    private static final long serialVersionUID = 6025270106014327291L;
    private boolean isSeclect;

    private ItemVO ticket;

    public boolean isSeclect() {
        return isSeclect;
    }

    public void setIsSeclect(boolean isSeclect) {
        this.isSeclect = isSeclect;
    }

    public ItemVO getTicket() {
        return ticket;
    }

    public void setTicket(ItemVO ticket) {
        this.ticket = ticket;
    }
}
