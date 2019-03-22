package com.example.dell.shoppingapp.other;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.shoppingapp.R;
import com.example.dell.shoppingapp.cart.CartDisplayActivity;
import com.example.dell.shoppingapp.data.CartContract.MenEntry;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class OtherDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

        private static final int EXISTING_KID_LOADER = 0;
        private Uri mCurrentKidUri;

        private TextView mNameTextView;
        private TextView mDescriptionTextView;
        private TextView mPriceTextView;
        private TextView mQuantityTextView;
        private TextView mTotalCostTextView;

        private boolean mKidHasChanged = false;
        private ImageView mimageView;

        Button buttonAddToCart;


        int total_cost;

        private int REQUEST_CODE_GALLERY = 999;

        private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mKidHasChanged = true;
                return false;
            }
        };

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.item_details);

            Intent intent = getIntent();
            mCurrentKidUri = intent.getData();

            mNameTextView = (TextView) findViewById(R.id.item_name);
            mDescriptionTextView = (TextView) findViewById(R.id.item_description);
            mPriceTextView = (TextView) findViewById(R.id.item_price);
            mQuantityTextView = (TextView) findViewById(R.id.item_quantity);
            mTotalCostTextView = (TextView) findViewById(R.id.item_total_cost);

            mimageView = (ImageView) findViewById(R.id.item_image);
            buttonAddToCart = (Button) findViewById(R.id.btn_cart);

            if (mCurrentKidUri == null) {
                setTitle("Add new Other");
                invalidateOptionsMenu();
            } else {
                setTitle("Edit text");
                getLoaderManager().initLoader(EXISTING_KID_LOADER, null, this);
            }

        }

        public void addToCart(View view) {

            Intent i = getIntent();
            mCurrentKidUri = i.getData();
            final String c_id = i.getStringExtra("CustomerId");
            final String fname = i.getStringExtra("FirstName");
            final String p_id=i.getStringExtra("ProductId");
            Toast.makeText(OtherDetailActivity.this,"Name "+fname+"id "+c_id+"Product Id "+p_id,Toast.LENGTH_SHORT).show();


            String nameString = mNameTextView.getText().toString().trim();
            String descriptionString = mDescriptionTextView.getText().toString().trim();
            int priceInteger = Integer.parseInt(mPriceTextView.getText().toString().trim());
            int quantityInteger = Integer.parseInt(mQuantityTextView.getText().toString().trim());
            int totalpriceInteger = Integer.parseInt(mTotalCostTextView.getText().toString().trim());


            ContentValues values = new ContentValues();
            values.put(MenEntry.COLUMN_PRODUCT_NAME, nameString);
            values.put(MenEntry.COLUMN_PRODUCT_TOTAL_PRICE, totalpriceInteger);
            values.put(MenEntry.COLUMN_PRODUCT_QUANTITY, quantityInteger);
            values.put(MenEntry.COLUMN_CUST_ID, c_id);
            values.put(MenEntry._ID, p_id );
            values.put(MenEntry.COLUMN_PRODUCT_IMAGE, imageViewToByte(mimageView));


            Uri newUri = getContentResolver().insert(MenEntry.CONTENT_URI_CART, values);

            if (newUri == null) {

                Toast.makeText(this, "Insert failed", Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(this, "Insert successful", Toast.LENGTH_SHORT).show();
            }


            ContentValues address_values = new ContentValues();
            address_values.put(MenEntry.COLUMN_CUST_ID, c_id);
            address_values.put(MenEntry._ID, p_id );

            Uri address_newUri = getContentResolver().insert(MenEntry.CONTENT_URI_ADDRESS, address_values);

            if (address_newUri == null) {

                Toast.makeText(this, "Insert failed", Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(this, "Insert successful", Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(OtherDetailActivity.this, CartDisplayActivity.class);
            startActivity(intent);

        }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {

            getMenuInflater().inflate(R.menu.menu_cart, menu);
            return true;
        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            switch (item.getItemId()) {

                case R.id.action_notifications:
                    Intent intent = new Intent(OtherDetailActivity.this, CartDisplayActivity.class);
                    startActivity(intent);
                    return true;

            }
            return super.onOptionsItemSelected(item);
        }


        @Override
        public void onBackPressed() {//TODO

            if (!mKidHasChanged) {
                super.onBackPressed();
                return;
            }


            DialogInterface.OnClickListener discardButtonClickListener =
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            finish();
                        }
                    };


            //showUnsavedChangesDialog(discardButtonClickListener);
        }

        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

            String[] projection = {
                    MenEntry._ID,
                    MenEntry.COLUMN_PRODUCT_NAME,
                    MenEntry.COLUMN_PRODUCT_DESCRIPTION,
                    MenEntry.COLUMN_PRODUCT_PRICE,
                    MenEntry.COLUMN_PRODUCT_IMAGE};


            return new CursorLoader(this,
                    mCurrentKidUri,
                    projection,
                    null,
                    null,
                    null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

            if (cursor == null || cursor.getCount() < 1) {
                return;
            }


            if (cursor.moveToFirst()) {

                int nameColumnIndex = cursor.getColumnIndex(MenEntry.COLUMN_PRODUCT_NAME);
                int descriptionColumnIndex = cursor.getColumnIndex(MenEntry.COLUMN_PRODUCT_DESCRIPTION);
                int priceColumnIndex = cursor.getColumnIndex(MenEntry.COLUMN_PRODUCT_PRICE);
                int imageColumnIndex = cursor.getColumnIndex(MenEntry.COLUMN_PRODUCT_IMAGE);


                String name = cursor.getString(nameColumnIndex);
                String description = cursor.getString(descriptionColumnIndex);
                int price = cursor.getInt(priceColumnIndex);
                byte[] image = cursor.getBlob(imageColumnIndex);


                mNameTextView.setText(name);
                mDescriptionTextView.setText(description);
                mPriceTextView.setText(Integer.toString(price));
                mTotalCostTextView.setText(Integer.toString(price));

                ByteArrayInputStream inputStream = new ByteArrayInputStream(image);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                mimageView.setImageBitmap(bitmap);


            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mNameTextView.setText("");
            mPriceTextView.setText("");
            mDescriptionTextView.setText("");

        }


        public static byte[] imageViewToByte(ImageView image) {
            Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            return byteArray;
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

            if (requestCode == REQUEST_CODE_GALLERY) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_CODE_GALLERY);
                } else {
                    Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
                Uri uri = data.getData();

                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);

                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    mimageView.setImageBitmap(bitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            super.onActivityResult(requestCode, resultCode, data);
        }

        private void displayQuantity(int numberOfItems) {
            mQuantityTextView.setText(String.valueOf(numberOfItems));
        }

        private void displayCost(int totalPrice) {
            mTotalCostTextView.setText(String.valueOf(totalPrice));
        }


        public void increment(View view) {

            int priceInteger = Integer.parseInt(mPriceTextView.getText().toString().trim());
            int quantityInteger = Integer.parseInt(mQuantityTextView.getText().toString().trim());


            quantityInteger = quantityInteger + 1;
            displayQuantity(quantityInteger);
            total_cost = quantityInteger * priceInteger;
            displayCost(total_cost);
        }

        public void decrement(View view) {

            int priceInteger = Integer.parseInt(mPriceTextView.getText().toString().trim());
            int quantityInteger = Integer.parseInt(mQuantityTextView.getText().toString().trim());

            if (quantityInteger > 1) {

                quantityInteger = quantityInteger - 1;
                displayQuantity(quantityInteger);
                total_cost = quantityInteger * priceInteger;
                displayCost(total_cost);

            } else {
                Toast.makeText(this, "You cannot buy 0 items!!", Toast.LENGTH_SHORT).show();
            }
        }

    }



    /*

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContentResolver = this.getContentResolver();
        //FragranceDbHelper dbHelper = new FragranceDbHelper(this);
        //mDb = dbHelper.getWritableDatabase();


        mImage = (ImageView) findViewById(R.id.fragranceImage);
        Intent intentThatStartedThisActivity = getIntent();
        addToCartButton = (Button) findViewById(R.id.cart_button);

        costTextView = (TextView) findViewById(
                R.id.cost_text_view);

        if (intentThatStartedThisActivity.hasExtra(ITEM_NAME)) {

            itemName = getIntent().getExtras().getString(ITEM_NAME);
            description = getIntent().getExtras().getString(ITEM_DESCRIPTION);
            price = getIntent().getExtras().getDouble(ITEM_PRICE);

            TextView desc = (TextView) findViewById(R.id.description);
            desc.setText(description);

            TextView fragmentPrice = (TextView) findViewById(R.id.price);
            DecimalFormat precision = new DecimalFormat("0.00");
            fragmentPrice.setText("$" + precision.format(price));

            float f = Float.parseFloat(Double.toString(rating));

            setTitle(itemName);


        }


        if (mQuantity == 1) {

            mTotalPrice = price;
            displayCost(mTotalPrice);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_notifications:
                Intent intent = new Intent(this, CartDisplayActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        new FetchCountTask().execute();
    }

    public void increment(View view) {

        price = getIntent().getExtras().getDouble(ITEM_PRICE);
        mQuantity = mQuantity + 1;
        displayQuantity(mQuantity);
        mTotalPrice = mQuantity * price;
        displayCost(mTotalPrice);
    }

    public void decrement(View view) {
        if (mQuantity > 1) {

            mQuantity = mQuantity - 1;
            displayQuantity(mQuantity);
            mTotalPrice = mQuantity * price;
            displayCost(mTotalPrice);

        }
    }

    private void displayQuantity(int numberOfItems) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText(String.valueOf(numberOfItems));
    }

    private void displayCost(double totalPrice) {

        String convertPrice = NumberFormat.getCurrencyInstance().format(totalPrice);
        costTextView.setText(convertPrice);
    }

    private void addValuesToCart() {

        ContentValues cartValues = new ContentValues();

        cartValues.put(MenEntry.COLUMN_MEN_NAME, itemName);
        cartValues.put(MenEntry.COLUMN_MEN_IMAGE, itemImage);
        cartValues.put(MenEntry.COLUMN_MEN_QUANTITY, mQuantity);
        cartValues.put(MenEntry.COLUMN_MEN_TOTAL_PRICE, mTotalPrice);


        mContentResolver.insert(MenEntry.CONTENT_URI_CART, cartValues);

        Toast.makeText(this, "Successfully added to Cart",
                Toast.LENGTH_SHORT).show();


    }

    public void addToCart(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Add to cart?");
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                addValuesToCart();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void updateNotificationsBadge(int count) {
        mNotificationsCount = count;

        invalidateOptionsMenu();
    }

    class FetchCountTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            String countQuery = "SELECT  * FROM CART";
            Cursor cursor = mDb.rawQuery(countQuery, null);
            int count = cursor.getCount();
            cursor.close();
            return count;

        }

        @Override
        public void onPostExecute(Integer count) {
            updateNotificationsBadge(count);
        }
    }

*/




