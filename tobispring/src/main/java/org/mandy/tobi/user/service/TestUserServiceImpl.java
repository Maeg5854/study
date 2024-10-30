package org.mandy.tobi.user.service;

import org.mandy.tobi.user.domain.User;

public class TestUserServiceImpl extends UserServiceImpl {
    private final int id = 4;

    public void upgradeLevel(User user) {
        if (user.getId() == this.id) {
            throw new TestUserServiceException();
        }
        levelUpgradePolicy.upgradeLevel(user);
        userDao.update(user);
    }
}
