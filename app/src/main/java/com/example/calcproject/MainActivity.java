package com.example.calcproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //pemdas, cant type in 2 digits on start, make double operations in general throw error
    private Button equals;
    private TextView display;

    private String equation;

    private boolean ifNewEq = false;

    private static DecimalFormat df = new DecimalFormat("0.00");

    public void setDisplay(String output)
    {
        display.setText(output);

    }

    public void setEquation(String equation)
    {
        boolean ifError = false;
        Log.d("here", equation+", "+this.equation+", "+ifNewEq);
        //checks for double operations and returns error if so
        if(equation.length()>2)
        {
            String lastElement = equation.substring(equation.length()-1);

            String secondToLastElement = equation.substring(equation.length()-2, equation.length()-1);

            if(lastElement.equals("+") || lastElement.equals("-") || lastElement.equals("*") || lastElement.equals("/")){

                if(secondToLastElement.equals("+") || secondToLastElement.equals("-") || secondToLastElement.equals("*") || secondToLastElement.equals("/")){
                    equation = "ERROR";
                    ifError = true;

                }
            }
        }

        if(!ifError)
        {
            //checks input for 0 in begin and gets rid fo the excess 0
            if(equation.length()>1 && equation.substring(0, 1).equals("0")) {
                if(!(equation.substring(1, 2).equals("+") || equation.substring(1, 2).equals("-") || equation.substring(1, 2).equals("*") || equation.substring(1, 2).equals("/")))
                    equation = equation.substring(1);
            }
        }

        //sets input to global variable
        this.equation = equation;
        setDisplay(equation);

        if(equation.equals("ERROR") || equation.equals("INFINITY"))
            this.equation = "0";

    }

    public boolean ifDecimal(Double d)
    {
        return d != Math.floor(d);

    }

    private Button zero,one, two, three, four, five, six, seven, eight, nine, add, subtract, multiply, divide, clear;
    //int num operation int num2 = output


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Numbers
        zero = findViewById(R.id.zero);
        zero.setOnClickListener(this);

        one = findViewById(R.id.button1);
        one.setOnClickListener(this);

        two = findViewById(R.id.button2);
        two.setOnClickListener(this);

        three = findViewById(R.id.button3);
        three.setOnClickListener(this);

        four = findViewById(R.id.button4);
        four.setOnClickListener(this);

        five = findViewById(R.id.button5);
        five.setOnClickListener(this);

        six = findViewById(R.id.button6);
        six.setOnClickListener(this);

        seven = findViewById(R.id.button7);
        seven.setOnClickListener(this);

        eight = findViewById(R.id.button8);
        eight.setOnClickListener(this);

        nine = findViewById(R.id.button9);
        nine.setOnClickListener(this);

        //Operations
        add = findViewById(R.id.button_add);
        add.setOnClickListener(this);

        subtract = findViewById(R.id.button_sub);
        subtract.setOnClickListener(this);

        clear = findViewById(R.id.button_clear);
        clear.setOnClickListener(this);

        multiply = findViewById(R.id.button_mult);
        multiply.setOnClickListener(this);

        divide = findViewById(R.id.button_div);
        divide.setOnClickListener(this);

        equals = findViewById(R.id.button_equals);
        equals.setOnClickListener(this);

        //TextViews
        display = findViewById(R.id.display);
        setEquation("0");


    }//pemdas
    public String doOrderOperation(boolean ifOrderOfOp) {
        StringTokenizer tokenizer = new StringTokenizer(equation, "+-*/", true);

        ArrayList<String> tokens = new ArrayList<>();
        while(tokenizer.hasMoreTokens())
            tokens.add(tokenizer.nextToken());

        String firstToken = tokens.get(0);
        if (firstToken.equals("-"))
            tokens.set(0, (-1 * Double.parseDouble(tokens.remove(1)))+"");

        String lastToken = tokens.get(tokens.size()-1);
        boolean ifError = false;
        if(lastToken.equals("+") || lastToken.equals("/") || lastToken.equals("-") || lastToken.equals("*"))
            ifError = true;

        if(!ifError)
        {
            while(tokens.size() > 2)
            {
                for(int i=0; i < tokens.size(); i++)
                {
                    Log.d("test", tokens.toString());

                    String token = tokens.get(i);
                    if(token.equals("*")) {
                        double firstNumber = Double.parseDouble(tokens.remove(i-1));
                        double secondNumber = Double.parseDouble(tokens.remove(i));
                        tokens.set(i-1, firstNumber * secondNumber+"");
                        i=0;

                    } else if(token.equals("/")) {
                        double firstNumber = Double.parseDouble(tokens.remove(i-1));
                        double secondNumber = Double.parseDouble(tokens.remove(i));

                        if(secondNumber == 0) {
                            return "INFINITY";
                        }
                        tokens.set(i-1, firstNumber / secondNumber+"");
                        i=0;
                    }
                }

                for(int i=0; i < tokens.size(); i++)
                {
                    String token = tokens.get(i);
                    if(token.equals("+")) {

                        double firstNumber = Double.parseDouble(tokens.remove(i-1));
                        double secondNumber = Double.parseDouble(tokens.remove(i));
                        tokens.set(i-1, firstNumber + secondNumber+"");

                    } else if(token.equals("-")) {
                        double firstNumber = Double.parseDouble(tokens.remove(i-1));
                        double secondNumber = Double.parseDouble(tokens.remove(i));
                        tokens.set(i-1, firstNumber - secondNumber+"");
                    }
                }
            }


            if (ifDecimal(Double.parseDouble(tokens.get(0)))) {
                //Formatting decimal
                return df.format(Double.parseDouble(tokens.get(0))) + "";
            } else {
                //double --> int
                int formattedResult = (int)Double.parseDouble(tokens.get(0));
                return formattedResult + "";
            }

        }
        return "ERROR";
    }

    public String doOperation()
    {
        boolean ifDivZero = false;
        boolean isDivZeroError = false;
        StringTokenizer tokenizer = new StringTokenizer(equation, "+-*/", true);
        double result = 0;

        String firstToken = tokenizer.nextToken();
        if(firstToken.equals("+"))
            result = Double.parseDouble(tokenizer.nextToken());
        //if token ia -, second token becomes -1
        else if(firstToken.equals("-"))
            result = -1*Double.parseDouble(tokenizer.nextToken());

        else
            result = Double.parseDouble(firstToken);//turns string into double

        String currentOperation = "";
        boolean ifError = false;

        while(tokenizer.hasMoreTokens())
        {
            String token = tokenizer.nextToken();

            if(token.equals("+")){
                currentOperation = "+";
            }
            else if(token.equals("-")){
                currentOperation = "-";
            }
            else if(token.equals("*")){
                currentOperation = "*";
            }
            else if(token.equals("/")) {
                currentOperation = "/";
            }
            else{//if next token is not operation, it is number and can be +, -, *, or /
                int number = Integer.parseInt(token);//turns next token into number bc it is in string form
                switch(currentOperation){
                    case "+":
                        result += number;
                        break;

                    case "-":
                        result -= number;
                        break;

                    case "*":
                        result *= number;
                        break;

                    case "/":
                        if(number==0) {
                            ifError = true;
                            isDivZeroError = true;
                        }
                        else
                            result /= number;
                        break;

                }
                currentOperation = "";//set back if its not an operation
            }
        }
        if(!currentOperation.isEmpty())
            ifError = true;//if its not empty, it never operated

        if(!ifError){//if correct return as decimal forma or int format

            if(ifDecimal(result)){
                return df.format(result)+"";
            }
            else
            {
                //double -> int
                int formattedResult = (int)result;
                return formattedResult+"";

            }
        }
        else {
            if (isDivZeroError)
                return "INFINITY";

            else
                return "ERROR";
        }


    }

    @Override
    public void onClick(View v) {
        Log.d("here", ifNewEq+"");
        switch (v.getId())
        {

            case R.id.button_add:
                setEquation(equation + "+");

                ifNewEq = false;
                break;

            case R.id.button_sub:
                setEquation(equation + "-");
                ifNewEq = false;
                break;

            case R.id.button_mult:
                setEquation(equation + "*");
                ifNewEq = false;
                break;

            case R.id.button_div:
                setEquation(equation + "/");
                ifNewEq = false;
                break;

            case R.id.button_clear:
                setDisplay("0");
                setEquation("0");
                break;

            case R.id.button_equals:
                setEquation(doOrderOperation(true));
                ifNewEq = true;
                break;

            case R.id.zero:
                if(ifNewEq) {
                    setEquation("0");
                    ifNewEq=false;
                }
                else
                    setEquation(equation + "0");
                break;

            case R.id.button1:
                if(ifNewEq) {
                    setEquation("1");
                    ifNewEq = false;
                }
                else
                    setEquation(equation + "1");
                break;

            case R.id.button2:
                if(ifNewEq) {
                    setEquation("2");
                    ifNewEq = false;
                }
                else
                    setEquation(equation + "2");
                break;

            case R.id.button3:
                if(ifNewEq) {
                    setEquation("3");
                    ifNewEq = false;
                }
                else
                    setEquation(equation + "3");
                break;

            case R.id.button4:
                if(ifNewEq) {
                    setEquation("4");
                    ifNewEq = false;
                }

                else
                    setEquation(equation + "4");
                break;

            case R.id.button5:
                if(ifNewEq) {
                    setEquation("5");
                    ifNewEq = false;
                }
                else
                    setEquation(equation + "5");
                break;

            case R.id.button6:
                if(ifNewEq) {
                    setEquation("6");
                    ifNewEq = false;
                }
                else
                    setEquation(equation + "6");
                break;

            case R.id.button7:
                if(ifNewEq) {
                    setEquation("7");
                    ifNewEq = false;
                }
                else
                    setEquation(equation + "7");
                break;

            case R.id.button8:
                if(ifNewEq) {
                    setEquation("8");
                    ifNewEq = false;
                }
                else
                    setEquation(equation + "8");
                break;

            case R.id.button9:
                if(ifNewEq) {
                    setEquation("9");
                    ifNewEq = false;
                }
                else
                    setEquation(equation + "9");
                break;

        }

    }
}
	
