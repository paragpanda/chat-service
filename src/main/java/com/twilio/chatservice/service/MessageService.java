package com.twilio.chatservice.service;

import com.twilio.chatservice.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class MessageService {
    @Autowired
    UserService userService;

    private final Map<Long, List<Message>> userMessageStore;
    private final Map<Long, Long> lastPolledIndexForUser;
    private final Map<Long, Map<Long, Long>> lastPolledIndexForCombo;

    public MessageService() {
        this.userMessageStore = new HashMap<>();
        this.lastPolledIndexForUser = new HashMap<>();
        this.lastPolledIndexForCombo = new HashMap<>();
    }

    //CREATE
    public void postMessage(Message message) throws Exception {
        if (userService.userValid(message.getSenderId()) && userService.userValid(message.getReceiverId())) {
            addToUserMessageStore(message.getSenderId(), message);
            addToUserMessageStore(message.getReceiverId(), message);
        } else {
            throw new Exception(String.format("Either of users %d, %d doesn't exist.", message.getSenderId(), message.getReceiverId()));
        }
    }

    public List<Message> getMessagesForUser(Long id, boolean pollHistoricalData) throws Exception {
        if (userService.userValid(id)) {
            if (pollHistoricalData) {
                return userMessageStore.get(id);
            } else {
                this.lastPolledIndexForUser.putIfAbsent(id, -1L);

                long lastPolledIndex = this.lastPolledIndexForUser.get(id);

                List<Message> messageList = userMessageStore.get(id);

                List<Message> messages = IntStream.range(0, messageList.size())
                        .filter(i -> i > lastPolledIndex)
                        .mapToObj(messageList::get)
                        .toList();

                this.lastPolledIndexForUser.put(id, (long) (messageList.size() - 1));

                return messages.stream()
                        .sorted(Comparator.comparing(Message::getTimestamp))
                        .collect(Collectors.toList());
            }
        } else {
            throw new Exception(String.format("User %d doesn't exist.", id));
        }
    }

    public List<Message> getMessagesForCombo(Long id1, Long id2, boolean pollHistoricalData) throws Exception {
        if (userService.userValid(id1) && userService.userValid(id2)) {
            List<Message> messageListUser1 = userMessageStore.get(id1);

            if (pollHistoricalData) {
                return messageListUser1.stream()
                        .filter(message -> message.getReceiverId() == id2 || message.getSenderId() == id2)
                        .sorted(Comparator.comparing(Message::getTimestamp))
                        .collect(Collectors.toList());
            }

            Map<Long, Long> lastPolledIndexForUser1 = this.lastPolledIndexForCombo.get(id1);

            if (lastPolledIndexForUser1 == null || !lastPolledIndexForUser1.containsKey(id2)) {
                this.lastPolledIndexForCombo.put(id1, new HashMap<>() {{
                    put(id2, -1L);
                }});
            }

            long lastPolledIndexForCombo = this.lastPolledIndexForCombo.get(id1).get(id2);

            List<Message> messages = IntStream.range(0, messageListUser1.size())
                    .filter(i -> i > lastPolledIndexForCombo &&
                            (messageListUser1.get(i).getReceiverId() == id2 || messageListUser1.get(i).getSenderId() == id2))
                    .mapToObj(messageListUser1::get)
                    .toList();

            this.lastPolledIndexForCombo.get(id1).put(id2, (long) (messageListUser1.size() - 1));

            return messages.stream()
                    .sorted(Comparator.comparing(Message::getTimestamp))
                    .collect(Collectors.toList());
        } else {
            throw new Exception(String.format("Either of users %d, %d doesn't exist.",id1, id2));
        }
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
