package com.asslpl.ainv;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class  SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    int whichRowItis;

    public void setValue(int x) {
        this.whichRowItis = x;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
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
                TextView dt = getActivity().findViewById(this.whichRowItis);
                dt.setText(day + "/" + month + "/" + year);
            }
        }
    }
}