package com.example.myapplication01.model;

import java.util.UUID;

/* JADX INFO: loaded from: classes4.dex */
public class ClipData {
    private String clip_content;
    private String clip_type;
    private String device_id;
    private long timestamp;
    private String id = UUID.randomUUID().toString();
    private String status = "PENDING";
    private int retry_count = 0;

    public ClipData(String device_id, long timestamp, String clip_content, String clip_type) {
        this.device_id = device_id;
        this.timestamp = timestamp;
        this.clip_content = clip_content;
        this.clip_type = clip_type;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return this.device_id;
    }

    public void setDeviceId(String device_id) {
        this.device_id = device_id;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getClipContent() {
        return this.clip_content;
    }

    public void setClipContent(String clip_content) {
        this.clip_content = clip_content;
    }

    public String getClipType() {
        return this.clip_type;
    }

    public void setClipType(String clip_type) {
        this.clip_type = clip_type;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRetryCount() {
        return this.retry_count;
    }

    public void setRetryCount(int retry_count) {
        this.retry_count = retry_count;
    }

    public void incrementRetryCount() {
        this.retry_count++;
    }
}
