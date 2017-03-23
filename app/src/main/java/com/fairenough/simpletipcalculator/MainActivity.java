package com.fairenough.simpletipcalculator;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {

    private double tip = 0.18;
    private double bill = 0;
    private int headCount = 1;
    private DecimalFormat df;
    private boolean doRounding = false;
    private TextView billEachTV;
    private TextView tipTotalTV;
    private TextView billTotalTV;
    private TextView extraPenniesTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up decimal formatter
        df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.DOWN);

        //setting up all fields
        billEachTV  = (TextView)findViewById(R.id.billEachNum);
        tipTotalTV  = (TextView)findViewById(R.id.tipTotalNum);
        billTotalTV = (TextView)findViewById(R.id.billTotalNum);
        extraPenniesTV = (TextView) findViewById(R.id.extraPennies);
        final TextView billEachTitle = (TextView)findViewById(R.id.billEachText);

        //setting up toolbar
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        //end toolbar

        //rounding switch
        final Switch rounding = (Switch) findViewById(R.id.roundSwitch);
        rounding.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                doRounding = isChecked;
                UpdateTotals();
            }
        });

        //headcount number picker
        final NumberPicker hcp = (NumberPicker) findViewById(R.id.headCountPicker);
        hcp.setMinValue(1);
        hcp.setMaxValue(10);
        hcp.setWrapSelectorWheel(false);
        hcp.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                headCount = newVal;
                if (headCount == 1 && billEachTV.getVisibility() == View.VISIBLE){
                    billEachTV.setVisibility(View.INVISIBLE);
                    billEachTitle.setVisibility(View.INVISIBLE);
                }
                else if (headCount > 1 && billEachTV.getVisibility() == View.INVISIBLE){
                    billEachTV.setVisibility(View.VISIBLE);
                    billEachTitle.setVisibility(View.VISIBLE);
                }
                UpdateTotals();
            }
        });

        //tip amount number picker
        final String[] displayedTipAmounts = {"18", "20", "25", "30"};
        final NumberPicker tpp = (NumberPicker) findViewById(R.id.tipPercentPicker);
        tpp.setMinValue(0);
        tpp.setMaxValue(displayedTipAmounts.length-1);
        tpp.setDisplayedValues(displayedTipAmounts);
        tpp.setWrapSelectorWheel(false);
        tpp.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                tip = Double.parseDouble(displayedTipAmounts[newVal]) / 100;
                UpdateTotals();
            }
        });




        //setting up bill text field
        final EditText billField = (EditText) findViewById(R.id.billField);
        billField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    bill = Double.parseDouble(billField.getText().toString());
                }
                catch(NullPointerException|NumberFormatException ex){
                    bill = 0;
                }

                UpdateTotals();
            }

            @Override
            public void afterTextChanged(Editable s) {
                UpdateTotals();
            }
        });
        //end bill text field
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_rate:
                Intent rateMe = new Intent(Intent.ACTION_VIEW);
                rateMe.setData(Uri.parse("market://details?id=com.fairenough.simpletipcalculator"));
                if (!TryStartActivity(rateMe)) Toast.makeText(this, "Could not open Android Market. Sorry!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_share:
                Intent shareMe = new Intent();
                shareMe.setAction(Intent.ACTION_SEND);
                shareMe.putExtra(Intent.EXTRA_TEXT, "This is my test text, plus a link: market://details?id=com.fairenough.simpletipcalculator");
                shareMe.setType("text/plain");
                startActivity(Intent.createChooser(shareMe, getResources().getText(R.string.shareSendText)));
                break;
            default:
                break;
        }

        return true;
    }

    private boolean TryStartActivity(Intent thisIntent){
        try{
            startActivity(thisIntent);
            return true;
        }
        catch (ActivityNotFoundException ex){
            return false;
        }
    }

    double roundUp(double input){
        int intInput = (int) Math.ceil(input);
        while (intInput % headCount != 0){
            intInput++;
        }
        return (double) intInput;
//        int roundTo;
//        if (input < 10) roundTo = 1;
//        else roundTo = 5;
//        return Math.ceil(input/roundTo) * roundTo;
    }

    void UpdateTotals(){
        double billEach;
        double tipTotal;
        double billTotal;
        if (doRounding){
            //TODO
            tipTotal = tip * bill;
            billTotal = bill + tipTotal;
            billTotal = roundUp(billTotal);
            tipTotal = billTotal - bill;
            billEach = billTotal / headCount;
            //replace this, just wanted to suppress a warning
        } else {
            tipTotal = tip * bill;
            billTotal = bill + tipTotal;
            billEach = billTotal / headCount;


        }
        double penniesEach = Math.floor(billEach * 100);
        double penniesTotal = Math.floor(billTotal * 100);
        int pennies = findExtraPennies((int)penniesEach, (int)penniesTotal);
        String penniesString = "";
        if (pennies > 0) {
            if (pennies == 1){
                penniesString = getResources().getText(R.string.penniesPt1) + String.valueOf(pennies) + " " + getResources().getText(R.string.pennyPt2);
            }
            else {
                penniesString = getResources().getText(R.string.penniesPt1) + String.valueOf(pennies) + " " + getResources().getText(R.string.penniesPt2);
            }
        }
        extraPenniesTV.setText(penniesString);
        billEachTV.setText(df.format(billEach));
        tipTotalTV.setText(df.format(tipTotal));
        billTotalTV.setText(df.format(billTotal));

    }

    int findExtraPennies(int billEach, int billTotal){
        return billTotal - (billEach * headCount);
    }
}
