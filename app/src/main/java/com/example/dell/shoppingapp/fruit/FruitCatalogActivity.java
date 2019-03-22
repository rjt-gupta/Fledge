package com.example.dell.shoppingapp.fruit;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dell.shoppingapp.R;
import com.example.dell.shoppingapp.cart.CartDisplayActivity;
import com.example.dell.shoppingapp.data.CartContract.MenEntry;

public class FruitCatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int WOMEN_LOADER = 0;
    FruitCursorAdapter mCursorAdapter;
    String mCurrentWomenType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Intent intent = getIntent();
        mCurrentWomenType = intent.getStringExtra("Type");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);


        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FruitCatalogActivity.this, FruitEditorActivity.class);
                startActivity(intent);
            }
        });


        final String c_id = intent.getStringExtra("CustomerId");
        final String fname = intent.getStringExtra("FirstName");
        Toast.makeText(FruitCatalogActivity.this,"Name "+fname+"id "+c_id,Toast.LENGTH_SHORT).show();


        ListView WomenListView = (ListView) findViewById(R.id.list);

        View emptyView = findViewById(R.id.empty_view);
        WomenListView.setEmptyView(emptyView);

        mCursorAdapter = new FruitCursorAdapter(this, null);
        WomenListView.setAdapter(mCursorAdapter);

        WomenListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, final long id) {

                CharSequence[] items = {"Edit", "Add to the cart?", "Back"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(FruitCatalogActivity.this);

                dialog.setTitle("Choose an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            Intent intent = new Intent(FruitCatalogActivity.this, FruitEditorActivity.class);
                            Uri currentWomenUri = ContentUris.withAppendedId(MenEntry.CONTENT_URI_PRODUCT, id);
                            intent.setData(currentWomenUri);
                            startActivity(intent);
                        }
                        if (item == 1) {

                            Intent intent = new Intent(FruitCatalogActivity.this, FruitDetailActivity.class);
                            Uri currentCartUri = ContentUris.withAppendedId(MenEntry.CONTENT_URI_PRODUCT, id);
                            intent.setData(currentCartUri);
                            intent.putExtra("ProductId",Long.toString(id));
                            intent.putExtra("CustomerId",c_id);
                            intent.putExtra("FirstName",fname);
                            startActivity(intent);}
                            else {
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
                return;
            }

        });

        getLoaderManager().initLoader(WOMEN_LOADER, null, this);

    }

    private void insertWomen() {

        ContentValues values = new ContentValues();
        values.put(MenEntry.COLUMN_PRODUCT_NAME, "Little Black Dress");
        values.put(MenEntry.COLUMN_PRODUCT_DESCRIPTION, "AND");
        values.put(MenEntry.COLUMN_PRODUCT_GENDER, MenEntry.GENDER_MALE);
        values.put(MenEntry.COLUMN_PRODUCT_PRICE, 1500);

        Uri newUri = getContentResolver().insert(MenEntry.CONTENT_URI_PRODUCT, values);
    }


    private void deleteAllWomens() {
        int rowsDeleted = getContentResolver().delete(MenEntry.CONTENT_URI_PRODUCT, null, null);
        Log.v("FruitCatalogActivity", rowsDeleted + " rows deleted from Women database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_delete_all_entries:
                deleteAllWomens();
                return true;
        }
        switch (item.getItemId()) {

            case R.id.action_notifications:
                Intent intent = new Intent(FruitCatalogActivity.this, CartDisplayActivity.class);
                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                MenEntry._ID,
                MenEntry.COLUMN_PRODUCT_NAME,
                MenEntry.COLUMN_PRODUCT_GENDER,
                MenEntry.COLUMN_PRODUCT_DESCRIPTION,
                MenEntry.COLUMN_PRODUCT_IMAGE};

        String selection=MenEntry.COLUMN_PRODUCT_GENDER+" = ? and " + MenEntry.COLUMN_PRODUCT_DESCRIPTION+" = ?";
        String selectionArgs[]={String.valueOf(MenEntry.GENDER_FEMALE),mCurrentWomenType};


        return new CursorLoader(this,
                MenEntry.CONTENT_URI_PRODUCT,
                projection,
                selection,
                selectionArgs,
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


