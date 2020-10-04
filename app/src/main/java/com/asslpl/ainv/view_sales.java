package com.asslpl.ainv;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import it.beppi.tristatetogglebutton_library.TriStateToggleButton;

import static java.security.AccessController.getContext;

public class view_sales extends AppCompatActivity {

    private List<Overview> dataToShow = new ArrayList<>();
    private List<Overview> headerData = new ArrayList<>();

    private static final String[] TABLE_HEADERS = { "In/Out", "BE / Pur. Inv.", "Sales Invoice", "In/Out Date", "Item", "Client", "Warehouse", "Customer", "Carton Quantity", "Total Value", "Full Paid ?", "Paid Amount", "Balance", "Cum. Balance", "Expd. Pymt. Date" };

    public void retrieveData(String filter) {
        Intent intent = getIntent();
        Context context = getApplicationContext();

        String invoiceNumber =  intent.getStringExtra("invoiceNumber");
        String clientId = intent.getStringExtra("clientId");
        String customerId = intent.getStringExtra("customerId");
        String itemFilter = intent.getStringExtra("itemFilter");

        String testEndpoint = getResources().getString(R.string.serverEndpoint);
        String searchURL = testEndpoint + "/api/search/overview/";
        String searchRequests = "salesInvoiceNumber=" + invoiceNumber + "&clientId=" + clientId + "&customerId=" + customerId + "&filter=" + filter + "&itemName=" + itemFilter;

        String searchResponse = "NULL";
        JSONArray searchResponseArray = null;
        ArrayList<JSONObject> salesCollection = new ArrayList<>();

        HttpPostRequest searchHttp = new HttpPostRequest();
        try {
            searchResponse = searchHttp.execute(searchURL, searchRequests).get();
            searchResponseArray = new JSONArray(searchResponse);

            float cumulativeBalance = 0;

            salesCollection = new ArrayList<>();
            for (int i = 0; i < searchResponseArray.length(); i++) {
                JSONObject salesTicket = searchResponseArray.getJSONObject(i);
                double balanceValue = Double.parseDouble(salesTicket.getString("totalValue")) - Double.parseDouble(salesTicket.getString("paidAmount"));

                if (salesTicket.getString("direction").equals("out")) {
                    cumulativeBalance += balanceValue;
                }

                Overview foo = new Overview(salesTicket.getString("billOfEntryId"), salesTicket.getString("billOfEntry"), salesTicket.getString("salesInvoiceId"), salesTicket.getString("salesInvoice"), salesTicket.getString("direction"), salesTicket.getString("entryDate"), salesTicket.getString("item"), salesTicket.getString("warehouse"), salesTicket.getString("client"), salesTicket.getString("customer"), salesTicket.getString("bigQuantity"), salesTicket.getString("totalValue"), salesTicket.getString("isPaid"), salesTicket.getString("paidAmount"), String.valueOf(balanceValue), String.valueOf(cumulativeBalance), salesTicket.getString("date"), invoiceNumber, clientId, customerId);
                dataToShow.add(foo);
            }

//            Toast toast = Toast.makeText(context, searchResponse, Toast.LENGTH_LONG);
//            toast.show();

        } catch (ExecutionException e) {
            searchResponse = "EXC";
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
            searchResponse = "IP";
        } catch (JSONException e) {
            e.printStackTrace();
        }


        TableView tableView = (TableView) findViewById(R.id.tableView);
//        tableView.setColumnCount(4);
        SimpleTableHeaderAdapter sha = new SimpleTableHeaderAdapter(this, TABLE_HEADERS);
        sha.setTextSize(14);
        tableView.setHeaderAdapter(sha);
        tableView.setDataAdapter(new OverviewTableAdapter (this, dataToShow));

        // tableView.setDataAdapter(new InvoiceTableDataAdapter (this, dataToShow));
    }

    public void loadItems() {

        String testEndpoint = getResources().getString(R.string.serverEndpoint);
        String searchURL = testEndpoint + "/api/get/items/?only=itemName";

        String searchResponse = "NULL";
        JSONArray availableItems = null;

        HttpGetRequest searchHttp = new HttpGetRequest();
        try {
            searchResponse = searchHttp.execute(searchURL).get();
            availableItems = new JSONArray(searchResponse);

        } catch (ExecutionException e) {
            searchResponse = "EXC";
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
            searchResponse = "IP";
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<String> availableItemsList = new ArrayList<>();
        availableItemsList.add("all");
        for (int i = 0; i < availableItems.length(); i++) {
            try {
                availableItemsList.add(availableItems.getString(i));
            } catch (JSONException e) {
                System.out.println("Skipping item due to JSON error");
            }
        }
        Spinner itemSelector = findViewById(R.id.itemSelector);

        ArrayAdapter<String> itemSelectorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, availableItemsList);
        itemSelectorAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        itemSelector.setAdapter(itemSelectorAdapter);

        Intent intent = getIntent();
        intent.putExtra("itemFilter", "all");

        itemSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = availableItemsList.get(i);

                Intent intent = getIntent();
                intent.putExtra("itemFilter", selectedItem);

                dataToShow.clear();

                String partialFilter = intent.getStringExtra("partialData");
                retrieveData(partialFilter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sales);

        Intent intent = getIntent();
        intent.putExtra("partialData", "all");

        TriStateToggleButton filterSwitcher = (TriStateToggleButton) findViewById(R.id.filterSwitcher);
        filterSwitcher.setOnToggleChanged(new TriStateToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(TriStateToggleButton.ToggleStatus toggleStatus, boolean booleanToggleStatus, int toggleIntValue) {

                dataToShow.clear();

                Intent intent = getIntent();

                switch (toggleStatus) {
                    case off: retrieveData("in");
                        intent.putExtra("partialData", "in");
                        break;
                    case mid: retrieveData("all");
                        intent.putExtra("partialData", "all");
                        break;
                    case on: retrieveData("out");
                        intent.putExtra("partialData", "out");
                        break;
                }
            }
        });

        loadItems();
        retrieveData("all");

    }
}
