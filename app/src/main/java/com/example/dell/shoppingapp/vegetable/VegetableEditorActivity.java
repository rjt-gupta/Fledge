package com.example.dell.shoppingapp.vegetable;

import android.Manifest;
import android.app.AlertDialog;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dell.shoppingapp.R;
import com.example.dell.shoppingapp.data.CartContract.MenEntry;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class VegetableEditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_MEN_LOADER = 0;
    private Uri mCurrentMenUri;
    private EditText mNameEditText;
    private EditText mDescriptionEditText;

    private EditText mPriceEditText;

    private Spinner mGenderSpinner;

    private int mGender = MenEntry.GENDER_UNKNOWN;

    private boolean mMenHasChanged = false;

    private Button btnChoose;
    private ImageView mimageView;

    private int REQUEST_CODE_GALLERY=999;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mMenHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentMenUri = intent.getData();

        if (mCurrentMenUri == null) {
            setTitle("Add new Vegetable");
            invalidateOptionsMenu();
        } else {
            setTitle("Edit text");
            getLoaderManager().initLoader(EXISTING_MEN_LOADER, null, this);
        }

        mNameEditText = (EditText) findViewById(R.id.edit_product_name);
        mDescriptionEditText = (EditText) findViewById(R.id.edit_product_description);
        mPriceEditText = (EditText) findViewById(R.id.edit_product_price);
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);
        btnChoose = (Button) findViewById(R.id.btnChoose);
        mimageView = (ImageView) findViewById(R.id.imageView);




        mNameEditText.setOnTouchListener(mTouchListener);
        mDescriptionEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mGenderSpinner.setOnTouchListener(mTouchListener);
        mimageView.setOnTouchListener(mTouchListener);

        setupSpinner();

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        VegetableEditorActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

    }

    private void setupSpinner() {
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals("Vegetables")) {
                        mGender = MenEntry.GENDER_MALE;
                    } else if (selection.equals("Fruits")) {
                        mGender = MenEntry.GENDER_FEMALE;
                    } else {
                        mGender = MenEntry.GENDER_UNKNOWN;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = MenEntry.GENDER_UNKNOWN;
            }
        });
    }


    private void saveMen() {

        String nameString = mNameEditText.getText().toString().trim();
        String descriptionString = mDescriptionEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();

        if (mCurrentMenUri == null &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(descriptionString) &&
                TextUtils.isEmpty(priceString) && mGender == MenEntry.GENDER_UNKNOWN) {

            return;
        }


        if (descriptionString.equals("Organic") || descriptionString.equals("Company") || descriptionString.equals("Other")) {

            ContentValues values = new ContentValues();
            values.put(MenEntry.COLUMN_PRODUCT_NAME, nameString);
            values.put(MenEntry.COLUMN_PRODUCT_DESCRIPTION, descriptionString);
            values.put(MenEntry.COLUMN_PRODUCT_GENDER, mGender);
            values.put(MenEntry.COLUMN_PRODUCT_IMAGE, imageViewToByte(mimageView));

            int weight = 0;
            if (!TextUtils.isEmpty(priceString)) {
                weight = Integer.parseInt(priceString);
            }
            values.put(MenEntry.COLUMN_PRODUCT_PRICE, weight);


            if (mCurrentMenUri == null) {

                Uri newUri = getContentResolver().insert(MenEntry.CONTENT_URI_PRODUCT, values);

                if (newUri == null) {

                    Toast.makeText(this, "Insert failed",
                            Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(this, "Insert successful",
                            Toast.LENGTH_SHORT).show();
                }
            } else {

                int rowsAffected = getContentResolver().update(mCurrentMenUri, values, null, null);


                if (rowsAffected == 0) {

                    Toast.makeText(this, "Update Failed",
                            Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(this, "Update successful",
                            Toast.LENGTH_SHORT).show();
                }
            }

            finish();
        }else {
            Toast.makeText(this,"Description can only be \"Organic\" , \"Company\" or \"Other\" ",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (mCurrentMenUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_save:
                saveMen();


                return true;

            case R.id.action_delete:

                showDeleteConfirmationDialog();
                return true;

            case android.R.id.home:

                if (!mMenHasChanged) {
                    NavUtils.navigateUpFromSameTask(VegetableEditorActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                NavUtils.navigateUpFromSameTask(VegetableEditorActivity.this);
                            }
                        };


                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        if (!mMenHasChanged) {
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


        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                MenEntry._ID,
                MenEntry.COLUMN_PRODUCT_NAME,
                MenEntry.COLUMN_PRODUCT_DESCRIPTION,
                MenEntry.COLUMN_PRODUCT_GENDER,
                MenEntry.COLUMN_PRODUCT_PRICE,
                MenEntry.COLUMN_PRODUCT_IMAGE};


        return new CursorLoader(this,
                mCurrentMenUri,
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
            int breedColumnIndex = cursor.getColumnIndex(MenEntry.COLUMN_PRODUCT_DESCRIPTION);
            int genderColumnIndex = cursor.getColumnIndex(MenEntry.COLUMN_PRODUCT_GENDER);
            int weightColumnIndex = cursor.getColumnIndex(MenEntry.COLUMN_PRODUCT_PRICE);
            int imageColumnIndex=cursor.getColumnIndex(MenEntry.COLUMN_PRODUCT_IMAGE);



            String name = cursor.getString(nameColumnIndex);
            String breed = cursor.getString(breedColumnIndex);
            int gender = cursor.getInt(genderColumnIndex);
            int weight = cursor.getInt(weightColumnIndex);
            byte[] image=cursor.getBlob(imageColumnIndex);


            mNameEditText.setText(name);
            mDescriptionEditText.setText(breed);
            mPriceEditText.setText(Integer.toString(weight));

            ByteArrayInputStream inputStream = new ByteArrayInputStream(image);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            mimageView.setImageBitmap(bitmap);


            switch (gender) {
                case MenEntry.GENDER_MALE:
                    mGenderSpinner.setSelection(1);
                    break;
                case MenEntry.GENDER_FEMALE:
                    mGenderSpinner.setSelection(2);
                    break;
                default:
                    mGenderSpinner.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEditText.setText("");
        mDescriptionEditText.setText("");
        mPriceEditText.setText("");
        mGenderSpinner.setSelection(0);

    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to discard changes made?");
        builder.setPositiveButton("Discard", discardButtonClickListener);
        builder.setNegativeButton("Keep Editing", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteMen();
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

    private void deleteMen() {
        if (mCurrentMenUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentMenUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, "Delete Men failed",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Delete Men successful",
                        Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
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

}

