package com.asslpl.asslplinventorymanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinner;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class search extends AppCompatActivity {

    JSONArray allItemsArray = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        TinyDB tdb = new TinyDB(getApplicationContext());
        Spinner itemSelector = findViewById(R.id.itemSelector);

        String testEndpoint = "https://ainv.glitch.me";
        String getcountryDataURL = testEndpoint + "/data";

        ArrayList<String> itemNames = null;

        HttpGetRequest itemsGetter = new HttpGetRequest();
        try {
            String allItems = itemsGetter.execute(getcountryDataURL).get();
            allItemsArray = new JSONArray(allItems);

            itemNames = new ArrayList<>();
            for (int i = 0; i < allItemsArray.length(); i++) {
                itemNames.add(allItemsArray.getJSONObject(i).getString("name"));
            }

            ArrayAdapter<String> itemDisplayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, itemNames);
            itemDisplayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            //adapter.setDropDownViewResource(R.layout.custom_spinner);
            itemSelector.setAdapter(itemDisplayAdapter);

        } catch(Exception e) {
            System.out.println(e);

            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, "Error fetching data!", Toast.LENGTH_SHORT);
            toast.show();

        }

        itemSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Spinner itemDetailsSelector = findViewById(R.id.itemDetailsSelector);

                Log.e("Item Click","Position: " + i);
                ArrayList<String> itemVariants = new ArrayList<>();
                JSONArray allVariants = null;

                try {
                    allVariants = allItemsArray.getJSONObject(i).getJSONArray("description");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (int j = 0; j < allVariants.length(); j++) {
//                    Log.e("sel: ", allVariants.getString(j));
                    try {
                        itemVariants.add(allVariants.getString(j));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                ArrayAdapter<String> descriptionAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, itemVariants);
                descriptionAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                //adapter.setDropDownViewResource(R.layout.custom_spinner);
                itemDetailsSelector.setAdapter(descriptionAdapter);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
    }

    public void selectLocations(View view) {
        /*
         * Getting array of String to Bind in Spinner
         */
        final List<String> list = Arrays.asList(getResources().getStringArray(R.array.items));
        final Boolean[] foobar = {true, true, false};

        ArrayList<MultiSelectModel> listOfCountries = new ArrayList<>();
        listOfCountries.add(new MultiSelectModel(1, "India"));
        listOfCountries.add(new MultiSelectModel(2, "US"));
        listOfCountries.add(new MultiSelectModel(3, "UK"));

        ArrayList<Integer> alreadySelectedCountries = new ArrayList<>();


        MultiSelectDialog multiSelectDialog = new MultiSelectDialog()
                .title(getResources().getString(R.string.multi_select_dialog_title)) //setting title for dialog
                .titleSize(25)
                .positiveText("Done")
                .negativeText("Cancel")
                .setMinSelectionLimit(1) //you can set minimum checkbox selection limit (Optional)
                .setMaxSelectionLimit(listOfCountries.size()) //you can set maximum checkbox selection limit (Optional)
                .preSelectIDsList(alreadySelectedCountries) //List of ids that you need to be selected
                .multiSelectList(listOfCountries) // the multi select model list with ids and name
                .onSubmit(new MultiSelectDialog.SubmitCallbackListener() {
                    @Override
                    public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                        Log.e("selectedIds", selectedIds.toString());
                        //will return list of selected IDS
                        for (int i = 0; i < selectedIds.size(); i++) {
                            Toast.makeText(search.this, "Selected Ids : " + selectedIds.get(i) + "\n" +
                                    "Selected Names : " + selectedNames.get(i) + "\n" +
                                    "DataString : " + dataString, Toast.LENGTH_LONG).show();
                        }

                        TextView selectedTexts = findViewById(R.id.selectedLocations);
                        selectedTexts.setText("Selected Locations are " + selectedNames.toString());


                    }

                    @Override
                    public void onCancel() {
                        System.out.println("cancelled");
                    }


                });

        multiSelectDialog.show(getSupportFragmentManager(), "multiSelectDialog");
    }
}
