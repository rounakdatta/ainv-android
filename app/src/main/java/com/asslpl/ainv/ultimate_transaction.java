package com.asslpl.ainv;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class ultimate_transaction extends AppCompatActivity {

    EditText field1;
    EditText field2;
    EditText remarks;

    TextView field1Header;
    TextView field2Header;

    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ultimate_transaction);

        Intent intent = getIntent();

        final String requestDataPayload = intent.getStringExtra("requestData");
        final String direction = intent.getStringExtra("direction");

        field1Header = findViewById(R.id.field1Header);
        field2Header = findViewById(R.id.field2Header);

        field1 = findViewById(R.id.field1);
        field2 = findViewById(R.id.field2);
        remarks = findViewById(R.id.remarks);

        if (direction.equals("out")) {
            field1Header.setText("Serv. Inv. Ack. Date");
            field2Header.setText("Sale Inv. Ack. Date");
        }

        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String transactionResponse= "NULL";

                // lock the button first to prevent multiple clicks
                submitButton.setEnabled(false);

                // sending data
                String testEndpoint = getResources().getString(R.string.serverEndpoint);
                String transactionURL = testEndpoint + "/api/put/transaction/";

                String modifiedPayload = requestDataPayload + "&field1=" + field1.getText() + "&field2=" + field2.getText() + "&remarks=" + remarks.getText();

                HttpPostRequest insertHttp = new HttpPostRequest();
                try {
                    transactionResponse = insertHttp.execute(transactionURL, modifiedPayload).get();
                    JSONObject transactionResponseJson = new JSONObject(transactionResponse);

                    Toast toast;

                    if (transactionResponseJson.getBoolean("success")) {
                        toast = Toast.makeText(getApplicationContext(), "Transaction successful!", Toast.LENGTH_LONG);

                        setResult(Activity.RESULT_OK);
                        finish();
                    } else {
                        toast = Toast.makeText(getApplicationContext(), "Transaction error!", Toast.LENGTH_LONG);
                        submitButton.setEnabled(true);
                    }

                    toast.show();

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // calendar button handler
        // set the calendar dialog
        final Button field1Button = findViewById(R.id.field1Button);

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                field1.setText(sdf.format(myCalendar.getTime()));
            }

        };

        field1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ultimate_transaction.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // calendar button handler
        // set the calendar dialog
        final Button field2Button = findViewById(R.id.field2Button);

        final Calendar myCalendar2 = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar2.set(Calendar.YEAR, year);
                myCalendar2.set(Calendar.MONTH, monthOfYear);
                myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                field2.setText(sdf.format(myCalendar2.getTime()));
            }

        };

        field2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ultimate_transaction.this, date2, myCalendar2
                        .get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
                        myCalendar2.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }
}
