package com.alomonshi.object.uiobjects.pagination;

import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

public class Pagination {
    private int pageNumber;
    private int pageSize;
    private int totalData;
    private int totalPageCount;
    private Object data;

    public Pagination(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    @JsonView(JsonViews.NormalViews.class)
    int getPageNumber() {
        return pageNumber;
    }

    @JsonView(JsonViews.NormalViews.class)
    int getPageSize() {
        return pageSize;
    }

    @JsonView(JsonViews.NormalViews.class)
    int getTotalData() {
        return totalData;
    }

    @JsonView(JsonViews.NormalViews.class)
    public Object getData() {
        return data;
    }

    @JsonView(JsonViews.NormalViews.class)
    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }
}
