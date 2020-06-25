package com.asslpl.ainv;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.net.InetAddress;

public class start_page extends AppCompatActivity {

    private boolean isAppConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public boolean isServerConnected() {
        String testEndpoint = getResources().getString(R.string.serverEndpoint);
        String baseURL = testEndpoint + "/";

        HttpGetRequest warehouseGetter = new HttpGetRequest();
        try {
            String serverStatus = warehouseGetter.execute(baseURL).get();
            if (serverStatus.equals("OK")) {
                return true;
            }

        } catch(Exception e) {
            return false;
        }

        return false;
    }

    public void heartBeatActivity() {
        boolean appStatus = isAppConnected();
        boolean serverStatus = isServerConnected();

        TextView appStatusText = findViewById(R.id.appStatus);
        TextView serverStatusText = findViewById(R.id.serverStatus);

        Button proceedButton = findViewById(R.id.proceedButton);

        if (!appStatus) {
            findViewById(R.id.app_green).setVisibility(View.INVISIBLE);
            findViewById(R.id.app_yellow).setVisibility(View.INVISIBLE);

            findViewById(R.id.server_green).setVisibility(View.INVISIBLE);
            findViewById(R.id.server_red).setVisibility(View.INVISIBLE);

            appStatusText.setText("OFFLINE");
            proceedButton.setEnabled(false);

            return;
        } else {
            findViewById(R.id.app_red).setVisibility(View.INVISIBLE);
            findViewById(R.id.app_yellow).setVisibility(View.INVISIBLE);

            appStatusText.setText("ONLINE");
        }

        if (!serverStatus) {
            findViewById(R.id.server_yellow).setVisibility(View.INVISIBLE);
            findViewById(R.id.server_red).setVisibility(View.INVISIBLE);

            proceedButton.setEnabled(false);
            serverStatusText.setText("OFFLINE");
        } else {
            findViewById(R.id.server_yellow).setVisibility(View.INVISIBLE);
            findViewById(R.id.server_red).setVisibility(View.INVISIBLE);

            serverStatusText.setText("ONLINE");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_page);

        TextView versionBox = findViewById(R.id.appVersion);
        versionBox.setText("Version " + BuildConfig.VERSION_NAME);

        heartBeatActivity();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                System.out.println("Heartbeat activity");
                heartBeatActivity();
            }
        }, 2000);
    }

    public void goto_user_onboarding(View view) {
        Intent userOnboarding = new Intent(getApplicationContext(), user_onboarding.class);
        startActivity(userOnboarding);
    }
}
