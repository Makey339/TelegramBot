package com.makey.telegram.services;

import com.makey.telegram.models.User;

import java.util.List;

public interface UserService {

    void save(User user);
    void delete(User user);
    List<User> getAll();
    User getById(long id);
}
