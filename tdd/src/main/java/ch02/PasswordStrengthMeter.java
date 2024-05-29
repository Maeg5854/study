package ch02;

import org.jetbrains.annotations.NotNull;

public class PasswordStrengthMeter {
    public PasswordStrength meter(String s) {
        if (s == null || s.equals("")) {
            return PasswordStrength.INVALID;
        }
        short metcounts = getMetCriteriaCount(s);

        if (metcounts <= 1) return PasswordStrength.WEAK;
        if (metcounts == 2 ) return PasswordStrength.NORMAL;

        return PasswordStrength.STRONG;
    }

    private short getMetCriteriaCount(String s) {
        short metcounts = 0;
        if(s.length() >= 8) metcounts++;
        if(isContainsUpperCase(s)) metcounts++;
        if(isContainsNum(s)) metcounts++;
        return metcounts;
    }

    private boolean isContainsNum(@NotNull String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) >= '0' && s.charAt(i) <= '9') {
                return true;
            }
        }
        return false;
    }

    private boolean isContainsUpperCase(@NotNull String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) >= 'A' && s.charAt(i) <= 'Z') {
                return true;
            }
        }
        return false;
    }
}
