package com.asslpl.ainv;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Transaction extends AppCompatActivity {

    Dialog dialog;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    public ViewPager mViewPager;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transaction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public List<String> getItemInventory(View view, String warehouseId, String itemId) {

        String testEndpoint = getResources().getString(R.string.serverEndpoint);
        String rateURL = testEndpoint + "/api/get/rate/";
        String searchRequests = "itemId=" + itemId + "&warehouseId=" + warehouseId;

        String searchResponse = null;
        JSONObject searchResponseDict = null;

        HttpPostRequest ratehHttp = new HttpPostRequest();
        try {
            searchResponse = ratehHttp.execute(rateURL, searchRequests).get();
            searchResponseDict = new JSONArray(searchResponse).getJSONObject(0);

            System.out.println(searchResponseDict);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String rawPerSmall = "0";
        String smallPerBig = "0";
        String cartonQuantity = "0";

        try {

            rawPerSmall = searchResponseDict.getString("rawPerSmall");
            smallPerBig = searchResponseDict.getString("smallPerBig");
            cartonQuantity = searchResponseDict.getString("cartonQuantity");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Arrays.asList(rawPerSmall, smallPerBig, cartonQuantity);

    }

    public void gotoSecondPage(View view) {


        // get the itemId and warehouseId from first page
        TextView itemIdTv = mViewPager.getRootView().findViewById(R.id.itemId);
        TextView warehouseIdTv = mViewPager.getRootView().findViewById(R.id.warehouseId);
        TextView comeOrGoTv = mViewPager.getRootView().findViewById(R.id.comeOrGo);


        try {
            String itemId = String.valueOf(itemIdTv.getText());
            String warehouseId = String.valueOf(warehouseIdTv.getText());

            List<String> response = getItemInventory(view, warehouseId, itemId);

            mViewPager.setCurrentItem(2);
            TextView comeOrGoThirdPageTv = mViewPager.getRootView().findViewById(R.id.comeOrGoThird);
            comeOrGoThirdPageTv.setText(comeOrGoTv.getText());

            mViewPager.setCurrentItem(1);
            TextView currentTv = mViewPager.getRootView().findViewById(R.id.currentValue);
            currentTv.setText(response.get(2));

            TextView secretRate1 = mViewPager.getRootView().findViewById(R.id.secretRate1);
            TextView secretRate2 = mViewPager.getRootView().findViewById(R.id.secretRate2);

            secretRate1.setText(response.get(0));
            secretRate2.setText(response.get(1));

            TextView comeOrGoSecondPageTv = mViewPager.getRootView().findViewById(R.id.comeOrGoSecond);
            comeOrGoSecondPageTv.setText(comeOrGoTv.getText());

        } catch (Exception e) {
            mViewPager.setCurrentItem(1);
        }
    }

    public void gotoFirstPage(View view) {
        mViewPager.setCurrentItem(0);
    }

    // called when CONFIRM TRANSACTION button is hit - posts the collected data to the server
    public void submitTransaction(View view) {

        // picking values from the first page
        mViewPager.setCurrentItem(0);
        TextView fe1p1 = mViewPager.getRootView().findViewById(R.id.billOfEntry);
        TextView fe2p1 = mViewPager.getRootView().findViewById(R.id.entryDateHeader);
        TextView fe3p1 = mViewPager.getRootView().findViewById(R.id.itemId);
        TextView fe4p1 = mViewPager.getRootView().findViewById(R.id.warehouseId);
        TextView fe5p1 = mViewPager.getRootView().findViewById(R.id.comeOrGo);
        TextView fe6p1 = mViewPager.getRootView().findViewById(R.id.clientId);

        // picking values from the second page
        mViewPager.setCurrentItem(1);
        TextView fe1p2 = mViewPager.getRootView().findViewById(R.id.bigQuantity);
        TextView fe2p2 = mViewPager.getRootView().findViewById(R.id.currentValue);
        TextView fe3p2 = mViewPager.getRootView().findViewById(R.id.changeValue);
        TextView fe4p2 = mViewPager.getRootView().findViewById(R.id.finalValue);
        TextView fe5p2 = mViewPager.getRootView().findViewById(R.id.secretRate1);
        TextView fe6p2 = mViewPager.getRootView().findViewById(R.id.secretRate2);
        TextView fe7p2 = mViewPager.getRootView().findViewById(R.id.totalPcs);

        // picking values from the third page
        mViewPager.setCurrentItem(2);
        TextView fe1p3 = mViewPager.getRootView().findViewById(R.id.assdValue);
        TextView fe2p3 = mViewPager.getRootView().findViewById(R.id.dutyValue);
        TextView fe3p3 = mViewPager.getRootView().findViewById(R.id.gstValue);
        TextView fe4p3 = mViewPager.getRootView().findViewById(R.id.totalValue);
        TextView fe5p3 = mViewPager.getRootView().findViewById(R.id.valuePerPiece);
        TextView fe6p3 = mViewPager.getRootView().findViewById(R.id.totalPieces);
        CheckBox fe7p3 = mViewPager.getRootView().findViewById(R.id.isPaid);
        TextView fe8p3 = mViewPager.getRootView().findViewById(R.id.expectedDateHeader);
        EditText fe9p3 = mViewPager.getRootView().findViewById(R.id.paidAmount);

        // posting the data to the server
        System.out.println(fe1p1.getText());
        System.out.println("------");
        System.out.println(fe1p2.getText());
        System.out.println("------");
        System.out.println(fe1p3.getText());

        // sending data
        String testEndpoint = getResources().getString(R.string.serverEndpoint);
        String transactionURL = testEndpoint + "/api/put/transaction/";

        String plusOrMinus = fe5p1.getText().toString();
        String transactionResponse= "NULL";

        if (plusOrMinus.equals("-")) {
            plusOrMinus = "out";
        } else {
            plusOrMinus = "in";
        }

        String data = "trackingNumber=" + fe1p1.getText() + "&entryDate=" + fe2p1.getText() + "&itemId=" + fe3p1.getText() + "&warehouseId=" + fe4p1.getText() + "&comeOrGo=" + plusOrMinus +"&clientId=" + fe6p1.getText() + "&bigQuantity=" + fe1p2.getText() +"&currentValue=" + fe2p2.getText() + "&changeValue=" + fe3p2.getText() +"&finalValue=" + fe4p2.getText() + "&secretRate1=" + fe5p2.getText() +"&secretRate2=" + fe6p2.getText() + "&totalPcs=" + fe7p2.getText() +"&assdValue=" + fe1p3.getText() + "&dutyValue=" + fe2p3.getText() +"&gstValue=" + fe3p3.getText() + "&totalValue=" + fe4p3.getText() +"&valuePerPiece=" + fe5p3.getText() + "&totalPieces=" + fe6p3.getText() +"&isPaid=" + fe7p3.isChecked() + "&paidAmount=" + fe9p3.getText() + "&date=" + fe8p3.getText();
        Log.e("postingData", data);

        HttpPostRequest insertHttp = new HttpPostRequest();
        try {
            transactionResponse = insertHttp.execute(transactionURL, data).get();
            JSONObject transactionResponseJson = new JSONObject(transactionResponse);

            Toast toast;

            if (transactionResponseJson.getBoolean("success")) {
                toast = Toast.makeText(getApplicationContext(), "Transaction successful!", Toast.LENGTH_LONG);

                // finish this activity upon successful completion
                finish();
            } else {
                toast = Toast.makeText(getApplicationContext(), "Transaction error!", Toast.LENGTH_LONG);
            }

            toast.show();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static int getWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public void selectNewRates(View view) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_rate);
        dialog.show();

        dialog.setCancelable(true);

        final TextView secretRate1 = mViewPager.getRootView().findViewById(R.id.secretRate1);
        final TextView secretRate2 = mViewPager.getRootView().findViewById(R.id.secretRate2);
        final TextView quantityDescription = mViewPager.getRootView().findViewById(R.id.quantityDescription);
        final EditText bigQuantity = mViewPager.getRootView().findViewById(R.id.bigQuantity);
        final TextView totalPcs = mViewPager.getRootView().findViewById(R.id.totalPcs);

        final TextView pcsPerBox = dialog.findViewById(R.id.pcsPerBox);
        final TextView boxPerCarton = dialog.findViewById(R.id.boxPerCarton);

        pcsPerBox.setText(secretRate1.getText());
        boxPerCarton.setText(secretRate2.getText());

        Button updateButton = dialog.findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                secretRate1.setText(pcsPerBox.getText());
                secretRate2.setText(boxPerCarton.getText());
                dialog.dismiss();

                int cartonCount = Integer.parseInt(String.valueOf(bigQuantity.getText()));
                int boxCount = cartonCount * Integer.parseInt(String.valueOf(secretRate1.getText()));
                int pcsCount = boxCount * Integer.parseInt(String.valueOf(secretRate2.getText()));

                String quantityDisplayer = String.format("%s carton = %s box = %s pcs", cartonCount, boxCount, pcsCount);
                quantityDescription.setText(quantityDisplayer);

                totalPcs.setText(Integer.toString(pcsCount));

            }
        });
    }

    public void gotoThirdPage(View view) {

        TextView totalPcs = mViewPager.getRootView().findViewById(R.id.totalPcs);
        String totalPcsCount = String.valueOf(totalPcs.getText());

        mViewPager.setCurrentItem(2);

        TextView totalPieces = mViewPager.getRootView().findViewById(R.id.totalPieces);
        totalPieces.setText(totalPcsCount);

        TextView comeOrGoThirdPage = mViewPager.getRootView().findViewById(R.id.comeOrGoThird);
        CheckBox paidTv = mViewPager.getRootView().findViewById(R.id.isPaid);
        TextView paymentDate = mViewPager.getRootView().findViewById(R.id.expectedDate);
        TextView paymentDateHeader = mViewPager.getRootView().findViewById(R.id.expectedDateHeader);
        EditText assdValue = mViewPager.getRootView().findViewById(R.id.assdValue);

        final EditText totalValue = mViewPager.getRootView().findViewById(R.id.totalValue);
        final EditText paidAmount = mViewPager.getRootView().findViewById(R.id.paidAmount);

        if (comeOrGoThirdPage.getText() == "+") {
            paidTv.setVisibility(View.INVISIBLE);
            paymentDate.setVisibility(View.INVISIBLE);
            paymentDateHeader.setVisibility(View.INVISIBLE);

            TextView assessedValueHeader = mViewPager.getRootView().findViewById(R.id.assdValueHeader);
            assessedValueHeader.setVisibility(View.VISIBLE);
            assdValue.setVisibility(View.VISIBLE);

            TextView dutyValueHeader = mViewPager.getRootView().findViewById(R.id.dutyValueHeader);
            dutyValueHeader.setText("Duty Value");

        } else {
            paidTv.setVisibility(View.VISIBLE);
            paymentDate.setVisibility(View.VISIBLE);
            paymentDateHeader.setVisibility(View.VISIBLE);

            TextView assessedValueHeader = mViewPager.getRootView().findViewById(R.id.assdValueHeader);
            assessedValueHeader.setVisibility(View.INVISIBLE);
            assdValue.setText("0");
            assdValue.setVisibility(View.INVISIBLE);

            TextView dutyValueHeader = mViewPager.getRootView().findViewById(R.id.dutyValueHeader);
            dutyValueHeader.setText("Material Value");
        }

        paidTv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    paidAmount.setText(totalValue.getText());
                    paidAmount.setEnabled(false);
                } else {
                    paidAmount.setText("");
                    paidAmount.setEnabled(true);
                }
            }
        });
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        Spinner itemSelector;
        Spinner itemVariantSelector;
        Spinner warehouseSelector;
        Spinner clientSelector;

        TextView itemIdTv;
        TextView warehouseIdTv;
        TextView clientIdTv;

        ArrayList<String> itemNames = null;
        ArrayList<String> itemVariants = null;
        ArrayList<String> warehouses = null;
        ArrayList<String> clients = null;

        JSONArray allItemsArray = null;
        JSONArray itemVariantsArray = null;
        JSONArray warehouseArray = null;
        JSONArray clientArray = null;

        int itemParentPosition = 0;
        String ITEMID = "-1";
        String WAREHOUSEID = "-1";
        String CLIENTID = "-1";

        View rootView;


        // for the second view
        EditText bigQuantityEntry;
        TextView currentValue;
        TextView changeValue;
        TextView finalValue;
        RadioGroup entryExitSelector;
        TextView comeOrGo;
        TextView comeOrGoSecondPage;
        TextView comeOrGoThirdPage;
        TextView quantityDescription;

        TextView secretRate1;
        TextView secretRate2;

        TextView totalPcs;

        EditText assdValue;
        EditText dutyValue;
        EditText gstValue;
        EditText totalValue;
        EditText valuePerPiece;
        TextView totalPcsP3;

        TextView billOrSalesHeader;
        EditText billOrSalesText;

        float totalPiecesP3;


        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {


            int pageNumber = getArguments().getInt(ARG_SECTION_NUMBER);

            if (pageNumber == 1) {
                // first page - first details
                rootView = inflater.inflate(R.layout.fragment_transaction_p1, container, false);

                entryExitSelector = rootView.findViewById(R.id.radioSelection);
                comeOrGo = rootView.findViewById(R.id.comeOrGo);

                // changing the bill-of-entry header dynamically
                billOrSalesHeader = rootView.findViewById(R.id.billOfEntryHeader);
                billOrSalesText = rootView.findViewById(R.id.billOfEntry);

                entryExitSelector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        // understand which of the radio buttons has been clicked
                        if (R.id.incomingRadio == checkedId) {
                            comeOrGo.setText("+");
                            billOrSalesHeader.setText("Bill of Entry");
                            billOrSalesText.setHint("Bill of Entry Number");
                        } else if (R.id.outgoingRadio == checkedId) {
                            comeOrGo.setText("-");
                            billOrSalesHeader.setText("Sales Invoice");
                            billOrSalesText.setHint("Sales Invoice Number");
                        }

                    }
                });

                // set the calendar dialog
                Button dobSelector = rootView.findViewById(R.id.entryDate);
                dobSelector.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogFragment newFragment = new SelectDateFragment();
                            newFragment.show(getFragmentManager(), "Date Picker");
                        }
                    });



                // set the item and warehouse data

                String testEndpoint = getResources().getString(R.string.serverEndpoint);
                String itemDataURL = testEndpoint + "/api/get/items/";


                itemSelector = rootView.findViewById(R.id.itemName);
                itemVariantSelector = rootView.findViewById(R.id.itemVariant);

                itemIdTv = rootView.findViewById(R.id.itemId);
                warehouseIdTv = rootView.findViewById(R.id.warehouseId);

                // logic for populating the spinner and selecting the first item by default
                HttpGetRequest itemsGetter = new HttpGetRequest();
                try {
                    String allItems = itemsGetter.execute(itemDataURL).get();
                    allItemsArray = new JSONArray(allItems);

                    itemNames = new ArrayList<>();
                    for (int i = 0; i < allItemsArray.length(); i++) {
                        itemNames.add(allItemsArray.getJSONObject(i).getString("name"));
                    }

                    ArrayAdapter<String> itemDisplayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, itemNames);
                    itemDisplayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    itemSelector.setAdapter(itemDisplayAdapter);

                    // now the item variant spinner must also be populated

                    try {
                        itemVariantsArray = allItemsArray.getJSONObject(0).getJSONArray("description");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    itemVariants = new ArrayList<>();
                    for (int i = 0; i < itemVariantsArray.length(); i++) {
                        itemVariants.add(itemVariantsArray.getString(i));
                    }

                    ArrayAdapter<String> variantDisplayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, itemVariants);
                    variantDisplayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    itemVariantSelector.setAdapter(variantDisplayAdapter);

                    // set the default capturing variable (index 0 variant) of first item
                    ITEMID = allItemsArray.getJSONObject(0).getJSONArray("itemId").getString(0);
                    itemIdTv.setText(ITEMID);


                } catch(Exception e) {
                    System.out.println(e);

                    Toast toast = Toast.makeText(getContext(), "Error fetching data!", Toast.LENGTH_SHORT);
                    toast.show();

                }

                // logic when a new item name is selected
                itemSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        try {
                            itemVariantsArray = allItemsArray.getJSONObject(position).getJSONArray("description");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        itemVariants.clear();

                        for (int i = 0; i < itemVariantsArray.length(); i++) {
                            try {
                                itemVariants.add(itemVariantsArray.getString(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        ArrayAdapter<String> variantDisplayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, itemVariants);
                        variantDisplayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        itemVariantSelector.setAdapter(variantDisplayAdapter);

                        // update the item name index -- MUST --
                        itemParentPosition = position;

                        // set the default capturing variable (index 0 variant) on other item select
                        try {
                            ITEMID = allItemsArray.getJSONObject(position).getJSONArray("itemId").getString(0);
                            itemIdTv.setText(ITEMID);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                // logic when a new item variant is selected
                itemVariantSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        // set the default capturing variable (index 0 variant) on other item select
                        try {
                            ITEMID = allItemsArray.getJSONObject(itemParentPosition).getJSONArray("itemId").getString(position);
                            itemIdTv.setText(ITEMID);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                warehouseSelector = rootView.findViewById(R.id.warehouse);
                clientIdTv = rootView.findViewById(R.id.clientId);

                // logic for populating the warehouse spinner
                String warehouseDataURL = testEndpoint + "/api/get/all/warehouses/";
                HttpGetRequest warehouseGetter = new HttpGetRequest();
                try {
                    String allWarehouses = warehouseGetter.execute(warehouseDataURL).get();
                    warehouseArray = new JSONArray(allWarehouses);

                    warehouses = new ArrayList<>();
                    for (int i = 0; i < warehouseArray.length(); i++) {
                        warehouses.add(warehouseArray.getJSONObject(i).getString("warehouseName"));
                    }

                    ArrayAdapter<String> warehouseDisplayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, warehouses);
                    warehouseDisplayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    warehouseSelector.setAdapter(warehouseDisplayAdapter);

                    // by default, the first warehouse will be selected
                    WAREHOUSEID = warehouseArray.getJSONObject(0).getString("warehouseId");
                    warehouseIdTv.setText(WAREHOUSEID);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // logic to define what happens when a new warehouse location is selected
                warehouseSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        try {
                            WAREHOUSEID = warehouseArray.getJSONObject(position).getString("warehouseId");
                            warehouseIdTv.setText(WAREHOUSEID);

                            // get the inventory details for this configuration
                            // getItemInventory(getView(), WAREHOUSEID, ITEMID);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                // ------- //
                // logic for populating the client spinner
                clientSelector = rootView.findViewById(R.id.client);

                // logic for populating the warehouse spinner
                String clientDataURL = testEndpoint + "/api/get/all/clients/";
                HttpGetRequest clientGetter = new HttpGetRequest();
                try {
                    String allClients= clientGetter.execute(clientDataURL).get();
                    clientArray = new JSONArray(allClients);

                    clients = new ArrayList<>();
                    for (int i = 0; i < clientArray.length(); i++) {
                        clients.add(clientArray.getJSONObject(i).getString("clientName"));
                    }

                    ArrayAdapter<String> clientDisplayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, clients);
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

                            // get the inventory details for this configuration
                            // getItemInventory(getView(), WAREHOUSEID, ITEMID);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });



            } else if (pageNumber == 2) {

                // second page - quantity details
                rootView = inflater.inflate(R.layout.fragment_transaction_p2, container, false);

                bigQuantityEntry = rootView.findViewById(R.id.bigQuantity);

                currentValue = rootView.findViewById(R.id.currentValue);
                changeValue = rootView.findViewById(R.id.changeValue);
                finalValue = rootView.findViewById(R.id.finalValue);

                quantityDescription = rootView.findViewById(R.id.quantityDescription);

                secretRate1 = rootView.findViewById(R.id.secretRate1);
                secretRate2 = rootView.findViewById(R.id.secretRate2);

                totalPcs = rootView.findViewById(R.id.totalPcs);

                comeOrGoSecondPage = rootView.findViewById(R.id.comeOrGoSecond);

                bigQuantityEntry.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        int calculatedValue = 0;
                        int rate1 = Integer.parseInt(secretRate1.getText().toString());
                        int rate2 = Integer.parseInt(secretRate2.getText().toString());

                        try {
                            if (comeOrGoSecondPage.getText() == "+") {

                                changeValue.setText("+" + s.toString());
                                calculatedValue = Integer.parseInt(String.valueOf(currentValue.getText())) + Integer.parseInt(s.toString());

                                int cartonCount = Integer.parseInt(s.toString());
                                int boxCount = cartonCount * rate1;
                                int pcsCount = boxCount * rate2;

                                String quantityDisplayer = String.format("%s carton = %s box = %s pcs", cartonCount, boxCount, pcsCount);
                                quantityDescription.setText(quantityDisplayer);

                                totalPcs.setText(Integer.toString(pcsCount));

                            } else {

                                changeValue.setText("-" + s.toString());
                                calculatedValue = Integer.parseInt(String.valueOf(currentValue.getText())) - Integer.parseInt(s.toString());

                                int cartonCount = Integer.parseInt(s.toString());
                                int boxCount = cartonCount * rate1;
                                int pcsCount = boxCount * rate2;

                                String quantityDisplayer = String.format("%s carton = %s box = %s pcs", cartonCount, boxCount, pcsCount);
                                quantityDescription.setText(quantityDisplayer);

                                totalPcs.setText(Integer.toString(pcsCount));

                            }
                        } catch(Exception e) {
                            s = "0";
                            changeValue.setText("+" + s.toString());
                            calculatedValue = Integer.parseInt(String.valueOf(currentValue.getText()));

                            int cartonCount = Integer.parseInt(s.toString());
                            int boxCount = cartonCount * rate1;
                            int pcsCount = boxCount * rate2;

                            String quantityDisplayer = String.format("%s carton = %s box = %s pcs", cartonCount, boxCount, pcsCount);
                            quantityDescription.setText(quantityDisplayer);

                            totalPcs.setText(Integer.toString(pcsCount));
                        }

                        // System.out.println(calculatedValue);
                        finalValue.setText(Integer.toString(calculatedValue));

                    }

                    @Override

                    public void afterTextChanged(Editable s) {

                    }
                });


            } else if (pageNumber == 3) {
                // third page - item valuation details
                rootView = inflater.inflate(R.layout.fragment_transaction_p3, container, false);

                assdValue = rootView.findViewById(R.id.assdValue);
                dutyValue = rootView.findViewById(R.id.dutyValue);
                gstValue = rootView.findViewById(R.id.gstValue);
                totalValue = rootView.findViewById(R.id.totalValue);
                valuePerPiece = rootView.findViewById(R.id.valuePerPiece);

                totalPcsP3 = rootView.findViewById(R.id.totalPieces);

                // payment date handler
                Button payDobSelector = rootView.findViewById(R.id.expectedDate);
                payDobSelector.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogFragment newFragment = new SelectDateFragment();
                        newFragment.show(getFragmentManager(), "Date Picker");
                    }
                });

                // incoming or outgoing handler
                comeOrGoThirdPage = rootView.findViewById(R.id.comeOrGoThird);

                assdValue.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        float assdValueFloat = 0;
                        float dutyValueFloat = 0;
                        float gstValueFloat = 0;

                        try {
                            assdValueFloat = Float.parseFloat(String.valueOf(assdValue.getText()));
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                        try {
                            dutyValueFloat = Float.parseFloat(String.valueOf(dutyValue.getText()));
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                        try {
                            gstValueFloat = Float.parseFloat(String.valueOf(gstValue.getText()));
                        } catch (Exception e) {
                            System.out.println(e);
                        }

                        float totalValueFloat = assdValueFloat + dutyValueFloat + gstValueFloat;
                        totalValue.setText(Float.toString(totalValueFloat));
                        totalPiecesP3 = Float.parseFloat(String.valueOf(totalPcsP3.getText()));
                        valuePerPiece.setText(Float.toString(totalValueFloat / totalPiecesP3));

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                dutyValue.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        float assdValueFloat = 0;
                        float dutyValueFloat = 0;
                        float gstValueFloat = 0;

                        try {
                            assdValueFloat = Float.parseFloat(String.valueOf(assdValue.getText()));
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                        try {
                            dutyValueFloat = Float.parseFloat(String.valueOf(dutyValue.getText()));
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                        try {
                            gstValueFloat = Float.parseFloat(String.valueOf(gstValue.getText()));
                        } catch (Exception e) {
                            System.out.println(e);
                        }

                        float totalValueFloat = assdValueFloat + dutyValueFloat + gstValueFloat;
                        totalValue.setText(Float.toString(totalValueFloat));
                        totalPiecesP3 = Float.parseFloat(String.valueOf(totalPcsP3.getText()));
                        valuePerPiece.setText(Float.toString(totalValueFloat / totalPiecesP3));

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                gstValue.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        float assdValueFloat = 0;
                        float dutyValueFloat = 0;
                        float gstValueFloat = 0;

                        try {
                            assdValueFloat = Float.parseFloat(String.valueOf(assdValue.getText()));
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                        try {
                            dutyValueFloat = Float.parseFloat(String.valueOf(dutyValue.getText()));
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                        try {
                            gstValueFloat = Float.parseFloat(String.valueOf(gstValue.getText()));
                        } catch (Exception e) {
                            System.out.println(e);
                        }

                        float totalValueFloat = assdValueFloat + dutyValueFloat + gstValueFloat;
                        totalValue.setText(Float.toString(totalValueFloat));
                        totalPiecesP3 = Float.parseFloat(String.valueOf(totalPcsP3.getText()));
                        valuePerPiece.setText(Float.toString(totalValueFloat / totalPiecesP3));

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

            }

//            TextView textView = (TextView) rootView.findViewById(R.id.transactionHeading);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
