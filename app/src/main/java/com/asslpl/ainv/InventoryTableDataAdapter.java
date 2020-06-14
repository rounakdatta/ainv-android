package com.asslpl.ainv;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.rey.material.widget.TextView;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;

public class InventoryTableDataAdapter extends TableDataAdapter<Inventory>{

    public InventoryTableDataAdapter(Context context, List<Inventory> data) {
        super(context, data);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        Inventory inv = getRowData(rowIndex);
        View renderedView = null;

        System.out.println(columnIndex);

        switch (columnIndex) {
            case 0:
                renderedView = renderClientName(inv);
                break;
            case 1:
                renderedView = renderItemName(inv);
                break;
            case 2:
                renderedView = renderItemVariant(inv);
                break;
            case 3:
                renderedView = renderHsnCode(inv);
                break;
            case 4:
                renderedView = renderItemQuantity(inv);
                break;
            case 5:
                renderedView = renderUomRaw(inv);
                break;
            case 6:
                renderedView = renderSmallboxQuantity(inv);
                break;
            case 7:
                renderedView = renderUomSmall(inv);
                break;
            case 8:
                renderedView = renderBigcartonQuantity(inv);
                break;
            case 9:
                renderedView = renderUomBig(inv);
                break;
            case 10:
                renderedView = renderWarehouseName(inv);
                break;
        }

        return renderedView;
    }

    private View renderItemName(final Inventory inv) {
        return renderString(inv.itemName);
    }

    private View renderItemVariant(final Inventory inv) {
        return renderString(inv.itemVariant);
    }

    private View renderHsnCode(final Inventory inv) {
        return renderString(inv.hsnCode);
    }

    private View renderItemQuantity(final Inventory inv) {
        return renderString(inv.itemQuantity);
    }

    private View renderUomRaw(final Inventory inv) {
        return renderString(inv.uomRaw);
    }

    private View renderSmallboxQuantity(final Inventory inv) {
        return renderString(inv.smallboxQuantity);
    }

    private View renderUomSmall(final Inventory inv) {
        return renderString(inv.uomSmall);
    }

    private View renderBigcartonQuantity(final Inventory inv) {
        return renderString(inv.bigcartonQuantity);
    }

    private View renderUomBig(final Inventory inv) {
        return renderString(inv.uomBig);
    }

    private View renderWarehouseName(final Inventory inv) {
        return renderString(inv.warehouseName + ", " + inv.warehouseLocation);
    }

    private View renderWarehouseLocation(final Inventory inv) {
        return renderString(inv.warehouseLocation);
    }

    private View renderClientName(final Inventory inv) {
        return renderString(inv.clientName);
    }

    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(15);
        return textView;
    }

}
