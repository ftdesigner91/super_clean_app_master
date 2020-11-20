package com.gmail.supercleanappmaster.employees;

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
import com.gmail.supercleanappmaster.chat.ChatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class EmpViewAssignmentsActivity extends AppCompatActivity {

    // list view
    private RecyclerView nAssignmentList;

    // Firestore
    FirebaseFirestore firebaseFirestore;

    FirestoreRecyclerAdapter adapter;
    FirestoreRecyclerOptions<EmpViewAssignmentsModel> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_view_assignments);

        // components
        nAssignmentList = findViewById(R.id.assignmentsList);

        // Firestore
        firebaseFirestore = FirebaseFirestore.getInstance();

        Query query = firebaseFirestore.collection("assigned_clients");

        options = new FirestoreRecyclerOptions.Builder<EmpViewAssignmentsModel>()
                .setQuery(query, EmpViewAssignmentsModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<EmpViewAssignmentsModel, EmpViewAssignmentsViewHolder>(options) {
            @NonNull
            @Override
            public EmpViewAssignmentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assignment_view_item, parent, false);
                return new EmpViewAssignmentsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull EmpViewAssignmentsViewHolder holder, int position,
                                            @NonNull final EmpViewAssignmentsModel model) {
                holder.nCt_name.setText("Name: "+model.getClient_name());
                holder.nCt_number.setText("Phone number: "+model.getClient_phone());
                holder.nCt_date.setText("Date: "+model.getDate());
                holder.nCt_type.setText("Service type: "+model.getService_type());
                holder.nCt_bedrooms.setText("Bedrooms: "+model.getBedrooms());
                holder.nCt_bathrooms.setText("Bathrooms: "+model.getBathrooms());
                holder.nCt_address.setText("Address: "+model.getClient_address());
                holder.nAcceptAssignmentBtn.setText("contact "+model.getClient_name());
                holder.nAcceptAssignmentBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent chatIntent = new Intent(EmpViewAssignmentsActivity.this, ChatActivity.class);
                        chatIntent.putExtra("path_to_client_realtime_db",model.getSub_date_and_client_id());
                        startActivity(chatIntent);
                    }
                });
            }
        };

        nAssignmentList.setHasFixedSize(true);
        nAssignmentList.setLayoutManager(new LinearLayoutManager(this));
        nAssignmentList.setAdapter(adapter);

    }

    public static class EmpViewAssignmentsViewHolder extends RecyclerView.ViewHolder {


        private Button nAcceptAssignmentBtn;
        private TextView nCt_name;
        private TextView nCt_number;
        private TextView nCt_date;
        private TextView nCt_type;
        private TextView nCt_bedrooms;
        private TextView nCt_bathrooms;
        private TextView nCt_address;

        public EmpViewAssignmentsViewHolder(@NonNull View itemView) {
            super(itemView);

            nAcceptAssignmentBtn = itemView.findViewById(R.id.acceptAssignmentBtn);
            nCt_name = itemView.findViewById(R.id.ct_name);
            nCt_number = itemView.findViewById(R.id.ct_number);
            nCt_date = itemView.findViewById(R.id.ct_date);
            nCt_type = itemView.findViewById(R.id.ct_type);
            nCt_bedrooms = itemView.findViewById(R.id.ct_bed);
            nCt_bathrooms = itemView.findViewById(R.id.ct_bath);
            nCt_address = itemView.findViewById(R.id.ct_address);
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