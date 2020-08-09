package com.asslpl.ainv;

public class Overview {
    public String billOfEntryId;
    public String billOfEntry;
    public String salesInvoiceId;
    public String salesInvoice;
    public String direction;
    public String entryDate;
    public String item;
    public String warehouse;
    public String client;
    public String customer;
    public String bigQuantity;
    public String totalValue;
    public String isPaid;
    public String paidAmount;
    public String balance;
    public String cumulativeBalance;
    public String date;

    // search query attributes
    public String search_invoiceNumber;
    public String search_clientId;
    public String search_customerId;

    public Overview(final String billOfEntryId, final String billOfEntry, final String salesInvoiceId, final String salesInvoice, final String direction, final String entryDate, final String item, final String warehouse, final String client, final String customer, final String bigQuantity, final String totalValue, final String isPaid, final String paidAmount, final String balance, final String cumulativeBalance, final String date, final String search_invoiceNumber, final String search_clientId, final String search_customerId) {
        this.billOfEntryId = billOfEntryId;
        this.billOfEntry = billOfEntry;
        this.salesInvoiceId = salesInvoiceId;
        this.salesInvoice = salesInvoice;
        this.direction = direction;
        this.entryDate = entryDate;
        this.item = item;
        this.warehouse = warehouse;
        this.client = client;
        this.customer = customer;
        this.bigQuantity = bigQuantity;
        this.totalValue = totalValue;
        this.isPaid = isPaid;
        this.paidAmount = paidAmount;
        this.balance = balance;
        this.cumulativeBalance = cumulativeBalance;
        this.date = date;

        this.search_invoiceNumber = search_invoiceNumber;
        this.search_clientId = search_clientId;
        this.search_customerId = search_customerId;
    }

}
