package com.amora.app.response.UserListResponseNew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetRatingTag {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("host_id")
    @Expose
    private Integer hostId;
    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("totalCount")
    @Expose
    private Integer totalCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHostId() {
        return hostId;
    }

    public void setHostId(Integer hostId) {
        this.hostId = hostId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

}

