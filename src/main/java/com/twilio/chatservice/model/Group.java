package com.twilio.chatservice.model;

import java.util.List;

public class Group {
    private final long id;
    private final List<Long> users;

    public Group(long id, List<Long> users) {
        this.id = id;
        this.users = users;
    }

    public long getId() {
        return id;
    }

    public List<Long> getUsers() {
        return users;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", users=" + users +
                '}';
    }
}
