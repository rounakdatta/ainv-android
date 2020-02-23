package com.asslpl.ainv;

public class Invoice {

    public String transactionId;
    public String trackingNumber;
    public String entryDate;
    public String itemId;
    public String itemName;
    public String itemVariant;
    public String warehouseId;
    public String warehouseName;
    public String warehouseLocation;
    public String clientId;
    public String clientName;
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

        public Invoice(final String transactionId, final String trackingNumber, final String entryDate, final String itemId, final String itemName, final String itemVariant, final String warehouseId, final String warehouseName, final String warehouseLocation, final String clientId, final String clientName, final String changeStock, final String finalStock, final String totalPcs, final String materialValue, final String gstValue, final String totalValue, final String valuePerPiece, final String isPaid, final String paidAmount, final String paymentDate, final String balance, final String cumulativeBalance) {
            this.transactionId = transactionId;
            this.trackingNumber = trackingNumber;
            this.entryDate = entryDate;
            this.itemId = itemId;
            this.itemName = itemName;
            this.itemVariant = itemVariant;
            this.warehouseId = warehouseId;
            this.warehouseName = warehouseName;
            this.warehouseLocation = warehouseLocation;
            this.clientId = clientId;
            this.clientName = clientName;
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
