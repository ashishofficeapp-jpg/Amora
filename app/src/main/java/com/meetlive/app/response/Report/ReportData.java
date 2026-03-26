package com.meetlive.app.response.Report;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportData {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("issue")
    @Expose
    private String issue;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("updated")
    @Expose
    private String updated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }
}
