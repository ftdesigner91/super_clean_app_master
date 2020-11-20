package com.gmail.supercleanappmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BookingActivity extends AppCompatActivity {

    private Button nSubmitBtn;
    private ProgressBar nProgressBar;

    // Firestore
    FirebaseFirestore firebaseFirestore;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    // get date and time
    private DateTimeFormatter dtf;
    private LocalDateTime currentTime;
    private SimpleDateFormat df;
    private Date date;
    private String formattedDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // firestore initialization
        firebaseFirestore = FirebaseFirestore.getInstance();

        // Firebase auth
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        nSubmitBtn = findViewById(R.id.submitBtn);
        nProgressBar = findViewById(R.id.progressBar);
        nProgressBar.setVisibility(View.GONE);

        // Date and time
        dtf = DateTimeFormatter.ofPattern("hhmmssddmmyy");
        currentTime = LocalDateTime.now();
        df = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
        date = Calendar.getInstance().getTime();
         formattedDate = df.format(date);


        nSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nProgressBar.setVisibility(View.VISIBLE);
                String uid = mUser.getUid();
                getClientInfoFromFirestore(uid);
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
                });
    }

    private void putClientSubmissionsInfoToNewDocument(final String uid, String name) {
        String email = mUser.getEmail();

        Map<String, Object> submissionMap = new HashMap<>();
        submissionMap.put("sub_client_name", name);
        submissionMap.put("sub_client_email", email);
        submissionMap.put("sub_client_price", 100);
        submissionMap.put("sub_client_id", uid);

        // Add client info + booking info in "submissions collection"
        // structure/ C:submissions > D: generated ID > C:clientID
        firebaseFirestore.collection("submissions").add(submissionMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        nProgressBar.setVisibility(View.GONE);
                        Toast.makeText(BookingActivity.this,"booked successfully",Toast.LENGTH_LONG).show();
                        Intent mainIntent = new Intent(BookingActivity.this, MainActivity.class);
                        mainIntent.putExtra("path_to_client_realtime_db",formattedDate+uid);
                        mainIntent.putExtra("client_id",uid);
                        startActivity(mainIntent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        nProgressBar.setVisibility(View.GONE);
                        Toast.makeText(BookingActivity.this,"booking failed: "+e,Toast.LENGTH_LONG).show();
                    }
                });
    }
}