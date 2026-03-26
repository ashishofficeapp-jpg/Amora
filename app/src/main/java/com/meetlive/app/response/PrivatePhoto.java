package com.meetlive.app.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PrivatePhoto {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("employee_id")
    @Expose
    private int employeeId;
    @SerializedName("private_photo")
    @Expose
    private String privatePhoto;
    @SerializedName("created")
    @Expose
    private String created;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getPrivatePhoto() {
        return privatePhoto;
    }

    public void setPrivatePhoto(String privatePhoto) {
        this.privatePhoto = privatePhoto;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
