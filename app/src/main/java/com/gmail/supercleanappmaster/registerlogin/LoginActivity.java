package com.gmail.supercleanappmaster.registerlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.supercleanappmaster.Admin.AdminActivity;
import com.gmail.supercleanappmaster.MainActivity;
import com.gmail.supercleanappmaster.R;
import com.gmail.supercleanappmaster.employees.EmpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;


public class LoginActivity extends AppCompatActivity {

    //  COMPONENTS
    private ImageView nLogoImgView;
    private TextInputEditText nEmailInpt;
    private TextInputEditText nPasswordInpt;
    private TextView nToRegisterActivity;
    private Button nLoginBtn;
    private ProgressBar nProgressBar;
    private TextView nNotification;
    private ConstraintLayout nUSer_login_parent_layout;

    // delayer
    private Handler handler;

    // FIREBASE
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    // Firestore
    private FirebaseFirestore firebaseFirestore;

    // user information
    private String email;
    private String password;

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
        setContentView(R.layout.activity_login);

        // INITIALIZATIONS - components
        nLogoImgView = findViewById(R.id.logoImageView);
        nEmailInpt = findViewById(R.id.emailInpt);
        nPasswordInpt = findViewById(R.id.passwordInpt);
        nToRegisterActivity = findViewById(R.id.toRegisterActivity);
        nLoginBtn = findViewById(R.id.loginBtn);
        nProgressBar = findViewById(R.id.progressBar1);
        nNotification = findViewById(R.id.notification);
        nUSer_login_parent_layout = findViewById(R.id.user_login_parent_layout);
        handler = new Handler();

        // INITIALIZATIONS - firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // INITIALIZATIONS - firestore
        firebaseFirestore = FirebaseFirestore.getInstance();



        // INITIALIZATION - domains
        adminDomain = "@admin.com";
        employeeDomain = "@emp.com";

        // progress bar is invisible
        nProgressBar.setVisibility(View.GONE);

        // view logo from drawable
        //nLogoImgView.setImageResource(R.drawable.ic_launcher_foreground);

        nUSer_login_parent_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                UIUtil.hideKeyboard(LoginActivity.this);
                return false;
            }
        });

        // authenticate user then log in if exist
        nLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UIUtil.hideKeyboard(LoginActivity.this);

                // User information
                email = String.valueOf(nEmailInpt.getText());
                password = String.valueOf(nPasswordInpt.getText());
                // User level
                domainLevel = String.valueOf(nEmailInpt.getText());
                // User domain
                admin = domainLevel.contains(adminDomain);
                employee = domainLevel.contains(employeeDomain);

                checkUserLoginInputsThenPerformAccordingly(email, password);

            }
        });

        // partial styling using typeface and size
        stylizeCreateAnAcountText();

        // User click if has an account
        nToRegisterActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toRegisterActivity();
            }
        });


    }

    private void checkUserLoginInputsThenPerformAccordingly(String email, String password) {

        // (&&) both condition should be true
        // (||) one condition should be true
        if (TextUtils.isEmpty(email)
                && TextUtils.isEmpty(password))
        {nNotification.setText("Empty fields!"); setTimedVisbilityForWarnning(nNotification);}

        else if (TextUtils.isEmpty(email))
        {nNotification.setText("Enter your email!"); setTimedVisbilityForWarnning(nNotification);}

        else if (TextUtils.isEmpty(password))
        {nNotification.setText("Enter your password!"); setTimedVisbilityForWarnning(nNotification);}

        else if (TextUtils.isEmpty(email)
                && TextUtils.isEmpty(password))
        {nNotification.setText("Enter email and password!"); setTimedVisbilityForWarnning(nNotification);}

        else if (!TextUtils.isEmpty(email)
                && !TextUtils.isEmpty(password))
        {loginUser();}

    }

    private void toRegisterActivity() {
        Intent regIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(regIntent);
    }

    private void stylizeCreateAnAcountText() {

        int start = nToRegisterActivity.length()-17;
        int end = nToRegisterActivity.length();
        float proportion = 1.1f;
        final SpannableStringBuilder sb = new SpannableStringBuilder(nToRegisterActivity.getText());
        final StyleSpan boldTxt = new StyleSpan(Typeface.BOLD);
        sb.setSpan(new RelativeSizeSpan(proportion),start,end,0);
        sb.setSpan(boldTxt, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        nToRegisterActivity.setText(sb);
    }

    private void loginUser() {

        nProgressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                    }
                }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                updateUI();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                nProgressBar.setVisibility(View.GONE);

                try
                {
                    throw e;

                }
                // if user enters wrong email.
                catch (FirebaseAuthInvalidUserException invalidEmail)
                {
                    nProgressBar.setVisibility(View.GONE);
                    nNotification.setText("Email does not exist!");
                    setTimedVisbilityForWarnning(nNotification);
                }
                // if user enters wrong password.
                catch (FirebaseAuthInvalidCredentialsException wrongPassword)
                {
                    nProgressBar.setVisibility(View.GONE);
                    nNotification.setText("Wrong password!");
                    setTimedVisbilityForWarnning(nNotification);

                }
                catch (Exception ee)
                {
                    nProgressBar.setVisibility(View.GONE);
                    nNotification.setText("Something went wrong! please try again");
                    setTimedVisbilityForWarnning(nNotification);
                }
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

    private boolean isAdmin()
    {
        // set email domain level admin
        String domainLevel = currentUser.getEmail();
        admin = domainLevel.contains("@admin.com");
        return admin;
    }
    private boolean isEmployee()
    {
        // set email domain levels employee
        String domainLevel = currentUser.getEmail();
        employee = domainLevel.contains("@emp.com");
        return employee;
    }
    private void updateUI() {

        currentUser = mAuth.getCurrentUser();


        if (currentUser != null)
        {

            if(!isAdmin() || !isEmployee())
            {
                Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(mainActivity);
                finish();
            }
            else if (isEmployee())
            {
                Intent employeeActivity = new Intent(LoginActivity.this, EmpActivity.class);
                startActivity(employeeActivity);
                finish();
            }
            else
            {
                Intent adminActivity = new Intent(LoginActivity.this, AdminActivity.class);
                startActivity(adminActivity);
                finish();
            }

        }
        else
        {
            Toast.makeText(LoginActivity.this, "Incorrect email or password.",
                    Toast.LENGTH_LONG).show();
        }
    }
}