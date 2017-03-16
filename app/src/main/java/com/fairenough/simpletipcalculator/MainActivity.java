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
    private TextView calcTotal;
    private TextView tipAmount;
    private TextView tipText;
    DecimalFormat df = new DecimalFormat("0.00");
    String temp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calcTotal = (TextView) findViewById(R.id.calcTotal);
        tipAmount = (TextView) findViewById(R.id.tipAmount);
        tipText = (TextView) findViewById(R.id.tipText);

        UpdateTipText(R.string.fifteen);


        final Button zero = (Button) findViewById(R.id.Zero);
        zero.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                temp = calcTotal.getText().toString() + getString(R.string.zero);
                calcTotal.setText(temp);
                UpdateTip();
            }
        });

        final Button one = (Button) findViewById(R.id.One);
        one.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                temp = calcTotal.getText().toString() + getString(R.string.one);
                calcTotal.setText(temp);
                UpdateTip();
            }
        });

        final Button two = (Button) findViewById(R.id.Two);
        two.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                temp = calcTotal.getText().toString() + getString(R.string.two);
                calcTotal.setText(temp);
                UpdateTip();
            }
        });

        final Button three = (Button) findViewById(R.id.Three);
        three.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                temp = calcTotal.getText().toString() + getString(R.string.three);
                calcTotal.setText(temp);
                UpdateTip();
            }
        });

        final Button four = (Button) findViewById(R.id.Four);
        four.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                temp = calcTotal.getText().toString() + getString(R.string.four);
                calcTotal.setText(temp);
                UpdateTip();
            }
        });

        final Button five = (Button) findViewById(R.id.Five);
        five.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                temp = calcTotal.getText().toString() + getString(R.string.five);
                calcTotal.setText(temp);
                UpdateTip();
            }
        });

        final Button six = (Button) findViewById(R.id.Six);
        six.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                temp = calcTotal.getText().toString() + getString(R.string.six);
                calcTotal.setText(temp);
                UpdateTip();
            }
        });

        final Button seven = (Button) findViewById(R.id.Seven);
        seven.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                temp = calcTotal.getText().toString() + getString(R.string.seven);
                calcTotal.setText(temp);
                UpdateTip();
            }
        });

        final Button eight = (Button) findViewById(R.id.Eight);
        eight.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                temp = calcTotal.getText().toString() + getString(R.string.eight);
                calcTotal.setText(temp);
                UpdateTip();
            }
        });

        final Button nine = (Button) findViewById(R.id.Nine);
        nine.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                temp = calcTotal.getText().toString() + getString(R.string.nine);
                calcTotal.setText(temp);
                UpdateTip();
            }
        });

        final Button point = (Button) findViewById(R.id.Point);
        point.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                temp = calcTotal.getText().toString() + getString(R.string.point);
                calcTotal.setText(temp);
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
                UpdateTip();
                UpdateTipText(R.string.fifteen);
            }
        });

        final Button twentyTip = (Button) findViewById(R.id.twentyTip);
        twentyTip.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                tip = 0.2f;
                UpdateTip();
                UpdateTipText(R.string.twenty);
            }
        });

        final Button twentyfiveTip = (Button) findViewById(R.id.twentyfiveTip);
        twentyfiveTip.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                tip = 0.25f;
                UpdateTip();
                UpdateTipText(R.string.twentyfive);
            }
        });

    }

    void UpdateTipText(int id){
        String newTipText = getString(id) + " " + getString(R.string.tipTitle);
        tipText.setText(newTipText);
    }

    void UpdateTip(){
        if (tip == 0) tipAmount.setText("0");
        else {
            double cash;
            try {
                cash = Double.parseDouble(calcTotal.getText().toString());
            }
            catch (NullPointerException|NumberFormatException ex) {
                cash = 0;
            }
            String tipString = (df.format(cash * tip));
            tipAmount.setText(tipString, TextView.BufferType.SPANNABLE);
        }
    }
}
