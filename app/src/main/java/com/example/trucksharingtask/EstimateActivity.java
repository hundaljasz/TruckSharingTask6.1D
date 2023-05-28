package com.example.trucksharingtask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.firestore.GeoPoint;
import com.google.maps.android.SphericalUtil;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class EstimateActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    Double distance;
    Intent intentReceive;
    TextView estDistance, estPrice;
    Button GpayBTN;
    String amount;
    public static final String clientKey = "ATdL4CGVBsyTQWggL11c2niYBDxYuQ8iFr2zgRFd-CbkYnoxXnNCqAGUuR57pBP0Rmkzkhc_nLn-xpXl";
    public static final int PAYPAL_REQUEST_CODE = 123;
    // Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(clientKey);

    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;

    private float GEOFENCE_RADIUS = 200;
    private String GEOFENCE_ID = "SOME_GEOFENCE_ID";

    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimate);
        intentReceive = getIntent();
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        estDistance = findViewById(R.id.estDistance);
        estPrice = findViewById(R.id.estPrice);
        GpayBTN = findViewById(R.id.payNow);

        GpayBTN.setOnClickListener(view -> {
            getPayment();
        });
    }


    private void getPayment() {

        // Creating a paypal payment on below line.
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "USD", "Course Fees",
                PayPalPayment.PAYMENT_INTENT_SALE);

        // Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        // Putting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        // Starting the intent activity for result
        // the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If the result is from paypal
        if (requestCode == PAYPAL_REQUEST_CODE) {

            // If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {

                // Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                // if confirmation is not null
                if (confirm != null) {
                    try {
                        // Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        // on below line we are extracting json response and displaying it in a text view.
                        JSONObject payObj = new JSONObject(paymentDetails);
                        String payID = payObj.getJSONObject("response").getString("id");
                        String state = payObj.getJSONObject("response").getString("state");
                        Toast.makeText(this, "Payment " + state + "\n with payment id is " + payID, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        // handling json exception on below line
                        Log.e("Error", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // on below line we are checking the payment status.
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                // on below line when the invalid paypal config is submitted.
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap = googleMap;
        PolylineOptions polylineOptions = new PolylineOptions()
                .add(getLocationFromAddress(intentReceive.getStringExtra("pick_up")),
                        getLocationFromAddress(intentReceive.getStringExtra("drop_off")))  // 'coordinates' should be a List<LatLng> containing your route coordinates
                .width(5)  // Customize the width of the route line
                .color(Color.RED);  // Customize the color of the route line
        googleMap.addPolyline(polylineOptions);
        googleMap.addMarker(new MarkerOptions().position(getLocationFromAddress(intentReceive.getStringExtra("pick_up"))).title("Pick Up"));
        googleMap.addMarker(new MarkerOptions().position(getLocationFromAddress(intentReceive.getStringExtra("drop_off"))).title("Drop Off"));
        // below line is use to zoom our camera on map.
//        googleMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));

        // below line is use to move our camera to the specific location.
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(getLocationFromAddress(intentReceive.getStringExtra("pick_up"))));

        distance = SphericalUtil.computeDistanceBetween(getLocationFromAddress(intentReceive.getStringExtra("pick_up")), getLocationFromAddress(intentReceive.getStringExtra("drop_off")));
        estDistance.setText("Estimate Distance: "+String.format("%.2f", distance / 1000)+" KM");
        estPrice.setText("Estimate Price: "+(String.format("%.2f", distance / 1000 + 25))+ " $");
        amount = String.format("%.2f", distance / 1000 + 25);
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public LatLng getLocationFromAddress(String strAddress) {
        try {
            Geocoder coder = new Geocoder(this);
            List<Address> address;
            GeoPoint p1 = null;

            try {
                address = coder.getFromLocationName(strAddress, 5);
                if (address == null) {
                    return null;
                }
                Address location = address.get(0);
                return new LatLng(location.getLatitude(), location.getLongitude());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }catch (Exception e){
            Log.d("reached glfa",e.getMessage());
            return null;
        }
    }

}