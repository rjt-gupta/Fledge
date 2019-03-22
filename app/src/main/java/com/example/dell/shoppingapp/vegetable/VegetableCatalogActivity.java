package com.example.dell.shoppingapp.vegetable;

import android.app.LoaderManager;
import android.content.ContentUris;
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




public class VegetableCatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int MEN_LOADER = 0;
    VegetableCursorAdapter mCursorAdapter;
    Cursor dataCursor;
    private ListView MenListView;
    String mCurrentMenType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        mCurrentMenType = intent.getStringExtra("Type");


        final String c_id = intent.getStringExtra("CustomerId");
        final String fname = intent.getStringExtra("FirstName");
        Toast.makeText(VegetableCatalogActivity.this,"Name "+fname+" Id "+c_id,Toast.LENGTH_SHORT).show();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VegetableCatalogActivity.this, VegetableEditorActivity.class);
                startActivity(intent);

            }
        });



        MenListView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        MenListView.setEmptyView(emptyView);

        mCursorAdapter = new VegetableCursorAdapter(this, null);
        MenListView.setAdapter(mCursorAdapter);





  MenListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, final long id) {


                CharSequence[] items = {"Edit","Add to the cart?", "Back"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(VegetableCatalogActivity.this);

                dialog.setTitle("Choose an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if(item==0){
                            Intent intent = new Intent(VegetableCatalogActivity.this, VegetableEditorActivity.class);
                            Uri currentMenUri = ContentUris.withAppendedId(MenEntry.CONTENT_URI_PRODUCT, id);
                            intent.setData(currentMenUri);
                            startActivity(intent);
                        }

                        if (item == 1) {

                            Intent intent = new Intent(VegetableCatalogActivity.this, VegetableDetailActivity.class);
                            Uri currentCartUri = ContentUris.withAppendedId(MenEntry.CONTENT_URI_PRODUCT, id);
                            intent.setData(currentCartUri);
                            intent.putExtra("ProductId",Long.toString(id));
                            intent.putExtra("CustomerId",c_id);
                            intent.putExtra("FirstName",fname);
                            startActivity(intent);


                            /*TextView textview =((TextView)view.findViewById(R.id.tvInVisitorName)).getText().toString();



                            Intent intent = new Intent(VegetableCatalogActivity.this, CartDisplayActivity.class);
                            intent.putExtra(FRAGRANCE_NAME, getItem(pos).name);
                            intent.putExtra(FRAGRANCE_DESCRIPTION, getItem(pos).description);
                            intent.putExtra(FRAGRANCE_PRICE, getItem(pos).price);
                            intent.putExtra(FRAGRANCE_IMAGE, getItem(pos).imageUrl);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);



                            Cursor itemCursor = (Cursor) VegetableCatalogActivity.this.MenListView.getItemAtPosition(position);
                            if (itemCursor == null || itemCursor.getCount() < 1){
                                return;
                            }
                            itemCursor.moveToFirst();


                            String itemName = itemCursor.getString(itemCursor.getColumnIndex(MenEntry.COLUMN_MEN_NAME));
                            String itemSummary = itemCursor.getString(itemCursor.getColumnIndex(MenEntry.COLUMN_MEN_DESCRIPTION));
                            int itemPrice =itemCursor.getInt(itemCursor.getColumnIndex(MenEntry.COLUMN_MEN_PRICE));
                            Intent intent = new Intent(getApplicationContext(), CartDisplayActivity.class);
                            intent.putExtra(ITEM_NAME, itemName);
                            intent.putExtra(ITEM_DESCRIPTION, itemSummary);
                            intent.putExtra(ITEM_PRICE, itemPrice);

                            startActivity(intent);*/


                        } else {
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
                return;




            }
        });



                getLoaderManager().initLoader(MEN_LOADER, null, this);



}



    private void deleteAllMens() {
        int rowsDeleted = getContentResolver().delete(MenEntry.CONTENT_URI_PRODUCT, null, null);
        Log.v("VegetableCatalogActivity", rowsDeleted + " rows deleted from Men database");
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
                deleteAllMens();
                return true;
        }
        switch (item.getItemId()) {

            case R.id.action_notifications:
                Intent intent = new Intent(VegetableCatalogActivity.this, CartDisplayActivity.class);
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
                MenEntry.COLUMN_PRODUCT_IMAGE };

        String selection=MenEntry.COLUMN_PRODUCT_GENDER+" = ? and " + MenEntry.COLUMN_PRODUCT_DESCRIPTION+" = ?";
        String selectionArgs[]={String.valueOf(MenEntry.GENDER_MALE),mCurrentMenType};

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

        switch (loader.getId()) {
            case MEN_LOADER:
                mCursorAdapter.swapCursor(null);
                break;
        }

    }

}

