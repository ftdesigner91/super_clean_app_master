package com.gmail.supercleanappmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private Toolbar nToolbar;
    private ProgressBar nProgressBar4;
    private CardView nProgressBar5;
    private ConstraintLayout nCheckout_parent_layout;

    // gps
    private FusedLocationProviderClient fusedLocationProviderClient;

    private Bundle bundle;

    // firestore
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference getPrice;

    // firebase
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    // bedroom info
    private TextView nBed_room_counter_result;
    private TextView nBedroom_counter_tv;
    private ImageButton nRemove_btn;
    private ImageButton nAdd_btn;

    // bathroom info
    private TextView nBath_counter_result;
    private TextView nBathroom_counter_tv;
    private ImageButton nRemove_btn_bath;
    private ImageButton nAdd_btn_bath;

    // date info
    private TextView nDate_result;
    private Button nSet_date_btn;

    // price info
    private TextView nPrice_result;
    private Button nBooking_submission_btn;

    // get location
    private ImageButton nSet_location_btn;
    private TextView nCurrent_location;
    private CardView nLocation_card_view;
    private TextInputEditText nAddress_edit_text;
    private ImageButton nSet_address;
    private Button nGet_current_location_btn;

    // expandbles layouts
    private ConstraintLayout nAdd_location_layout;

    // heading
    private TextView nCheckout_heading_identifier;

    // get date and time
    private DateTimeFormatter dtf;
    private LocalDateTime currentTime;
    private SimpleDateFormat df;
    private Date date;
    private String formattedDate;

    // service type
    String house;
    String apartment;
    String condo;

    // counter
    private int bed_counter = 1;
    private int bath_counter = 1;

    String number= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        nToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(nToolbar);
        nProgressBar4 = findViewById(R.id.progressBar4);
        nProgressBar5 = findViewById(R.id.progressBar5);
        nCheckout_parent_layout = findViewById(R.id.checkout_parent_layout);
        //gps
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Firestore
        firebaseFirestore = FirebaseFirestore.getInstance();
        getPrice = firebaseFirestore.collection("service_pricing");

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();

        // components -- initialize
        // bedroom
        nBed_room_counter_result = findViewById(R.id.bed_room_counter_result);
        nBedroom_counter_tv = findViewById(R.id.bedroom_counter_tv);
        nRemove_btn = findViewById(R.id.remove_btn);
        nAdd_btn = findViewById(R.id.add_btn);
        // bathroom
        nBath_counter_result = findViewById(R.id.bath_counter_result);
        nBathroom_counter_tv = findViewById(R.id.bathroom_counter_tv);
        nRemove_btn_bath = findViewById(R.id.remove_btn_bath);
        nAdd_btn_bath = findViewById(R.id.add_btn_bath);
        // date
        nDate_result = findViewById(R.id.date_result);
        nSet_date_btn = findViewById(R.id.set_date_btn);
        // price info
        nPrice_result = findViewById(R.id.price_result);
        nBooking_submission_btn = findViewById(R.id.booking_submission_btn);
        // heading Textview
        nCheckout_heading_identifier = findViewById(R.id.checkout_heading_identifier);
        // address
        nCurrent_location = findViewById(R.id.current_location);
        nSet_location_btn = findViewById(R.id.set_location_btn);
        nAdd_location_layout = findViewById(R.id.add_location_layout);
        nLocation_card_view = findViewById(R.id.location_card_view);
        nAddress_edit_text = findViewById(R.id.address_edit_text);
        nSet_address = findViewById(R.id.set_address);
        nGet_current_location_btn = findViewById(R.id.get_current_location_btn);


        // Current date and time
        dtf = DateTimeFormatter.ofPattern("hhmmssddmmyy");
        currentTime = LocalDateTime.now();
        df = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
        date = Calendar.getInstance().getTime();
        formattedDate = df.format(date);

        // - - - BEDROOM COUNTER - - -
        nAdd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBedroomCounter();
            }
        });
        nRemove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeBedroomCounter();
            }
        });

        // - - - BATHROOM COUNTER - - -
        nAdd_btn_bath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBathroomCounter();
            }
        });
        nRemove_btn_bath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeBathroomCounter();
            }
        });

        nSet_date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        nBooking_submission_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nProgressBar5.setVisibility(View.VISIBLE);
                String uid = mUser.getUid();
                if (!String.valueOf(nCurrent_location.getText()).equals("Add your address") &&
                        !String.valueOf(nCurrent_location.getText()).equals("Date"))
                {getClientInfoFromFirestore(uid);}
                else
                {
                    nProgressBar5.setVisibility(View.GONE);
                    Toast.makeText(CheckoutActivity.this, "incomplete details!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        firebaseFirestore.collection("clients").document(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String address = String.valueOf(documentSnapshot.get("client_address"));
                        if (documentSnapshot.get("client_address") != null)
                        {
                            nCurrent_location.setText(address);
                        }
                        else
                        {
                            //nCurrent_location.setText("");
                        }
                    }
                });

        nSet_location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nAdd_location_layout.getVisibility() == View.GONE)
                { // open
                    TransitionManager.beginDelayedTransition(nLocation_card_view, new AutoTransition());
                    nAdd_location_layout.setVisibility(View.VISIBLE);
                }
                else
                { // close
                    TransitionManager.beginDelayedTransition(nLocation_card_view, new AutoTransition());
                    nAdd_location_layout.setVisibility(View.GONE);
                }
            }
        });

        nSet_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(nAddress_edit_text.getText()))
                {nCurrent_location.setText(String.valueOf(nAddress_edit_text.getText()));}
            }
        });

        nGet_current_location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(CheckoutActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    getCurrentLocation();
                }else
                {
                    ActivityCompat.requestPermissions(CheckoutActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                    getCurrentLocation();
                }
            }
        });

        bundle = getIntent().getExtras();
        house = bundle.getString("house");
        apartment = bundle.getString("apartment");
        condo = bundle.getString("condo");
        getStringFromHomeFragment(house, apartment, condo);

        collapseKeyboard();
    }

    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(CheckoutActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1);

                        String addressLine =addresses.get(0).getAddressLine(0);
                        nCurrent_location.setText(addressLine);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }

    private void getStringFromHomeFragment(String house, String apartment, String condo) {
        if (house != null)
        {nCheckout_heading_identifier.setText("Select details for your "+house+" cleaning service");}
        if (apartment != null)
        {nCheckout_heading_identifier.setText("Select details for your "+apartment+" cleaning service");}
        if (condo != null)
        {nCheckout_heading_identifier.setText("Select details for your "+condo+" cleaning service");}
    }

    private void removeBedroomCounter() {
        bed_counter--;
        if (bed_counter <= 4)
        {
            //counter = 1;
            switch (bed_counter)
            {
                case 1:
                    nBedroom_counter_tv.setText(String.valueOf(bed_counter));
                    nBed_room_counter_result.setText("Studio");
                    getBedroomPriceFromFirestore(1);
                    break;
                case 2:
                    nBedroom_counter_tv.setText(String.valueOf(bed_counter));
                    nBed_room_counter_result.setText(bed_counter+" Bedroom");
                    getBedroomPriceFromFirestore(2);
                    break;
                case 3:
                    nBedroom_counter_tv.setText(String.valueOf(bed_counter));
                    nBed_room_counter_result.setText(bed_counter+" Bedroom");
                    getBedroomPriceFromFirestore(3);
                    break;
                case 4:
                    nBedroom_counter_tv.setText(String.valueOf(bed_counter));
                    nBed_room_counter_result.setText(bed_counter+" Bedroom");
                    getBedroomPriceFromFirestore(4);
                    break;
            }
        }
        if (bed_counter < 1){bed_counter=1;}
    }
    private void addBedroomCounter() {
        bed_counter++;
        if (bed_counter <= 4)
        {
            switch (bed_counter)
            {
                case 2:
                    nBedroom_counter_tv.setText(String.valueOf(bed_counter));
                    nBed_room_counter_result.setText(bed_counter+" Bedroom");
                    getBedroomPriceFromFirestore(2);
                    break;
                case 3:
                    nBedroom_counter_tv.setText(String.valueOf(bed_counter));
                    nBed_room_counter_result.setText(bed_counter+" Bedroom");
                    getBedroomPriceFromFirestore(3);
                    break;
                case 4:
                    nBedroom_counter_tv.setText(String.valueOf(bed_counter));
                    nBed_room_counter_result.setText(bed_counter+" Bedroom");
                    getBedroomPriceFromFirestore(4);
                    break;
            }
        }
        else {bed_counter=4;}
    }

    private void removeBathroomCounter() {
        bath_counter--;
        if (bath_counter <= 4)
        {
            //counter = 1;
            switch (bath_counter)
            {
                case 1:
                    nBathroom_counter_tv.setText(String.valueOf(bath_counter));
                    nBath_counter_result.setText(bath_counter+" Bathroom");
                    getBathroomPriceFromFirestore(1);
                    break;
                case 2:
                    nBathroom_counter_tv.setText(String.valueOf(bath_counter));
                    nBath_counter_result.setText(bath_counter+" Bathroom");
                    getBathroomPriceFromFirestore(2);
                    break;
                case 3:
                    nBathroom_counter_tv.setText(String.valueOf(bath_counter));
                    nBath_counter_result.setText(bath_counter+" Bathroom");
                    getBathroomPriceFromFirestore(3);
                    break;
                case 4:
                    nBathroom_counter_tv.setText(String.valueOf(bath_counter));
                    nBath_counter_result.setText(bath_counter+" Bathroom");
                    getBathroomPriceFromFirestore(4);
                    break;
            }
        }
        if (bath_counter < 1){bath_counter=1;}
    }
    private void addBathroomCounter() {
        bath_counter++;
        if (bath_counter <= 4)
        {
            switch (bath_counter)
            {
                case 2:
                    nBathroom_counter_tv.setText(String.valueOf(bath_counter));
                    nBath_counter_result.setText(bath_counter+" Bathroom");
                    getBathroomPriceFromFirestore(2);
                    break;
                case 3:
                    nBathroom_counter_tv.setText(String.valueOf(bath_counter));
                    nBath_counter_result.setText(bath_counter+" Bathroom");
                    getBathroomPriceFromFirestore(3);
                    break;
                case 4:
                    nBathroom_counter_tv.setText(String.valueOf(bath_counter));
                    nBath_counter_result.setText(bath_counter+" Bathroom");
                    getBathroomPriceFromFirestore(4);
                    break;
            }
        }
        else {bath_counter=4;}
    }

    private void getBedroomPriceFromFirestore(final long Qty) {
        nProgressBar4.setVisibility(View.VISIBLE);
        getPrice.document("one_bedroom").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        long bathCheck = Long.parseLong(String.valueOf(nBathroom_counter_tv.getText()));
                        long oneBathPrice = 50;
                        long bathPrice = bathCheck * oneBathPrice;

                        String priceString = String.valueOf(documentSnapshot.get("price"));
                        long price = (Qty * Long.parseLong(priceString)) + bathPrice;
                        nProgressBar4.setVisibility(View.GONE);
                        nPrice_result.setText("PHP "+price+".00");

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CheckoutActivity.this, "Failed fetching price! please " +
                        "check your connections and try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getBathroomPriceFromFirestore(final long Qty) {
        nProgressBar4.setVisibility(View.VISIBLE);
        getPrice.document("one_bathroom").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        long bedCheck = Long.parseLong(String.valueOf(nBedroom_counter_tv.getText()));
                        long oneBedPrice = 100;
                        long bedPrice = bedCheck * oneBedPrice;

                        String priceString = String.valueOf(documentSnapshot.get("price"));
                        long price = (Qty* Long.parseLong(priceString)) + bedPrice;
                        nProgressBar4.setVisibility(View.GONE);
                        nPrice_result.setText("PHP "+price+".00");
                    }
                });
    }

    private void getClientInfoFromFirestore(final String uid) {

        // Get client info from his document on firebase
        firebaseFirestore.collection("clients").document(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String name = documentSnapshot.getString("client_name");
                        putClientSubmissionsInfoToNewDocument(uid, name);
                    }
                });
    }
    private String getStringFromHomeFragmentToFirestore() {

        bundle = getIntent().getExtras();
        house = bundle.getString("house");
        apartment = bundle.getString("apartment");
        condo = bundle.getString("condo");

        if (house != null)
        {return house;}
        else if (apartment != null)
        {return apartment;}
        else if (condo != null)
        {return condo;}
        else
        {return null;}
    }
    private void putClientSubmissionsInfoToNewDocument(final String uid, String name) {
        String email = mUser.getEmail();
        String client_input_address = String.valueOf(nCurrent_location.getText());
        String price = String.valueOf(nPrice_result.getText());
        String service_date = String.valueOf(nDate_result.getText());
        String bedroom_qty = String.valueOf(nBedroom_counter_tv.getText());
        String bathroom_qty = String.valueOf(nBathroom_counter_tv.getText());


        firebaseFirestore.collection("clients").document(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        number = String.valueOf(documentSnapshot.get("client_phone"));
                    }
                });

        Map<String, Object> submissionMap = new HashMap<>();
        submissionMap.put("sub_client_id", uid);
        submissionMap.put("sub_client_name", name);
        submissionMap.put("sub_client_email", email);
        submissionMap.put("sub_client_date", service_date);
        submissionMap.put("sub_client_address", client_input_address);
        submissionMap.put("sub_client_phone", number);
        submissionMap.put("sub_client_bedroom_count", bedroom_qty);
        submissionMap.put("sub_client_bathroom_count", bathroom_qty);
        submissionMap.put("sub_client_service_type", getStringFromHomeFragmentToFirestore());
        submissionMap.put("sub_client_price", price);

        // Add client info + booking info in "submissions collection"
        // structure/ C:submissions > D: generated ID > C:clientID
        firebaseFirestore.collection("submissions").document(formattedDate+uid).set(submissionMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        nProgressBar5.setVisibility(View.GONE);
                        Toast.makeText(CheckoutActivity.this,"booked successfully",Toast.LENGTH_LONG).show();
                        Intent mainIntent = new Intent(CheckoutActivity.this, MainActivity.class);
                        mainIntent.putExtra("path_to_client_realtime_db",formattedDate+uid);
                        mainIntent.putExtra("client_id",uid);
                        startActivity(mainIntent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        nProgressBar5.setVisibility(View.GONE);
                        Toast.makeText(CheckoutActivity.this,"booking failed! please check your connection"
                                ,Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void collapseKeyboard() {
        // collapse keyboard on screen touched
        nCheckout_parent_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                UIUtil.hideKeyboard(CheckoutActivity.this);
                return false;
            }
        });
        nSet_address.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                UIUtil.hideKeyboard(CheckoutActivity.this);
                return false;
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        String currentDateString = DateFormat.getDateInstance().format(c.getTime());


        nDate_result.setText(currentDateString);
    }

}