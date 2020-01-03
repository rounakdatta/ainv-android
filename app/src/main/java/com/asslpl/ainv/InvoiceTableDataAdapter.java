package com.asslpl.ainv;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.rey.material.widget.TextView;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;

public class InvoiceTableDataAdapter extends TableDataAdapter<Invoice>{

    public InvoiceTableDataAdapter(Context context, List<Invoice> data) {
        super(context, data);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        Invoice inv = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderInvoiceNumber(inv);
                break;
            case 1:
                renderedView = renderEntryDate(inv);
                break;
            case 2:
                renderedView = renderItem(inv);
                break;
            case 3:
                renderedView = renderWarehouse(inv);
                break;
            case 4:
                renderedView = renderClient(inv);
                break;
            case 5:
                renderedView = renderStockChange(inv);
                break;
            case 6:
                renderedView = renderTotalPcs(inv);
                break;
            case 7:
                renderedView = renderTotalPayment(inv);
                break;
            case 8:
                renderedView = renderIsPaid(inv);
                break;
            case 9:
                renderedView = renderExpectedPaymentDate(inv);
                break;
        }

        return renderedView;
    }

    private View renderInvoiceNumber(final Invoice inv) {
        return renderString(inv.trackingNumber);
    }

    private View renderEntryDate(final Invoice inv) {
        return renderString(inv.entryDate);
    }

    private View renderItem(final Invoice inv) {
        return renderString(inv.itemName + " - " + inv.itemVariant);
    }

    private View renderWarehouse(final Invoice inv) {
        return renderString(inv.warehouseName + ", " + inv.warehouseLocation);
    }

    private View renderClient(final Invoice inv) {
        return renderString(inv.clientName);
    }

    private View renderStockChange(final Invoice inv) {
        return renderString(inv.changeStock);
    }

    private View renderTotalPcs(final Invoice inv) {
        return renderString(inv.totalPcs);
    }

    private View renderTotalPayment(final Invoice inv) {
        return renderString(inv.totalValue);
    }

    private View renderIsPaid(final Invoice inv) {
        return renderString(inv.isPaid);
    }

    private View renderExpectedPaymentDate(final Invoice inv) {
        return renderString(inv.paymentDate);
    }

    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(20);
        return textView;
    }

}
