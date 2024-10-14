package org.mandy.tobi.learn.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;

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

    @Test
    public void proxyFactoryBean() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());
        pfBean.addAdvice(new UppercaseAdvice());

        Hello proxyHello = (Hello) pfBean.getObject();
        assertThat(proxyHello.sayThankYou("Toby")).isEqualTo("THANK YOU TOBY");
        assertThat(proxyHello.sayHi("Toby")).isEqualTo("HI TOBY");
        assertThat(proxyHello.sayHello("Toby")).isEqualTo("HELLO TOBY");
    }

    static class UppercaseAdvice implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String ret = (String) invocation.proceed();
            return ret.toUpperCase();
        }
    }
}
