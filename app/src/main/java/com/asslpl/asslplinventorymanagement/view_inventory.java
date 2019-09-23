package com.asslpl.asslplinventorymanagement;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

    private static final String[] TABLE_HEADERS = { "Item Description", "Item Variant Description", "HSN Code", "Big Carton Quantity", "UoM", "Small Box Quantity", "UoM", "Item Variant Quantity", "UoM", "Warehouse Name", "Warehouse Location" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);

        Intent intent = getIntent();
        Context context = getApplicationContext();


        String locationsRequest = intent.getStringExtra("locations");
        String idRequest = intent.getStringExtra("id");

        Log.e("locations", locationsRequest);
        Log.e("ids", idRequest);

        String testEndpoint = "http://157.245.99.108";
        String searchURL = testEndpoint + "/api/search/items";
        String searchRequests = "itemId=" + idRequest + "&locations=" + locationsRequest;

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
                Inventory foo = new Inventory(newInv.getString("itemName"), newInv.getString("itemVariant"), newInv.getString("hsnCode"), newInv.getString("bigcartonQuantity"), newInv.getString("uomBig"), newInv.getString("smallboxQuantity"), newInv.getString("uomSmall"), newInv.getString("itemQuantity"), newInv.getString("uomRaw"), newInv.getString("warehouseName"), newInv.getString("warehouseLocation"));
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
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, TABLE_HEADERS));
        tableView.setDataAdapter(new InventoryTableDataAdapter (this, dataToShow));

    }
}
