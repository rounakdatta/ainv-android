package com.asslpl.ainv;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class  SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    int whichRowItis;
    Dialog dg;
    String columnToUpdate;

    public void setValue(int x) {
        this.whichRowItis = x;
    }

    public void setDialogIndicator(Dialog d) {
        this.dg = d;
    }

    public void setWhichColumnUpdater(String column) {
        this.columnToUpdate = column;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dp = new DatePickerDialog(getActivity(), this, yy, mm, dd);

        // Uncomment this to allow minimum selectable date to be >= today
        // dp.getDatePicker().setMinDate(System.currentTimeMillis());

        return dp;
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        populateSetDate(view, yy, mm+1, dd);
    }
    public void populateSetDate(DatePicker view, int year, int month, int day) {
        TextView dob;

        try {
            dob = getActivity().findViewById(R.id.entryDateHeader);
            dob.setText(day + "/" + month + "/" + year);
            return;
        } catch (Exception e) {

            try {
                System.out.println(e);
                dob = getActivity().findViewById(R.id.expectedDateHeader);
                dob.setText(day + "/" + month + "/" + year);

            } catch (Exception e1) {
                System.out.println(e1);

                TextView dt = dg.findViewById(this.whichRowItis + 999);
                String settingDate = day + "/" + month + "/" + year;

                dt.setText(settingDate);

                // send the data to server
                String testEndpoint = getResources().getString(R.string.serverEndpoint);
                String updateURL = testEndpoint + "/api/update/" + columnToUpdate + "/";
                String updateResponse = "NULL";

                String data = "transactionId=" + this.whichRowItis + "&" + columnToUpdate + "=" + settingDate;
                HttpPostRequest insertHttp = new HttpPostRequest();
                try {
                    updateResponse = insertHttp.execute(updateURL, data).get();
                    JSONObject transactionResponseJson = new JSONObject(updateResponse);

                    Toast toast;

                    if (transactionResponseJson.getBoolean("success")) {
                        toast = Toast.makeText(getContext(), "Transaction successful!", Toast.LENGTH_LONG);

                    } else {
                        toast = Toast.makeText(getContext(), "Transaction error!", Toast.LENGTH_LONG);
                    }

                    toast.show();

                } catch (ExecutionException ex) {
                    e.printStackTrace();
                } catch (InterruptedException ex) {
                    e.printStackTrace();
                } catch (JSONException ex) {
                    e.printStackTrace();
                }
            }
        }
    }
}