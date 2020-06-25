package com.asslpl.ainv;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rey.material.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

import de.codecrafters.tableview.TableDataAdapter;

import static java.lang.Float.parseFloat;

public class OverviewTableAdapter extends TableDataAdapter<Overview>{

    public OverviewTableAdapter(Context context, List<Overview> data) {
        super(context, data);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        Overview inv = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderDirection(inv);
                break;
            case 1:
                renderedView = renderBillOfEntry(inv);
                break;
            case 2:
                renderedView = renderSalesInvoice(inv);
                break;
            case 3:
                renderedView = renderEntryDate(inv);
                break;
            case 4:
                renderedView = renderItem(inv);
                break;
            case 5:
                renderedView = renderWarehouse(inv);
                break;
            case 6:
                renderedView = renderClient(inv);
                break;
            case 7:
                renderedView = renderCustomer(inv);
                break;
            case 8:
                renderedView = renderTotalQuantity(inv);
                break;
            case 9:
                renderedView = renderTotalPayment(inv);
                break;
            case 10:
                renderedView = renderIsPaid(inv);
                break;
            case 11:
                renderedView = renderPaidAmount(inv);
                break;
            case 12:
                renderedView = renderBalance(inv);
                break;
            case 13:
                renderedView = renderCumulativeBalance(inv);
                break;
            case 14:
                renderedView = renderExpectedPaymentDate(inv, rowIndex);
                break;
        }

        return renderedView;
    }

    private View renderDirection(final Overview inv) {
        return renderString(inv.direction);
    }

    private View renderBillOfEntry(final Overview inv) {
        return renderString(inv.billOfEntry);
    }

    private View renderSalesInvoice(final Overview inv) {
        return renderString(inv.salesInvoice);
    }

    private View renderTotalQuantity(final Overview inv) {
        return renderString(inv.bigQuantity);
    }

    private View renderEntryDate(final Overview inv) {
        return renderString(inv.entryDate);
    }

    private View renderItem(final Overview inv) {
        return renderString(inv.item);
    }

    private View renderWarehouse(final Overview inv) {
        return renderString(inv.warehouse);
    }

    private View renderClient(final Overview inv) {
        return renderString(inv.client);
    }

    private View renderCustomer(final Overview inv) {
        return renderString(inv.customer);
    }

    private View renderTotalPayment(final Overview inv) {
        return renderPaymentAmountColumn(inv.totalValue, Double.parseDouble(inv.totalValue));
    }

    private View renderBalance(final Overview inv) {
        Double balanceToPay = Double.parseDouble(inv.balance);
        String balanceToShow = String.valueOf(Math.round(balanceToPay));

        if (balanceToPay < 1) {
            balanceToShow = "< 1";
        } else if (balanceToPay > 1 && balanceToPay < 5) {
            balanceToShow = "< 5";
        } else {
            balanceToShow = "~ " + balanceToShow;
        }

        return renderString(balanceToShow);
    }
    private View renderCumulativeBalance(final Overview inv) {
        return renderString(inv.cumulativeBalance);
    }

    private View renderPaidAmount(final Overview inv) {
        return renderString(inv.paidAmount);
    }

    private View renderIsPaid(final Overview inv) {
        return renderString(inv.isPaid);
    }

    private View renderExpectedPaymentDate(final Overview inv, final int rowIndex) {
        return renderString(inv.date);
    }

    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(15);

        return textView;
    }

    private View renderPaymentAmountColumn(final String value, final double rowIndex) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setId((int)rowIndex + 443);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(15);

        return textView;
    }

}
