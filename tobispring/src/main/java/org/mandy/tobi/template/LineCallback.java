package org.mandy.tobi.template;

public interface LineCallback<T> {
    T doSomethingWithLine(String line, T value);
}
