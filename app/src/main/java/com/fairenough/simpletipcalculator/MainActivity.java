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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.math.RoundingMode;
import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {

    private double tip = 0.2;
    private double bill = 0;
    private double tipTotal;
    private double billTotal;
    private double billEach;
    private int headCount = 1;
    private int oldCents = 0;
    private DecimalFormat df;
    private boolean doRounding = false;
    private TextView billEachTV, tipTotalTV, billTotalTV, extraCentsTV, roundedTipTV, roundedPercentTitle, billEachTitle;
    private Animation fadeRoundingIn, fadeRoundingOut, fadeBillEachIn, fadeBillEachOut, fadeCentsIn, fadeCentsOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitializeToolbar();
        ConfigureDecimalFormatter();
        ConfigureAnimations();
        InitializeTextFields();
        InitializeRoundingSwitch();
        InitializeHeadCountPicker();
        InitializeTipPercentPicker();
        InitializeBillTextField();
        SetInitialAlphas();
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

    private void InitializeToolbar(){
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
    }

    private void ConfigureDecimalFormatter(){
        df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.DOWN);
    }

    private void ConfigureAnimations(){
        fadeRoundingIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        fadeRoundingOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        fadeBillEachIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        fadeBillEachOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        fadeCentsIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        fadeCentsOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);
    }

    private void InitializeTextFields(){
        billEachTV  = (TextView)findViewById(R.id.billEachNum);
        tipTotalTV  = (TextView)findViewById(R.id.tipTotalNum);
        billTotalTV = (TextView)findViewById(R.id.billTotalNum);
        extraCentsTV = (TextView) findViewById(R.id.extraCents);
        roundedTipTV = (TextView) findViewById(R.id.roundedPercent);
        billEachTitle = (TextView)findViewById(R.id.billEachText);
        roundedPercentTitle = (TextView) findViewById(R.id.actualRoundedTipText);
    }

    private void SetInitialAlphas(){
        CheckFadedStateSplitBill(headCount, headCount);
        CheckFadedStateRounding(false);
        extraCentsTV.startAnimation(fadeCentsOut);
    }

    private void InitializeRoundingSwitch(){
        final Switch rounding = (Switch) findViewById(R.id.roundSwitch);
        rounding.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                doRounding = isChecked;
                CheckFadedStateRounding(isChecked);
                UpdateTotals();
            }
        });
    }

    private void InitializeHeadCountPicker(){
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
    }

    private void InitializeTipPercentPicker(){
        final String[] displayedTipAmounts = {"15", "18", "20", "25", "30"};
        final NumberPicker tpp = (NumberPicker) findViewById(R.id.tipPercentPicker);
        tpp.setMinValue(0);
        tpp.setMaxValue(displayedTipAmounts.length-1);
        tpp.setDisplayedValues(displayedTipAmounts);
        tpp.setWrapSelectorWheel(false);
        tpp.setValue(2);
        tpp.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                tip = Double.parseDouble(displayedTipAmounts[newVal]) / 100;
                UpdateTotals();
            }
        });
    }

    private void InitializeBillTextField(){
        //setting up bill text field
        final EditText billField = (EditText) findViewById(R.id.billField);
        billField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
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
            roundedTipTV.startAnimation(fadeRoundingIn);
            roundedPercentTitle.startAnimation(fadeRoundingIn);
        }
        else {
            roundedTipTV.startAnimation(fadeRoundingOut);
            roundedPercentTitle.startAnimation(fadeRoundingOut);
        }
    }

    private void CheckFadedStateSplitBill(int oldVal, int newVal){

        if (newVal == 1){
            billEachTV.startAnimation(fadeBillEachOut);
            billEachTitle.startAnimation(fadeBillEachOut);

        }
        else if (newVal > 1 && oldVal == 1){
            billEachTV.startAnimation(fadeBillEachIn);
            billEachTitle.startAnimation(fadeBillEachIn);
        }
    }

    private void UpdateTotals(){
        CalculateTotals();
        if (doRounding){
            RoundTotals();
        }
        billEach = billTotal / headCount;
        CalculateAndDisplayRemainingCents();
        DisplayTotals();
    }

    private void CalculateTotals(){
        tipTotal = tip * bill;
        billTotal = bill + tipTotal;
    }

    private void RoundTotals(){
        int roundedBillTotal = (int) Math.round(billTotal);
        tipTotal = roundedBillTotal - bill;
        int roundedTipPercent = (int) Math.round((tipTotal / bill) * 100);
        if (roundedTipPercent < 15){
            roundedBillTotal = (int) Math.ceil(billTotal);
        }
        while (roundedBillTotal % headCount != 0){
            roundedBillTotal++;
        }
        tipTotal = roundedBillTotal - bill;
        billTotal = roundedBillTotal;
        roundedTipPercent = (int) Math.round((tipTotal / bill) * 100);
        String roundedTipString = roundedTipPercent + "%";
        roundedTipTV.setText(roundedTipString);
    }

    private void CalculateAndDisplayRemainingCents(){
        double centsEach = Math.floor(billEach * 100);
        double centsTotal = Math.floor(billTotal * 100);
        int cents = (int)centsTotal - ((int)centsEach * headCount);
        if (oldCents == 0 && cents != 0){
            extraCentsTV.startAnimation(fadeCentsIn);
        }
        else if (cents == 0 && oldCents != 0){
            extraCentsTV.startAnimation(fadeCentsOut);
        }
        oldCents = cents;
        String centsString;
        if (cents == 1){
            centsString = getResources().getText(R.string.centsPt1) + String.valueOf(cents) + " " + getResources().getText(R.string.centPt2);
        }
        else {
            centsString = getResources().getText(R.string.centsPt1) + String.valueOf(cents) + " " + getResources().getText(R.string.centsPt2);
        }

        extraCentsTV.setText(centsString);
    }

    private void DisplayTotals(){
        billEachTV.setText(df.format(billEach));
        tipTotalTV.setText(df.format(tipTotal));
        billTotalTV.setText(df.format(billTotal));
    }
}
