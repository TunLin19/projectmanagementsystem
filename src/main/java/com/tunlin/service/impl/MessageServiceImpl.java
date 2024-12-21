package com.tunlin.service.impl;

import com.tunlin.modal.Chat;
import com.tunlin.modal.Message;
import com.tunlin.modal.User;
import com.tunlin.repository.MessageRepository;
import com.tunlin.repository.UserRepository;
import com.tunlin.service.MessageService;
import com.tunlin.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    MessageRepository messageRepository;

    @Override
    public Message sendMessage(Long senderId, Long projectId, String content) throws Exception {

        User sender = userRepository.findById(senderId).orElseThrow(()->
                new Exception("User not found with id" + senderId));

        Chat chat = projectService.getProjectById(projectId).getChat();
        return null;
    }

    @Override
    public List<Message> getMessageByProjectId(Long projectId) throws Exception {
        return null;
    }
}
