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

public class view_sales extends AppCompatActivity {

    private List<Invoice> dataToShow = new ArrayList<>();
    private List<Invoice> headerData = new ArrayList<>();

    private static final String[] TABLE_HEADERS = { "Sales Invoice Number", "Entry Date", "Item", "Warehouse", "Client Name", "Change in Stock", "Total Pieces", "Total Payment", "Is Paid?", "Paid Amount", "Expected Payment Date" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sales);

        Intent intent = getIntent();
        Context context = getApplicationContext();


        String invoiceNumber =  intent.getStringExtra("invoiceNumber");
        String clientId = intent.getStringExtra("clientId");

        String testEndpoint = getResources().getString(R.string.serverEndpoint);
        String searchURL = testEndpoint + "/api/search/sales/";
        String searchRequests = "salesInvoiceNumber=" + invoiceNumber + "&clientId=" + clientId;

        String searchResponse = "NULL";
        JSONArray searchResponseArray = null;
        ArrayList<JSONObject> salesCollection = new ArrayList<>();

        HttpPostRequest searchHttp = new HttpPostRequest();
        try {
            searchResponse = searchHttp.execute(searchURL, searchRequests).get();
            searchResponseArray = new JSONArray(searchResponse);

            salesCollection = new ArrayList<>();
            for (int i = 0; i < searchResponseArray.length(); i++) {
                JSONObject salesTicket = searchResponseArray.getJSONObject(i);
                Invoice foo = new Invoice(salesTicket.getString("transactionId"), salesTicket.getString("trackingNumber"), salesTicket.getString("entryDate"), salesTicket.getString("itemId"), salesTicket.getString("itemName"), salesTicket.getString("itemVariant"), salesTicket.getString("warehouseId"), salesTicket.getString("warehouseName"), salesTicket.getString("warehouseLocation"), salesTicket.getString("clientId"), salesTicket.getString("clientName"), salesTicket.getString("changeStock"), salesTicket.getString("finalStock"), salesTicket.getString("totalPcs"), salesTicket.getString("materialValue"), salesTicket.getString("gstValue"), salesTicket.getString("totalValue"), salesTicket.getString("valuePerPiece"), salesTicket.getString("isPaid"), salesTicket.getString("paidAmount"), salesTicket.getString("paymentDate"));
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
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, TABLE_HEADERS));
        tableView.setDataAdapter(new InvoiceTableDataAdapter (this, dataToShow));

    }
}
