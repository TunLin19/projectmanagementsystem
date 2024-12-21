package com.tunlin.service.impl;

import com.tunlin.modal.Chat;
import com.tunlin.repository.ChatRepository;
import com.tunlin.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Override
    public Chat createChat(Chat chat) {

        return chatRepository.save(chat);

    }

}
