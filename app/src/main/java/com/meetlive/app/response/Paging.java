package com.meetlive.app.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Paging {
    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("current")
    @Expose
    private int current;
    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("prevPage")
    @Expose
    private boolean prevPage;
    @SerializedName("nextPage")
    @Expose
    private boolean nextPage;
    @SerializedName("pageCount")
    @Expose
    private int pageCount;
    @SerializedName("limit")
    @Expose
    private int limit;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isPrevPage() {
        return prevPage;
    }

    public void setPrevPage(boolean prevPage) {
        this.prevPage = prevPage;
    }

    public boolean isNextPage() {
        return nextPage;
    }

    public void setNextPage(boolean nextPage) {
        this.nextPage = nextPage;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
