package com.asslpl.ainv;

public class Inventory {

    public String itemName;
    public String itemVariant;
    public String hsnCode;
    public String itemQuantity;
    public String uomRaw;
    public String smallboxQuantity;
    public String uomSmall;
    public String bigcartonQuantity;
    public String uomBig;
    public String warehouseName;
    public String warehouseLocation;
    public String clientName;

    public Inventory(final String itemName, final String itemVariant, final String hsnCode, final String itemQuantity, final String uomRaw, final String smallboxQuantity, final String uomSmall, final String bigcartonQuantity, final String uomBig, final String warehouseName, final String warehouseLocation, final String clientName) {
        this.itemName = itemName;
        this.itemVariant = itemVariant;
        this.hsnCode = hsnCode;
        this.itemQuantity = itemQuantity;
        this.uomRaw = uomRaw;
        this.smallboxQuantity = smallboxQuantity;
        this.uomSmall = uomSmall;
        this.bigcartonQuantity = bigcartonQuantity;
        this.uomBig = uomBig;
        this.warehouseName = warehouseName;
        this.warehouseLocation = warehouseLocation;
        this.clientName = clientName;
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
