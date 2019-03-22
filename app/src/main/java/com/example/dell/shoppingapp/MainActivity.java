package com.example.dell.shoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.dell.shoppingapp.cart.CartDisplayActivity;
import com.example.dell.shoppingapp.user.UserDetails;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Intent intent = getIntent();
        final String id = intent.getStringExtra("CustomerId");
        final String fname = intent.getStringExtra("FirstName");
        Toast.makeText(MainActivity.this,"Name "+fname+"id "+id,Toast.LENGTH_SHORT).show();
        /*TextView mFNameTextView = (TextView) findViewById(R.id.nav_fname);
        TextView mIDTextView = (TextView) findViewById(R.id.nav_id);
        mFNameTextView.setText(id);
        mIDTextView.setText(fname);*/




        ImageButton man = (ImageButton) findViewById(R.id.btn_men);
        man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, VegetableTypeSelectActivity.class);
                //Intent intent = new Intent(MainActivity.this,VegetableCatalogActivity.class);
                intent.putExtra("CustomerId",id);
                intent.putExtra("FirstName",fname);
                startActivity(intent);
            }
        });

        ImageButton women = (ImageButton) findViewById(R.id.btn_women);
        women.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FruitTypeSelectActivity.class);
                intent.putExtra("CustomerId",id);
                intent.putExtra("FirstName",fname);
                startActivity(intent);
            }
        });

        ImageButton kid = (ImageButton) findViewById(R.id.btn_kid);
        kid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OtherTypeSelectActivity.class);
                intent.putExtra("CustomerId",id);
                intent.putExtra("FirstName",fname);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_women) {
            Intent i = new Intent(MainActivity.this, FruitTypeSelectActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_men) {
            Intent i = new Intent(MainActivity.this, VegetableTypeSelectActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_kid) {
            Intent i = new Intent(MainActivity.this, OtherTypeSelectActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_cart) {
            Intent i = new Intent(MainActivity.this,CartDisplayActivity.class);
            startActivity(i);

        }
        else if (id == R.id.nav_login) {
            Intent i = new Intent(MainActivity.this, UserDetails.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
