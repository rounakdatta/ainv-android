package com.asslpl.asslplinventorymanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.evrencoskun.tableview.TableView;

import static java.security.AccessController.getContext;

public class view_inventory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);

        TableView tableView = new TableView(getApplicationContext());
    }
}
