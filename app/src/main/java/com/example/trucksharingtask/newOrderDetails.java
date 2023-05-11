package com.example.trucksharingtask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;

public class newOrderDetails extends AppCompatActivity {
    RadioGroup goodTypeRG, vehicleTypeRG;
    RadioButton goodTypeRBTN, vehicleTypeRBTN;
    EditText weightET,heightET , widthET, otherGoodTypeET, otherVehicleTypeET,  lengthET;
    Button createOrder;
    String receiver, time, location, sender;
    Long date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order_details);

        goodTypeRG = findViewById(R.id.GoodTypeRG);
        vehicleTypeRG = findViewById(R.id.DetailVehicleTypeRG);
        otherGoodTypeET = findViewById(R.id.newDeliveryDetailOtherGoodEditText);
        otherVehicleTypeET = findViewById(R.id.OtherVehicleET);
        weightET = findViewById(R.id.Weight);
        widthET = findViewById(R.id.Width);
        lengthET = findViewById(R.id.Length);
        heightET = findViewById(R.id.Height);

        createOrder = findViewById(R.id.CreateOrderBTN);

        DB db = new DB(this);

        Intent intentReceive = getIntent();

        receiver = intentReceive.getStringExtra("receiver");
        sender = intentReceive.getStringExtra("sender");
        location = intentReceive.getStringExtra("location");
        time = intentReceive.getStringExtra("time");
        date = intentReceive.getLongExtra("date", Calendar.getInstance().getTime().getTime());

        createOrder.setOnClickListener(view -> {

            int goodTypeSelectedButtonId = goodTypeRG.getCheckedRadioButtonId();
            int vehicleTypeSelectedButtonId = vehicleTypeRG.getCheckedRadioButtonId();

            if (goodTypeSelectedButtonId == -1) {
                Toast.makeText(this, "Please select a good type", Toast.LENGTH_SHORT).show();
                return;
            }
            if (vehicleTypeSelectedButtonId == -1) {
                Toast.makeText(this, "Please select a vehicle type", Toast.LENGTH_SHORT).show();
                return;
            }
            if (weightET.getText().toString().equals("") || widthET.getText().toString().equals("") || lengthET.getText().toString().equals("") || heightET.getText().toString().equals("") ) {
                Toast.makeText(this, "Please enter all the required dimensions", Toast.LENGTH_SHORT).show();
                return;
            }
            goodTypeRBTN = findViewById(goodTypeSelectedButtonId);
            vehicleTypeRBTN = findViewById(vehicleTypeSelectedButtonId);

            int weight = Integer.parseInt(weightET.getText().toString());
            int width = Integer.parseInt(widthET.getText().toString());
            int length = Integer.parseInt(lengthET.getText().toString());
            int height = Integer.parseInt(heightET.getText().toString());

            // check for radio button selection, if other selected: get other text
            String goodType;
            if (goodTypeSelectedButtonId == R.id.OtherGoodRBTN) {
                goodType = otherGoodTypeET.getText().toString();
            } else {
                goodType = goodTypeRBTN.getText().toString();
            }
            String vehicleType;
            if (vehicleTypeSelectedButtonId == R.id.OtherVehicleRBTN) {
                vehicleType = otherVehicleTypeET.getText().toString();
            } else {
                vehicleType = vehicleTypeRBTN.getText().toString();
            }

            orderModel order = new orderModel(sender, receiver, goodType,vehicleType, time, location ,date, weight, length, width, height);
            long result = db.addOrder(order);
            if (result > -1) {
                Toast.makeText(this, "Order created Successfully!....", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, "Failed to make order, error: " + result, Toast.LENGTH_SHORT).show();
            }
        });
    }
}