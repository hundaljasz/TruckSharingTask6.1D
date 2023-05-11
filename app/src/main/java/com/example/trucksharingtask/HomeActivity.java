package com.example.trucksharingtask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    public String user;
    public int UID;
    Intent intentReceive;
    RecyclerView orderRecyclerView;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    ImageView addOrderBTN;
    orderAdaptor orderAdaptor;
    List<orderModel> orderList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        DB db = new DB(this);
        orderList = db.getOrders();
        addOrderBTN = findViewById(R.id.addOrder);

        addOrderBTN.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, newOrder.class);
            intent.putExtra("sender", user);
            startActivityForResult(intent, 100);
        });

        intentReceive = getIntent();
        user = intentReceive.getStringExtra("user");
        UID  = intentReceive.getIntExtra("UID",0);

        orderRecyclerView = findViewById(R.id.order_recycler);

        orderAdaptor = new orderAdaptor( HomeActivity.this, orderList);
        orderRecyclerView.setAdapter(orderAdaptor);
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        orderRecyclerView.setLayoutManager(recyclerViewLayoutManager);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {
            if (requestCode==100) {
                DB db = new DB(this);
                orderList = db.getOrders();
                orderAdaptor = new orderAdaptor(HomeActivity.this, orderList);
                orderRecyclerView.setAdapter(orderAdaptor);
                recyclerViewLayoutManager = new LinearLayoutManager(this);
                orderRecyclerView.setLayoutManager(recyclerViewLayoutManager);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_my_location:
                Intent intent = new Intent(HomeActivity.this, Maps.class);
                intent.putExtra("UID", UID);
                startActivity(intent);
                finish();
                return true;
            case R.id.nav_home:
                Intent intentHome = new Intent(HomeActivity.this, HomeActivity.class);
                intentHome.putExtra("UID", UID);
                startActivity(intentHome);
                finish();
                return true;
            case R.id.nav_account:
                Intent intentAccount = new Intent(HomeActivity.this, accountActivity.class);
                intentAccount.putExtra("UID", UID);
                startActivity(intentAccount);
                finish();
                return true;
            case R.id.nav_my_orders:
                Intent intentorders = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(intentorders);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}