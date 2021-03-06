package com.asslpl.ainv;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.rey.material.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

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
            case 6:
                renderedView = renderWarehouse(inv);
                break;
            case 5:
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
        return renderString(inv, inv.direction);
    }

    private View renderBillOfEntry(final Overview inv) {
        return renderString(inv, inv.billOfEntry);
    }

    private View renderSalesInvoice(final Overview inv) {
        return renderString(inv, inv.salesInvoice);
    }

    private View renderTotalQuantity(final Overview inv) {
        return renderString(inv, inv.bigQuantity);
    }

    private View renderEntryDate(final Overview inv) {
        return renderString(inv, inv.entryDate);
    }

    private View renderItem(final Overview inv) {
        return renderString(inv, inv.item);
    }

    private View renderWarehouse(final Overview inv) {
        return renderString(inv, inv.warehouse);
    }

    private View renderClient(final Overview inv) {
        return renderString(inv, inv.client);
    }

    private View renderCustomer(final Overview inv) {
        return renderString(inv, inv.customer);
    }

    private View renderTotalPayment(final Overview inv) {
        return renderString(inv, inv.totalValue);
        // return renderPaymentAmountColumn(inv, inv.totalValue, Double.parseDouble(inv.totalValue));
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

        return renderString(inv, balanceToShow);
    }
    private View renderCumulativeBalance(final Overview inv) {
        return renderString(inv, inv.cumulativeBalance);
    }

    private View renderPaidAmount(final Overview inv) {
        return renderString(inv, inv.paidAmount);
    }

    private View renderIsPaid(final Overview inv) {
        return renderString(inv, inv.isPaid);
    }

    private View renderExpectedPaymentDate(final Overview inv, final int rowIndex) {
        return renderString(inv, inv.date);
    }

    private View renderString(final Overview inv, final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(15);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.activity_sales_deep_search);
                dialog.show();

                List<Invoice> dataToShow = new ArrayList<>();
                List<Invoice> headerData = new ArrayList<>();

                String requestedBillOfEntry = inv.billOfEntryId;
                String requestedDirection = inv.direction;
                String requestedClientId = inv.search_clientId;
                String requestedCustomerId = inv.search_customerId;

                String[] TABLE_HEADERS_IN = { "Bill Of Entry", "Sales Invoice No.", "Entry Date", "Client Name", "Warehouse", "Customer Name", "Item", "Carton IN/OUT", "Total Units", "Total Value", "Full Paid ?", "Paid Amount", "Balance", "Cuml. Balance", "Expd. Pymt. Date", "CFS Delv. Date", "Client Delv. Date", "Remarks" };
                String[] TABLE_HEADERS_OUT = { "Bill Of Entry", "Sales Invoice No.", "Entry Date", "Client Name", "Warehouse", "Customer Name", "Item", "Carton IN/OUT", "Total Units", "Total Value", "Full Paid ?", "Paid Amount", "Balance", "Cuml. Balance", "Expd. Pymt. Date", "Serv. Inv. Ack. Date", "Sale Inv. Ack. Date", "Remarks" };

                String testEndpoint = getResources().getString(R.string.serverEndpoint);
                String searchURL = testEndpoint + "/api/search/sales/";
                String searchRequests = "billOfEntry=" + requestedBillOfEntry + "&clientId=" + requestedClientId + "&customerId=" + requestedCustomerId + "&filter=" + requestedDirection;

                System.out.println(searchRequests);

                String searchResponse = "NULL";
                JSONArray searchResponseArray = null;
                ArrayList<JSONObject> salesCollection = new ArrayList<>();

                HttpPostRequest searchHttp = new HttpPostRequest();
                try {
                    searchResponse = searchHttp.execute(searchURL, searchRequests).get();
                    System.out.println(searchResponse.toString());
                    searchResponseArray = new JSONArray(searchResponse);

                    float cumulativeBalance = 0;

                    salesCollection = new ArrayList<>();
                    for (int i = 0; i < searchResponseArray.length(); i++) {
                        JSONObject salesTicket = searchResponseArray.getJSONObject(i);
                        float balanceValue = Float.parseFloat(salesTicket.getString("totalValue")) - Float.parseFloat(salesTicket.getString("paidAmount"));

                        if (salesTicket.getString("comeOrGo").equals("out")) {
                            cumulativeBalance += balanceValue;
                        }

                        Invoice foo = new Invoice(dialog, salesTicket.getString("transactionId"), salesTicket.getString("billOfEntry"), salesTicket.getString("salesInvoice"), salesTicket.getString("entryDate"), salesTicket.getString("itemId"), salesTicket.getString("itemName"), salesTicket.getString("itemVariant"), salesTicket.getString("warehouseName"), salesTicket.getString("warehouseLocation"), salesTicket.getString("clientId"), salesTicket.getString("clientName"), salesTicket.getString("customerId"), salesTicket.getString("customerName"), salesTicket.getString("changeStock"), salesTicket.getString("finalStock"), salesTicket.getString("totalPcs"), salesTicket.getString("materialValue"), salesTicket.getString("gstValue"), salesTicket.getString("totalValue"), salesTicket.getString("valuePerPiece"), salesTicket.getString("isPaid"), salesTicket.getString("paidAmount"), salesTicket.getString("paymentDate"), String.valueOf(balanceValue), String.valueOf(cumulativeBalance), salesTicket.getString("field1"), salesTicket.getString("field2"), salesTicket.getString("remarks"), salesTicket.getString("rawUnit"));
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

                TableView tableView = (TableView) dialog.findViewById(R.id.tableView);

                SimpleTableHeaderAdapter sha;
                if (inv.direction.equals("in")) {
                    sha = new SimpleTableHeaderAdapter(getContext(), TABLE_HEADERS_IN);
                } else {
                    sha = new SimpleTableHeaderAdapter(getContext(), TABLE_HEADERS_OUT);
                }

                sha.setTextSize(11);
                tableView.setHeaderAdapter(sha);
                tableView.setDataAdapter(new InvoiceTableDataAdapter (getContext(), dataToShow));

                dialog.setCancelable(true);
            }
        });

        return textView;
    }

    private View renderPaymentAmountColumn(final Overview inv, final String value, final double rowIndex) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setId((int)rowIndex + 443);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(15);

        return textView;
    }

}
