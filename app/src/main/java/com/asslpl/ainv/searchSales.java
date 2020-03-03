package com.asslpl.ainv;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class searchSales extends AppCompatActivity {

    JSONArray clientArray = null;
    ArrayList<String> clients = null;
    JSONArray customerArray = null;
    ArrayList<String> customers = null;

    Spinner clientSelector;
    Spinner customerSelector;

    String CLIENTID = "-1";
    TextView clientIdTv;
    TextView salesTv;
    String CUSTID = "-1";
    TextView customerTv;

    EditText numberBox;

    CheckBox allSales;
    CheckBox allClients;
    CheckBox allCustomers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_sales);

        clientSelector = findViewById(R.id.clientSelector);
        clientIdTv = findViewById(R.id.selectedClient);

        String testEndpoint = getResources().getString(R.string.serverEndpoint);
        String clientDataURL = testEndpoint + "/api/get/all/clients/";
        HttpGetRequest clientGetter = new HttpGetRequest();
        try {
            String allClients= clientGetter.execute(clientDataURL).get();
            clientArray = new JSONArray(allClients);

            clients = new ArrayList<>();
            for (int i = 0; i < clientArray.length(); i++) {
                clients.add(clientArray.getJSONObject(i).getString("clientName"));
            }

            ArrayAdapter<String> clientDisplayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, clients);
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

        // --- customer spinner population starts

        customerSelector = findViewById(R.id.customerSelector);
        customerTv = findViewById(R.id.selectedCustomers);

        String customerDataURL = testEndpoint + "/api/get/all/customers/";
        HttpGetRequest customerGetter = new HttpGetRequest();
        try {
            String allCustomers = customerGetter.execute(customerDataURL).get();
            customerArray = new JSONArray(allCustomers);

            customers = new ArrayList<>();
            for (int i = 0; i < customerArray.length(); i++) {
                customers.add(customerArray.getJSONObject(i).getString("customerName"));
            }

            ArrayAdapter<String> customerDisplayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, customers);
            customerDisplayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            customerSelector.setAdapter(customerDisplayAdapter);

            CUSTID = customerArray.getJSONObject(0).getString("customerId");
            customerTv.setText(CUSTID);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // logic to define what happens when a new client is selected
        customerSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {
                    CUSTID = customerArray.getJSONObject(position).getString("customerId");
                    customerTv.setText(CUSTID);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // --- customer spinner population ends

        // allSales checkbox selected or deselected
        allSales = findViewById(R.id.selectAllInvoice);
        allClients = findViewById(R.id.selectAllClients);
        allCustomers = findViewById(R.id.selectAllCustomers);
        numberBox = findViewById(R.id.trackingNumber);
        salesTv = findViewById(R.id.selectedSales);

        allSales.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    salesTv.setText("all");
                    numberBox.setEnabled(false);
                } else {
                    salesTv.setText(numberBox.getText());
                    numberBox.setEnabled(true);
                }
            }
        });

        // allClients checkbox selected or deselected
        allClients.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    clientIdTv.setText("all");
                    clientSelector.setEnabled(false);
                } else {
                    clientIdTv.setText(CLIENTID);
                    clientSelector.setEnabled(true);
                }
            }
        });

        // allClients checkbox selected or deselected
        allCustomers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    customerTv.setText("all");
                    customerSelector.setEnabled(false);
                } else {
                    customerTv.setText(CUSTID);
                    customerSelector.setEnabled(true);
                }
            }
        });

        // logic to update the salesNumber when typed
        numberBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                salesTv.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    public void startSearch(View view) {
        Intent viewSalesPage = new Intent(getApplicationContext(), view_sales.class);

        TextView selectedSalesTv = findViewById(R.id.selectedSales);
        TextView selectedClientTv = findViewById(R.id.selectedClient);
        TextView selectedCustomerTv = findViewById(R.id.selectedCustomers);

        viewSalesPage.putExtra("invoiceNumber", selectedSalesTv.getText());
        viewSalesPage.putExtra("clientId", selectedClientTv.getText());
        viewSalesPage.putExtra("customerId", selectedCustomerTv.getText());
        startActivity(viewSalesPage);
        finish();
    }
}
