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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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
    private TextView roundedTipTV;
    private TextView roundedPercentTitle;
    private TextView billEachTitle;
    private Animation fadeIn;
    private Animation fadeOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up decimal formatter
        df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.DOWN);

        //setting up animations
        fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(1000);
        fadeIn.setFillAfter(true);
        fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(1000);
        fadeOut.setFillAfter(true);



        //setting up all fields
        billEachTV  = (TextView)findViewById(R.id.billEachNum);
        tipTotalTV  = (TextView)findViewById(R.id.tipTotalNum);
        billTotalTV = (TextView)findViewById(R.id.billTotalNum);
        extraPenniesTV = (TextView) findViewById(R.id.extraPennies);
        roundedTipTV = (TextView) findViewById(R.id.roundedPercent);
        billEachTitle = (TextView)findViewById(R.id.billEachText);
        roundedPercentTitle = (TextView) findViewById(R.id.actualRoundedTipText);

        //setting up toolbar
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        //end toolbar

        //set initial visibilities for optional TextViews
//        CheckFadedStateSplitBill(headCount, headCount);
//        CheckFadedStateRounding(doRounding);

        //rounding switch
        final Switch rounding = (Switch) findViewById(R.id.roundSwitch);
        rounding.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                doRounding = isChecked;
                CheckFadedStateRounding(isChecked);
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
                CheckFadedStateSplitBill(oldVal, newVal);
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
                LinkToRatingPage();
                break;
            case R.id.action_share:
                ShareTheApp();
                break;
            default:
                break;
        }
        return true;
    }

    private void LinkToRatingPage(){
        Intent rateMe = new Intent(Intent.ACTION_VIEW);
        rateMe.setData(Uri.parse("market://details?id=com.fairenough.simpletipcalculator"));
        if (!TryStartActivity(rateMe)) Toast.makeText(this, "Could not open Android Market. Sorry!", Toast.LENGTH_SHORT).show();
    }

    private void ShareTheApp(){
        Intent shareMe = new Intent();
        shareMe.setAction(Intent.ACTION_SEND);
        shareMe.putExtra(Intent.EXTRA_TEXT, "Check out this tip calculator! https://play.google.com/store/apps/details?id=com.fairenough.simpletipcalculator");
        shareMe.setType("text/plain");
        startActivity(Intent.createChooser(shareMe, getResources().getText(R.string.shareSendText)));
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

    private void CheckFadedStateRounding(boolean rounded){
        if (rounded){
            roundedTipTV.startAnimation(fadeIn);
            roundedPercentTitle.startAnimation(fadeIn);
            extraPenniesTV.startAnimation(fadeOut);

        }
        else {
            roundedTipTV.startAnimation(fadeOut);
            roundedPercentTitle.startAnimation(fadeOut);
            extraPenniesTV.startAnimation(fadeIn);

        }
    }

    private void CheckFadedStateSplitBill(int oldVal, int newVal){

        if (newVal == 1){
            billEachTV.startAnimation(fadeOut);
            billEachTitle.startAnimation(fadeOut);

        }
        else if (newVal > 1 && oldVal == 1){
            billEachTV.startAnimation(fadeIn);
            billEachTitle.startAnimation(fadeIn);
        }
    }

    double roundUp(double input){
        int intInput = (int) Math.ceil(input);
        while (intInput % headCount != 0){
            intInput++;
        }
        return (double) intInput;
    }

    void UpdateTotals(){
        if (doRounding){
            CalculateAndDisplayTotalsWithRounding();
        } else {
            CalculateAndDisplayTotals();
        }
    }

    void CalculateAndDisplayTotalsWithRounding(){
        double tipTotal = tip * bill;
        double billTotal = bill + tipTotal;
        billTotal = roundUp(billTotal);
        tipTotal = billTotal - bill;
        double billEach = billTotal / headCount;
        CalculateAndDisplayRoundedPercentage(tipTotal);
        DisplayTotals(billEach, tipTotal, billTotal);
    }
    void CalculateAndDisplayRoundedPercentage(double tipTotal){
        int roundedTipPercent = (int) Math.round((tipTotal / bill) * 100);
        String roundedTipString = roundedTipPercent + "%";
        roundedTipTV.setText(roundedTipString);
    }

    void CalculateAndDisplayTotals(){
        double tipTotal = tip * bill;
        double billTotal = bill + tipTotal;
        double billEach = billTotal / headCount;
        CalculateAndDisplayPennies(billEach, billTotal);
        DisplayTotals(billEach, tipTotal, billTotal);
    }

    void CalculateAndDisplayPennies(double billEach, double billTotal){
        double penniesEach = Math.floor(billEach * 100);
        double penniesTotal = Math.floor(billTotal * 100);
        int pennies = (int)penniesTotal - ((int)penniesEach * headCount);
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
    }

    void DisplayTotals(double billEach, double tipTotal, double billTotal){
        billEachTV.setText(df.format(billEach));
        tipTotalTV.setText(df.format(tipTotal));
        billTotalTV.setText(df.format(billTotal));
    }

}
