package com.asslpl.asslplinventorymanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import org.json.JSONArray;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class WarehouseEntry extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty
    EditText warehouseNameView;

    @NotEmpty
    EditText warehouseLocationView;

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

        warehouseNameView = (EditText) findViewById(R.id.warehouseName);
        warehouseLocationView = (EditText) findViewById(R.id.warehouseLocation);
        gstinView = (EditText) findViewById(R.id.gstin);
        contactNameView = (EditText) findViewById(R.id.contactName);
        contactNumberView = (EditText) findViewById(R.id.contactNumber);

        validator.setValidationListener(this);
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
