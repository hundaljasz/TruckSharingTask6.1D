package com.example.trucksharingtask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class OrderDetails extends AppCompatActivity {
    TextView senderTV, receiverTV, dateTV, timeTV,
            locationTV, weightTV, widthTV, heightTV,
            lengthTV, goodTypeTV;

    Button callDriver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        senderTV = findViewById(R.id.SenderTV);
        receiverTV = findViewById(R.id.ReceiverTV);
        dateTV = findViewById(R.id.DateTV);
        timeTV = findViewById(R.id.TimeTV);
        locationTV = findViewById(R.id.LocationTV);
        weightTV = findViewById(R.id.WeightTV);
        widthTV = findViewById(R.id.WidthTV);
        heightTV = findViewById(R.id.HeightTV);
        lengthTV = findViewById(R.id.lengthTV);
        goodTypeTV = findViewById(R.id.GoodTypeTV);
        callDriver = findViewById(R.id.callDriver);

        Intent intentReceive = getIntent();
        String sender = "Sender: " + intentReceive.getStringExtra("sender");
        String receiver = "Receiver: " + intentReceive.getStringExtra("receiver");
        Long date = intentReceive.getLongExtra("date", 0);
        String time = "Pickup time: " + intentReceive.getStringExtra("time");
        String location = "Pickup location: " + intentReceive.getStringExtra("location");
        String goodType = "Good type: \n" + intentReceive.getStringExtra("goodType");
        int weight = intentReceive.getIntExtra("weight", 0);
        int width = intentReceive.getIntExtra("width", 0);
        int height = intentReceive.getIntExtra("height", 0);
        int length = intentReceive.getIntExtra("length", 0);
        String weightString = "Weight: \n" + Integer.toString(weight);
        String widthString = "Width: \n" + Integer.toString(width);
        String heightString = "Height: \n" + Integer.toString(height);
        String lengthString = "Length: \n" + Integer.toString(length);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String dateString = "Pickup date: " + simpleDateFormat.format(date).toString();

        senderTV.setText(sender);
        receiverTV.setText(receiver);
        dateTV.setText(dateString);
        timeTV.setText(time);
        locationTV.setText(location);
        weightTV.setText(weightString);
        widthTV.setText(widthString);
        heightTV.setText(heightString);
        lengthTV.setText(lengthString);
        goodTypeTV.setText(goodType);

        callDriver.setOnClickListener(view -> {
            String phoneNumber = "1234567890";
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
            startActivity(intent);
        });
    }
}