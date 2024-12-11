package org.mandy.tobi.learn.factorybean;

public class Pepper {
    String flavor;

    private Pepper(String flavor) {
        this.flavor = flavor;
    }

    public String getFlavor() {
        return this.flavor;
    }

    public static Pepper newPepper(String flavor) {
        return new Pepper(flavor);
    }
}
