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

public class ClientEntry extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty
    EditText clientNameView;

    String clientName;

    Validator validator = new Validator(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_entry);

        clientNameView = findViewById(R.id.clientName);

        // setting the validator to starting listening
        validator.setValidationListener(this);
    }

    @Override
    public void onValidationSucceeded() {
        clientName = clientNameView.getText().toString();

        String testEndpoint = getResources().getString(R.string.serverEndpoint);
        String clientURL = testEndpoint + "/api/put/client/";

        String searchResponse = "NULL";
        String searchRequests = "clientName=" + clientName;

        HttpPostRequest insertHttp = new HttpPostRequest();
        try {
            searchResponse = insertHttp.execute(clientURL, searchRequests).get();

            Toast toast = Toast.makeText(getApplicationContext(), "Client Entry successful!", Toast.LENGTH_LONG);
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

    public void enterNewClient(View view) {
        validator.validate();
    }
}
