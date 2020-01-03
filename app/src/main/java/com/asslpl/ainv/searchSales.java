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

    Spinner clientSelector;

    String CLIENTID = "-1";
    TextView clientIdTv;
    TextView salesTv;

    EditText numberBox;

    CheckBox allSales;
    CheckBox allClients;

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

        // allSales checkbox selected or deselected
        allSales = findViewById(R.id.selectAllInvoice);
        allClients = findViewById(R.id.selectAllClients);
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

        viewSalesPage.putExtra("invoiceNumber", selectedSalesTv.getText());
        viewSalesPage.putExtra("clientId", selectedClientTv.getText());
        startActivity(viewSalesPage);
        finish();
    }
}
