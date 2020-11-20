package com.gmail.supercleanappmaster;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gmail.supercleanappmaster.chat.ChatActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class JobsFragment extends Fragment {

    private View view;
    private Button nChatBtn;
    private TextView nClient_notif;

    // firestore
    private FirebaseFirestore firebaseFirestore;

    // firebase user
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    // firebase database
    private FirebaseDatabase db;
    private DatabaseReference dbRef;

    // get date and time
    private SimpleDateFormat df;
    private Date date;

    public JobsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_jobs, container, false);

        nClient_notif = view.findViewById(R.id.client_notif);
        nChatBtn = view.findViewById(R.id.chatBtn);
        nClient_notif.setText("No jobs available for today");

        // firstore
        firebaseFirestore = FirebaseFirestore.getInstance();
        // firebase
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();

        // Date and time
        df = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
        date = Calendar.getInstance().getTime();
        final String formattedDate = df.format(date);

        // firebase database
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference().child("client_emp_messages");
        dbRef.child(formattedDate+uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists())
                        {
                            nChatBtn.setVisibility(View.VISIBLE);
                            nClient_notif.setText("our employee is trying to contact you");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        nChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                startActivity(chatIntent);
            }
        });

        return view;
    }
}