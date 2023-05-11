package com.example.trucksharingtask;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class newOrder extends AppCompatActivity {
    EditText receiver, time, location;
    CalendarView calendarView;
    Button nextBTN;
    Long date;
    String Receiver, Time, Location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        Intent intentReceive = getIntent();
        String sender = intentReceive.getStringExtra("sender");

        nextBTN = findViewById(R.id.nextBTN);
        receiver = findViewById(R.id.orderReceiver);
        time = findViewById(R.id.orderTime);
        location = findViewById(R.id.orderLocation);
        calendarView = findViewById(R.id.orderCalendarView);


        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String curYear = String.valueOf(year);
            String curMonth = String.valueOf(month+1);
            String curDate = String.valueOf(dayOfMonth);
            String dateString = curDate + "/" + curMonth + "/" + curYear;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyy", Locale.ENGLISH);
            try {
                Date date1 = simpleDateFormat.parse(dateString);
                assert date1 != null;
                date = date1.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        nextBTN.setOnClickListener(view -> {
            if (receiver.getText().toString().equals("")) {
                Toast.makeText(this, "Enter receiver name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (time.getText().toString().equals("")) {
                Toast.makeText(this, "Enter pickup time", Toast.LENGTH_SHORT).show();
                return;
            }
            if (location.getText().toString().equals("")) {
                Toast.makeText(this, "Enter pickup location", Toast.LENGTH_SHORT).show();
                return;
            }
                Receiver = receiver.getText().toString();
                Time = time.getText().toString();
                Location = location.getText().toString();
                Intent detailIntent = new Intent(newOrder.this, newOrderDetails.class);
                detailIntent.putExtra("receiver", Receiver);
                detailIntent.putExtra("sender", sender);
                detailIntent.putExtra("time", Time);
                detailIntent.putExtra("location", Location);
                detailIntent.putExtra("date", date);
                startActivityForResult(detailIntent, 10);
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {
            if (requestCode==10) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}