package com.meetlive.app.response.coin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CoinUpdateData {
    @SerializedName("fieldCount")
    @Expose
    private int fieldCount;
    @SerializedName("affectedRows")
    @Expose
    private int affectedRows;
    @SerializedName("insertId")
    @Expose
    private int insertId;
    @SerializedName("info")
    @Expose
    private String info;
    @SerializedName("serverStatus")
    @Expose
    private int serverStatus;
    @SerializedName("warningStatus")
    @Expose
    private int warningStatus;
    @SerializedName("changedRows")
    @Expose
    private int changedRows;

    public int getFieldCount() {
        return fieldCount;
    }

    public void setFieldCount(int fieldCount) {
        this.fieldCount = fieldCount;
    }

    public int getAffectedRows() {
        return affectedRows;
    }

    public void setAffectedRows(int affectedRows) {
        this.affectedRows = affectedRows;
    }

    public int getInsertId() {
        return insertId;
    }

    public void setInsertId(int insertId) {
        this.insertId = insertId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(int serverStatus) {
        this.serverStatus = serverStatus;
    }

    public int getWarningStatus() {
        return warningStatus;
    }

    public void setWarningStatus(int warningStatus) {
        this.warningStatus = warningStatus;
    }

    public int getChangedRows() {
        return changedRows;
    }

    public void setChangedRows(int changedRows) {
        this.changedRows = changedRows;
    }
}
