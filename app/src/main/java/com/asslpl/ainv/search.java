package com.asslpl.ainv;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class search extends AppCompatActivity {

    JSONArray allItemsArray = null;
    JSONArray allCountriesArray = null;
    ArrayList<MultiSelectModel> listOfCountries = new ArrayList<>();
    ArrayList<MultiSelectModel> itemVariantsObject = new ArrayList<>();

    final Dictionary itemDescription2IdMapper = new Hashtable();
    Dictionary warehouseLocation2IdMapper = new Hashtable();

    ArrayList<String> queryLocations = new ArrayList<>();
    ArrayList<String> queryIds= new ArrayList<>();
    String queryItemName = null;
    String queryItemVariant = null;
    ArrayList<String> itemVariants = new ArrayList<>();
    ArrayList<String> itemVariantIds = new ArrayList<>();

    JSONArray clientArray = null;
    ArrayList<String> clients = null;
    ArrayList<String> clientsNums = null;
    Spinner clientSelector;
    String CLIENTID = "-1";
    TextView clientIdTv;
    CheckBox allClients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        TinyDB tdb = new TinyDB(getApplicationContext());
        Spinner itemSelector = findViewById(R.id.itemSelector);

        final CheckBox selectAllVariants = (CheckBox) findViewById(R.id.selectAllVariants);
        CheckBox selectAllLocations = (CheckBox) findViewById(R.id.selectAllLocations);


        String testEndpoint = getResources().getString(R.string.serverEndpoint);
        String getcountryDataURL = testEndpoint + "/api/get/items/";

        ArrayList<String> itemNames = null;

        HttpGetRequest itemsGetter = new HttpGetRequest();
        try {
            String allItems = itemsGetter.execute(getcountryDataURL).get();
            allItemsArray = new JSONArray(allItems);

            itemNames = new ArrayList<>();
            for (int i = 0; i < allItemsArray.length(); i++) {
                itemNames.add(allItemsArray.getJSONObject(i).getString("name"));
            }

            try {
                queryItemName = allItemsArray.getJSONObject(0).getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ArrayAdapter<String> itemDisplayAdapter = new ArrayAdapter<>(search.this, android.R.layout.simple_spinner_item, itemNames);
            itemDisplayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            //adapter.setDropDownViewResource(R.layout.custom_spinner);
            itemSelector.setAdapter(itemDisplayAdapter);

        } catch(Exception e) {
            System.out.println(e);

            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, "Error fetching data!", Toast.LENGTH_SHORT);
            toast.show();

        }

        clientSelector = findViewById(R.id.clientSelector);
        clientIdTv = findViewById(R.id.selectedClient);

        String clientDataURL = testEndpoint + "/api/get/all/clients/";
        HttpGetRequest clientGetter = new HttpGetRequest();
        try {
            String allClients= clientGetter.execute(clientDataURL).get();
            clientArray = new JSONArray(allClients);

            clients = new ArrayList<>();
            clientsNums = new ArrayList<>();
            for (int i = 0; i < clientArray.length(); i++) {
                clients.add(clientArray.getJSONObject(i).getString("clientName"));
                clientsNums.add(clientArray.getJSONObject(i).getString("clientId"));
            }

            ArrayAdapter<String> clientDisplayAdapter = new ArrayAdapter<>(search.this, android.R.layout.simple_spinner_item, clients);
            clientDisplayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            clientSelector.setAdapter(clientDisplayAdapter);

            // by default, the first warehouse will be selected
            CLIENTID = clientArray.getJSONObject(0).getString("clientId");
            clientIdTv.setText(CLIENTID);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // logic to define what happens when a new client is selected
        clientSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {
                    CLIENTID = clientArray.getJSONObject(position).getString("clientId");
                    clientIdTv.setText(CLIENTID);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        allClients = findViewById(R.id.selectAllClients);
        // allClients checkbox selected or deselected
        allClients.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println(clientsNums.toString());
                if (isChecked) {
                    String selectedClients = TextUtils.join(" ", clientsNums);
                    clientIdTv.setText(selectedClients);
                    clientSelector.setEnabled(false);
                } else {
                    clientIdTv.setText(CLIENTID);
                    clientSelector.setEnabled(true);
                }
            }
        });

        itemSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Spinner itemDetailsSelector = findViewById(R.id.itemDetailsSelector);

                try {
                    queryItemName = allItemsArray.getJSONObject(i).getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("Item Click","Position: " + i);

                JSONArray allVariants = null;
                itemVariantsObject.clear();
                itemVariantIds.clear();

                try {
                    allVariants = allItemsArray.getJSONObject(i).getJSONArray("description");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (int j = 0; j < allVariants.length(); j++) {
//                    Log.e("sel: ", allVariants.getString(j));
                    try {
                        itemVariants.add(allVariants.getString(j));
                        String key = allItemsArray.getJSONObject(i).getString("name") + allVariants.getString(j);
                        String val = allItemsArray.getJSONObject(i).getJSONArray("itemId").getString(j);

                        itemVariantsObject.add(new MultiSelectModel(Integer.parseInt(val), allVariants.getString(j)));
                        itemVariantIds.add(val);

                        Log.e("inserting-key", key);
                        Log.e("inserting-val", val);

                        itemDescription2IdMapper.put(key, val);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    queryItemVariant = allVariants.getString(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ArrayAdapter<String> descriptionAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, itemVariants);
                descriptionAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

                if (selectAllVariants.isChecked()) {
                    Log.e("doingDuty", "Now I must update the values");
                    Log.e("itemsOnMyBucket", itemVariantIds.toString());
                    queryIds.clear();

                    for (int j = 0; j < itemVariantIds.size(); j++) {
                        queryIds.add(String.valueOf(itemVariantIds.get(j)));
                    }
                }


                //adapter.setDropDownViewResource(R.layout.custom_spinner);
//                itemDetailsSelector.setAdapter(descriptionAdapter);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });


        String getWarehouseDataURL = testEndpoint + "/api/get/all/warehouses/";

        ArrayList<String> countryNames = null;

        HttpGetRequest warehouseGetter = new HttpGetRequest();
        try {
            String allItems = warehouseGetter.execute(getWarehouseDataURL).get();
            allCountriesArray = new JSONArray(allItems);

            countryNames = new ArrayList<>();
            for (int i = 0; i < allCountriesArray.length(); i++) {
                String wLocation = allCountriesArray.getJSONObject(i).getString("warehouseName");

                countryNames.add(wLocation);
                listOfCountries.add(new MultiSelectModel(i, wLocation));

//                JSONArray wIds = allCountriesArray.getJSONObject(i).getJSONArray("warehouseId");
////                for (int j = 0; j < wIds.length(); j++) {
////                    warehouseLocation2IdMapper.put(wLocation, wIds.getString(j));
////                }
//
//                // String outputLocation = TextUtils.join(" ", (Iterable) wIds);
//                String outputLocation = "";
//                for (int k = 0; k <wIds.length(); k++) {
//                    outputLocation += (wIds.getString(k) + " ");
//                }

                String outputLocation = allCountriesArray.getJSONObject(i).getString("warehouseId");
                warehouseLocation2IdMapper.put(wLocation, outputLocation);
            }

        } catch(Exception e) {
            System.out.println(e);

            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, "Error fetching data!", Toast.LENGTH_SHORT);
            toast.show();

            return;
        }





        // handling on all variants checkbox selected
        selectAllVariants.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Button variantSelector = findViewById(R.id.variantSelector);
                if (b) {
                    variantSelector.setEnabled(false);

                    queryIds.clear();

                    for (int i = 0; i < itemVariantIds.size(); i++) {
                        queryIds.add(String.valueOf(itemVariantIds.get(i)));
                    }
                } else {
                    variantSelector.setEnabled(true);
                }
            }
        });


        // handling on all locations checkbox selected
        selectAllLocations.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Button locationSelector = findViewById(R.id.locationSelector);
                if (b) {
                    locationSelector.setEnabled(false);

                    TextView selectedTexts = findViewById(R.id.selectedLocations);
                    selectedTexts.setText("");

                    queryLocations.clear();

                    StringBuffer selectedNamesDisplay = new StringBuffer();
                    for (int i = 0; i < allCountriesArray.length(); i++) {
                        try {
                            String wId = allCountriesArray.getJSONObject(i).getString("warehouseName");
                            queryLocations.add(wId);
                            selectedTexts.append(wId);
                            selectedTexts.append("\n");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    locationSelector.setEnabled(true);

                    TextView selectedTexts = findViewById(R.id.selectedLocations);
                    selectedTexts.setText("");
                }
            }
        });

    }

    public void selectLocations(View view) {
        /*
         * Getting array of String to Bind in Spinner
         */
        final List<String> list = Arrays.asList(getResources().getStringArray(R.array.items));
        final Boolean[] foobar = {true, true, false};


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
                        // will return list of selected IDS
//                        for (int i = 0; i < selectedIds.size(); i++) {
//                            Toast.makeText(search.this, "Selected Ids : " + selectedIds.get(i) + "\n" +
//                                    "Selected Names : " + selectedNames.get(i) + "\n" +
//                                    "DataString : " + dataString, Toast.LENGTH_LONG).show();
//                        }

                        TextView selectedTexts = findViewById(R.id.selectedLocations);
                        selectedTexts.setText("");

                        queryLocations.clear();

                        StringBuffer selectedNamesDisplay = new StringBuffer();
                        for (int i = 0; i < selectedNames.size(); i++) {
                            queryLocations.add(selectedNames.get(i));
                            selectedTexts.append(selectedNames.get(i));
                            selectedTexts.append("\n");
                        }

                        // selectedTexts.setText("Selected Locations are " + selectedNames.toString());


                    }

                    @Override
                    public void onCancel() {
                        System.out.println("cancelled");
                    }


                });

        multiSelectDialog.show(getSupportFragmentManager(), "multiSelectDialog");
    }

    public void startSearch(View view) {

//        Spinner itemDetailsSelector = findViewById(R.id.itemDetailsSelector);
//        queryItemVariant = itemDetailsSelector.getSelectedItem().toString();
//
//        String queryId = (String) itemDescription2IdMapper.get(queryItemName + queryItemVariant);
//
//        Context context = getApplicationContext();
//        Toast toast = Toast.makeText(context, queryId, Toast.LENGTH_SHORT);
//        toast.show();


        Intent viewInventoryPage = new Intent(getApplicationContext(), view_inventory.class);

        String queryLocationsString = "";
        for (int i = 0; i < queryLocations.size(); i++) {
            queryLocationsString += (warehouseLocation2IdMapper.get(queryLocations.get(i)) + " ");
        }

        String queryIdsString = "";
        for (int i = 0; i < queryIds.size(); i++) {
            queryIdsString += (queryIds.get(i) + " ");
        }

        viewInventoryPage.putExtra("locations", queryLocationsString);
        viewInventoryPage.putExtra("id", queryIdsString);
        viewInventoryPage.putExtra("clients", clientIdTv.getText());

        Log.e("locations", queryLocationsString);
        Log.e("id", queryIdsString);

        startActivity(viewInventoryPage);
        finish();
    }

    public void selectVariant(View view) {

        /*
         * Getting array of String to Bind in Spinner
         */
        final List<String> list = Arrays.asList(getResources().getStringArray(R.array.items));
        final Boolean[] foobar = {true, true, false};


        ArrayList<Integer> alreadySelectedCountries = new ArrayList<>();


        MultiSelectDialog multiSelectDialog = new MultiSelectDialog()
                .title("Select Variant(s)") //setting title for dialog
                .titleSize(25)
                .positiveText("Done")
                .negativeText("Cancel")
                .setMinSelectionLimit(1) //you can set minimum checkbox selection limit (Optional)
                .setMaxSelectionLimit(itemVariantsObject.size()) //you can set maximum checkbox selection limit (Optional)
                .preSelectIDsList(alreadySelectedCountries) //List of ids that you need to be selected
                .multiSelectList(itemVariantsObject) // the multi select model list with ids and name
                .onSubmit(new MultiSelectDialog.SubmitCallbackListener() {
                    @Override
                    public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                        Log.e("selectedIds", selectedIds.toString());
                        // will return list of selected IDS
//                        for (int i = 0; i < selectedIds.size(); i++) {
//                            Toast.makeText(search.this, "Selected Ids : " + selectedIds.get(i) + "\n" +
//                                    "Selected Names : " + selectedNames.get(i) + "\n" +
//                                    "DataString : " + dataString, Toast.LENGTH_LONG).show();
//                        }

//                        TextView selectedTexts = findViewById(R.id.selectedLocations);
//                        selectedTexts.setText("");
//
//                        StringBuffer selectedNamesDisplay = new StringBuffer();

                        queryIds.clear();

                        for (int i = 0; i < selectedIds.size(); i++) {
                            queryIds.add(String.valueOf(selectedIds.get(i)));
                        }

                        // selectedTexts.setText("Selected Locations are " + selectedNames.toString());


                    }

                    @Override
                    public void onCancel() {
                        System.out.println("cancelled");
                    }


                });

        multiSelectDialog.show(getSupportFragmentManager(), "multiSelectDialog");

    }
}
