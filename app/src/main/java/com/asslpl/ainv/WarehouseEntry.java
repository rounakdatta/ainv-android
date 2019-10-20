package com.asslpl.ainv;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class WarehouseEntry extends AppCompatActivity implements Validator.ValidationListener {

    ArrayList<String> warehouseLocationSuggestions = new ArrayList<String>();

    @NotEmpty
    EditText warehouseNameView;

    @NotEmpty
    AutoCompleteTextView warehouseLocationView;

    @Length(max = 15, min = 15)
    @NotEmpty
    EditText gstinView;

    @NotEmpty
    EditText contactNameView;

    @NotEmpty
    @Pattern(regex = "^[7-9][0-9]{9}$", message = "Invalid Mobile Number")
    EditText contactNumberView;

    String warehouseName = "";
    String warehouseLocation = "";
    String gstin = "";
    String contactName = "";
    String contactNumber = "";

    Validator validator = new Validator(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse_entry);

        warehouseNameView = findViewById(R.id.warehouseName);
        warehouseLocationView = findViewById(R.id.warehouseLocation);
        gstinView = findViewById(R.id.gstin);
        contactNameView = findViewById(R.id.contactName);
        contactNumberView = findViewById(R.id.contactNumber);

        // setting the validator to starting listening
        validator.setValidationListener(this);

        // getting the warehouse locations
        String testEndpoint = getResources().getString(R.string.serverEndpoint);
        String getWarehouseDataURL = testEndpoint + "/api/get/warehouses/";

        JSONArray allLocationsArray;

        HttpGetRequest warehouseGetter = new HttpGetRequest();
        try {
            String allItems = warehouseGetter.execute(getWarehouseDataURL).get();
            allLocationsArray = new JSONArray(allItems);

            for (int i = 0; i < allLocationsArray.length(); i++) {
                String wLocation = allLocationsArray.getJSONObject(i).getString("warehouseLocation");
                warehouseLocationSuggestions.add(wLocation);
            }

        } catch(Exception e) {
            System.out.println(e);

            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, "Error fetching data!", Toast.LENGTH_SHORT);
            toast.show();

            return;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, warehouseLocationSuggestions);

        warehouseLocationView.setThreshold(1);
        warehouseLocationView.setAdapter(adapter);
    }

    public void enterNewWarehouse(View view) {

        validator.validate();

    }

    public void onValidationSucceeded() {
        // Toast.makeText(this, "Yay! we got it right!", Toast.LENGTH_SHORT).show();

        warehouseName = warehouseNameView.getText().toString();
        warehouseLocation = warehouseLocationView.getText().toString();
        gstin = gstinView.getText().toString();
        contactName = contactNameView.getText().toString();
        contactNumber = contactNumberView.getText().toString();

        String testEndpoint = getResources().getString(R.string.serverEndpoint);
        String warehouseURL = testEndpoint + "/api/put/warehouse/";

        String searchResponse = "NULL";
        String searchRequests = "warehouseName=" + warehouseName + "&warehouseLocation=" + warehouseLocation + "&gstin=" + gstin+ "&contactName=" + contactName + "&contactNumber=" + contactNumber;

        HttpPostRequest insertHttp = new HttpPostRequest();
        try {
            searchResponse = insertHttp.execute(warehouseURL, searchRequests).get();

            Toast toast = Toast.makeText(getApplicationContext(), "Warehouse Entry successful!", Toast.LENGTH_LONG);
            toast.show();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent activityChooserPage = new Intent(getApplicationContext(), ActivityChooser.class);
        startActivity(activityChooserPage);
        finish();
    }

    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
