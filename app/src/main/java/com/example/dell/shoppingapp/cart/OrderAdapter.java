package com.example.dell.shoppingapp.cart;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.shoppingapp.R;
import com.example.dell.shoppingapp.data.CartContract.MenEntry;
import com.example.dell.shoppingapp.user.UserDetails;

import java.io.ByteArrayInputStream;

public class OrderAdapter extends CursorAdapter {



    public OrderAdapter(Context context, Cursor c) {
        super(context, c, 0 );
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.cart_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView=(TextView)view.findViewById(R.id.cart_name);
        TextView summaryTextView=(TextView)view.findViewById(R.id.cart_description);
        TextView priceTextView=(TextView)view.findViewById(R.id.cart_price);
        TextView quantityTextView=(TextView)view.findViewById(R.id.cart_quantity);

        String Name=cursor.getString(cursor.getColumnIndexOrThrow(MenEntry.COLUMN_PRODUCT_NAME));
        String Price=cursor.getString(cursor.getColumnIndexOrThrow(MenEntry.COLUMN_PRODUCT_TOTAL_PRICE));
        String Quantity=cursor.getString(cursor.getColumnIndexOrThrow(MenEntry.COLUMN_PRODUCT_QUANTITY));

//        int total_price=(Integer.parseInt(quantityTextView.toString()))*(Integer.parseInt(Price));


        nameTextView.setText(Name);
        priceTextView.setText(Price);
        quantityTextView.setText(Quantity);

        ImageView mimageView = (ImageView) view.findViewById(R.id.image);
        int imageColumnIndex=cursor.getColumnIndex(MenEntry.COLUMN_PRODUCT_IMAGE);
        byte[] image=cursor.getBlob(imageColumnIndex);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(image);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        mimageView.setImageBitmap(bitmap);


    }
}


