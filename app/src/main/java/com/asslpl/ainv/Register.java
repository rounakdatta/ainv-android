package com.asslpl.ainv;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class Register extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty
    @Pattern(regex = "^\\S*$", message = "Must not contain space")
    EditText username;

    @Password(min = 5)
    EditText password;

    @ConfirmPassword
    EditText confirmPassword;

    Validator validator = new Validator(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);

        // setting the validator to starting listening
        validator.setValidationListener(this);

        Button registerButton = findViewById(R.id.submitButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        String usernameText = String.valueOf(username.getText());
        String passwordText = String.valueOf(password.getText());

        String testEndpoint = getResources().getString(R.string.serverEndpoint);
        String registerURL = testEndpoint + "/api/register/";

        String searchResponse = "NULL";
        String searchRequests = "username=" + usernameText + "&password=" + passwordText;

        HttpPostRequest insertHttp = new HttpPostRequest();
        try {
            searchResponse = insertHttp.execute(registerURL, searchRequests).get();

            Toast toast = Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG);
            toast.show();

            Intent userOnboarding = new Intent(getApplicationContext(), user_onboarding.class);
            startActivity(userOnboarding);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
