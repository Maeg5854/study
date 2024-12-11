package org.mandy.tobi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationContext.class)
public class GroupDaoTest {

    @Autowired
    private org.springframework.context.ApplicationContext context;

    @Test
    public void test() {
        System.out.println(context);
        System.out.println(this);
    }
}
