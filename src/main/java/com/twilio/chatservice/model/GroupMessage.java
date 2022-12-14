package com.twilio.chatservice.model;

public class GroupMessage {
    private final long groupId;
    private final long senderId;
    private final String content;

    private final long timestamp;

    public GroupMessage(long groupId, long senderId, String content) {
        this.groupId = groupId;
        this.senderId = senderId;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }

    public long getGroupId() {
        return groupId;
    }

    public long getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Message{" +
                "groupId=" + groupId +
                ", senderId=" + senderId +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
