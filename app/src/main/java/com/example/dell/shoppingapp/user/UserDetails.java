package com.example.dell.shoppingapp.user;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.shoppingapp.MainActivity;
import com.example.dell.shoppingapp.R;
import com.example.dell.shoppingapp.data.CartContract.MenEntry;

import org.w3c.dom.Text;

public class UserDetails extends AppCompatActivity {

    //private Uri mCurrentMenUri;
    private EditText mCustomerIdEditText;
    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private EditText mPhoneEditText;
    private EditText mAddressEditText;

    private Button btnOrderNow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        //final Intent intent = getIntent();
        //mCurrentMenUri = intent.getData();

        /*if (mCurrentMenUri == null) {
            setTitle("Add new Men");
            invalidateOptionsMenu();
        } else {
            setTitle("Edit text");
            getLoaderManager().initLoader(EXISTING_MEN_LOADER, null, this);
        }*/

        mFirstNameEditText = (EditText) findViewById(R.id.edit_first_name);
        mLastNameEditText = (EditText) findViewById(R.id.edit_last_name);
        mPhoneEditText = (EditText) findViewById(R.id.edit_phone);
        mAddressEditText = (EditText) findViewById(R.id.edit_address);
        mCustomerIdEditText=(EditText)findViewById(R.id.edit_customer_id);

        btnOrderNow = (Button) findViewById(R.id.btn_order_now);

        btnOrderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fnameString = mFirstNameEditText.getText().toString().trim();
                String lnameString = mLastNameEditText.getText().toString().trim();
                String phoneString = mPhoneEditText.getText().toString().trim();
                String addressString = mAddressEditText.getText().toString().trim();
                String idString = mCustomerIdEditText.getText().toString().trim();

                if (fnameString.equals("")) {
                    Toast.makeText(UserDetails.this, "Enter first name", Toast.LENGTH_SHORT).show();
                }
                else if (lnameString.equals("")) {
                    Toast.makeText(UserDetails.this, "Enter last name", Toast.LENGTH_SHORT).show();
                }
                else if (addressString.equals("")) {
                    Toast.makeText(UserDetails.this, "Enter address", Toast.LENGTH_SHORT).show();
                }
                else if (idString.equals("")) {
                    Toast.makeText(UserDetails.this, "Enter ID", Toast.LENGTH_SHORT).show();
                }
                else if (phoneString .length()!=10) {
                    Toast.makeText(UserDetails.this, "Enter correct phone number", Toast.LENGTH_SHORT).show();
                }
                else {


                    ContentValues phone_values = new ContentValues();
                    phone_values.put(MenEntry.COLUMN_CUST_ID, idString);
                    phone_values.put(MenEntry.COLUMN_PHONE, phoneString );

                    Uri phone_newUri = getContentResolver().insert(MenEntry.CONTENT_URI_PHONE, phone_values);

                    /*if (phone_newUri == null) {

                        Toast.makeText(UserDetails.this, "Insert failed", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(UserDetails.this, "Insert successful", Toast.LENGTH_SHORT).show();
                    }*/




                    ContentValues values = new ContentValues();
                    values.put(MenEntry.COLUMN_FIRST_NAME, fnameString);
                    values.put(MenEntry.COLUMN_LAST_NAME, lnameString);
                    values.put(MenEntry.COLUMN_PHONE, phoneString);
                    values.put(MenEntry.COLUMN_ADDRESS, addressString);
                    values.put(MenEntry.COLUMN_CUST_ID, idString);

                    Uri newUri = getContentResolver().insert(MenEntry.CONTENT_URI_CUSTOMER, values);

                    if (newUri == null) {

                        Toast.makeText(UserDetails.this, "Insert failed , cannot insert same ID", Toast.LENGTH_SHORT).show();
                        mFirstNameEditText.setText(" ");
                        mLastNameEditText.setText(" ");
                        mPhoneEditText.setText(" ");
                        mAddressEditText.setText(" ");
                        mCustomerIdEditText.setText(" ");
                    } else {

                        Toast.makeText(UserDetails.this, "Insert successful", Toast.LENGTH_SHORT).show();
                        mFirstNameEditText.setText(" ");
                        mLastNameEditText.setText(" ");
                        mPhoneEditText.setText(" ");
                        mAddressEditText.setText(" ");
                        mCustomerIdEditText.setText(" ");

                        Intent i=new Intent(UserDetails.this, MainActivity.class);
                        i.putExtra("CustomerId",idString);
                        i.putExtra("FirstName",fnameString);
                        startActivity(i);
                    }





                }
            }

        });

    }

}
