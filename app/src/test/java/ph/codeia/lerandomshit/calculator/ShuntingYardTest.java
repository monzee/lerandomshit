package ph.codeia.lerandomshit.calculator;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static ph.codeia.lerandomshit.calculator.ShuntingYard.Apply;
import static ph.codeia.lerandomshit.calculator.ShuntingYard.Push;

public class ShuntingYardTest {
    @Test
    public void simpleOperation() {
        ShuntingYard eq = new ShuntingYard(
                new Push(4),
                Apply.PLUS,
                new Push(6)
        );
        assertEquals(BigDecimal.TEN, eq.evaluate());
    }

    @Test
    public void leftToRight() {
        ShuntingYard eq = new ShuntingYard(
                new Push(2),
                Apply.PLUS,
                new Push(1),
                Apply.MINUS,
                new Push(2)
        );
        assertEquals(BigDecimal.ONE, eq.evaluate());
    }

    @Test
    public void mixedPrecedence() {
        ShuntingYard eq = new ShuntingYard(
                new Push(1),
                Apply.PLUS,
                new Push(2),
                Apply.TIMES,
                new Push(3)
        );
        assertEquals(BigDecimal.valueOf(7), eq.evaluate());
    }

    @Test
    public void longishEquation() {
        ShuntingYard eq = new ShuntingYard(
                new Push(100),
                Apply.PLUS,
                new Push(321),
                Apply.DIVIDE,
                new Push(3),
                Apply.MINUS,
                new Push(2156),
                Apply.PLUS,
                new Push(1032),
                Apply.TIMES,
                new Push(32),
                Apply.DIVIDE,
                new Push(4)
        );
        assertEquals(BigDecimal.valueOf(100 + 321 / 3 - 2156 + 1032 * 32 / 4), eq.evaluate());
    }
}