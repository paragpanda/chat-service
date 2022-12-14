package com.twilio.chatservice.controller;

import com.twilio.chatservice.model.Group;
import com.twilio.chatservice.model.GroupMessage;
import com.twilio.chatservice.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GroupController {
    @Autowired
    GroupService groupService;

    @RequestMapping(value="/group/{groupId}", method= RequestMethod.POST)
    public void createGroup(@PathVariable(value = "groupId") Long id, @RequestBody String users) {
        groupService.createGroup(id, users);
    }

    @RequestMapping(value="/group/{groupId}", method= RequestMethod.GET)
    public Group getGroup(@PathVariable(value = "groupId") Long id) throws Exception {
        return groupService.getGroup(id);
    }

    @RequestMapping(value="/group/messages/{groupId}", method= RequestMethod.POST)
    public void postGroupMessage(@RequestBody  GroupMessage message) throws Exception {
        groupService.postMessage(message);
    }

    @RequestMapping(value="/group/messages/{groupId}", method= RequestMethod.GET)
    public List<GroupMessage> getGroupMessages(@PathVariable(value = "groupId") Long id) throws Exception {
        return groupService.getGroupMessages(id);
    }
}
