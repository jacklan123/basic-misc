package com.fangdd.framework.contract;

import java.io.Serializable;

public class PageInfo implements Serializable {
    private int pageNo;
    private int pageSize;
    private boolean needTotal = false;

    public PageInfo() {
        this.pageNo = 1;
        this.pageSize = 20;
    }

    public PageInfo(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isNeedTotal() {
        return this.needTotal;
    }

    public void setNeedTotal(boolean needTotal) {
        this.needTotal = needTotal;
    }
}
