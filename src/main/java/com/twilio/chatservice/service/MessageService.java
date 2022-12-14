package com.twilio.chatservice.service;

import com.twilio.chatservice.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageService {
    @Autowired
    UserService userService;

    private final Map<Long, List<Message>> userMessageStore;

    public MessageService() {
        this.userMessageStore = new HashMap<>();
    }

    //CREATE
    public void postMessage(Message message) throws Exception {
        if (userService.userValid(message.getSenderId()) && userService.userValid(message.getSenderId())) {

            addToUserMessageStore(message.getSenderId(), message);
            addToUserMessageStore(message.getReceiverId(), message);
        } else {
            throw new Exception(String.format("Either of users %d, %d doesn't exist.", message.getSenderId(), message.getReceiverId()));
        }
    }

    public List<Message> getMessagesForUser(Long id) throws Exception {
        if (userService.userValid(id)) {
            return userMessageStore.get(id);
        } else {
            throw new Exception(String.format("User %d doesn't exist.", id));
        }
    }

    public List<Message> getMessagesForCombo(Long id1, Long id2) throws Exception {
        List<Message> messageList1 = getMessagesForUser(id1);

        return messageList1.stream()
                .filter(message -> message.getReceiverId() == id2 || message.getSenderId() == id2)
                .sorted(Comparator.comparing(Message::getTimestamp))
                .collect(Collectors.toList());
    }

    private void addToUserMessageStore(long id, Message message) {
        List<Message> senderMessageList = userMessageStore.get(id);

        if (CollectionUtils.isEmpty(senderMessageList)) {
            userMessageStore.put(id, new ArrayList<>() {
                {
                    add(message);
                }
            });
        } else {
            senderMessageList.add(message);
        }
    }
}
