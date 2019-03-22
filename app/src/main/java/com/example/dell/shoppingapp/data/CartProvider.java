package com.example.dell.shoppingapp.data;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.dell.shoppingapp.data.CartContract.MenEntry;

public class CartProvider extends ContentProvider {

    public static final String LOG_TAG = CartProvider.class.getSimpleName();

    public CartDbHelper mDbHelper;

    private static final int PRODUCTS = 100;
    private static final int PRODUCT_ID = 101;
    private static final int CARTS = 200;
    private static final int CART_ID = 201;
    private static final int CUSTOMERS = 300;
    private static final int CUSTOMER_ID = 301;
    private static final int ADDRESSS = 400;
    private static final int ADDRESS_ID = 401;
    private static final int PHONES = 500;
    private static final int PHONE_ID = 501;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(CartContract.CONTENT_AUTHORITY, CartContract.PATH_PRODUCTS, PRODUCTS);
        sUriMatcher.addURI(CartContract.CONTENT_AUTHORITY, CartContract.PATH_PRODUCTS + "/#", PRODUCT_ID);
        sUriMatcher.addURI(CartContract.CONTENT_AUTHORITY, CartContract.PATH_CARTS, CARTS);
        sUriMatcher.addURI(CartContract.CONTENT_AUTHORITY, CartContract.PATH_CARTS+ "/#", CART_ID);
        sUriMatcher.addURI(CartContract.CONTENT_AUTHORITY, CartContract.PATH_CUSTOMERS, CUSTOMERS);
        sUriMatcher.addURI(CartContract.CONTENT_AUTHORITY, CartContract.PATH_CUSTOMERS+ "/#", CUSTOMER_ID);
        sUriMatcher.addURI(CartContract.CONTENT_AUTHORITY, CartContract.PATH_ADDRESSS, ADDRESSS);
        sUriMatcher.addURI(CartContract.CONTENT_AUTHORITY, CartContract.PATH_ADDRESSS+ "/#", ADDRESS_ID);
        sUriMatcher.addURI(CartContract.CONTENT_AUTHORITY, CartContract.PATH_PHONES, PHONES);
        sUriMatcher.addURI(CartContract.CONTENT_AUTHORITY, CartContract.PATH_PHONES+ "/#", PHONE_ID);

    }

    @Override
    public boolean onCreate() {
        mDbHelper = new CartDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                cursor = database.query(MenEntry.TABLE_NAME_PRODUCTS, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PRODUCT_ID:
                selection = MenEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(MenEntry.TABLE_NAME_PRODUCTS, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case CARTS:
                cursor = database.query(MenEntry.TABLE_NAME_CART, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CART_ID:
                selection = MenEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(MenEntry.TABLE_NAME_CART, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case CUSTOMERS:
                cursor = database.query(MenEntry.TABLE_NAME_CUSTOMER, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CUSTOMER_ID:
                selection = MenEntry.COLUMN_CUST_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(MenEntry.TABLE_NAME_CUSTOMER, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case ADDRESSS:
                cursor = database.query(MenEntry.TABLE_NAME_ADDRESS, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case ADDRESS_ID:
                selection = MenEntry.COLUMN_CUST_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(MenEntry.TABLE_NAME_ADDRESS, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case PHONES:
                cursor = database.query(MenEntry.TABLE_NAME_PHONE, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PHONE_ID:
                selection = MenEntry.COLUMN_CUST_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(MenEntry.TABLE_NAME_PHONE, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return insertProduct(uri, contentValues);
            case CARTS:
                return insertCart(uri, contentValues);
            case CUSTOMERS:
                return insertCustomer(uri, contentValues);
            case PHONES:
                return insertPhone(uri, contentValues);
            case ADDRESSS:
                return insertAddress(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertProduct(Uri uri, ContentValues values) {

        String name = values.getAsString(MenEntry.COLUMN_PRODUCT_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Requires a name");
        }

        Integer gender = values.getAsInteger(MenEntry.COLUMN_PRODUCT_GENDER);
        if (gender == null || !MenEntry.isValidGender(gender)) {
            throw new IllegalArgumentException("Requires valid gender");
        }

        Integer price = values.getAsInteger(MenEntry.COLUMN_PRODUCT_PRICE);
        if (price != null && price < 0) {
            throw new IllegalArgumentException("Requires valid price");
        }


        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(MenEntry.TABLE_NAME_PRODUCTS, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }


    private Uri insertCart(Uri uri, ContentValues values) {

        String name = values.getAsString(MenEntry.COLUMN_PRODUCT_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Requires a name");
        }

        Integer weight = values.getAsInteger(MenEntry.COLUMN_PRODUCT_PRICE);
        if (weight != null && weight < 0) {
            throw new IllegalArgumentException("Requires valid price");
        }


        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(MenEntry.TABLE_NAME_CART, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertCustomer(Uri uri, ContentValues values) {

        String fname = values.getAsString(MenEntry.COLUMN_FIRST_NAME);
        if (fname == null) {
            throw new IllegalArgumentException("Requires a name");
        }

        String lname = values.getAsString(MenEntry.COLUMN_LAST_NAME);
        if (lname == null) {
            throw new IllegalArgumentException("Requires a name");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(MenEntry.TABLE_NAME_CUSTOMER, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertPhone(Uri uri, ContentValues values) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(MenEntry.TABLE_NAME_PHONE, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertAddress(Uri uri, ContentValues values) {


        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(MenEntry.TABLE_NAME_ADDRESS, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }


    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return updateProduct(uri, contentValues, selection, selectionArgs);
            case PRODUCT_ID:
                selection = MenEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateProduct(uri, contentValues, selection, selectionArgs);
            case CARTS:
                return updateCart(uri, contentValues, selection, selectionArgs);
            case CART_ID:
                selection = MenEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateCart(uri, contentValues, selection, selectionArgs);
            case CUSTOMERS:
                return updateCustomer(uri, contentValues, selection, selectionArgs);
            case CUSTOMER_ID:
                selection = MenEntry.COLUMN_CUST_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateCustomer(uri, contentValues, selection, selectionArgs);
            case ADDRESSS:
                return updateAddress(uri, contentValues, selection, selectionArgs);
            case ADDRESS_ID:
                selection = MenEntry.COLUMN_CUST_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateAddress(uri, contentValues, selection, selectionArgs);
            case PHONES:
                return updatePhone(uri, contentValues, selection, selectionArgs);
            case PHONE_ID:
                selection = MenEntry.COLUMN_CUST_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePhone(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }

    }

    private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(MenEntry.COLUMN_PRODUCT_NAME)) {
            String name = values.getAsString(MenEntry.COLUMN_PRODUCT_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Requires a name");
            }
        }

        if (values.containsKey(MenEntry.COLUMN_PRODUCT_GENDER)) {
            Integer gender = values.getAsInteger(MenEntry.COLUMN_PRODUCT_GENDER);
            if (gender == null || !MenEntry.isValidGender(gender)) {
                throw new IllegalArgumentException("Requires valid gender");
            }
        }


        if (values.containsKey(MenEntry.COLUMN_PRODUCT_PRICE)) {
            Integer weight = values.getAsInteger(MenEntry.COLUMN_PRODUCT_PRICE);
            if (weight != null && weight < 0) {
                throw new IllegalArgumentException("Requires valid weight");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(MenEntry.TABLE_NAME_PRODUCTS, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }


    private int updateCart(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(MenEntry.COLUMN_PRODUCT_NAME)) {
            String name = values.getAsString(MenEntry.COLUMN_PRODUCT_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Requires a name");
            }
        }


        if (values.containsKey(MenEntry.COLUMN_PRODUCT_GENDER)) {
            Integer gender = values.getAsInteger(MenEntry.COLUMN_PRODUCT_GENDER);
            if (gender == null || !MenEntry.isValidGender(gender)) {
                throw new IllegalArgumentException("Requires valid gender");
            }
        }

        if (values.containsKey(MenEntry.COLUMN_PRODUCT_PRICE)) {
            Integer price = values.getAsInteger(MenEntry.COLUMN_PRODUCT_PRICE);
            if (price != null && price < 0) {
                throw new IllegalArgumentException("Requires valid price");
            }
        }

        if (values.containsKey(MenEntry.COLUMN_PRODUCT_DESCRIPTION)) {
            String type = values.getAsString(MenEntry.COLUMN_PRODUCT_DESCRIPTION);
            if (type != "Top" || type != "Bottom" || type != "Shoe") {
                throw new IllegalArgumentException("Type can only be Top , Bottom or Shoe (case sensitive)");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(MenEntry.TABLE_NAME_CART, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    private int updateCustomer(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String fname = values.getAsString(MenEntry.COLUMN_FIRST_NAME);
        if (fname == null) {
            throw new IllegalArgumentException("Requires a name");
        }

        String lname = values.getAsString(MenEntry.COLUMN_LAST_NAME);
        if (lname == null) {
            throw new IllegalArgumentException("Requires a name");
        }


        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(MenEntry.TABLE_NAME_CUSTOMER, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    private int updatePhone(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(MenEntry.TABLE_NAME_PHONE, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }


    private int updateAddress(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(MenEntry.TABLE_NAME_ADDRESS, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted;


        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                rowsDeleted = database.delete(MenEntry.TABLE_NAME_PRODUCTS, selection, selectionArgs);
                break;

            case PRODUCT_ID:
                selection = MenEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(MenEntry.TABLE_NAME_PRODUCTS, selection, selectionArgs);
                break;

            case CARTS:
                rowsDeleted = database.delete(MenEntry.TABLE_NAME_CART, selection, selectionArgs);
                break;

            case CART_ID:
                selection = MenEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(MenEntry.TABLE_NAME_CART, selection, selectionArgs);
                break;

            case CUSTOMERS:
                rowsDeleted = database.delete(MenEntry.TABLE_NAME_CUSTOMER, selection, selectionArgs);
                break;

            case CUSTOMER_ID:
                selection = MenEntry.COLUMN_CUST_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(MenEntry.TABLE_NAME_CUSTOMER, selection, selectionArgs);
                break;
            case ADDRESSS:
                rowsDeleted = database.delete(MenEntry.TABLE_NAME_ADDRESS, selection, selectionArgs);
                break;

            case ADDRESS_ID:
                selection = MenEntry.COLUMN_CUST_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(MenEntry.TABLE_NAME_ADDRESS, selection, selectionArgs);
                break;
            case PHONES:
                rowsDeleted = database.delete(MenEntry.TABLE_NAME_PHONE, selection, selectionArgs);
                break;

            case PHONE_ID:
                selection = MenEntry.COLUMN_CUST_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(MenEntry.TABLE_NAME_PHONE, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return MenEntry.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return MenEntry.CONTENT_ITEM_TYPE;
            case CARTS:
                return MenEntry.CONTENT_LIST_TYPE;
            case CART_ID:
                return MenEntry.CONTENT_ITEM_TYPE;
            case CUSTOMERS:
                return MenEntry.CONTENT_LIST_TYPE;
            case CUSTOMER_ID:
                return MenEntry.CONTENT_ITEM_TYPE;
            case ADDRESSS:
                return MenEntry.CONTENT_LIST_TYPE;
            case ADDRESS_ID:
                return MenEntry.CONTENT_ITEM_TYPE;
            case PHONES:
                return MenEntry.CONTENT_LIST_TYPE;
            case PHONE_ID:
                return MenEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }



}


