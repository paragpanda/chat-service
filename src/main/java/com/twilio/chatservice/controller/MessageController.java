package com.twilio.chatservice.controller;

import com.twilio.chatservice.model.Message;
import com.twilio.chatservice.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MessageController {
    @Autowired
    MessageService messageService;

    @RequestMapping(value="/message", method= RequestMethod.POST)
    public void postMessage(@RequestBody Message message) throws Exception {
        messageService.postMessage(message);
    }

    @RequestMapping(value="/message/{userId}", method= RequestMethod.GET)
    public List<Message> getMessagesForUser(@PathVariable(value = "userId") Long id) throws Exception {
        return messageService.getMessagesForUser(id);
    }

    @RequestMapping(value="/message/{userId1}/{userId2}", method= RequestMethod.GET)
    public List<Message> getMessagesForCombo(@PathVariable(value = "userId1") Long id1,
                                             @PathVariable(value = "userId2") Long id2) throws Exception {
        return messageService.getMessagesForCombo(id1, id2);
    }
}
