package com.asslpl.ainv;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class start_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_page);
    }

    public void goto_user_onboarding(View view) {
        Intent userOnboarding = new Intent(getApplicationContext(), user_onboarding.class);
        startActivity(userOnboarding);
    }
}
