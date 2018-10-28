package com.example.josh.week1weekend_calculator;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Initialization
    String leftNumber = "", rightNumber = "", outputString = "";
    Boolean parenth = false, decimal = false;
    TextView tvOutput;
    Button btnClear, btnBackSpace, btnParenthesis, btnDivision, btnSeven, btnEight, btnNine, btnMultiply,
            btnFour, btnFive, btnSix, btnSubtract, btnOne, btnTwo, btnThree, btnAdd, btnPlusMinus,
            btnZero, btnDecimal, btnEquals, btnLn, btnSqrt, btnX2, btnPi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvOutput = findViewById(R.id.tvOutput);
        connectButtons();
    }

    //Used to append a negative value before a number
    public void plusOrMinus() {
        CharSequence outputText = tvOutput.getText();
        if (outputText.length() == 0) {
            tvOutput.append("-");
            return;
        }
        char lastChar = tvOutput.getText().charAt(outputText.length() - 1);
        if (lastChar != '-') {
            tvOutput.append("-");
        }

    }

    public void isNumber(char c) {
        double output;
        try {
            output = Double.parseDouble(tvOutput.getText().toString());
            if (c == 'l')
                output = Math.log(output);
            else if (c == 's')
                output = Math.sqrt(output);
            else if (c == 'x')
                output = output * output;

            tvOutput.setText(Double.toString(output));

        } catch (Exception e) {

        }
    }


    //Ensures that two operation signs cannot be used at the same time
    public boolean checkLastChar() {
        CharSequence outputText = tvOutput.getText();

        if (outputText.length() == 0) {
            return false;
        }
        char lastChar = tvOutput.getText().charAt(outputText.length() - 1);
        if (lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '÷')
            return false;
        else
            return true;
    }

    //Main Work Method of the Program
    //Recurses through parenthisis first then operates from left to right using helper function compute
    public String equals(String input) {
        int i = 0;
        while (i < input.length()) {

            if (input.indexOf("(") != -1) {
                int temp = input.indexOf(")");
                if (temp == -1) {
                    System.out.println("Must close paren");
                    return input;
                } else {
                    input = input.substring(0, input.indexOf("(")) + equals(input.substring(input.indexOf("(") + 1, temp))
                            + input.substring(temp + 1);
                }
            }
            if (input.charAt(i) == '+') {
                input = compute(input.substring(0, i), input.substring(i + 1, input.length()), '+');
                i = 1;
            } else if (input.charAt(i) == '-' && i != 0) {
                input = compute(input.substring(0, i), input.substring(i + 1, input.length()), '-');
                i = 1;
            } else if (input.charAt(i) == '÷') {
                input = compute(input.substring(0, i), input.substring(i + 1, input.length()), '÷');
                i = 1;
            } else if (input.charAt(i) == '*') {
                input = compute(input.substring(0, i), input.substring(i + 1, input.length()), '*');
                i = 1;
            }
            i++;
        }

        return input;
    }


    //Helper function that does the computation by splitting the two halves and computing their values
    public String compute(String left, String right, char operation) {
        double output = 0;
        String temp = right;
        int leftOver = 0;
        for (int i = 0; i < right.length(); i++) {
            if (right.charAt(i) == '+' || right.charAt(i) == '-' || right.charAt(i) == '*' || right.charAt(i) == '÷' || i == right.length() - 1) {
                if (right.charAt(i) == '-' && i == 0)
                    continue;
                leftOver = i;
                if (i != right.length() - 1) {
                    right = right.substring(0, i);
                }

                switch (operation) {
                    case '+':
                        output = Double.valueOf(left) + Double.valueOf(right);
                        break;
                    case '-':
                        output = Double.valueOf(left) - Double.valueOf(right);
                        break;
                    case '*':
                        output = Double.valueOf(left) * Double.valueOf(right);
                        break;
                    case '÷':
                        output = Double.valueOf(left) / Double.valueOf(right);
                        break;
                }
            }

        }
        if (leftOver == temp.length() - 1)
            return Double.toString(output);
        else
            return Double.toString(output) + temp.substring(leftOver, temp.length());

    }

    //Used to process the clicks of the various buttons, numbers are parsed and appended and math symbols are appended if helper function checkLastChar() returns true.
    @Override
    public void onClick(View v) {
        char lastChar = 'a';
        Button temp = findViewById(v.getId());
        String clicked = (String) temp.getText();
        try {
            Integer.parseInt(clicked);
            tvOutput.append(clicked);
        } catch (Exception e) {

            switch (v.getId()) {
                case R.id.btnClear:
                    tvOutput.setText("");
                    parenth = false;
                    break;
                case R.id.btnBackSpace:
                    CharSequence tempSub = tvOutput.getText();
                    if (tempSub.length() != 0) {
                        if (tempSub.charAt(tempSub.length() - 1) == ')') {
                            parenth = true;
                        } else if (tempSub.charAt(tempSub.length() - 1) == '(') {
                            parenth = false;
                        } else if (tempSub.charAt(tempSub.length() - 1) == '.') {
                            decimal = false;
                        }

                        tvOutput.setText(tempSub.subSequence(0, tempSub.length() - 1));
                    }
                    break;
                case R.id.btnParenthesis:
                    if (!parenth) {
                        if (!checkLastChar())
                            tvOutput.append("(");
                        else
                            tvOutput.append("*(");
                        parenth = true;
                    } else if (parenth && checkLastChar()) {
                        tvOutput.append(")");
                        parenth = false;
                    }
                    break;
                case R.id.btnDivision:
                    if (checkLastChar())
                        tvOutput.append("÷");
                    decimal = false;
                    break;
                case R.id.btnMultiply:
                    if (checkLastChar())
                        tvOutput.append("*");
                    decimal = false;
                    break;
                case R.id.btnSubtract:
                    if (checkLastChar())
                        tvOutput.append("-");
                    decimal = false;
                    break;
                case R.id.btnAdd:
                    if (checkLastChar())
                        tvOutput.append("+");
                    decimal = false;
                    break;
                case R.id.btnPlusMinus:
                    plusOrMinus();
                    break;
                case R.id.btnDecimal:
                    tvOutput.append(".");
                    decimal = true;
                    break;
                case R.id.btnEquals:
                    String output = tvOutput.getText().toString();
                    tvOutput.setText(equals(output));
                    break;
                case R.id.btnLn:
                    isNumber('l');
                    break;
                case R.id.btnX2:
                    isNumber('x');
                    break;
                case R.id.btnPi:
                    tvOutput.append("3.1415");
                    break;
                case R.id.btnSqrt:
                    isNumber('s');
                    break;

            }


        }

    }

    //Initializes the buttons and connects them to the interface
    public void connectButtons() {

        btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);
        btnBackSpace = findViewById(R.id.btnBackSpace);
        btnBackSpace.setOnClickListener(this);
        btnParenthesis = findViewById(R.id.btnParenthesis);
        btnParenthesis.setOnClickListener(this);
        btnDivision = findViewById(R.id.btnDivision);
        btnDivision.setOnClickListener(this);
        btnMultiply = findViewById(R.id.btnMultiply);
        btnMultiply.setOnClickListener(this);
        btnSubtract = findViewById(R.id.btnSubtract);
        btnSubtract.setOnClickListener(this);
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        btnPlusMinus = findViewById(R.id.btnPlusMinus);
        btnPlusMinus.setOnClickListener(this);
        btnDecimal = findViewById(R.id.btnDecimal);
        btnDecimal.setOnClickListener(this);
        btnEquals = findViewById(R.id.btnEquals);
        btnEquals.setOnClickListener(this);

        //Used to assign landscape buttons if phone is in landscape position.
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

            btnLn = findViewById(R.id.btnLn);
            btnLn.setOnClickListener(this);
            btnSqrt = findViewById(R.id.btnSqrt);
            btnSqrt.setOnClickListener(this);
            btnPi = findViewById(R.id.btnPi);
            btnPi.setOnClickListener(this);
            btnX2 = findViewById(R.id.btnX2);
            btnX2.setOnClickListener(this);
        }

        btnZero = findViewById(R.id.btnZero);
        btnZero.setOnClickListener(this);
        btnOne = findViewById(R.id.btnOne);
        btnOne.setOnClickListener(this);
        btnTwo = findViewById(R.id.btnTwo);
        btnTwo.setOnClickListener(this);
        btnThree = findViewById(R.id.btnThree);
        btnThree.setOnClickListener(this);
        btnFour = findViewById(R.id.btnFour);
        btnFour.setOnClickListener(this);
        btnFive = findViewById(R.id.btnFive);
        btnFive.setOnClickListener(this);
        btnSix = findViewById(R.id.btnSix);
        btnSix.setOnClickListener(this);
        btnSeven = findViewById(R.id.btnSeven);
        btnSeven.setOnClickListener(this);
        btnEight = findViewById(R.id.btnEight);
        btnEight.setOnClickListener(this);
        btnNine = findViewById(R.id.btnNine);
        btnNine.setOnClickListener(this);


    }

}
