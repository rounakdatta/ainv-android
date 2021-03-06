package com.asslpl.ainv;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
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

public class view_inventory extends AppCompatActivity {

    private List<Inventory> dataToShow = new ArrayList<>();
    private List<Inventory> headerData = new ArrayList<>();

    private static final String[] TABLE_HEADERS = { "Client Name", "Item Description", "Item Variant Description", "Big Carton Quantity", "Small Box Quantity", "Item Variant Quantity", "Warehouse Name", "HSN Code"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);

        Intent intent = getIntent();
        Context context = getApplicationContext();


        String locationsRequest = intent.getStringExtra("locations");
        String idRequest = intent.getStringExtra("id");
        String clientRequest = intent.getStringExtra("clients");

        Log.e("locations", locationsRequest);
        Log.e("ids", idRequest);

        String testEndpoint = getResources().getString(R.string.serverEndpoint);
        String searchURL = testEndpoint + "/api/search/items/";
        String searchRequests = "itemId=" + idRequest + "&locations=" + locationsRequest + "&clients=" + clientRequest;

        String searchResponse = "NULL";
        JSONArray searchResponseArray = null;
        ArrayList<JSONObject> inventoryCollection = new ArrayList<>();

        HttpPostRequest searchHttp = new HttpPostRequest();
        try {
            searchResponse = searchHttp.execute(searchURL, searchRequests).get();
            searchResponseArray = new JSONArray(searchResponse);

            inventoryCollection = new ArrayList<>();
            for (int i = 0; i < searchResponseArray.length(); i++) {
                JSONObject newInv = searchResponseArray.getJSONObject(i);
                Inventory foo = new Inventory(newInv.getString("itemName"), newInv.getString("itemVariant"), newInv.getString("hsnCode"), newInv.getString("bigcartonQuantity"), newInv.getString("uomBig"), newInv.getString("smallboxQuantity"), newInv.getString("uomSmall"), newInv.getString("itemQuantity"), newInv.getString("uomRaw"), newInv.getString("warehouseName"), newInv.getString("warehouseLocation"), newInv.getString("clientName"));
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

//        Inventory foo = new Inventory("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11");
//        Inventory bar = new Inventory("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11");
//        dataToShow.add(foo);
//        dataToShow.add(bar);
//        dataToShow.add(bar);
//        dataToShow.add(bar);
//        dataToShow.add(bar);
//        dataToShow.add(bar);
//        dataToShow.add(bar);
//        dataToShow.add(bar);
//        dataToShow.add(bar);
//        dataToShow.add(bar);


//        headerData.add(foo);

        TableView tableView = (TableView) findViewById(R.id.tableView);
//        tableView.setColumnCount(4);
        SimpleTableHeaderAdapter sha = new SimpleTableHeaderAdapter(this, TABLE_HEADERS);
        sha.setTextSize(14);
        tableView.setHeaderAdapter(sha);

        tableView.setDataAdapter(new InventoryTableDataAdapter (this, dataToShow));

    }
}
