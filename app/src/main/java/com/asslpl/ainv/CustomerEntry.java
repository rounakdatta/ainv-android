package com.asslpl.ainv;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CustomerEntry extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty
    EditText customerNameView;

    String customerName;

    Validator validator = new Validator(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_entry);

        customerNameView = findViewById(R.id.customerName);

        // setting the validator to starting listening
        validator.setValidationListener(this);
    }

    @Override
    public void onValidationSucceeded() {
        customerName = customerNameView.getText().toString();

        String testEndpoint = getResources().getString(R.string.serverEndpoint);
        String customerURL = testEndpoint + "/api/put/customer/";

        String searchResponse = "NULL";
        String searchRequests = "customerName=" + customerName;

        HttpPostRequest insertHttp = new HttpPostRequest();
        try {
            searchResponse = insertHttp.execute(customerURL, searchRequests).get();

            Toast toast = Toast.makeText(getApplicationContext(), "Customer Entry successful!", Toast.LENGTH_LONG);
            toast.show();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent activityChooserPage = new Intent(getApplicationContext(), ActivityChooser.class);
        startActivity(activityChooserPage);
        finish();

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

    }

    public void enterNewCustomer(View view) {
        validator.validate();
    }
}
