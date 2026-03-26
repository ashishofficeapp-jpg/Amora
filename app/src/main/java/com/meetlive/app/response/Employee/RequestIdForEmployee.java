package com.meetlive.app.response.Employee;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestIdForEmployee {
    @SerializedName("employee_id")
    @Expose
    private String employee_id;

    public RequestIdForEmployee(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }
}
