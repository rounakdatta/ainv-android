package com.asslpl.ainv;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ItemMasterEntry extends AppCompatActivity implements Validator.ValidationListener {

    ArrayList<String> itemNameSuggestions = new ArrayList<String>();

    @NotEmpty
    AutoCompleteTextView itemNameView;

    @NotEmpty
    EditText itemVariantView;

    @NotEmpty
    EditText hsnCodeView;

    @NotEmpty
    EditText uomRawView;

    @NotEmpty
    EditText uomSmallView;

    @NotEmpty
    EditText uomBigView;

    @NotEmpty
    EditText rawPerSmallView;

    @NotEmpty
    EditText smallPerBigView;

    String itemName = "";
    String itemVariant = "";
    String hsnCode = "";
    String uomRaw = "";
    String uomSmall = "";
    String uomBig = "";

    String rawPerSmall = "";
    String smallPerBig = "";

    Validator validator = new Validator(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_master_entry);

        itemNameView = findViewById(R.id.itemName);
        itemVariantView = findViewById(R.id.itemVariant);
        hsnCodeView = findViewById(R.id.hsnCode);
        uomRawView = findViewById(R.id.uomRaw);
        uomSmallView = findViewById(R.id.uomSmall);
        uomBigView = findViewById(R.id.uomBig);

        rawPerSmallView = findViewById(R.id.rawPerSmall);
        smallPerBigView = findViewById(R.id.smallPerBig);

        // setting the validator to starting listening
        validator.setValidationListener(this);

        // getting the warehouse locations
        String testEndpoint = getResources().getString(R.string.serverEndpoint);
        String getItemNameDataURL = testEndpoint + "/api/get/items/?only=itemName";

        JSONArray allItemsArray;

        HttpGetRequest itemGetter = new HttpGetRequest();
        try {
            String allItems = itemGetter.execute(getItemNameDataURL).get();
            allItemsArray = new JSONArray(allItems);

            for (int i = 0; i < allItemsArray.length(); i++) {
                String iName = allItemsArray.getString(i);
                itemNameSuggestions.add(iName);
            }

        } catch(Exception e) {
            System.out.println(e);

            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, "Error fetching data!", Toast.LENGTH_SHORT);
            toast.show();

            return;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, itemNameSuggestions);

        itemNameView.setThreshold(1);
        itemNameView.setAdapter(adapter);
    }

    public void enterNewItem(View view) {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        itemName = itemNameView.getText().toString();
        itemVariant = itemVariantView.getText().toString();
        hsnCode = hsnCodeView.getText().toString();
        uomRaw = uomRawView.getText().toString();
        uomSmall = uomSmallView.getText().toString();
        uomBig = uomBigView.getText().toString();

        rawPerSmall = rawPerSmallView.getText().toString();
        smallPerBig = smallPerBigView.getText().toString();

        String testEndpoint = getResources().getString(R.string.serverEndpoint);
        String itemMasterURL = testEndpoint + "/api/put/itemmaster/";

        String searchResponse = "NULL";
        String searchRequests = "itemName=" + itemName + "&itemVariant=" + itemVariant+ "&hsnCode=" + hsnCode + "&uomRaw=" + uomRaw + "&uomSmall=" + uomSmall + "&uomBig=" + uomBig + "&rawPerSmall=" + rawPerSmall + "&smallPerBig=" + smallPerBig;

        HttpPostRequest insertHttp = new HttpPostRequest();
        try {
            searchResponse = insertHttp.execute(itemMasterURL, searchRequests).get();

            Toast toast = Toast.makeText(getApplicationContext(), "Item Entry successful!", Toast.LENGTH_LONG);
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

    @Override
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
