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
            lengthTV, goodTypeTV, dropOff;

    Button callDriver,estimateBtm;
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
        estimateBtm = findViewById(R.id.getEstBtn);
        dropOff = findViewById(R.id.dropLocationTV);

        Intent intentReceive = getIntent();
        String sender = "Sender: " + intentReceive.getStringExtra("sender");
        String receiver = "Receiver: " + intentReceive.getStringExtra("receiver");
        Long date = intentReceive.getLongExtra("date", 0);
        String time = "Pickup time: " + intentReceive.getStringExtra("time");
        String location = "Pickup Location: " + intentReceive.getStringExtra("location");
        String dropL = "Drop Off Location: " + intentReceive.getStringExtra("dropOff");
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
        dropOff.setText(dropL);

        callDriver.setOnClickListener(view -> {
            String phoneNumber = "1234567890";
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
            startActivity(intent);
        });

        estimateBtm.setOnClickListener(view -> {
            Intent estimateActivity = new Intent(OrderDetails.this,EstimateActivity.class);
            estimateActivity.putExtra("pick_up",intentReceive.getStringExtra("location"));
            estimateActivity.putExtra("drop_off",intentReceive.getStringExtra("dropOff"));
            startActivity(estimateActivity);
        });
    }
}