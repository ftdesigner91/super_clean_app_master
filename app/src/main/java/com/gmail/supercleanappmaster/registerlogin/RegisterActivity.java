package com.gmail.supercleanappmaster.registerlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.supercleanappmaster.Admin.RegistarAdminsEmployeesActivity;
import com.gmail.supercleanappmaster.MainActivity;
import com.gmail.supercleanappmaster.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    // COMPONENTS
    private TextInputEditText nNameInpt;
    private TextInputEditText nEmailInpt;
    private TextInputEditText nPasswordInpt;
    private Button nSignUpBtn;
    private TextView nToLoginActivity;
    private ProgressBar nProgressBar;
    private TextView nNotification;
    private ConstraintLayout nUser_regester_parent_layout;

    // delayer
    private Handler handler;

    // FIREBASE
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    // Firestore
    private FirebaseFirestore firebaseFirestore;

    // user information
    private String email;
    private String password;
    private String name;

    // users levels
    private boolean employee;
    private boolean admin;
    // Domains
    private String adminDomain;
    private String employeeDomain;
    // Domain level value setter
    private String domainLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // INITIALIZATIONS - components
        nNameInpt = findViewById(R.id.nameInpt);
        nEmailInpt = findViewById(R.id.emailInpt);
        nPasswordInpt = findViewById(R.id.passwordInpt);
        nSignUpBtn = findViewById(R.id.loginBtn);
        nToLoginActivity = findViewById(R.id.toRegisterActivity);
        nProgressBar = findViewById(R.id.progressBar1);
        nNotification = findViewById(R.id.notification);
        nUser_regester_parent_layout = findViewById(R.id.user_regester_parent_layout);
        handler = new Handler();

        // INITIALIZATIONS - firebase
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        // INITIALIZATIONS - firestore
        firebaseFirestore = FirebaseFirestore.getInstance();

        // INITIALIZATION - domains
        adminDomain = "@admin.com";
        employeeDomain = "@emp.com";

        // progress bar is invisible
        nProgressBar.setVisibility(View.GONE);

        // Hide keyboard when screen touched
        nUser_regester_parent_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                UIUtil.hideKeyboard(RegisterActivity.this);
                return false;
            }
        });

        // user clicks if has an account
        nToLoginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeUsertoLoginActivity();
}
        });

        // Sign up button
        nSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UIUtil.hideKeyboard(RegisterActivity.this);

                // user information
                email = String.valueOf(nEmailInpt.getText());
                password = String.valueOf(nPasswordInpt.getText());
                name = String.valueOf(nNameInpt.getText());

                // User level
                domainLevel = String.valueOf(nEmailInpt.getText());

                // user domain
                admin = domainLevel.contains(adminDomain);
                employee = domainLevel.contains(employeeDomain);

                checkUserRegistrationInputsThenPerformAccordingly(name, email, password,
                        domainLevel, admin, employee);

            }
        });
    }

    private void takeUsertoLoginActivity() {

        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }

    private void checkUserRegistrationInputsThenPerformAccordingly(String name, String email, String password,
                                                       String domainLevel, boolean admin, boolean employee) {

        // (&&) both condition should be true
        // (||) one condition should be true
        if (TextUtils.isEmpty(name)
                && TextUtils.isEmpty(email)
                && TextUtils.isEmpty(password))
        {nNotification.setText("Empty fields!"); setTimedVisbilityForWarnning(nNotification);}

        else if (TextUtils.isEmpty(name)
                && TextUtils.isEmpty(email))
        {nNotification.setText("Name and email are missing!"); setTimedVisbilityForWarnning(nNotification);}

        else if (TextUtils.isEmpty(name)
                && TextUtils.isEmpty(password))
        {nNotification.setText("Name and password are missing!"); setTimedVisbilityForWarnning(nNotification);}

        else if (TextUtils.isEmpty(email)
                && TextUtils.isEmpty(password))
        {nNotification.setText("Email and password are missing!"); setTimedVisbilityForWarnning(nNotification);}

        else if (TextUtils.isEmpty(email))
        {nNotification.setText("Email field is empty!"); setTimedVisbilityForWarnning(nNotification);}
        else if (TextUtils.isEmpty(password))
        {nNotification.setText("Password field is empty!"); setTimedVisbilityForWarnning(nNotification);}
        else if (TextUtils.isEmpty(name))
        {nNotification.setText("Name field is empty!"); setTimedVisbilityForWarnning(nNotification);}

        else if (admin || employee){nNotification.setText("Restricted domain!!!"); setTimedVisbilityForWarnning(nNotification);}

        else if (!TextUtils.isEmpty(name)
                && !TextUtils.isEmpty(email)
                && !TextUtils.isEmpty(password))
        {registerUser();}

    }

    private void registerUser() {

        nProgressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                    }
                }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                addUserInfoToFirestore(name, email, password);
                nProgressBar.setVisibility(View.GONE);
                updateUI();
                Toast.makeText(RegisterActivity.this, "Hello "+name,
                        Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                nProgressBar.setVisibility(View.GONE);
                nNotification.setText("Something went wrong! please try again");
            }
        });
    }

    private void setTimedVisbilityForWarnning(final TextView nNotification) {
        nNotification.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 1 sec = millis 1000
                nNotification.setVisibility(View.GONE);
            }
        },5000);
    }

    private void addUserInfoToFirestore(String name, String email, String password) {

        // user ID
        String uid = mAuth.getUid();

        Map<String, Object> clientMap = new HashMap<>();
        clientMap.put("client_name", name);
        clientMap.put("client_email", email);
        clientMap.put("client_password", password);
        clientMap.put("client_id", uid);
        clientMap.put("client_image", "not_set");
        clientMap.put("client_address", "not_set");
        clientMap.put("client_phone", "not_set");

        firebaseFirestore.collection("clients").document(uid)
                .set(clientMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void updateUI() {

            Intent mainActivity = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(mainActivity);
            finish();

    }
}