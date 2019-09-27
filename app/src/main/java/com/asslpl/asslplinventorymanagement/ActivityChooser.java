package com.asslpl.asslplinventorymanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ActivityChooser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);
    }

    public void gotoWarehouseEntryPage(View view) {
        Intent warehouseEntryPage = new Intent(getApplicationContext(), WarehouseEntry.class);
        startActivity(warehouseEntryPage);
        finish();
    }

    public void gotoItemSearchPage(View view) {
        Intent itemSearchPage = new Intent(getApplicationContext(), search.class);
        startActivity(itemSearchPage);
        finish();
    }
}
