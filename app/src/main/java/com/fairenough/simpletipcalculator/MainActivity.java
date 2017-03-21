package com.fairenough.simpletipcalculator;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.MenuItemHoverListener;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {

    private double tip = 0.15;
    private double bill = 0;
    private int headCount = 0;
    private DecimalFormat df = new DecimalFormat("0.00");
    private int roundOption;
    private TextView tipEachTV;
    private TextView tipTotalTV;
    private TextView billEachTV;
    private TextView billTotalTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting up all fields
        tipEachTV = (TextView)findViewById(R.id.tipEachNum);
        tipTotalTV  = (TextView)findViewById(R.id.tipTotalNum);
        billEachTV  = (TextView)findViewById(R.id.billEachNum);
        billTotalTV = (TextView)findViewById(R.id.billTotalNum);

        //setting up toolbar
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        //end toolbar


        //setting up rounding spinner
        Spinner roundingOption = (Spinner) findViewById(R.id.roundingOptionSpinner);
        ArrayAdapter<CharSequence> roundingAdapter = ArrayAdapter.createFromResource(this,R.array.roundFor, android.R.layout.simple_spinner_item);
        roundingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roundingOption.setAdapter(roundingAdapter);

        roundingOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                roundOption = position;
                UpdateTotals();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                roundOption = 0;
                UpdateTotals();
            }
        });
        //end rounding spinner

        //setting up the headcount spinner
        Spinner pplCount = (Spinner) findViewById(R.id.headCountSpinner);
        ArrayAdapter<CharSequence> pplAdapter = ArrayAdapter.createFromResource(this,R.array.headCountArray, android.R.layout.simple_spinner_item);
        pplAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pplCount.setAdapter(pplAdapter);

        pplCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                headCount = Integer.parseInt(parent.getItemAtPosition(position).toString());
                //parent.getItemAtPosition(position), returns text of item at that position
                UpdateTotals();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                headCount = 0;
                UpdateTotals();
            }
        });
        //end headcount spinner

        //setting up the tip amount spinner
        Spinner tipAmt = (Spinner) findViewById(R.id.tipPercentSpinner);
        ArrayAdapter<CharSequence> tipAmtAdapter = ArrayAdapter.createFromResource(this,R.array.tipAmountArray, android.R.layout.simple_spinner_item);
        tipAmtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipAmt.setAdapter(tipAmtAdapter);

        tipAmt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tip = Double.parseDouble(parent.getItemAtPosition(position).toString().substring(0,2)) / 100;
                //parent.getItemAtPosition(position), returns text of item at that position
                UpdateTotals();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tip = 0;
                UpdateTotals();
            }
        });
        //end tip amount spinner


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
        int roundTo;
        if (input < 10) roundTo = 1;
        else roundTo = 5;
        return Math.ceil(input/roundTo) * roundTo;
    }

    void UpdateTotals(){

        double tipEach;
        double tipTotal;
        double billEach;
        double billTotal;
        try {
        switch (roundOption){
            case 0: //no rounding
                tipEach = (bill * tip) / headCount;
                tipTotal = tipEach * headCount;
                billTotal = bill + tipTotal;
                billEach = billTotal / headCount;
                break;
            case 1: //round for tip each
                tipEach = (bill * tip) / headCount;
                tipEach = roundUp(tipEach);
                tipTotal = tipEach * headCount;
                billTotal = bill + tipTotal;
                billEach = billTotal / headCount;
                break;
            case 2: //round for tip total
                tipTotal = bill * tip;
                tipTotal = roundUp(tipTotal);
                tipEach = tipTotal / headCount;
                billTotal = bill + tipTotal;
                billEach = billTotal / headCount;
                break;
            case 3: //round for bill each
                tipTotal = bill * tip;
                billTotal = bill + tipTotal;
                billEach = billTotal / headCount;
                billEach = roundUp(billEach);
                tipEach = billEach - (bill / headCount);
                tipTotal = tipEach * headCount;
                billTotal = bill + tipTotal;
                break;
            case 4: //round for bill total
                tipTotal = (bill * tip);
                billTotal = bill + tipTotal;
                billTotal = roundUp(billTotal);
                tipTotal = billTotal - bill;
                tipEach = tipTotal / headCount;
                billEach = billTotal / headCount;
                break;
            default:
                throw new ArithmeticException();
        }
        }catch (ArithmeticException ex){
            tipEach = 0;
            tipTotal = 0;
            billEach = 0;
            billTotal = 0;
        }

        tipEachTV.setText(df.format(tipEach));
        tipTotalTV.setText(df.format(tipTotal));
        billEachTV.setText(df.format(billEach));
        billTotalTV.setText(df.format(billTotal));

    }
}
