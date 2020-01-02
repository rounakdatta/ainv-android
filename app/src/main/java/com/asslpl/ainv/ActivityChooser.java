package com.asslpl.ainv;

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
    }

    public void gotoItemSearchPage(View view) {
        Intent itemSearchPage = new Intent(getApplicationContext(), search.class);
        startActivity(itemSearchPage);
    }

    public void gotoItemEntryPage(View view) {
        Intent itemEntryPage = new Intent(getApplicationContext(), ItemMasterEntry.class);
        startActivity(itemEntryPage);
    }

    public void gotoTransactionPage(View view) {
        Intent transactionPage = new Intent(getApplicationContext(), Transaction.class);
        startActivity(transactionPage);
    }

    public void gotoSalesSearchPage(View view) {
        Intent salesSearchPage = new Intent(getApplicationContext(), searchSales.class);
        startActivity(salesSearchPage );
    }
}
