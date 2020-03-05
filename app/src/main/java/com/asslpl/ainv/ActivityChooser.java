package com.asslpl.ainv;

import android.app.ProgressDialog;
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
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void gotoItemSearchPage(View view) {
        Intent itemSearchPage = new Intent(getApplicationContext(), search.class);
        startActivity(itemSearchPage);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void gotoItemEntryPage(View view) {
        Intent itemEntryPage = new Intent(getApplicationContext(), ItemMasterEntry.class);
        startActivity(itemEntryPage);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void gotoTransactionPage(View view) {
        Intent transactionPage = new Intent(getApplicationContext(), Transaction.class);
        startActivity(transactionPage);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void gotoSalesSearchPage(View view) {
        Intent salesSearchPage = new Intent(getApplicationContext(), searchSales.class);
        startActivity(salesSearchPage );
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void gotoClientEntryPage(View view) {
        Intent clientEntryPage = new Intent(getApplicationContext(), ClientEntry.class);
        startActivity(clientEntryPage);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void gotoCustomerEntryPage(View view) {
        Intent customerEntryPage = new Intent(getApplicationContext(), CustomerEntry.class);
        startActivity(customerEntryPage);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
