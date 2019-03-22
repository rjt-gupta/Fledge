package com.example.dell.shoppingapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class CartContract {

    private CartContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.example.dell.shoppingapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PRODUCTS = "product";
    public static final String PATH_CARTS = "cart";
    public static final String PATH_CUSTOMERS = "customer";
    public static final String PATH_ADDRESSS = "address";
    public static final String PATH_PHONES = "phone";

    public static final class MenEntry implements BaseColumns {

        public static final Uri CONTENT_URI_PRODUCT = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);
        public static final Uri CONTENT_URI_CART = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CARTS);
        public static final Uri CONTENT_URI_CUSTOMER = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CUSTOMERS);
        public static final Uri CONTENT_URI_ADDRESS = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ADDRESSS);
        public static final Uri CONTENT_URI_PHONE = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PHONES);

        public final static String TABLE_NAME_PRODUCTS = "products";
        public final static String TABLE_NAME_CART= "cart";
        public final static String TABLE_NAME_CUSTOMER= "customer";
        public final static String TABLE_NAME_ADDRESS= "address";
        public final static String TABLE_NAME_PHONE= "phone";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_PRODUCT_NAME = "name";
        public final static String COLUMN_PRODUCT_DESCRIPTION = "type";
        public final static String COLUMN_PRODUCT_GENDER = "sex";
        public final static String COLUMN_PRODUCT_PRICE = "price";

        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;

        public static final String COLUMN_PRODUCT_IMAGE="image";
        public static final String COLUMN_PRODUCT_QUANTITY="quantity";
        public static final String COLUMN_PRODUCT_TOTAL_PRICE="totalprice";

        public static final String COLUMN_CUST_ID = "customer"+BaseColumns._ID;
        public static final String COLUMN_FIRST_NAME="fname";
        public static final String COLUMN_LAST_NAME="lname";
        public static final String COLUMN_ADDRESS="address";
        public static final String COLUMN_PHONE="phone";

        public static boolean isValidGender(int gender) {
            if (gender == GENDER_UNKNOWN || gender == GENDER_MALE || gender == GENDER_FEMALE) {
                return true;
            }
            return false;
        }

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

    }

}

