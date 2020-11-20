package com.gmail.supercleanappmaster.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gmail.supercleanappmaster.ViewAllEmpActivity;
import com.gmail.supercleanappmaster.registerlogin.LoginActivity;
import com.gmail.supercleanappmaster.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminActivity extends AppCompatActivity {


    private Button nLogOut;
    private Button nView_submissions_btn;
    private Button nRegBtn;
    private Button nAddServiceBtn;
    private Button nViewAllEmpBtn;

    // Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        nLogOut = findViewById(R.id.signOut);
        nView_submissions_btn = findViewById(R.id.profileBtn);
        nRegBtn = findViewById(R.id.checkAssignments);
        nViewAllEmpBtn = findViewById(R.id.viewAllEmpBtn);
        nAddServiceBtn = findViewById(R.id.addServiceBtn);

        // Firebase
        mAuth = FirebaseAuth.getInstance();

        nLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
                updateUI(null);
            }
        });

        nAddServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent subIntent = new Intent(AdminActivity.this, AdminAddServiceActivity.class);
                startActivity(subIntent);
            }
        });

        nView_submissions_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent subIntent = new Intent(AdminActivity.this, SubmissionsActivity.class);
                startActivity(subIntent);
            }
        });

        nRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regIntent = new Intent(AdminActivity.this, RegistarAdminsEmployeesActivity.class);
                startActivity(regIntent);
            }
        });

        nViewAllEmpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent viewAllEmpIntent = new Intent(AdminActivity.this, ViewAllEmpActivity.class);
                startActivity(viewAllEmpIntent);
            }
        });
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        updateUI(null);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser == null) {
            Intent loginIntent = new Intent(AdminActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }
}