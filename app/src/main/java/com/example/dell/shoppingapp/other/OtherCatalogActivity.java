package com.example.dell.shoppingapp.other;

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

public class OtherCatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int KID_LOADER = 0;
    OtherCursorAdapter mCursorAdapter;
    String mCurrentKidType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Intent intent = getIntent();
        mCurrentKidType = intent.getStringExtra("Type");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);


        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OtherCatalogActivity.this, OtherEditorActivity.class);
                startActivity(intent);
            }
        });



        final String c_id = intent.getStringExtra("CustomerId");
        final String fname = intent.getStringExtra("FirstName");
       // Toast.makeText(OtherCatalogActivity.this,"Name "+fname+"id "+c_id,Toast.LENGTH_SHORT).show();

        ListView KidListView = (ListView) findViewById(R.id.list);

        View emptyView = findViewById(R.id.empty_view);
        KidListView.setEmptyView(emptyView);

        mCursorAdapter =new OtherCursorAdapter(this,null);
        KidListView.setAdapter(mCursorAdapter);

        KidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, final long id) {

                CharSequence[] items = {"Edit", "Add to the cart?", "Back"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(OtherCatalogActivity.this);

                dialog.setTitle("Choose an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            Intent intent = new Intent(OtherCatalogActivity.this, OtherEditorActivity.class);
                            Uri currentKidUri = ContentUris.withAppendedId(MenEntry.CONTENT_URI_PRODUCT, id);
                            intent.setData(currentKidUri);
                            startActivity(intent);
                        }
                        if (item == 1) {

                            Intent intent = new Intent(OtherCatalogActivity.this, OtherDetailActivity.class);
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

        getLoaderManager().initLoader(KID_LOADER, null, this);
    }


    private void insertKid() {

        ContentValues values = new ContentValues();
        values.put(MenEntry.COLUMN_PRODUCT_NAME, "Orange Full Lenght Set");
        values.put(MenEntry.COLUMN_PRODUCT_DESCRIPTION, "GINI AND JONY");
        values.put(MenEntry.COLUMN_PRODUCT_GENDER, MenEntry.GENDER_MALE);
        values.put(MenEntry.COLUMN_PRODUCT_PRICE, 450);
        //values.put(MenEntry.COLUMN_MEN_IMAGE, File.createTempFile("fhmgjbnk" ,"C:\\Users\\DELL\\Documents\\AndroidStudio\\DeviceExplorer\\panasonic-eluga_ray_700-LJWCGIRG6D5T69YL\\sdcard\\DCIM\\Camera\\IMG_20180327_114911.jpg"));

        Uri newUri = getContentResolver().insert(MenEntry.CONTENT_URI_PRODUCT, values);
    }


    private void deleteAllKids() {
        int rowsDeleted = getContentResolver().delete(MenEntry.CONTENT_URI_PRODUCT, null, null);
        Log.v("OtherCatalogActivity", rowsDeleted + " rows deleted from KID database");
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
                deleteAllKids();
                return true;
        }
        switch (item.getItemId()) {

            case R.id.action_notifications:
                Intent intent = new Intent(OtherCatalogActivity.this, CartDisplayActivity.class);
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
        String selectionArgs[]={String.valueOf(MenEntry.GENDER_UNKNOWN),mCurrentKidType};

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

