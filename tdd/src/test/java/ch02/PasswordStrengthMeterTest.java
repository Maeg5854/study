package ch02;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PasswordStrengthMeterTest {
    PasswordStrengthMeter meter = new PasswordStrengthMeter();

    private void assertStrength(String s, PasswordStrength strong) {
        PasswordStrength 결과 = meter.meter(s);
        assertEquals(strong, 결과);
    }

    @Test
    void 암호가_모든_조건을_충족하면_암호_강도는_강함이어야함() {
        assertStrength("ab12!@AB", PasswordStrength.STRONG);
        assertStrength("abc1!Add", PasswordStrength.STRONG);
    }

    @Test
    void 길이_조건만_만족하지_못하면_암호_강도는_보통이어야함() {
        assertStrength("ab12!@A", PasswordStrength.NORMAL);
        assertStrength("AA00AA", PasswordStrength.NORMAL);
    }

    @Test
    void 숫자_포함_조건만_만족하지_못하면_암호_강도는_보통이어야함() {
        assertStrength("AAaaBBbb", PasswordStrength.NORMAL);
    }

    @Test
    void 값이_없는_경우_암호_강도는_유효하지않음이어야함 () {
        assertStrength(null, PasswordStrength.INVALID);
    }

    @Test
    void 빈_문자열_일_경우_암호_강도는_유효하지않음이어야함 () {
        assertStrength("", PasswordStrength.INVALID);
    }

    @Test
    void 대문자조건만_만족하지_못하면_암호강도는_보통이어야함() {
        assertStrength("aabb0000", PasswordStrength.NORMAL);
    }

    @Test
    void 길이조건만_충족할_경우_암호강도는_약함이어야함() {
        assertStrength("aabbccdd", PasswordStrength.WEAK);
    }

    @Test
    void 숫자포함조건만_충족할_경우_암호강도는_약함이어야함() {
        assertStrength("000", PasswordStrength.WEAK);
    }

    @Test
    void 대문자조건만_충족할_경우_암호강도는_약함이어야함() {
        assertStrength("AAA", PasswordStrength.WEAK);
    }
    @Test
    void 아무_조건도_충족하지_않는_경우_암호강도는_약함이어야함() {
        assertStrength("aaa", PasswordStrength.WEAK);
    }
}
