package main;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Calculator implements InputProcessor, OutputProducer {
    /**
     * Type of data, currently displayed to user
     */
    private enum Displayed {
        /**
         * When numbered is entered, user can see it in WYSIWYG mode
         */
        UserInput,
        /**
         * Operation results and previously entered arguments are displayed from the cache
         */
        Cache,
        /**
         * Math error happened and displayed to user
         */
        ErrorMessage
    }
    private static ArrayList<String> bOpList = new ArrayList<>(Arrays.asList("+","-","*","/"));
    private static int DEFAULT_WIDTH = 15;
    private int outputWidth;

    private double cached = 0;
    private EnteredNumber entered;

    private DecimalFormat doubleFormat;
    private DecimalFormat expFormat;

    private String selectedOperation = null;

    private Displayed displayed = Displayed.UserInput;

    private String error = null;

    public Calculator(int width){
        outputWidth = width;

        String doubleMask = String.join("", Collections.nCopies(width-2, "#"));
        doubleFormat = new DecimalFormat("#."+doubleMask);
        String expMask = String.join("", Collections.nCopies(outputWidth-4, "#"));
        expFormat = new DecimalFormat("#."+expMask+"E0");

        entered = new EnteredNumber(doubleFormat, width);
    }
    public Calculator(){
        this(DEFAULT_WIDTH);
    }
    public void addDigit(int digit){
        if(displayed == Displayed.ErrorMessage)
            return;
        displayed = Displayed.UserInput;
        entered.addDigit(digit);
    }
    public void addDot(){
        if(displayed != Displayed.UserInput)
            return;
        entered.addDot();
    }
    public void backspace(){
        if(displayed != Displayed.UserInput)
            return;
        entered.backspace();
    }
    public void changeSign(){
        if(displayed == Displayed.UserInput) {
            entered.changeSign();
        }else if(displayed == Displayed.Cache && selectedOperation == null){
            cached = -cached;
        }
    }
    public void count(){
        if(selectedOperation == null) return;
        double secondArg = (displayed == Displayed.UserInput) ? entered.getValue() : cached;
        entered.resetValue();

        makeBinaryOperation(selectedOperation, secondArg);
        selectedOperation = null;
        displayed = (error == null) ? Displayed.Cache : Displayed.ErrorMessage;
    }
    public double getValue(){
        switch (displayed){
            case UserInput:       return entered.getValue();
            case ErrorMessage:    return 0;
            case Cache:           return cached;
        }
        return 0;
    }
    public String format(double value){
        String res1 = doubleFormat.format(value);
        if(res1.length() > outputWidth || res1.equals("0") && value != 0){
            return expFormat.format(value);
        }
        else return res1;
    }
    public String formatValue(){
        switch (displayed){
            case UserInput:    return entered.formatValue();
            case ErrorMessage: return "ERROR: "+error;
            case Cache:        return format(cached);
        }
        return "";
    }
    private boolean isBinaryOp(String op){
        return bOpList.contains(op);
    }
    private boolean isUnaryOp(String op){
        return op.equals("√");
    }
    private void makeBinaryOperation(String sign, double current){
        switch (sign) {
            case "+":
                cached += current;
                break;
            case "-":
                cached -= current;
                break;
            case "*":
                cached *= current;
                break;
            case "/":
                if(current != 0) cached /= current;
                else error = "ZERO_DIV";
                break;
        }
    }
    private void makeBinaryOperation(String sign){
        double current = entered.getValue();
        entered.resetValue();
        makeBinaryOperation(sign, current);
    }
    private void makeUnaryOperation(String sign){
        if(sign.equals("√")){
            if(cached >= 0) cached = Math.sqrt(cached);
            else{
                error = "SQRT_NEG";
            }
        }
    }
    public void resetValue(){
        entered.resetValue();
        selectedOperation = null;
        error = null;
        displayed = Displayed.UserInput;
    }
    public void selectOperation(String sign){
        if(isUnaryOp(sign)) switch (displayed){
            case UserInput:
                if(selectedOperation == null){
                    cached = entered.getValue();
                    entered.resetValue();
                }
                else makeBinaryOperation(selectedOperation);

                makeUnaryOperation(sign);
                selectedOperation = null;
                displayed = Displayed.Cache;
            break;
            case Cache:
                makeUnaryOperation(sign);
                selectedOperation = null;
            break;
        }
        else switch (displayed){
            case UserInput:
                if(selectedOperation == null){
                    cached = entered.getValue();
                    entered.resetValue();
                }
                else makeBinaryOperation(selectedOperation);

                selectedOperation = sign;
                displayed = Displayed.Cache;
            break;
            case Cache:
                selectedOperation = sign;
            break;
        }
        if(error != null) displayed=Displayed.ErrorMessage;
    }
}
