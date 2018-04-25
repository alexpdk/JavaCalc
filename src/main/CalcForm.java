package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CalcForm {
    private JTextField textField;
    private JPanel mainPanel;
    private JPanel grid;

    private static Object[] signs = new Object[]{
            "-", "+", "*", "/",
            7,   8,   9,   "√",
            4,   5,   6,   "±",
            1,   2,   3,   ".",
            0,   "←", "RM","="
    };

    private static Calculator calc = new Calculator(12);

    public static void main(String[] args) {
        JFrame frame = new JFrame("CalcForm");
        frame.setContentPane(new CalcForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        frame.setSize(360, 300);
        // center window
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        textField = new JTextField();
        textField.setBorder(BorderFactory.createLineBorder(Color.black));
        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                char keyChar = keyEvent.getKeyChar();
                if (keyChar == '\b') keyChar = '←';
                if (keyChar == '\n') keyChar = '=';
                Object sign = (keyChar - '0' >= 0 && keyChar - '0' < 10) ? keyChar - '0' : String.valueOf(keyChar);
                for(Object s : signs) if(s.equals(sign)){
                    handleButtonSign(sign);
                }
            }
            @Override
            public void keyPressed(KeyEvent keyEvent) {

            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });

        grid = new JPanel();
        grid.setLayout(new GridLayout(5, 0));

         for (Object sign : signs) {
            JButton button = new JButton(sign.toString());
            button.setFont(new Font("Arial Unicode MS", Font.PLAIN, 22));
            button.addActionListener((e)->{
                handleButtonSign(sign);
            });
            grid.add(button);
        }
    }

    private void handleButtonSign(Object sign){
        if(sign instanceof Integer) {
            calc.addDigit((Integer) sign);
        }
        else if(sign.equals(".")){
            calc.addDot();
        }
        else if(sign.equals("←")){
            calc.backspace();
        }
        else if(sign.equals("±")){
            calc.changeSign();
        }
        else if(sign.equals("RM")){
            calc.resetValue();
        }
        else if(sign.equals("=")){
            calc.count();
        }
        // math operations
        else{
            calc.selectOperation(sign.toString());
        }
        textField.setText(calc.formatValue());
    }
}
