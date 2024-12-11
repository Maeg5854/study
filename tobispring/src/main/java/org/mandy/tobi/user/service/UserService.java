package org.mandy.tobi.user.service;


import org.mandy.tobi.user.domain.User;

public interface UserService {
    void add(User user);
    void upgradeLevels();

}
