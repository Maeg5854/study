package org.mandy.tobi.user.domain;

public class TestUserService extends UserService {
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
