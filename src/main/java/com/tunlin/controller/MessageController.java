package com.tunlin.controller;

import com.tunlin.modal.Chat;
import com.tunlin.modal.Message;
import com.tunlin.modal.User;
import com.tunlin.request.MessageRequest;
import com.tunlin.service.MessageService;
import com.tunlin.service.ProjectService;
import com.tunlin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody MessageRequest messageRequest) throws Exception {

        User user = userService.findUserById(messageRequest.getSenderId());

        Chat chat = projectService.getProjectById(messageRequest.getProjectId()).getChat();

        if (chat == null) throw new Exception("Chat not found");
        Message sentMessage = messageService.sendMessage(messageRequest.getSenderId(), messageRequest.getProjectId(),messageRequest.getContent());
        return ResponseEntity.ok(sentMessage);

    }

    @GetMapping("/chat/{projectId}")
    public ResponseEntity<List<Message>> getMessageByChatId(@PathVariable Long projectId) throws Exception {

        List<Message> messages = messageService.getMessageByProjectId(projectId);
        return ResponseEntity.ok(messages);

    }

}
