package com.makey.telegram.services;

import com.makey.telegram.models.Message;

import java.util.List;

public interface MessageService {
    void save(Message message);
    void delete(Message message);
    List<Message> getAll();
    Message getById(Integer id);
}
