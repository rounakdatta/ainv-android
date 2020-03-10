package com.asslpl.ainv;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class user_onboarding extends AppCompatActivity {

    TinyDB tinydb;

    public void auth(String username, String password) {
        String testEndpoint = getResources().getString(R.string.serverEndpoint);
        String loginURL = testEndpoint + "/api/login/";

        String loginRequests = "username=" + username + "&password=" + password;
        String loginResponse;
        JSONObject loginResponseJson;

        HttpPostRequest loginHttp = new HttpPostRequest();
        try {
            loginResponse = loginHttp.execute(loginURL, loginRequests).get();
            loginResponseJson = new JSONObject(loginResponse);
            System.out.println(loginResponseJson);

            boolean loginRStatus = loginResponseJson.getBoolean("success");
            if (loginRStatus) {
                Toast toast = Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG);
                toast.show();

                tinydb.putString("username", username);
                tinydb.putString("password", password);
                tinydb.putBoolean("permission_createNew", loginResponseJson.getBoolean("permission_createNew"));
                tinydb.putBoolean("permission_transactionIn", loginResponseJson.getBoolean("permission_transactionIn"));
                tinydb.putBoolean("permission_transactionOut", loginResponseJson.getBoolean("permission_transactionOut"));
                tinydb.putBoolean("permission_view", loginResponseJson.getBoolean("permission_view"));
                tinydb.putBoolean("loginStatus", true);

                Intent activityChooserPage = new Intent(getApplicationContext(), ActivityChooser.class);
                startActivity(activityChooserPage);
                finish();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Incorrect credentials!", Toast.LENGTH_LONG);
                toast.show();
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_onboarding);

        tinydb = new TinyDB(getApplicationContext());

        boolean loginStatus = tinydb.getBoolean("loginStatus");
        if (loginStatus) {

            String username = tinydb.getString("username");
            String password = tinydb.getString("password");

            auth(username, password);
        }
    }

    public void gotoActivityChooserPage(View view) {

        EditText usernameTv = findViewById(R.id.username);
        EditText passwordTv = findViewById(R.id.password);

        String username = String.valueOf(usernameTv.getText());
        String password = String.valueOf(passwordTv.getText());

        auth(username, password);
    }

    public void gotoRegisterPage(View view) {
        Intent registerPage = new Intent(getApplicationContext(), Register.class);
        startActivity(registerPage);
        finish();
    }
}
