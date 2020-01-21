package com.asslpl.ainv;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
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
                renderedView = renderPaidAmount(inv);
                break;
            case 10:
                renderedView = renderExpectedPaymentDate(inv, rowIndex);
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

    private View renderPaidAmount(final Invoice inv) {
        return renderUpdatablePayment(inv.paidAmount, Integer.parseInt(inv.transactionId));
    }

    private View renderExpectedPaymentDate(final Invoice inv, final int rowIndex) {
        return renderUpdatableDate(inv.paymentDate, Integer.parseInt(inv.transactionId));
    }

    private View renderUpdatablePayment(final String value, final int rowIndex) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setId(rowIndex);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(20);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.pay_amount);
                dialog.show();

                dialog.setCancelable(true);


                final EditText paymentAmount = dialog.findViewById(R.id.paymentAmount);

                Button updateButton = dialog.findViewById(R.id.updateButton);
                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        textView.setText(paymentAmount.getText());

                        // send the data to server
                        String testEndpoint = getResources().getString(R.string.serverEndpoint);
                        String updateURL = testEndpoint + "/api/update/paidamount/";
                        String updateResponse = "NULL";

                        String data = "transactionId=" + rowIndex + "&paidAmount=" + paymentAmount.getText();
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

                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });

        return textView;
    }

    private View renderUpdatableDate(final String value, final int rowIndex) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setId(rowIndex + 999);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(20);



        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.set_date);
                dialog.show();

                dialog.setCancelable(true);

                final Context context = getContext();

                final Button updatedDate = dialog.findViewById(R.id.updatedDate);

                updatedDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        DialogFragment newFragment = new SelectDateFragment();
                        ((SelectDateFragment) newFragment).setValue(rowIndex);
                        newFragment.show(this.getFragmentManager(), "Date Picker");
                    }

                    private FragmentManager getFragmentManager() {
                        FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                        return fragmentManager;
                    }
                });

            }
        });

        return textView;
    }

    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(20);

        return textView;
    }

}
