package com.meetlive.app.response.Employee;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.meetlive.app.response.Language;

public class EmployeeLanguage {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("employee_id")
    @Expose
    private int employeeId;
    @SerializedName("language_id")
    @Expose
    private int languageId;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("language")
    @Expose
    private Language language;

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

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

}
