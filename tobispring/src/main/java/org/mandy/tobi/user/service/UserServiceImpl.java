package org.mandy.tobi.user.service;

import org.mandy.tobi.dao.UserDao;
import org.mandy.tobi.user.domain.Level;
import org.mandy.tobi.user.domain.User;
import org.mandy.tobi.user.domain.UserLevelUpgradePolicy;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private MailSender mailSender;
    UserDao userDao;
    UserLevelUpgradePolicy levelUpgradePolicy;

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }
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
                upgradeLevel(user);
            }
        }
    }

    protected void upgradeLevel(User user) {
        levelUpgradePolicy.upgradeLevel(user);
        userDao.update(user);
        sendUpgradeEMail(user);
    }

    private void sendUpgradeEMail(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("useradmin@ksug.org");
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText("사용자님의 등급이 "+user.getLevel().name() + "로 업그레이드되었습니다.");

        mailSender.send(mailMessage);
    }

    public void add(User user) {
        if (user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);

    }
}
