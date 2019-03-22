package com.example.dell.shoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.dell.shoppingapp.vegetable.VegetableCatalogActivity;


public class VegetableTypeSelectActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_type);

        Button top_type =  findViewById(R.id.top);
        Button bottom_type = findViewById(R.id.bottom);
        Button shoe_type = findViewById(R.id.shoe);


        Intent intent = getIntent();
        final String id = intent.getStringExtra("CustomerId");
        final String fname = intent.getStringExtra("FirstName");
        Toast.makeText(VegetableTypeSelectActivity.this,"Name "+fname+"id "+id,Toast.LENGTH_SHORT).show();


        top_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VegetableTypeSelectActivity.this, VegetableCatalogActivity.class);
                intent.putExtra("Type", "Organic");
                intent.putExtra("CustomerId",id);
                intent.putExtra("FirstName",fname);
                startActivity(intent);

            }
        });

        bottom_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VegetableTypeSelectActivity.this, VegetableCatalogActivity.class);
                intent.putExtra("Type", "Company");
                intent.putExtra("CustomerId",id);
                intent.putExtra("FirstName",fname);
                startActivity(intent);
            }
        });

        shoe_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VegetableTypeSelectActivity.this, VegetableCatalogActivity.class);
                intent.putExtra("Type", "Other");
                intent.putExtra("CustomerId",id);
                intent.putExtra("FirstName",fname);
                startActivity(intent);
            }
        });
    }


}


