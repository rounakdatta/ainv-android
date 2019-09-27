package com.asslpl.asslplinventorymanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class user_onboarding extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_onboarding);
    }

    public void gotoActivityChooserPage(View view) {
        Intent activityChooserPage = new Intent(getApplicationContext(), ActivityChooser.class);
        startActivity(activityChooserPage);
        finish();
    }
}
