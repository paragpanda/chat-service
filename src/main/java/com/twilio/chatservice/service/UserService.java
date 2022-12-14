package com.twilio.chatservice.service;

import com.twilio.chatservice.model.User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final Map<Long, User> userStore;

    public UserService() {
        this.userStore = new HashMap<>();
    }

    // CREATE
    public void createUser(User user) {
        userStore.put(user.getId(), user);
    }

    // READ
    public User getUser(long id) throws Exception {
        if (!userStore.containsKey(id)) {
            throw new Exception(String.format("User with id : %d does not exist.", id));
        }
        return userStore.get(id);
    }

    public boolean userValid(long id) {
        return userStore.containsKey(id);
    }

    public Collection<User> getAllUsers() {
        return userStore.values();
    }

    //DELETE
    public void deleteUser(long id) throws Exception {
        if (!userStore.containsKey(id)) {
            throw new Exception(String.format("User with id : %d does not exist.", id));
        }
        userStore.remove(id);
    }
}
