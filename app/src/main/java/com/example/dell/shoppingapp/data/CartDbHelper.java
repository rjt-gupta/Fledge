package com.example.dell.shoppingapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.dell.shoppingapp.data.CartContract.MenEntry;

public class CartDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = CartDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "cart.db";
    private static final int DATABASE_VERSION = 1;

    public CartDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_PRODUCT_TABLE =  "CREATE TABLE " + MenEntry.TABLE_NAME_PRODUCTS + " ("
                + MenEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MenEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + MenEntry.COLUMN_PRODUCT_DESCRIPTION + " TEXT, "
                + MenEntry.COLUMN_PRODUCT_GENDER + " INTEGER NOT NULL, "
                + MenEntry.COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL DEFAULT 0,"
                + MenEntry.COLUMN_PRODUCT_IMAGE + " BLOB );";

        db.execSQL(SQL_CREATE_PRODUCT_TABLE);


        String SQL_CREATE_CART_TABLE =  "CREATE TABLE " + MenEntry.TABLE_NAME_CART + " ("
                + MenEntry._ID + " INTEGER , "
                + MenEntry.COLUMN_CUST_ID + " INTEGER , "
                + MenEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + MenEntry.COLUMN_PRODUCT_TOTAL_PRICE + " INTEGER NOT NULL DEFAULT 0,"
                + MenEntry.COLUMN_PRODUCT_IMAGE + " BLOB , "
                + MenEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL);";


        db.execSQL(SQL_CREATE_CART_TABLE);

        String SQL_CREATE_CUSTOMER_TABLE =  "CREATE TABLE " + MenEntry.TABLE_NAME_CUSTOMER + " ("
                + MenEntry.COLUMN_CUST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + MenEntry.COLUMN_FIRST_NAME + " TEXT NOT NULL, "
                + MenEntry.COLUMN_LAST_NAME + " TEXT NOT NULL, "
                + MenEntry.COLUMN_PHONE + " INTEGER , "
                + MenEntry.COLUMN_ADDRESS + " TEXT NOT NULL);";


        db.execSQL(SQL_CREATE_CUSTOMER_TABLE);

        String SQL_CREATE_ADDRESS_TABLE =  "CREATE TABLE " + MenEntry.TABLE_NAME_ADDRESS + " ("
                + MenEntry._ID + " INTEGER , "
                + MenEntry.COLUMN_CUST_ID + " INTEGER );";


        db.execSQL(SQL_CREATE_ADDRESS_TABLE);

        String SQL_CREATE_PHONE_TABLE =  "CREATE TABLE " + MenEntry.TABLE_NAME_PHONE + " ("
                + MenEntry.COLUMN_PHONE + " INTEGER , "
                + MenEntry.COLUMN_CUST_ID + " INTEGER );";

        db.execSQL(SQL_CREATE_PHONE_TABLE);

        //create table address as select products._id,customer._id from products,customer;

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

