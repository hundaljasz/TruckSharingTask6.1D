package com.example.trucksharingtask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.util.BitSet;

public class accountActivity extends AppCompatActivity {
    int UID;
    Intent intentReceive;

    TextView name,username,phone;
    ImageView image;
    BitSet byteStream;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        DB db = new DB(this);
        intentReceive = getIntent();
        UID = intentReceive.getIntExtra("UID",0);

        DB.MyResult result =  db.GUS(UID);

        Log.v("test",String.valueOf(result.name));

        name = findViewById(R.id.Name);
        username = findViewById(R.id.username);
        phone = findViewById(R.id.phone);
        image = findViewById(R.id.profile);

        name.setText(String.valueOf(result.name));
        phone.setText(String.valueOf(result.phone));
        username.setText(String.valueOf(result.username));
        if(result.image.length > 0) {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(result.image);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            image.setImageBitmap(bitmap);
        }
    }
    @Override
    public void onBackPressed() {
        finish();
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
                Intent intent = new Intent(accountActivity.this, Maps.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.nav_home:
                Intent intentHome = new Intent(accountActivity.this, HomeActivity.class);
                startActivity(intentHome);
                finish();
                return true;
            case R.id.nav_account:
                Intent intentAccount = new Intent(accountActivity.this, accountActivity.class);
                intentAccount.putExtra("UID", UID);
                startActivity(intentAccount);
                finish();
                return true;
            case R.id.nav_my_orders:
                Intent intentorders = new Intent(accountActivity.this, HomeActivity.class);
                startActivity(intentorders);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}