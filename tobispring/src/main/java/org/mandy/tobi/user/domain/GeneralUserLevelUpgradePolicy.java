package org.mandy.tobi.user.domain;

import org.mandy.tobi.dao.UserDao;
import org.springframework.stereotype.Component;

@Component
public class GeneralUserLevelUpgradePolicy implements UserLevelUpgradePolicy {
    public static final int MIN_LOGIN_COUNT_FOR_SILVER = 50;
    public static final int MIN_RECCOMEND_COUNT_FOR_GOLD = 30;

    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean canUpgradeLevel(User user) {
        Level level = user.getLevel();
        switch (level) {
            case BASIC :
                return (user.getLoginCount() >= MIN_LOGIN_COUNT_FOR_SILVER);
            case SILVER: ;
                return (user.getRecommendCount() >= MIN_RECCOMEND_COUNT_FOR_GOLD);
            case GOLD:
                return false;
            default :
                throw new IllegalArgumentException("Unknown Level: " + level); // 새로운 등급이 추가 됐을 때 에외로 알아차릴 수 있음.
        }
    }

    @Override
    public void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }
}
