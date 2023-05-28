package com.example.trucksharingtask;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class newOrder extends AppCompatActivity {
    EditText receiver, time;
    TextView location,DropOff;
    CalendarView calendarView;
    Button nextBTN;
    Long date;
    String Receiver, Time, Location,dropLocation;
    private static final String API_KEY = "AIzaSyB65LpbHTkKKJ4EE7gV--oCVD4ryOaYjrI";
    private PlacesClient placesClient;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
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
        DropOff = findViewById(R.id.DropOffLocation);
        calendarView = findViewById(R.id.orderCalendarView);
        Places.initialize(getApplicationContext(), API_KEY);
        placesClient = Places.createClient(this);
        initiatePlacesCode();
        initiateDropOffPlacesCode();
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

            if (DropOff.getText().toString().equals("")) {
                Toast.makeText(this, "Enter Drop Off location", Toast.LENGTH_SHORT).show();
                return;
            }
                Receiver = receiver.getText().toString();
                Time = time.getText().toString();
                Location = location.getText().toString();
                dropLocation = DropOff.getText().toString();
            Toast.makeText(this, Location, Toast.LENGTH_SHORT).show();
                Intent detailIntent = new Intent(newOrder.this, newOrderDetails.class);
                detailIntent.putExtra("receiver", Receiver);
                detailIntent.putExtra("sender", sender);
                detailIntent.putExtra("time", Time);
                detailIntent.putExtra("location", Location);
                detailIntent.putExtra("dropOff",dropLocation);
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

    public void initiatePlacesCode() {
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setTypeFilter(TypeFilter.ESTABLISHMENT);
        autocompleteFragment.setCountries("AU");
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ADDRESS, Place.Field.NAME));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener( ) {
            @Override
            public void onPlaceSelected (Place place) {
                location.setText(place.getAddress());
            };
            @Override
            public void onError (Status status) {
                // TODO: Handle the error
                Log. i (TAG, "An error occurred: " + status) ;
            }
        });
    }

    public void initiateDropOffPlacesCode() {
        AutocompleteSupportFragment autocompleteFragment1 = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment2);
        autocompleteFragment1.setTypeFilter(TypeFilter.ESTABLISHMENT);
        autocompleteFragment1.setCountries("AU");
        autocompleteFragment1.setPlaceFields(Arrays.asList(Place.Field.ADDRESS, Place.Field.NAME));

        autocompleteFragment1.setOnPlaceSelectedListener(new PlaceSelectionListener( ) {
            @Override
            public void onPlaceSelected (Place place) {
                DropOff.setText(place.getAddress());
            };
            @Override
            public void onError (Status status) {
                // TODO: Handle the error
                Log. i (TAG, "An error occurred: " + status) ;
            }
        });
    }
}