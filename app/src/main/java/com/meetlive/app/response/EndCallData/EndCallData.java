package com.meetlive.app.response.EndCallData;

public class EndCallData {

    private String unique_id;
    private String end_time;

    public EndCallData(String unique_id, String end_time) {
        this.unique_id = unique_id;
        this.end_time = end_time;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}
