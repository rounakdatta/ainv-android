package com.asslpl.ainv;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import it.beppi.tristatetogglebutton_library.TriStateToggleButton;

public class view_sales extends AppCompatActivity {

    private List<Overview> dataToShow = new ArrayList<>();
    private List<Overview> headerData = new ArrayList<>();

    private static final String[] TABLE_HEADERS = { "In/Out", "BE / Pur. Inv.", "Sales Invoice", "In/Out Date", "Item", "Warehouse", "Client", "Customer", "Carton Quantity", "Total Value", "Full Paid ?", "Paid Amount", "Balance", "Cum. Balance", "Expd. Pymt. Date" };

    public void retrieveData(String filter) {
        Intent intent = getIntent();
        Context context = getApplicationContext();

        String invoiceNumber =  intent.getStringExtra("invoiceNumber");
        String clientId = intent.getStringExtra("clientId");
        String customerId = intent.getStringExtra("customerId");

        String testEndpoint = getResources().getString(R.string.serverEndpoint);
        String searchURL = testEndpoint + "/api/search/overview/";
        String searchRequests = "salesInvoiceNumber=" + invoiceNumber + "&clientId=" + clientId + "&customerId=" + customerId + "&filter=" + filter;

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

                Overview foo = new Overview(salesTicket.getString("billOfEntryId"), salesTicket.getString("billOfEntry"), salesTicket.getString("salesInvoiceId"), salesTicket.getString("salesInvoice"), salesTicket.getString("direction"), salesTicket.getString("entryDate"), salesTicket.getString("item"), salesTicket.getString("warehouse"), salesTicket.getString("client"), salesTicket.getString("customer"), salesTicket.getString("bigQuantity"), salesTicket.getString("totalValue"), salesTicket.getString("isPaid"), salesTicket.getString("paidAmount"), String.valueOf(balanceValue), String.valueOf(cumulativeBalance), salesTicket.getString("date"));
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sales);

        TriStateToggleButton filterSwitcher = (TriStateToggleButton) findViewById(R.id.filterSwitcher);
        filterSwitcher.setOnToggleChanged(new TriStateToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(TriStateToggleButton.ToggleStatus toggleStatus, boolean booleanToggleStatus, int toggleIntValue) {

                dataToShow.clear();

                switch (toggleStatus) {
                    case off: retrieveData("in"); break;
                    case mid: retrieveData("all"); break;
                    case on: retrieveData("out"); break;
                }
            }
        });

        retrieveData("all");
    }
}
