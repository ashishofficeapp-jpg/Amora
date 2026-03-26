package com.meetlive.app.body;

public class CallRecordBody {

    // caller_id

    public String receiver_id, unique_id, call_type;
    public Duration duration;
    public boolean is_free_call;

    public CallRecordBody(String receiver_id, /*String caller_id,*/ String unique_id, Duration duration) {
        this.receiver_id = receiver_id;
//        this.caller_id = caller_id;
        this.unique_id = unique_id;
        this.duration = duration;
    }

    public CallRecordBody(String receiver_id, /*String caller_id,*/ String unique_id, boolean is_free_call, Duration duration) {
        this.receiver_id = receiver_id;
        this.is_free_call = is_free_call;
        this.unique_id = unique_id;
        this.duration = duration;
    }

    public CallRecordBody(String receiver_id, String unique_id, String call_type, Duration duration) {
        this.receiver_id = receiver_id;
        this.unique_id = unique_id;
        this.call_type = call_type;
        this.duration = duration;
    }

    public static class Duration {
        String start, end;

        public Duration(String start, String end) {
            this.start = start;
            this.end = end;
        }
    }
}
