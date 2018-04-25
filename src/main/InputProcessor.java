package main;

public interface InputProcessor {
    void addDigit(int digit);
    void addDot();
    void backspace();
    void changeSign();
    double getValue();
    void resetValue();
}
