package org.mandy.tobi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DaoFactory.class)
public class GroupDaoTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void test() {
        System.out.println(context);
        System.out.println(this);
    }
}
