package org.mandy.tobi.user.domain;


public enum Level {
    GOLD(3, null), SILVER(2, GOLD), BASIC(1, SILVER);

    private final int value;
    private final Level nextLevel;

    Level(int value, Level nextLevel) {
        this.value = value;
        this.nextLevel = nextLevel;
    }

    public int intValue() {
        return value;
    }

    public Level nextLevel() {
        return this.nextLevel;
    }

    public static Level valueOf(int value) {
        for(Level lvl : Level.values()) {
            if (lvl.intValue() == value) {
                return lvl;
            }
        }
        throw new AssertionError("Unknown value: " + value);
    }
}
