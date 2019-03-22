package com.example.dell.shoppingapp.cart;

import android.database.Cursor;
import com.example.dell.shoppingapp.data.CartContract.MenEntry;

public class Order {

    public int id;

    public String name;
    public String description;
    public String imageUrl;
    public Double price;



    public Order(Cursor cursor) {
        this.name = cursor.getString(cursor.getColumnIndex(MenEntry.COLUMN_PRODUCT_NAME));
        this.description = cursor.getString(cursor.getColumnIndex(MenEntry.COLUMN_PRODUCT_DESCRIPTION));
        this.imageUrl = cursor.getString(cursor.getColumnIndex(MenEntry.COLUMN_PRODUCT_IMAGE));
        this.price = cursor.getDouble(cursor.getColumnIndex(MenEntry.COLUMN_PRODUCT_PRICE));
    }
}
