package org.mandy.tobi.user.service;

import org.mandy.tobi.user.domain.User;

public class TestUserService extends UserServiceImpl {
    private int id;
    public TestUserService(int id) {
        this.id = id;
    }

    protected void upgradeLevel(User user) {
        if (user.getId() == this.id) {
            throw new TestUserServiceException();
        }
        levelUpgradePolicy.upgradeLevel(user);
    }
}
