package primes.ats.com.primes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "NthPrimes";
    public EditText editTextEnlistPrimes;
    public EditText editTextQueryN;
    public Button btnEnter;
    public Button btnQueryNEnter;
    public Button btnClear;
    public TextView tvEnlistResult;
    public TextView tvQueryResult;
    public String primeValues = "";
    public String previousInputString = "";
    ArrayList<Integer> totalPrimesArray = new ArrayList<Integer>();
    public int queriedPrimes = 0;
    public int totalPrimes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.editTextEnlistPrimes = (EditText) findViewById(R.id.evEnlistPrimes);
        this.editTextQueryN = (EditText) findViewById(R.id.evQueryN);
        this.btnEnter = (Button) findViewById(R.id.btnEnter);
        this.btnQueryNEnter = (Button) findViewById(R.id.btnQueryNEnter);
        this.btnClear = (Button) findViewById(R.id.btnClear);
        this.tvEnlistResult = (TextView) findViewById(R.id.tvEnlistResult);
        this.tvQueryResult = (TextView) findViewById(R.id.tvQueryResult);
        EditText EnlistPrimesEntryEditText = this.editTextEnlistPrimes;
        EnlistPrimesEntryEditText.setOnEditorActionListener(new DoneOnEditorActionListener());
        EditText QueryPrimesEntryEditText = this.editTextQueryN;
        QueryPrimesEntryEditText.setOnEditorActionListener(new DoneOnEditorActionListener2());
    }

    public void btnSendAppStop_OnClick(View view) {
        Log.i(LOG_TAG, "btnSendAppStop clicked...");
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void btnEnter_OnClick(View view) {
        String inputString = editTextEnlistPrimes.getText().toString().trim();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        if (inputString.length() == 0) {
            btnClear_OnClick(view);
            AlertDialog myDialog = new AlertDialog.Builder(MainActivity.this).create();
            myDialog.setMessage(getString(R.string.GLOBAL_Starting_EnlistPrimes_Entered));
            myDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.GLOBAL_OK), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            myDialog.show();
        } else {
            totalPrimes = 0;
            totalPrimes = Integer.parseInt(inputString);

            if (totalPrimes > 0) {
                Integer inputValue = Integer.parseInt(inputString);
                if (!inputString.equalsIgnoreCase(previousInputString)) {   //simular enlist will not populate array again
                    primeValues = "";
                    totalPrimesArray.clear();
                    this.tvQueryResult.setText("");
                    this.tvEnlistResult.setText("");
                    this.editTextQueryN.setText("");
                    Log.i(LOG_TAG, "Enlisting Primes...");
                    enlistPrimes(inputValue);
                    previousInputString = inputString;
                }
            } else {
                AlertDialog myDialog = new AlertDialog.Builder(MainActivity.this).create();
                myDialog.setMessage(getString(R.string.GLOBAL_EnlistPrimes_GreaterThanZero));
                myDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.GLOBAL_OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                myDialog.show();
            }
        }
    }

    public void btnQueryNEnter_OnClick(View view) {
        String inputEnlistString = editTextEnlistPrimes.getText().toString().trim();
        String inputQueryString = editTextQueryN.getText().toString().trim();
        String formattedQueryResult = "";

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (inputQueryString.length() == 0) {
            this.tvQueryResult.setText("");
            AlertDialog myDialog = new AlertDialog.Builder(MainActivity.this).create();
            myDialog.setMessage(getString(R.string.GLOBAL_Starting_QueryPrimes_Entered));
            myDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.GLOBAL_OK), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            myDialog.show();
        }
        else {
            if (inputEnlistString.equalsIgnoreCase("")) {
                AlertDialog myDialog = new AlertDialog.Builder(MainActivity.this).create();
                myDialog.setMessage(getString(R.string.GLOBAL_Starting_EnlistPrimes_Entered));
                myDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.GLOBAL_OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                myDialog.show();
            } else {
                queriedPrimes = Integer.parseInt(editTextEnlistPrimes.getText().toString().trim());
                if (queriedPrimes > 0) {
                    Integer inputValue = Integer.parseInt(inputQueryString);
                    String primeDescription = "";
                    if (inputValue <= totalPrimes) {                //dont let user query a value greater than what was enlisted
                        String arrayItem = totalPrimesArray.get(inputValue-1).toString();
                        formattedQueryResult = formatQueryResult(inputQueryString, arrayItem);
                        this.tvQueryResult.setText(formattedQueryResult);
                    } else {
                        AlertDialog myDialog = new AlertDialog.Builder(MainActivity.this).create();
                        myDialog.setMessage(getString(R.string.GLOBAL_QueryPrimes_GreaterThanEnlisted));
                        myDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.GLOBAL_OK), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        myDialog.show();
                    }
                } else {
                    AlertDialog myDialog = new AlertDialog.Builder(MainActivity.this).create();
                    myDialog.setMessage(getString(R.string.GLOBAL_QueryPrimes_GreaterThanZero));
                    myDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.GLOBAL_OK), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    myDialog.show();
                }
            }
         }
    }

    public static String formatQueryResult(String inputQueryString, String arrayItem) {
        String primeDescription = "";
        Log.i(LOG_TAG, "Formatting Query result...");

        if (inputQueryString.equalsIgnoreCase("1")) {
            primeDescription = "st prime is: ";
        } else if (inputQueryString.equalsIgnoreCase("2")) {
            primeDescription = "nd prime is: ";
        } else if (inputQueryString.equalsIgnoreCase("3")) {
            primeDescription = "rd prime is: ";
        } else {
            primeDescription = "th prime is: ";
        }
        primeDescription = "The " + inputQueryString + primeDescription + arrayItem.toString();
        return primeDescription;
    }

    public void enlistPrimes(int totalEntered)   {
        totalPrimesArray.clear();
        int status = 1;
        int calculatedResult = 3;

        if (totalEntered >= 1) {
            //2 is a known prime number
            primeValues = primeValues + String.valueOf("2");
            totalPrimesArray.add(2);
        }
        for (int i = 2; i <= totalEntered; ) {
            while(totalPrimesArray.size() < totalEntered) {
                for (int divisor = 2; divisor <= Math.sqrt(calculatedResult); divisor++) {
                    if (calculatedResult % divisor == 0) {
                        //no remainder = not prime
                        status = 0;
                        break;
                    }
                }
                if (status != 0) {
                    //remainder exists = prime
                    primeValues = primeValues + "," + String.valueOf(calculatedResult);
                    this.tvEnlistResult.setText(primeValues);
                    totalPrimesArray.add(calculatedResult);
                    i++;
                }
                status = 1;
                calculatedResult++;
            }
        }
    }

    public void btnClear_OnClick(View view) {
        Log.i(LOG_TAG, "btnClear clicked...");
        totalPrimes = 0;
        primeValues = "";
        totalPrimesArray.clear();
        this.tvQueryResult.setText("");
        this.tvEnlistResult.setText("");
        this.editTextEnlistPrimes.setText("");
        this.editTextQueryN.setText("");
        previousInputString = "";
    }

    class DoneOnEditorActionListener implements EditText.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                btnEnter.performClick();
                return true;
            }
            return false;
        }
    }

    class DoneOnEditorActionListener2 implements EditText.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                btnQueryNEnter.performClick();
                return true;
            }
            return false;
        }
    }

}
