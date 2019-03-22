package com.example.dell.shoppingapp.vegetable;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.shoppingapp.R;
import com.example.dell.shoppingapp.data.CartContract.MenEntry;


import java.io.ByteArrayInputStream;

public class VegetableCursorAdapter extends CursorAdapter {

    int pos;

    public VegetableCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }


    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView summaryTextView = (TextView) view.findViewById(R.id.summary);
        TextView typeTextView = (TextView) view.findViewById(R.id.gender);

        String MenName = cursor.getString(cursor.getColumnIndexOrThrow(MenEntry.COLUMN_PRODUCT_NAME));
        String MenDescription = cursor.getString(cursor.getColumnIndexOrThrow(MenEntry.COLUMN_PRODUCT_DESCRIPTION));
        int MenType = cursor.getInt(cursor.getColumnIndexOrThrow(MenEntry.COLUMN_PRODUCT_GENDER));

        if (TextUtils.isEmpty(MenDescription)) {
            MenDescription = "Unknown";
        }

        nameTextView.setText(MenName);
        summaryTextView.setText(MenDescription);

        if(MenType==1){
            typeTextView.setText("Vegetables");
        }


        ImageView mimageView = (ImageView) view.findViewById(R.id.image);

        int imageColumnIndex = cursor.getColumnIndex(MenEntry.COLUMN_PRODUCT_IMAGE);
        byte[] image = cursor.getBlob(imageColumnIndex);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(image);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        mimageView.setImageBitmap(bitmap);


        pos = cursor.getPosition();

    }
}

