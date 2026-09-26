package com.solargreen.app.models;

public class NotificationItem {
    public String id;
    public String title;
    public String message;
    public String timestamp;
    public boolean isRead;

    public NotificationItem(String id, String title, String message, String timestamp, boolean isRead) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
        this.isRead = isRead;
    }
}