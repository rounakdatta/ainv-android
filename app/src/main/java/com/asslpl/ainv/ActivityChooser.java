package com.asslpl.ainv;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ActivityChooser extends AppCompatActivity {

    TinyDB tinydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);

        tinydb = new TinyDB(getApplicationContext());

        boolean permission_createNew = tinydb.getBoolean("permission_createNew");
        boolean permission_transactionIn = tinydb.getBoolean("permission_transactionIn");
        boolean permission_transactionOut = tinydb.getBoolean("permission_transactionOut");
        boolean permission_view = tinydb.getBoolean("permission_view");

        if (!permission_createNew) {
            findViewById(R.id.clientEntryButton).setEnabled(false);
            findViewById(R.id.itemEntryButton).setEnabled(false);
            findViewById(R.id.warehouseEntryButton).setEnabled(false);
            findViewById(R.id.customerEntryButton).setEnabled(false);
        }

        if (!permission_view) {
            findViewById(R.id.stockSearchButton).setEnabled(false);
            findViewById(R.id.salesSearchButton).setEnabled(false);
        }

        if (!permission_transactionIn && !permission_transactionOut) {
            findViewById(R.id.transactionButton).setEnabled(false);
        }


        ImageView logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tinydb.clear();

                Intent userOnboarding = new Intent(getApplicationContext(), user_onboarding.class);
                startActivity(userOnboarding);
                finish();
            }
        });
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
