package org.mandy.chap1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

public class UserDaoTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId(1);
        user.setName("김혜지");
        user.setPassword("맨디맨디");
        dao.add(user);

        System.out.println(user.getId() + " 등록성공");

        User user1 = dao.get(user.getId());
        System.out.println(user1.getName());
        System.out.println(user1.getPassword());
        System.out.println(user1.getId() + " 조회성공");
    }
}
