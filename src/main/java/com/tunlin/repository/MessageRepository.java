package com.tunlin.repository;

import com.tunlin.modal.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {

    List<Message> findByChatIdOrderByCreatedAtAsc(Long chatId);

}
