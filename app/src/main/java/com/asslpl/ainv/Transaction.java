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
import android.util.DisplayMetrics;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class Transaction extends AppCompatActivity {

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

    public void gotoSecondPage(View view) {
        mViewPager.setCurrentItem(1);
    }

    public void gotoFirstPage(View view) {
        mViewPager.setCurrentItem(0);
    }

    public static int getWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public void selectNewRates(View view) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_rate);
        dialog.show();

        dialog.setCancelable(true);
//        dialog.getWindow().setLayout(((getWidth(context) / 100) * 90), LinearLayout.LayoutParams.MATCH_PARENT);
//        dialog.getWindow().setGravity(Gravity.END);
    }

    public void gotoThirdPage(View view) {
        mViewPager.setCurrentItem(2);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        Spinner itemSelector;
        Spinner itemVariantSelector;
        Spinner warehouseSelector;

        TextView currentStock;

        ArrayList<String> itemNames = null;
        ArrayList<String> itemVariants = null;
        ArrayList<String> warehouses = null;

        JSONArray allItemsArray = null;
        JSONArray itemVariantsArray = null;
        JSONArray warehouseArray = null;

        int itemParentPosition = 0;
        String ITEMID = "-1";
        String WAREHOUSEID = "-1";

        View rootView;

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

        public void getItemInventory(View view, String warehouseId, String itemId) {

            String testEndpoint = "http://157.245.99.108";
            String rateURL = testEndpoint + "/api/get/rate";
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

            System.out.println(currentStock.getText());
            // currentStock.setText(cartonQuantity);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {


            int pageNumber = getArguments().getInt(ARG_SECTION_NUMBER);

            if (pageNumber == 1) {
                // first page - first details
                rootView = inflater.inflate(R.layout.fragment_transaction_p1, container, false);

                // set the calendar dialog
                Button dobSelector = rootView.findViewById(R.id.date);
                dobSelector.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogFragment newFragment = new SelectDateFragment();
                        newFragment.show(getFragmentManager(), "Date Picker");
                    }
                });

                // set the item and warehouse data

                String testEndpoint = "http://157.245.99.108";
                String itemDataURL = testEndpoint + "/api/get/items";


                itemSelector = rootView.findViewById(R.id.itemName);
                itemVariantSelector = rootView.findViewById(R.id.itemVariant);

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

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                warehouseSelector = rootView.findViewById(R.id.warehouse);

                // logic for populating the warehouse spinner
                String warehouseDataURL = testEndpoint + "/api/get/all/warehouses";
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


            } else if (pageNumber == 3) {
                // third page - item valuation details
                rootView = inflater.inflate(R.layout.fragment_transaction_p3, container, false);
            }

//            TextView textView = (TextView) rootView.findViewById(R.id.transactionHeading);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            System.out.println("NOW PREINTING VIEW ID");
            System.out.println(rootView.getId());
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
