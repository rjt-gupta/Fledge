package com.example.dell.shoppingapp.cart;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dell.shoppingapp.R;
import com.example.dell.shoppingapp.data.CartContract.MenEntry;


public class CartDisplayActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CART_LOADER = 0;
    OrderAdapter mCursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartCatalogActivity.this, CartEditorActivity.class);
                startActivity(intent);

            }
        });*/


        ListView CartListView = (ListView) findViewById(R.id.cart_list);

        View emptyView = findViewById(R.id.cart_empty_view);
        CartListView.setEmptyView(emptyView);

        mCursorAdapter = new OrderAdapter(this, null);
        CartListView.setAdapter(mCursorAdapter);



        CartListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, final long id) {

                /*Intent intent = new Intent(CartCatalogActivity.this, OtherEditorActivity.class);
                Uri currentKIDUri = ContentUris.withAppendedId(MenEntry.CONTENT_URI_KID, id);
                intent.setData(currentKIDUri);
                startActivity(intent);*/
                CharSequence[] items = {"Delete from cart", "Back"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(CartDisplayActivity.this);

                dialog.setTitle("Choose an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        Uri mCurrentProductUri = ContentUris.withAppendedId(MenEntry.CONTENT_URI_CART , id);

                        if (item == 0) {

                            if (mCurrentProductUri != null) {
                                int rowsDeleted = getContentResolver().delete(mCurrentProductUri, null, null);

                                if (rowsDeleted == 0) {
                                    Toast.makeText(CartDisplayActivity.this, "Delete failed!!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(CartDisplayActivity.this, "Deleted from cart successfully", Toast.LENGTH_SHORT).show();
                                }
                            }

                            //int rowsDeleted = getContentResolver().delete(MenEntry.CONTENT_URI_CART, null, null);


                           /* if (mCurrentMenUri != null) {
                                int rowsDeleted = getContentResolver().delete(mCurrentMenUri, null, null);

                                if (rowsDeleted == 0) {
                                    Toast.makeText(this, "Delete Men failed",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(this, "Delete Men successful",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                            finish();*/



                        } else {
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
                return;
            }

        });


        Button proceed_to_order=(Button)findViewById(R.id.proceed_to_order);
        proceed_to_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CartDisplayActivity.this,"Thanks for shopping!!!",Toast.LENGTH_SHORT).show();
            }
        });



        getLoaderManager().initLoader(CART_LOADER, null, this);

    }




    private void deleteAllCarts() {
        int rowsDeleted = getContentResolver().delete(MenEntry.CONTENT_URI_CART, null, null);
        Log.v("CartCatalogActivity", rowsDeleted + " rows deleted from Cart database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_delete_all_entries:
                deleteAllCarts();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                MenEntry._ID,
                MenEntry.COLUMN_PRODUCT_NAME,
                MenEntry.COLUMN_PRODUCT_TOTAL_PRICE,
                MenEntry.COLUMN_PRODUCT_IMAGE,
                MenEntry.COLUMN_PRODUCT_QUANTITY};

        return new CursorLoader(this,
                MenEntry.CONTENT_URI_CART,
                projection,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mCursorAdapter.swapCursor(null);
    }


}


