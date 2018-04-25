import main.Calculator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IOControllerTest {
    @Test
    void testFormat() {
        Calculator calc = new Calculator(6);
        assertEquals(calc.format(12.345), "12.345");
        assertEquals(calc.format(12.3456), "1.23E1");
        assertEquals(calc.format(1.23456), "1.2346");
        assertEquals(calc.format(12), "12");
        assertEquals(calc.format(123456), "123456");
        assertEquals(calc.format(1234567), "1.23E6");
    }
    @Test
    void testDotInput(){
        Calculator calc = new Calculator(6);
        assertEquals(calc.formatValue(), "0");
        calc.addDigit(1);
        calc.addDigit(2);
        assertEquals(calc.formatValue(), "12");
        calc.addDot();
        assertEquals(calc.formatValue(), "12.");
        calc.addDigit(3);
        calc.addDigit(4);
        assertEquals(calc.formatValue(), "12.34");
        calc.addDot();
        assertEquals(calc.formatValue(), "12.34");
    }
    @Test
    void testBackspace(){
        Calculator calc = new Calculator(6);
        calc.addDigit(1);
        calc.addDigit(2);
        calc.addDot();
        calc.addDigit(3);
        calc.backspace();
        assertEquals(calc.formatValue(), "12.");
        calc.addDigit(3);
        // Not 12.03 or sth like
        assertEquals(calc.formatValue(), "12.3");
        calc.backspace();
        calc.backspace();
        assertEquals(calc.formatValue(), "12");
        calc.addDigit(3);
        assertEquals(calc.formatValue(), "123");
        calc.backspace();
        calc.backspace();
        assertEquals(calc.formatValue(), "1");
        calc.backspace();
        assertEquals(calc.formatValue(), "0");
        calc.backspace();
        assertEquals(calc.formatValue(), "0");
    }
    @Test
    void testDotInputNotOverflowsSize(){
        // There's a problem when we enter numbers like 12345.| and then next symbol
        // Length check can decide, that length of 12345. is 5, and we suddenly get exponential view during input
        Calculator calc = new Calculator(6);
        calc.addDigit(1);
        calc.addDigit(2);
        calc.addDigit(3);
        calc.addDigit(4);
        calc.addDigit(5);
        calc.addDot();
        // Should have no effect
        calc.addDigit(6);
        assertEquals(calc.formatValue(), "12345.");
        calc.backspace();
        assertEquals(calc.formatValue(), "12345");
        calc.addDigit(6);
        assertEquals(calc.formatValue(), "123456");
        //Should have no effect
        calc.addDigit(7);
        assertEquals(calc.formatValue(), "123456");
        //Should have no effect
        calc.addDot();
        assertEquals(calc.formatValue(), "123456");
        calc.backspace();
        assertEquals(calc.formatValue(), "12345");
    }
}