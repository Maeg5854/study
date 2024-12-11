package org.mandy.tobi.learn.factorybean;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class FactoryBeanTest {
    @Autowired
    ApplicationContext context;

    @Test
    public void getPepperFromFactory() {
        Object pepper = context.getBean("pepper");
        assertThat(pepper).isInstanceOf(Pepper.class);
        assertThat(((Pepper)pepper).getFlavor()).isEqualTo("violet");
    }
}
