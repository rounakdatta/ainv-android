package com.asslpl.asslplinventorymanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.concurrent.ExecutionException;

public class WarehouseEntry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse_entry);
    }

    public void enterNewWarehouse(View view) {
        EditText warehouseNameView = (EditText) findViewById(R.id.warehouseName);
        EditText warehouseLocationView = (EditText) findViewById(R.id.warehouseLocation);
        EditText gstinView = (EditText) findViewById(R.id.gstin);
        EditText contactNameView = (EditText) findViewById(R.id.contactName);
        EditText contactNumberView = (EditText) findViewById(R.id.contactNumber);

        String warehouseName = warehouseNameView.getText().toString();
        String warehouseLocation = warehouseLocationView.getText().toString();
        String gstin = gstinView.getText().toString();
        String contactName = contactNameView.getText().toString();
        String contactNumber = contactNumberView.getText().toString();

        String testEndpoint = "http://157.245.99.108";
        String warehouseURL = testEndpoint + "/api/put/warehouse";

        String searchResponse = "NULL";
        String searchRequests = "warehouseName=" + warehouseName + "&warehouseLocation=" + warehouseLocation + "&gstin=" + gstin+ "&contactName=" + contactName + "&contactNumber=" + contactNumber;

        HttpPostRequest insertHttp = new HttpPostRequest();
        try {
            searchResponse = insertHttp.execute(warehouseURL, searchRequests).get();

            Toast toast = Toast.makeText(getApplicationContext(), searchResponse, Toast.LENGTH_LONG);
            toast.show();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
