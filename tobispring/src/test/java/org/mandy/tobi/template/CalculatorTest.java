package org.mandy.tobi.template;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class CalculatorTest {

    @Test
    public void sumOfNumbers() throws IOException {
        final String filename = "numbers.txt";
        Calculator calculator = new Calculator();
        int sum = calculator.calcSum(getClass().getResource(filename).getPath());

        assertThat(sum).isEqualTo(10);
    }

    @Test
    public void productOfNumbers() throws IOException {
        final String filename = "numbers.txt";
        Calculator calculator = new Calculator();
        int product = calculator.calcProduct(getClass().getResource(filename).getPath());

        assertThat(product).isEqualTo(24);
    }
}
