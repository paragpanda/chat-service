package com.twilio.chatservice.service;

import com.twilio.chatservice.model.Group;
import com.twilio.chatservice.model.GroupMessage;
import com.twilio.chatservice.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GroupService {
    @Autowired
    UserService userService;

    private final Map<Long, Group> groupStore;
    private final Map<Long, List<GroupMessage>> groupMessageStore;

    public GroupService() {
        this.groupStore = new HashMap<>();
        this.groupMessageStore = new HashMap<>();
    }


    //CREATE GROUP
    public void createGroup(long id, String users) {
        List<String> userList = Arrays.asList(users.split("\\s*,\\s*"));

        List<Long> userListLong = userList.stream().map(Long::parseLong).toList();

        //keep only valid users
        groupStore.put(id, new Group(id,
                userListLong.stream().filter(user -> userService.userValid(user)).collect(Collectors.toList())));
    }

    //GET GROUP
    public Group getGroup(long id) throws Exception {
        if (groupStore.containsKey(id)) {
            return groupStore.get(id);
        } else {
            throw new Exception(String.format("Group %s doesn't exist.", id));
        }
    }

    //CREATE
    public void postMessage(GroupMessage message) throws Exception {
        if (groupStore.containsKey(message.getGroupId())) {
            List<GroupMessage> groupMessages = groupMessageStore.getOrDefault(message.getGroupId(), new ArrayList<>());

            groupMessages.add(message);

            groupMessageStore.put(message.getGroupId(), groupMessages);
        } else {
            throw new Exception(String.format("Group %d doesn't exist.", message.getGroupId()));
        }
    }

    public List<GroupMessage> getGroupMessages(Long id) throws Exception {
        if (groupStore.containsKey(id)) {
            return groupMessageStore.get(id).stream()
                    .sorted(Comparator.comparing(GroupMessage::getTimestamp))
                    .collect(Collectors.toList());
        } else {
            throw new Exception(String.format("Group %d doesn't exist.", id));
        }
    }
}
