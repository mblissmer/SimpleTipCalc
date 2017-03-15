package com.fairenough.simpletipcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {

    private double tip = 0.15;
    private boolean goodTip = false;
    private EditText calcTotal;
    private EditText tipAmount;
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calcTotal = (EditText) findViewById(R.id.calcTotal);
        tipAmount = (EditText) findViewById(R.id.tipAmount);

        final Button zero = (Button) findViewById(R.id.Zero);
        zero.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                calcTotal.setText(calcTotal.getText() + "0");
                UpdateTip();
            }
        });

        final Button one = (Button) findViewById(R.id.One);
        one.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                calcTotal.setText(calcTotal.getText() + "1");
                UpdateTip();
            }
        });

        final Button two = (Button) findViewById(R.id.Two);
        two.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                calcTotal.setText(calcTotal.getText() + "2");
                UpdateTip();
            }
        });

        final Button three = (Button) findViewById(R.id.Three);
        three.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                calcTotal.setText(calcTotal.getText() + "3");
                UpdateTip();
            }
        });

        final Button four = (Button) findViewById(R.id.Four);
        four.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                calcTotal.setText(calcTotal.getText() + "4");
                UpdateTip();
            }
        });

        final Button five = (Button) findViewById(R.id.Five);
        five.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                calcTotal.setText(calcTotal.getText() + "5");
                UpdateTip();
            }
        });

        final Button six = (Button) findViewById(R.id.Six);
        six.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                calcTotal.setText(calcTotal.getText() + "6");
                UpdateTip();
            }
        });

        final Button seven = (Button) findViewById(R.id.Seven);
        seven.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                calcTotal.setText(calcTotal.getText() + "7");
                UpdateTip();
            }
        });

        final Button eight = (Button) findViewById(R.id.Eight);
        eight.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                calcTotal.setText(calcTotal.getText() + "8");
                UpdateTip();
            }
        });

        final Button nine = (Button) findViewById(R.id.Nine);
        nine.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                calcTotal.setText(calcTotal.getText() + "9");
                UpdateTip();
            }
        });

        final Button point = (Button) findViewById(R.id.Point);
        point.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                calcTotal.setText(calcTotal.getText() + ".");
                UpdateTip();
            }
        });

        final Button clr = (Button) findViewById(R.id.Clear);
        clr.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                calcTotal.setText("");
                UpdateTip();
            }
        });

        final Button fifteenTip = (Button) findViewById(R.id.fifteenTip);
        fifteenTip.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                tip = 0.15f;
                goodTip = false;
                UpdateTip();
            }
        });

        final Button twentyTip = (Button) findViewById(R.id.twentyTip);
        twentyTip.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                tip = 0.2f;
                goodTip = false;
                UpdateTip();
            }
        });

        final Button good = (Button) findViewById(R.id.goodTip);
        good.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                tip = 0.2f;
                goodTip = true;
                UpdateTip();
            }
        });

    }

    void UpdateTip(){
        if (tip == 0){
            tipAmount.setText("0");
        }
        else {
            double cash = 0;
            try {
                cash = Double.parseDouble(calcTotal.getText().toString());
            }
            catch (NullPointerException|NumberFormatException ex) {
            }

            if (!goodTip) {
                String tipString = (df.format(cash * tip));
                tipAmount.setText(tipString, TextView.BufferType.SPANNABLE);
            }
            else {

            }
        }

    }
}
