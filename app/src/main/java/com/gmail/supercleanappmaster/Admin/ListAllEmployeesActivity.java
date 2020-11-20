package com.gmail.supercleanappmaster.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gmail.supercleanappmaster.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ListAllEmployeesActivity extends AppCompatActivity {


    // submissions list view
    private RecyclerView nEmpList;

    // Firebase firestore
    private FirebaseFirestore firebaseFirestore;

    // Firebase database
    private FirebaseDatabase DB;
    private DatabaseReference DBref;

    // Firebase user
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String uid;

    // get date
    private Date date;
    private SimpleDateFormat df;
    private DateTimeFormatter dtf;
    private LocalDateTime currentTime;

    // Firestore adapter
    private FirestoreRecyclerAdapter adapter;

    // Firestore recycler options builder
    FirestoreRecyclerOptions<EmployeesListModel> options;

    // view for onCreateViewHolder to inflate submissions layout view
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_employees);

        // INITIALIZATION - submissions list view
        nEmpList = findViewById(R.id.empList);

        // INITIALIZATION - Firestore
        firebaseFirestore = FirebaseFirestore.getInstance();

        // INITIALIZATION - Firebase database
        DB = FirebaseDatabase.getInstance();
        DBref = DB.getReference("client_emp_messages");

        // INITIALIZATION - Firebase user
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        uid = mUser.getUid();

        // Date
        date = Calendar.getInstance().getTime();
        df = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
        dtf = DateTimeFormatter.ofPattern("hhmmssddmmyy");
        currentTime = LocalDateTime.now();
        final String formattedDate = df.format(date);

        // set query
        Query query = firebaseFirestore.collection("employees");

        // build Firestore recycler adapter
        options = new FirestoreRecyclerOptions.Builder<EmployeesListModel>()
                .setQuery(query, EmployeesListModel.class)
                .build();

        // set adapter
        adapter = new FirestoreRecyclerAdapter<EmployeesListModel, EmployeesListViewHolder>(options) {
            @NonNull
            @Override
            public EmployeesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_employees_list_item, parent, false);
                return new EmployeesListViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final EmployeesListViewHolder holder,
                                            int position, @NonNull final EmployeesListModel model) {

                holder.nEmpname.setText("Name: "+model.getEmp_name());
                holder.nAssignEmpBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // get document path to already made document() by SubmissionsActivity.java
                        // the path points to the combination of user's ID and date of submission
                        Bundle bundle = getIntent().getExtras();
                        final String path = bundle.getString("doc_path");
                        // get client submissions info from SubmissionsActivity.java
                        final String clientID = bundle.getString("client_id");
                        final String clientName = bundle.getString("client_name");
                        final String clientPhone = bundle.getString("client_phone");
                        final String clientAddress = bundle.getString("client_address");
                        final String serviceType = bundle.getString("service_type");
                        final String bedrooms = bundle.getString("bedrooms");
                        final String bathrooms = bundle.getString("bathrooms");
                        final String subDate = bundle.getString("date");
                        // get employee ID from model
                        String employee_id = model.getEmp_id();

                        // in collection("assigned_clients")
                        // make document(@emp.com + date + employeeID) and store client (id+date) in it
                        String empEmail = model.getEmp_email();
                        String date = dtf.format(currentTime);
                        // assigned clients collection
                        Map<String, String> assigned_client_map = new HashMap<>();
                        assigned_client_map.put("sub_date_and_client_id", path);
                        assigned_client_map.put("client_id", clientID);
                        assigned_client_map.put("client_name", clientName);
                        assigned_client_map.put("client_phone", clientPhone);
                        assigned_client_map.put("client_address", clientAddress);
                        assigned_client_map.put("service_type", serviceType);
                        assigned_client_map.put("bedrooms", bedrooms);
                        assigned_client_map.put("bathrooms", bathrooms);
                        assigned_client_map.put("date", subDate);
                        firebaseFirestore.collection("assigned_clients")
                                .document(empEmail + date + "IN" +uid)
                        .set(assigned_client_map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                // read client info to pair with employee
                                firebaseFirestore.collection("clients").document(clientID)
                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        String client_name = String.valueOf(documentSnapshot.get("client_name"));
                                        String client_image = String.valueOf(documentSnapshot.get("client_image"));

                                        // pair emp and client to get each others info
                                        Map<String, String> paird_client_and_emp_map = new HashMap<>();
                                        paird_client_and_emp_map.put("emp_name",model.getEmp_name());
                                        paird_client_and_emp_map.put("emp_image",model.getEmp_image());
                                        paird_client_and_emp_map.put("client_name",client_name);
                                        paird_client_and_emp_map.put("client_image",client_image);
                                        firebaseFirestore.collection("paird_client_and_emp").document(path)
                                                .set(paird_client_and_emp_map);
                                    }
                                });
                            }
                        });


                    }
                });
            }
        };

        nEmpList.setHasFixedSize(true);
        nEmpList.setLayoutManager(new LinearLayoutManager(this));
        nEmpList.setAdapter(adapter);
    }

    private void getEmpidAndSendtoChatActivity(String emp_id) {

        String empID = emp_id;
        Intent emplistIntent = new Intent(ListAllEmployeesActivity.this, ListAllEmployeesActivity.class);
        emplistIntent.putExtra("emp_id_at_list_all_employees_activity", empID);
    }

    public static class EmployeesListViewHolder extends RecyclerView.ViewHolder {


        private TextView nEmpname;
        private Button nAssignEmpBtn;
        public EmployeesListViewHolder(@NonNull View itemView) {
            super(itemView);

            nEmpname = itemView.findViewById(R.id.emp_name);
            nAssignEmpBtn = itemView.findViewById(R.id.assignEmpBtn);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

}