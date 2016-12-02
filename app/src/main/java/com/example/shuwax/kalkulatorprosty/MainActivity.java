package com.example.shuwax.kalkulatorprosty;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    private TextView textView; // Areao for resoult
    private TextView textView2; // History of action
    private Button button; // Kind od button clicked

    private String character = null; // actual ckicked character
    private String actualCharacter = null; // last important character
    private boolean itCharacter = false; // it is character ?

    private boolean nowVar = false; // new use varriable?

    private boolean firstVar = false; // first number is set?

    private BigDecimal firstDig; // First number
    private BigDecimal secondDig; // Second numer
    private static final String[] actionTab = {"+", "-", "x", "/"}; // posible action with use two number

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        SetTextView();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Make to run your application only in portrait mode
        textView.setText("0");
        textView2.setText("");
        //Emable scroll textView
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView2.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.activity_setting)
        {
            Intent intent = new Intent(this,AboutActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void getVibe(){
        Vibrator vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
        vibrator.vibrate(25);
    }

    public void SetTextView() {
        //take value from textView field
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
    }

    public void SetBtn(View view) {
        // take value from button field
        button = (Button) view;
    }

    public void onClickClear(View view) {
        getVibe();
        SetTextView();
        textView.setText("0");
        textView2.setText("");
        character = null;
        nowVar = false;
        firstVar = false;
    }

    public void onClickNumericBtn(View view) {
        getVibe();
        SetTextView();
        SetBtn(view);
        String text = textView.getText().toString();
        String but = button.getText().toString();
        itCharacter = false;
        if (text.length() == 1 && text.equals("0")) {
            // if fielad textview is 0, change it to cliked button number
            textView.setText(but);
        } else if (nowVar) {
            {
                actualCharacter = character;
                text = "";
                but = text + but;
                textView.setText(but);
                nowVar = false;
            }
        } else if (!nowVar) {
            but = text + but;
            textView.setText(but);
        }
    }

    public void OnClickAction(View view) {
        getVibe();
        SetTextView();
        SetBtn(view);
        String action = button.getText().toString();
        String text = textView.getText().toString();
        String text2 = textView2.getText().toString();
        String result;
        nowVar = true;
        character = action;

        boolean anotherChar = false;

        if (!text2.isEmpty() && itCharacter) {
            for (String anActionTab : actionTab) {
                // user click again to character button?
                if (anActionTab.equals(text2.substring(text2.length() - 1))) {
                    anotherChar = true;
                }
            }
        }
        if (anotherChar) {
            // if yes, change character
            textView2.setText(text2.substring(0, text2.length() - 1) + action);
        } else {
            if (!firstVar) {
                firstDig = new BigDecimal(text);
                firstVar = true;
                nowVar = true;
                actualCharacter = character;
            } else if (firstVar) {
                secondDig = new BigDecimal(text);
                result = Decision();
                textView.setText(result);
            }
            itCharacter = true;
            textView2.setText(text2 + text + character);
        }
    }
    public String Decision() {
        String result = "";
        switch (actualCharacter) {
            case "+":
                result = String.valueOf(firstDig.add(secondDig));
                firstDig = firstDig.add(secondDig);
                break;
            case "-":
                result = String.valueOf(firstDig.subtract(secondDig));
                firstDig = firstDig.subtract(secondDig);
                break;
            case "x":
                result = String.valueOf(firstDig.multiply(secondDig));
                firstDig = firstDig.multiply(secondDig);
                break;
            case "/":
                if (secondDig.doubleValue() != 0) {
                    result = String.valueOf(firstDig.divide(secondDig, 3, BigDecimal.ROUND_HALF_UP));
                    firstDig = firstDig.divide(secondDig, 3, BigDecimal.ROUND_HALF_UP);
                }
                break;
        }
        return result;
    }

    public void onClickResult(View view) {
        getVibe();
        SetTextView();
        String result = "";
        String text = textView.getText().toString();
        if (!nowVar) {
            secondDig = new BigDecimal(text);
            if (!textView2.getText().toString().equals("") || !textView2.getText().toString().equals("")) {
                result = Decision();
            }
            textView2.setText(" ");
            firstVar = false;
            textView.setText(result);
        }
    }

    public void OnClickActionNegative(View view) {

        SetTextView();
        BigDecimal liczba = BigDecimal.ZERO;
        String text = textView.getText().toString();
        if(!nowVar) {
            getVibe();
            if (!text.equals("0")) {
                if (!firstVar) {
                    firstDig = new BigDecimal(text);
                    firstDig = firstDig.multiply(BigDecimal.valueOf(-1));
                    liczba = firstDig;
                } else if (firstVar) {
                    secondDig = new BigDecimal(text);
                    secondDig = secondDig.multiply(BigDecimal.valueOf(-1));
                    liczba = secondDig;
                }
                String pom = String.valueOf(liczba);
                textView.setText(pom);
            }
        }
    }

    public void OnClickActionSqrt(View view) {

        SetTextView();
        String text = textView.getText().toString();
        BigDecimal liczba = new BigDecimal(text);

        if(!nowVar) {
            getVibe();
            if (liczba.doubleValue() >= 0) {
                if (!firstVar) {
                    firstDig = new BigDecimal(Math.sqrt(liczba.doubleValue()));
                    liczba = firstDig;
                } else if (firstVar) {
                    secondDig = new BigDecimal(Math.sqrt(liczba.doubleValue()));
                    liczba = secondDig;
                }
                String pom = String.valueOf(liczba.doubleValue());
                textView.setText(pom);
            }
        }
    }
    public void OnClickActionDot(View view) {

        SetTextView();
        boolean dot = false; // to flag if "." is allready placed
        String text = textView.getText().toString();
        if (!nowVar) {
            for (int i = 0; i < text.length(); i++) {
                if (text.charAt(i) == '.') {
                    dot = true;
                }
            }
            if (!dot) {
                getVibe();
                text = text + ".";
                textView.setText(text);
            }
        }
    }

    public void OnClickDel(View view) {

        SetTextView();
        String text = textView.getText().toString();
        if(!nowVar) {
            getVibe();
            if (text.length() > 0) {
                if (text.length() == 1) {
                    textView.setText("0");
                } else {
                    text = text.substring(0, text.length() - 1);
                    textView.setText(text);
                }
            }
        }
    }
}



