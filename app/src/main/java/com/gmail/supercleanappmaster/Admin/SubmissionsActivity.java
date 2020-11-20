package com.gmail.supercleanappmaster.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SubmissionsActivity extends AppCompatActivity {

    // toolbar
    private Toolbar nToolbar;
    // submissions list view
    private RecyclerView nSubmissionsList;
    // Firebase firestore
    private FirebaseFirestore firebaseFirestore;
    // Write a message to the database
    FirebaseDatabase DB;
    DatabaseReference DBref;

    // Firestore adapter
    private FirestoreRecyclerAdapter adapter;
    // Firestore recycler options builder
    FirestoreRecyclerOptions<SubmissionsListModel> options;
    // view for onCreateViewHolder to inflate submissions layout view
    private View view;
    // get date
    private Date date;
    private SimpleDateFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submissions);
        nToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(nToolbar);


        // INITIALIZATION - submissions list view
        nSubmissionsList = findViewById(R.id.submissionsList);
        // INITIALIZATION - Firestore
        firebaseFirestore = FirebaseFirestore.getInstance();

        // INITIALIZATION - firebase database
        DB = FirebaseDatabase.getInstance();
        DBref = DB.getReference("client_emp_messages");

        // INITIALIZATION - date
         date = Calendar.getInstance().getTime();
        df = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
        final String formattedDate = df.format(date);

        // set query
        Query query = firebaseFirestore.collection("submissions");

        // build Firestore recycler adapter
        options = new FirestoreRecyclerOptions.Builder<SubmissionsListModel>()
                .setQuery(query, SubmissionsListModel.class)
                .build();

        // set adapter
        adapter = new FirestoreRecyclerAdapter<SubmissionsListModel, SubmissionsListViewHolder>(options) {
            @NonNull
            @Override
            public SubmissionsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.submissions_item, parent, false);
                return new SubmissionsListViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull SubmissionsListViewHolder holder, final int position,
                                            @NonNull final SubmissionsListModel model) {

                holder.nUsername.setText("Name: "+model.getSub_client_name());
                holder.nNumber.setText("Number: "+model.getSub_client_phone());
                holder.nAddress.setText("Address:\n"+model.getSub_client_address());
                holder.nType.setText("Service type: "+model.getSub_client_service_type()+" cleaning");
                holder.nBed.setText("Bedrooms: "+model.getSub_client_bedroom_count());
                holder.nBath.setText("Bathrooms: "+model.getSub_client_bathroom_count());
                holder.nDate.setText("Due date: "+model.getSub_client_date());
                holder.nPrice.setText("Price: "+model.getSub_client_price());
                holder.nAssignBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String clientID = model.getSub_client_id();
                        String clientName = model.getSub_client_name();
                        String clientPhone = model.getSub_client_phone();
                        String clientAddress = model.getSub_client_address();
                        String serviceType = model.getSub_client_service_type();
                        String beds = model.getSub_client_bedroom_count();
                        String baths = model.getSub_client_bathroom_count();
                        String date = model.getSub_client_date();

                        // set the path to client realtime database to: current date + client ID
                        String client_id_and_date = formattedDate+clientID;

                        getClientInfoToAssignAnEmployeeInListAllEmpActivity(clientID, client_id_and_date, clientName
                        , clientPhone, clientAddress, serviceType, beds, baths, date);
                    }
                });
            }
        };

        nSubmissionsList.setHasFixedSize(true);
        nSubmissionsList.setLayoutManager(new LinearLayoutManager(this));
        nSubmissionsList.setAdapter(adapter);
    }

    private void getClientInfoToAssignAnEmployeeInListAllEmpActivity(String clientID,
    String client_id_and_date, String clientName, String clientPhone, String clientAddress,
    String serviceType, String beds, String baths, String date) {

        // set the realtime database path to (date+clientID)
        Map<String, String> client_id_map = new HashMap<>();
        //client_id_map.put("client_id_date", client_id_and_date);
        DBref.child(client_id_and_date).setValue(client_id_map);

        // get client path then navigate employees list
        Intent emplistIntent = new Intent(SubmissionsActivity.this,
                ListAllEmployeesActivity.class);
        emplistIntent.putExtra("doc_path", client_id_and_date);
        emplistIntent.putExtra("client_id", clientID);
        emplistIntent.putExtra("client_name", clientName);
        emplistIntent.putExtra("client_phone", clientPhone);
        emplistIntent.putExtra("client_address", clientAddress);
        emplistIntent.putExtra("service_type", serviceType);
        emplistIntent.putExtra("bedrooms", beds);
        emplistIntent.putExtra("bathrooms", baths);
        emplistIntent.putExtra("client_id", clientID);
        emplistIntent.putExtra("date", date);
        startActivity(emplistIntent);
    }


    public static class SubmissionsListViewHolder extends RecyclerView.ViewHolder {

        private TextView nUsername;
        private TextView nNumber;
        private TextView nAddress;
        private TextView nType;
        private TextView nBed;
        private TextView nBath;
        private TextView nDate;
        private TextView nPrice;
        private Button nAssignBtn;

        public SubmissionsListViewHolder(@NonNull View itemView) {
            super(itemView);

            nUsername = itemView.findViewById(R.id.ct_name);
            nNumber = itemView.findViewById(R.id.ct_number);
            nDate = itemView.findViewById(R.id.ct_date);
            nType = itemView.findViewById(R.id.ct_type);
            nBed = itemView.findViewById(R.id.ct_bed);
            nBath = itemView.findViewById(R.id.ct_bath);
            nPrice = itemView.findViewById(R.id.price);
            nAddress = itemView.findViewById(R.id.ct_address);
            nAssignBtn = itemView.findViewById(R.id.acceptAssignmentBtn);
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