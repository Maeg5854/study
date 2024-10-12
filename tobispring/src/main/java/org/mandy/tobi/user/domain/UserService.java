package org.mandy.tobi.user.domain;

import org.mandy.tobi.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.util.List;

@Service
public class UserService {
    @Autowired
    DataSource dataSource;

    private MailSender mailSender;
    UserDao userDao;
    UserLevelUpgradePolicy levelUpgradePolicy;

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;}
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
    public void setLevelUpgradePolicy(UserLevelUpgradePolicy levelUpgradePolicy) {
        this.levelUpgradePolicy = levelUpgradePolicy;
    }

    public void upgradeLevels() throws Exception {
        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());


        try {
            List<User> users = userDao.getAll();
            for (User user : users) {
                if (levelUpgradePolicy.canUpgradeLevel(user)) {
                    upgradeLevel(user);
                }
            }
            transactionManager.commit(status);
        } catch(Exception e) {
            transactionManager.rollback(status);
            throw e;
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
