package org.mandy.tobi.learn.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UppercaseHandler implements InvocationHandler {
    Object target;

    public UppercaseHandler(Object target) {
        this.target = target;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // target = 메소드 안에서 this에 해당되는 객체를 첫번째 인자로 전달한다.
        Object ret = method.invoke(target, args);
        if(ret instanceof String) {
            return ((String)ret).toUpperCase();
        }
        return ret;
    }
}
