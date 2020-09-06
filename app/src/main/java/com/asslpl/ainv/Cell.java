package com.asslpl.ainv;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evrencoskun.tableview.filter.IFilterableModel;
import com.evrencoskun.tableview.sort.ISortableModel;

/**
 * Created by evrencoskun on 11/06/2017.
 */

public class Cell implements ISortableModel, IFilterableModel {
    @NonNull
    private String mId;
    @Nullable
    private Object mData;
    @NonNull
    private String mFilterKeyword;

    public Cell(@NonNull String id, @Nullable Object data) {
        this.mId = id;
        this.mData = data;
        this.mFilterKeyword = String.valueOf(data);
    }

    /**
     * This is necessary for sorting process.
     * See ISortableModel
     */
    @NonNull
    @Override
    public String getId() {
        return mId;
    }

    /**
     * This is necessary for sorting process.
     * See ISortableModel
     */
    @Nullable
    @Override
    public Object getContent() {
        return mData;
    }

    @Nullable
    public Object getData() {
        return mData;
    }

    public void setData(@Nullable String data) {
        mData = data;
    }

    @NonNull
    @Override
    public String getFilterableKeyword() {
        return mFilterKeyword;
    }
}