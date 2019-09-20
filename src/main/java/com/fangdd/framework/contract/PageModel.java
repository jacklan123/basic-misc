package com.fangdd.framework.contract;

import java.util.List;

public class PageModel<T> extends PageInfo {
    private int totalRecord = 0;
    private int totalPage;
    private List<T> results;

    public PageModel() {
    }

    public PageModel(int pageNo, int pageSize) {
        super(pageNo, pageSize);
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
        int totalPage = totalRecord % this.getPageSize() == 0 ? totalRecord / this.getPageSize() : totalRecord / this.getPageSize() + 1;
        this.setTotalPage(totalPage);
    }

    public int getTotalPage() {
        return this.totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getResults() {
        return this.results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public int getTotalRecord() {
        return this.totalRecord;
    }
}
