package com.meetlive.app.response.Report;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestReport {
    @SerializedName("report_issue_id")
    @Expose
    private int reportissueid;

    @SerializedName("friend_id")
    @Expose
    private int friendid;

    public RequestReport(int reportissueid, int friendid) {
        this.reportissueid = reportissueid;
        this.friendid = friendid;
    }

    public int getReportissueid() {
        return reportissueid;
    }

    public void setReportissueid(int reportissueid) {
        this.reportissueid = reportissueid;
    }

    public int getFriendid() {
        return friendid;
    }

    public void setFriendid(int friendid) {
        this.friendid = friendid;
    }
}
