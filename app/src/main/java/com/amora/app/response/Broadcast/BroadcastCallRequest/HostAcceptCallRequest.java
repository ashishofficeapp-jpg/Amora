package com.amora.app.response.Broadcast.BroadcastCallRequest;

public class HostAcceptCallRequest {
    private String broadcast_id;
    private String user_id;

    public HostAcceptCallRequest(String broadcast_id, String user_id) {
        this.broadcast_id = broadcast_id;
        this.user_id = user_id;
    }
}
