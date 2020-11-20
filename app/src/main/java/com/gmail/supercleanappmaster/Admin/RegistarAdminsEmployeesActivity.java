package com.gmail.supercleanappmaster.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.telecom.CallScreeningService;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegistarAdminsEmployeesActivity extends AppCompatActivity {


    // Components
    private Toolbar nToolbar;
    private TextInputEditText nAdmEmpName;
    private TextInputEditText nAdmEmpEmail;
    private TextInputEditText nAdmEmpPassword;
    private Button nAdminRegBtn;
    private TextView nNotification;
    private TextView nRegHeadline;
    private ProgressBar nProgressBar;
    private ConstraintLayout nAdmin_regestrar_parent_layout;

    // delayer
    private Handler handler;

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
    
    // Firebase
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private String authID;

    // Fierstore
    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar_admins_employees);
        nToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(nToolbar);

        // INITIALIZATIONS - Components
        nAdmEmpName = findViewById(R.id.admEmpName);
        nAdmEmpEmail = findViewById(R.id.admEmpEmail);
        nAdmEmpPassword = findViewById(R.id.admEmpPassword);
        nAdminRegBtn = findViewById(R.id.adminReg);
        nNotification = findViewById(R.id.notification);
        nRegHeadline = findViewById(R.id.regHeadline);
        nProgressBar = findViewById(R.id.progressBar2);
        nAdmin_regestrar_parent_layout = findViewById(R.id.admin_regestrar_parent_layout);
        handler = new Handler();

        // INITIALIZATION - domains
        adminDomain = "@admin.com";
        employeeDomain = "@emp.com";

        // INITIALIZATIONS - Firstore
        firebaseFirestore = FirebaseFirestore.getInstance();

        // INITIALIZATIONS - Firebase
        mAuth = FirebaseAuth.getInstance();




        // progress bar is invisible
        nProgressBar.setVisibility(View.GONE);

        // Hide keyboard when screen touched
        nAdmin_regestrar_parent_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                UIUtil.hideKeyboard(RegistarAdminsEmployeesActivity.this);
                return false;
            }
        });

        // Watch e
        nAdmEmpEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // User level
                domainLevel = String.valueOf(nAdmEmpEmail.getText());

                // User domain
                admin = domainLevel.contains(adminDomain);
                employee = domainLevel.contains(employeeDomain);

                if (admin){nRegHeadline.setTextColor(nNotification.getTextColors());
                    nRegHeadline.setText("Registering Admin");}
                if (employee){nRegHeadline.setTextColor(nNotification.getTextColors());
                    nRegHeadline.setText("Registering Employee");}
                if (!employee && !admin){nRegHeadline.setTextColor(R.id.regHeadline);
                    nRegHeadline.setText("Register Admin or Employee");}
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        nAdminRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UIUtil.hideKeyboard(RegistarAdminsEmployeesActivity.this);

                // User information
                name = String.valueOf(nAdmEmpName.getText());
                email = String.valueOf(nAdmEmpEmail.getText());
                password = String.valueOf(nAdmEmpPassword.getText());

                // User level
                domainLevel = String.valueOf(nAdmEmpEmail.getText());

                // User domain
                admin = domainLevel.contains(adminDomain);
                employee = domainLevel.contains(employeeDomain);

                checkAdminInputsThenPerformAccordingly(name, email, password,
                        domainLevel, admin, employee);
            }
        });
    }

    private void checkAdminInputsThenPerformAccordingly(String name, String email, String password,
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
        else if (!admin && !employee)
        {nNotification.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            nNotification.setText("use "+adminDomain+" for admin, "+employeeDomain+" for employee");}

        //else if (admin){nRegHeadline.setText("Restricted domain!!!");}

        else if (!TextUtils.isEmpty(name)
                && !TextUtils.isEmpty(email)
                && !TextUtils.isEmpty(password))
        {
            if (admin){registerAnAdmin();}
            else if (employee){registerAnEmployee();}
            else {Toast.makeText(RegistarAdminsEmployeesActivity.this,
                    "please try again! ",Toast.LENGTH_LONG).show();}
        }
    }


    // --- REGISTER ADMINS ---
    private void registerAnAdmin() {

        nProgressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                    }
                }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                authID = mAuth.getUid();
                addAdminToFirestore(name, email, password, authID);
                nProgressBar.setVisibility(View.GONE);
                Toast.makeText(RegistarAdminsEmployeesActivity.this,
                        "Admin registered successfully! ",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                nProgressBar.setVisibility(View.GONE);
                Toast.makeText(RegistarAdminsEmployeesActivity.this,
                        "registered failed! ",Toast.LENGTH_LONG).show();
            }
        });

    }
    private void addAdminToFirestore(String name, String email, String password, String authID) {
        Map<String, Object> adminsMap = new HashMap<>();
        adminsMap.put("admin_name", name);
        adminsMap.put("admin_email", email);
        adminsMap.put("admin_password", password);
        adminsMap.put("admin_id", authID);
        adminsMap.put("admin_image", "not_set");
        firebaseFirestore.collection("admins").document(authID)
                .set(adminsMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mAuth.signOut();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    // --- REGISTER EMPLOYEES
    private void registerAnEmployee() {
        nProgressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                    }
                }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                authID = mAuth.getUid();
                addEmployeeToFirestore(name, email, password, authID);
                nProgressBar.setVisibility(View.GONE);
                Toast.makeText(RegistarAdminsEmployeesActivity.this,
                        "Employee registered successfully! ",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                nProgressBar.setVisibility(View.GONE);
                Toast.makeText(RegistarAdminsEmployeesActivity.this,
                        "registered failed! ",Toast.LENGTH_LONG).show();
            }
        });

    }
    private void addEmployeeToFirestore(String name, String email, String password, String authID) {

        Map<String, Object> employeesMap = new HashMap<>();
        employeesMap.put("emp_name", name);
        employeesMap.put("emp_email", email);
        employeesMap.put("emp_password", password);
        employeesMap.put("emp_id", authID);
        employeesMap.put("emp_image", "not_set");
        firebaseFirestore.collection("employees").document(authID)
                .set(employeesMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mAuth.signOut();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    // --- VISIBILITY TIMER FOR WARNING MESSAGES
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

}