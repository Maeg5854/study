package org.mandy.tobi.learn.proxy;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;

public class HelloClientTest {

    @Test
    public void simpleProxy() {
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("Toby")).isEqualTo("Hello Toby");
        assertThat(hello.sayHi("Toby")).isEqualTo("Hi Toby");
        assertThat(hello.sayThankYou("Toby")).isEqualTo("Thank You Toby");

        Hello proxyHello = new HelloUppercase(new HelloTarget());
        assertThat(proxyHello.sayThankYou("Toby")).isEqualTo("THANK YOU TOBY");
        assertThat(proxyHello.sayHi("Toby")).isEqualTo("HI TOBY");
        assertThat(proxyHello.sayHello("Toby")).isEqualTo("HELLO TOBY");
    }

    @Test
    public void dynamicProxy() {
        Hello proxyHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{Hello.class},
                new UppercaseHandler(new HelloTarget())
        );
        assertThat(proxyHello.sayThankYou("Toby")).isEqualTo("THANK YOU TOBY");
        assertThat(proxyHello.sayHi("Toby")).isEqualTo("HI TOBY");
        assertThat(proxyHello.sayHello("Toby")).isEqualTo("HELLO TOBY");

    }
}
