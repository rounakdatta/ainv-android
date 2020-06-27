package com.asslpl.ainv;

import android.app.Dialog;

public class Invoice {

    public String transactionId;
    public String billOfEntry;
    public String salesInvoice;
    public String entryDate;
    public String itemId;
    public String itemName;
    public String itemVariant;
    public String warehouseId;
    public String warehouseName;
    public String warehouseLocation;
    public String clientId;
    public String clientName;
    public String customerId;
    public String customerName;
    public String changeStock;
    public String finalStock;
    public String totalPcs;
    public String materialValue;
    public String gstValue;
    public String totalValue;
    public String valuePerPiece;
    public String isPaid;
    public String paidAmount;
    public String paymentDate;
    public String balance;
    public String cumulativeBalance;

    Dialog dg;

        public Invoice(final Dialog dg, final String transactionId, final String billOfEntry, final String salesInvoice, final String entryDate, final String itemId, final String itemName, final String itemVariant, final String warehouseName, final String warehouseLocation, final String clientId, final String clientName, final String customerId, final String customerName, final String changeStock, final String finalStock, final String totalPcs, final String materialValue, final String gstValue, final String totalValue, final String valuePerPiece, final String isPaid, final String paidAmount, final String paymentDate, final String balance, final String cumulativeBalance) {
            this.dg = dg;
            this.transactionId = transactionId;
            this.billOfEntry = billOfEntry;
            this.salesInvoice = salesInvoice;
            this.entryDate = entryDate;
            this.itemId = itemId;
            this.itemName = itemName;
            this.itemVariant = itemVariant;
            this.warehouseName = warehouseName;
            this.warehouseLocation = warehouseLocation;
            this.clientId = clientId;
            this.clientName = clientName;
            this.customerId = customerId;
            this.customerName = customerName;
            this.changeStock = changeStock;
            this.finalStock = finalStock;
            this.totalPcs = totalPcs;
            this.materialValue = materialValue;
            this.gstValue = gstValue;
            this.totalValue = totalValue;
            this.valuePerPiece = valuePerPiece;
            this.isPaid = isPaid;
            this.paidAmount = paidAmount;
            this.paymentDate = paymentDate;
            this.balance = balance;
            this.cumulativeBalance = cumulativeBalance;
    }

//    public String getName() {
//        return itemName;
//    }
//
//    public String getVariant() {
//        return itemVariant;
//    }
//
//    public String getHsn() {
//        return hsnCode;
//    }
}
