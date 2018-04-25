package main;

import java.text.DecimalFormat;
import java.util.Collections;

public class EnteredNumber implements InputProcessor, OutputProducer {
    private DecimalFormat format;
    private double value = 0;
    private int width;

    private double inputMagnitude = 1;
    private boolean negative = false;
    private boolean pointInserted = false;

    public EnteredNumber(DecimalFormat format, int width){
        this.width = width;
        this.format = format;
    }
    public void addDigit(int digit){
        double newVal = (pointInserted) ? value + digit * inputMagnitude / 10 : 10*value+digit;
        // In case of input field overflow, button 'backspace' behavior will not be visualised to user
        // So check is required
        if(format.format(newVal).length() <= width){
            value = newVal;
            if(pointInserted) inputMagnitude /= 10;
        }
    }
    public void addDot(){
        // Trailing point is appended to output, so current output length should be checked
        if( !pointInserted && format.format(value).length() < width) {
            pointInserted = true;
        }
    }
    public void backspace(){
        //String tmp = format.format(value);
        String tmp = formatValue(true);

        if(pointInserted && inputMagnitude == 1){
            pointInserted = false;
        }
        // Number is not single digit
        else if(tmp.length() > 1){
            value = Double.parseDouble(tmp.substring(0, tmp.length()-1));
            if(pointInserted) inputMagnitude *= 10;
        }
        // Number is single digit
        else value = 0;
        // This check is separate, as number"-0." will be initially handled by the first check
        if(value == 0){
            negative = false;
        }
    }
    public void changeSign(){
        negative = !negative;
    }

    public String formatValue(){
        return formatValue(false);
    }
    public String formatValue(boolean absolute){
        double value = (absolute) ? Math.abs(getValue()) : getValue();

        String res = format.format(value);
        if(pointInserted && inputMagnitude == 1){
            res += ".";
        }else if(inputMagnitude < 1){
            int fractionLength = -(int) Math.log10(inputMagnitude);
            int dotPos = res.indexOf(".");
            if(dotPos == -1){
                res += ".";
                dotPos = res.length()-1;
            }
            int displayedFractionLength = (res.length() - dotPos - 1);
            String leftPad = String.join("", Collections.nCopies(
                    fractionLength - displayedFractionLength, "0"
            ));
            res += leftPad;
        }
        return res;
    }
    public double getValue(){
        return (negative) ? -value : value;
    }
    public void resetValue(){
        value = 0;
        inputMagnitude = 1;
        pointInserted = false;
        negative = false;
    }
}
