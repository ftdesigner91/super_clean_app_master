package com.gmail.supercleanappmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.transition.Transition;
import de.hdodenhof.circleimageview.CircleImageView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.supercleanappmaster.registerlogin.LoginActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    // parent layout
    private ConstraintLayout nProfile_parent_layout;

    // - - - COMPONENTS - - -
    // edit texts
    private RelativeLayout nText_input_holder;
    private TextInputEditText name;
    private TextInputEditText email;
    private TextInputEditText phoneNumber;
    private TextInputEditText address;
    private TextView getLocation;
    private TextView nProfileTitle;
    private Button nUpdateBtn;
    // views
    private CircleImageView nCircleImageView;
    private TextView nUsernameView;
    private TextView nUserAddressView;
    private TextView nUserPhoneView;
    private TextView nUserEmailView;
    // layouts
    private ConstraintLayout expandableLayout;
    private ConstraintLayout nUser_info_layout;
    private CardView nCardView;
    // progress bar
    private ProgressBar nProgressBar;
    // user level
    private boolean employee;
    private String userLvel;
    // current location getter
    private FusedLocationProviderClient fusedLocationProviderClient;
    // firestore
    private FirebaseFirestore firebaseFirestore;

    // firebase user
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // collapse keyboard on screen touched
//        collapseKeyboard();

        // components - edit texts
        name = findViewById(R.id.username);
        email = findViewById(R.id.userEmail);
        phoneNumber = findViewById(R.id.userPhoneNumber);
        address = findViewById(R.id.userAddress);
        nProfileTitle = findViewById(R.id.profileTitle);
        // components - buttons
        getLocation = findViewById(R.id.get_address_btn);
        nUpdateBtn = findViewById(R.id.updateBtn);
        // components - views
        nCircleImageView = findViewById(R.id.circleImageView);
        nUsernameView = findViewById(R.id.usernameView);
        nUserEmailView = findViewById(R.id.userEmailView);
        nUserPhoneView = findViewById(R.id.userPhoneView);
        nUserAddressView = findViewById(R.id.userAddressView);
        // components - layouts
        expandableLayout = findViewById(R.id.user_update_info_layout);
        nUser_info_layout = findViewById(R.id.user_info_layout);
        nCardView = findViewById(R.id.cardView);
        // component - progressbar
        nProgressBar = findViewById(R.id.progressBar3);
        // parent layout
        nProfile_parent_layout = findViewById(R.id.profile_parent_layout);
        // current location getter - initialization
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // firbase user
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        userLvel = mUser.getEmail();
        employee = userLvel.contains("@emp.com");
        uid = mUser.getUid();

        // Firestore
        firebaseFirestore = FirebaseFirestore.getInstance();

        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ActivityCompat.checkSelfPermission(ProfileActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    getCurrentLocation();


                }else {

                    ActivityCompat.requestPermissions(ProfileActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                }

            }
        });
        nUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (expandableLayout.getVisibility() == View.GONE) {
                    openCardForUpdatingInformation();
                    nProfileTitle.setVisibility(View.GONE);
                } else {
                    collapseKeyboard();
                    clientInputChecker();
                }
            }
        });


    }

    private void collapseKeyboard() {
        // collapse keyboard on screen touched
        nProfile_parent_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                UIUtil.hideKeyboard(ProfileActivity.this);
                return false;
            }
        });
        nUpdateBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                UIUtil.hideKeyboard(ProfileActivity.this);
                return false;
            }
        });
    }

    private void clientInputChecker() {

        if (!TextUtils.isEmpty(name.getText()) || !TextUtils.isEmpty(email.getText())
                || !TextUtils.isEmpty(address.getText()) || !TextUtils.isEmpty(phoneNumber.getText())) {

            firebaseFirestore.collection("clients").document(uid).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String cName = String.valueOf(name.getText());
                            String cEmail = String.valueOf(email.getText());
                            String cPhone = String.valueOf(phoneNumber.getText());
                            String cAddress = String.valueOf(address.getText());
                            if (!TextUtils.isEmpty(cName) && !cName.equals(documentSnapshot.get("client_name"))) {
                                addOrUpdateClientDataInFirestore("client_name"
                                        , cName, "name");

                            }
                            if (!TextUtils.isEmpty(cEmail) && !cEmail.equals(documentSnapshot.get("client_email"))) {
                                addOrUpdateClientDataInFirestore("client_email"
                                        , cEmail, "email");


                            }
                            if (!TextUtils.isEmpty(cPhone) && !cPhone.equals(documentSnapshot.get("client_phone"))) {
                                addOrUpdateClientDataInFirestore("client_phone"
                                        , cPhone, "phone number");


                            }
                            if (!TextUtils.isEmpty(cAddress) && !cAddress.equals(documentSnapshot.get("client_address"))) {
                                addOrUpdateClientDataInFirestore("client_address"
                                        , cAddress, "address");


                            }
                        }
                    });
        } else {
            //Toast.makeText(ProfileActivity.this, "Fields are empty!", Toast.LENGTH_SHORT).show();
        }


    }

    private void setUserHintAndTextView(final TextInputEditText hint, final String hintValue,
                                        final TextView userInfoView, final String infoView) {

        firebaseFirestore.collection("clients").document(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        hint.setHint(hintValue);

                        userInfoView.setHint(infoView);

                    }
                });


    }

    private void addOrUpdateClientDataInFirestore(String key, String value, final String msg) {

        nProgressBar.setVisibility(View.VISIBLE);

        Map<String, Object> clientMap = new HashMap<>();
        clientMap.put(key, value);

        firebaseFirestore.collection("clients").document(uid)
                .update(clientMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                nProgressBar.setVisibility(View.GONE);

                firebaseFirestore.collection("clients").document(uid).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                setUserHintAndTextView(name, String.valueOf(documentSnapshot.get("client_name"))
                                        , nUsernameView, String.valueOf(documentSnapshot.get("client_name")));
                                name.getText().clear();
                                setUserHintAndTextView(email, String.valueOf(documentSnapshot.get("client_email"))
                                        , nUserEmailView, String.valueOf(documentSnapshot.get("client_email")));
                                email.getText().clear();
                                setUserHintAndTextView(phoneNumber, String.valueOf(documentSnapshot.get("client_phone"))
                                        , nUserPhoneView, String.valueOf(documentSnapshot.get("client_phone")));
                                phoneNumber.getText().clear();
                                setUserHintAndTextView(address, String.valueOf(documentSnapshot.get("client_address"))
                                        , nUserAddressView, String.valueOf(documentSnapshot.get("client_address")));
                                address.getText().clear();
                            }
                        });

                cardClosedAndUpdated();
                nProfileTitle.setVisibility(View.VISIBLE);
                Toast.makeText(ProfileActivity.this, "Your " + msg + " updated successfully", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                nProgressBar.setVisibility(View.GONE);
                Toast.makeText(ProfileActivity.this, "something went wrong! please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cardClosedAndUpdated() {
        nUpdateBtn.setText("edit");
        nUpdateBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.profile_edit_btn));
        TransitionManager.beginDelayedTransition(nCardView, new AutoTransition());
        //TransitionManager.beginDelayedTransition(expandableLayout);
        expandableLayout.setVisibility(View.GONE);

        // counter background style -
        nUser_info_layout.setBackgroundColor(getColor(R.color.colorPrimary));
        nUsernameView.setTextColor(getColor(R.color.common_google_signin_btn_text_light_default));
        nUserEmailView.setTextColor(getColor(R.color.common_google_signin_btn_text_light_default));
        nUserPhoneView.setTextColor(getColor(R.color.common_google_signin_btn_text_light_default));
        nUserAddressView.setTextColor(getColor(R.color.common_google_signin_btn_text_light_default));
    }

    private void openCardForUpdatingInformation() {

        nUpdateBtn.setText("update");
        nUpdateBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.profile_update_btn));
        TransitionManager.beginDelayedTransition(nCardView, new AutoTransition());
        TransitionManager.beginDelayedTransition(expandableLayout, new AutoTransition());
        expandableLayout.setVisibility(View.VISIBLE);
        // counter background style -
        //nUser_info_layout.setBackgroundColor(getColor(R.color.colorAccent));
        TransitionManager.beginDelayedTransition(nUser_info_layout, new AutoTransition());
        nUsernameView.setTextColor(getColor(R.color.colorAccent));
        nUserEmailView.setTextColor(getColor(R.color.colorAccent));
        nUserPhoneView.setTextColor(getColor(R.color.colorAccent));
        nUserAddressView.setTextColor(getColor(R.color.colorAccent));
    }

    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                        Geocoder geocoder = new Geocoder(ProfileActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1);

                        String addressLine =addresses.get(0).getAddressLine(0);
                        addOrUpdateClientDataInFirestore("client_address", addressLine, "address");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mUser != null)
        {

            firebaseFirestore.collection("clients").document(uid).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            setUserHintAndTextView(name,String.valueOf(documentSnapshot.get("client_name"))
                                    ,nUsernameView,String.valueOf(documentSnapshot.get("client_name")));

                            setUserHintAndTextView(email,String.valueOf(documentSnapshot.get("client_email"))
                                    ,nUserEmailView,String.valueOf(documentSnapshot.get("client_email")));

                            setUserHintAndTextView(address,String.valueOf(documentSnapshot.get("client_address"))
                                    ,nUserAddressView,String.valueOf(documentSnapshot.get("client_address")));

                            setUserHintAndTextView(phoneNumber,String.valueOf(documentSnapshot.get("client_phone"))
                                    ,nUserPhoneView,String.valueOf(documentSnapshot.get("client_phone")));
                            /*
                            // client profile
                            nUsernameView.setText(String.valueOf(documentSnapshot.get("client_name")));
                            nUserEmailView.setText(String.valueOf(documentSnapshot.get("client_email")));
                            nUserPhoneView.setText(String.valueOf(documentSnapshot.get("client_phone")));
                            nUserAddressView.setText(String.valueOf(documentSnapshot.get("client_address")));

                            name.setHint(String.valueOf(documentSnapshot.get("client_name")));
                            email.setHint(String.valueOf(documentSnapshot.get("client_email")));
                            phoneNumber.setHint(String.valueOf(documentSnapshot.get("client_phone")));
                            address.setHint(String.valueOf(documentSnapshot.get("client_address")));
                            */
                        }
                    });

        }
    }
}