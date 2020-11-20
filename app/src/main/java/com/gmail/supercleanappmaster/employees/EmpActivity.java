package com.gmail.supercleanappmaster.employees;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.supercleanappmaster.MainActivity;
import com.gmail.supercleanappmaster.ProfileActivity;
import com.gmail.supercleanappmaster.R;
import com.gmail.supercleanappmaster.registerlogin.LoginActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EmpActivity extends AppCompatActivity {

    // toolbar
    Toolbar nToolbar;

    private Button nLogOut;
    private Button nProfile;
    private Button nCheckAssignments;

    // Firebase
    private FirebaseAuth mAuth;

    // Firestore
    private FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp);
        nToolbar =findViewById(R.id.toolbar);
        setSupportActionBar(nToolbar);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        // Firestore
        firestore = FirebaseFirestore.getInstance();

        nLogOut = findViewById(R.id.signOut);
        nLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent loginIntent = new Intent(EmpActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });
        nCheckAssignments = findViewById(R.id.checkAssignments);
        nCheckAssignments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(EmpActivity.this, EmpViewAssignmentsActivity.class);
                startActivity(loginIntent);
            }
        });

        nProfile = findViewById(R.id.profileBtn);
        nProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(EmpActivity.this, ProfileActivity.class);
                startActivity(loginIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        firestore.collection("employees").document(mAuth.getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name = documentSnapshot.getString("emp_name");
                Toast.makeText(EmpActivity.this, "Hello "+name+"!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}