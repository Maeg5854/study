package org.mandy.tobi.learn.factorybean;

import org.springframework.beans.factory.FactoryBean;

public class PepperFactoryBean implements FactoryBean<Pepper> {
    String flavor;

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    @Override
    public Pepper getObject() throws Exception {
        return Pepper.newPepper(this.flavor);
    }

    @Override
    public Class<?> getObjectType() {
        return Pepper.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
