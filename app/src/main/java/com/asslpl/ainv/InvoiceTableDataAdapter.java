package com.asslpl.ainv;

import android.app.Dialog;
import android.content.Context;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
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
                renderedView = renderBill(inv);
                break;
            case 1:
                renderedView = renderInvoiceNumber(inv);
                break;
            case 2:
                renderedView = renderEntryDate(inv);
                break;
            case 6:
                renderedView = renderItem(inv);
                break;
            case 4:
                renderedView = renderWarehouse(inv);
                break;
            case 3:
                renderedView = renderClient(inv);
                break;
            case 5:
                renderedView = renderCustomer(inv);
                break;
            case 7:
                renderedView = renderStockChange(inv);
                break;
            case 8:
                renderedView = renderTotalPcs(inv);
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
            case 15:
                renderedView = renderField1(inv);
                break;
            case 16:
                renderedView = renderField2(inv);
                break;
            case 17:
                renderedView = renderRemarks(inv);
                break;
        }

        return renderedView;
    }

    private View renderBill(final Invoice inv) {
        return renderString(inv.billOfEntry);
    }

    private View renderInvoiceNumber(final Invoice inv) {
        return renderString(inv.salesInvoice);
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

    private View renderCustomer(final Invoice inv) {
        return renderString(inv.customerName);
    }

    private View renderStockChange(final Invoice inv) {
        return renderString(inv.changeStock);
    }

    private View renderTotalPcs(final Invoice inv) {
        return renderString(inv.totalPcs + " " + inv.rawUnit);
    }

    private View renderTotalPayment(final Invoice inv) {
        return renderPaymentAmountColumn(inv.totalValue, Integer.parseInt(inv.transactionId));
    }

    private View renderBalance(final Invoice inv) {
        return renderString(inv.balance);
    }
    private View renderCumulativeBalance(final Invoice inv) {
        return renderString(inv.cumulativeBalance);
    }

    private View renderPaidAmount(final Invoice inv) {
        return renderUpdatablePayment(inv.paidAmount, Integer.parseInt(inv.transactionId), inv.totalValue);
    }

    private View renderIsPaid(final Invoice inv) {
        if (inv.isPaid.equals("0")) {
            return renderString("No");
        } else {
            return renderString("Yes");
        }
    }

    private View renderExpectedPaymentDate(final Invoice inv, final int rowIndex) {
        return renderUpdatableDate(inv, inv.paymentDate, Integer.parseInt(inv.transactionId), "paymentdate");
    }

    private View renderField1(final Invoice inv) {
        return renderUpdatableDate(inv, inv.field1, Integer.parseInt(inv.transactionId), "field1");
    }

    private View renderField2(final Invoice inv) {
        return renderUpdatableDate(inv, inv.field2, Integer.parseInt(inv.transactionId), "field2");
    }

    private View renderRemarks(final Invoice inv) {
        return renderUpdatableRemarks(inv, inv.remarks);
    }

    private View renderUpdatableRemarks(final Invoice inv, final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(15);

        final String transactionId = inv.transactionId;

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.activity_update_remarks);
                dialog.show();

                dialog.setCancelable(true);

                final EditText newRemarks = dialog.findViewById(R.id.newRemarks);
                final Button updateRemarksButton = dialog.findViewById(R.id.updatedRemarks);

                updateRemarksButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                        textView.setText(newRemarks.getText());

                        // send the data to server
                        String testEndpoint = getResources().getString(R.string.serverEndpoint);
                        String updateURL = testEndpoint + "/api/update/remarks/";
                        String updateResponse = "NULL";

                        String data = "transactionId=" + transactionId + "&remarks=" + newRemarks.getText();
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

    private View renderUpdatablePayment(final String value, final int rowIndex, final String totalPayVal) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setId(rowIndex);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(15);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.pay_amount);
                dialog.show();

                dialog.setCancelable(true);


                final EditText alreadyPaidTv = dialog.findViewById(R.id.alreadyPaidAmount);
                final EditText toPayAmountTv = dialog.findViewById(R.id.toPayAmount);
                final EditText paymentAmount = dialog.findViewById(R.id.paymentAmount);
                final EditText grandSumTv = dialog.findViewById(R.id.grandSum);

                alreadyPaidTv.setText(textView.getText());
                grandSumTv.setText(totalPayVal);

                toPayAmountTv.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.toString().isEmpty()) {
                            paymentAmount.setText("0.00");
                            return;
                        }

                        String alreadyPaidAmount = alreadyPaidTv.getText().toString();
                        String toPayAmount = toPayAmountTv.getText().toString();

                        String totalAmount = String.valueOf(parseFloat(alreadyPaidAmount) + parseFloat(toPayAmount));

                        paymentAmount.setText(totalAmount);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

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

    private View renderUpdatableDate(final Invoice inv, final String value, final int rowIndex, final String whichColumnToUpdate) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setId(rowIndex + 999);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(15);

        System.out.println(textView.getId());

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.set_date);
                dialog.show();

                dialog.setCancelable(true);

                final Context context = getContext();

                final Button updatedDate = dialog.findViewById(R.id.updatedRemarks);

                updatedDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        DialogFragment newFragment = new SelectDateFragment();
                        ((SelectDateFragment) newFragment).setValue(rowIndex);
                        ((SelectDateFragment) newFragment).setDialogIndicator(inv.dg);
                        ((SelectDateFragment) newFragment).setWhichColumnUpdater(whichColumnToUpdate);

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
        textView.setTextSize(15);

        return textView;
    }

    private View renderPaymentAmountColumn(final String value, final int rowIndex) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setId(rowIndex + 443);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(15);

        return textView;
    }

}
