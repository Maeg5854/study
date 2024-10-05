package org.mandy.tobi.user.domain;

import org.mandy.tobi.dao.UserDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    UserDao userDao;
    UserLevelUpgradePolicy levelUpgradePolicy;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
    public void setLevelUpgradePolicy(UserLevelUpgradePolicy levelUpgradePolicy) {
        this.levelUpgradePolicy = levelUpgradePolicy;
    }

    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            if (levelUpgradePolicy.canUpgradeLevel(user)) {
                levelUpgradePolicy.upgradeLevel(user);
            }
        }
    }

    public void add(User user) {
        if (user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);

    }
}
