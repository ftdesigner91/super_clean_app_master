package com.gmail.supercleanappmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ViewAllEmpActivity extends AppCompatActivity {

    private RecyclerView nView_emp_list;

    // firestore
    private FirebaseFirestore firebaseFirestore;
    // firestore - options
    private FirestoreRecyclerOptions<ViewAllEmpModel> options;
    // firestore - adapter
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_emp);

        nView_emp_list = findViewById(R.id.view_emp_list);

        // firestore
        firebaseFirestore = FirebaseFirestore.getInstance();

        // Query
        Query query = firebaseFirestore.collection("employees");

        // firestore - options
        options = new FirestoreRecyclerOptions.Builder<ViewAllEmpModel>()
                .setQuery(query, ViewAllEmpModel.class)
                .build();

        // firestore - adapter
        adapter = new FirestoreRecyclerAdapter<ViewAllEmpModel, ViewAllEmpViewHolder>(options) {
            @NonNull
            @Override
            public ViewAllEmpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_all_emp_list, parent, false);
                return new ViewAllEmpViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull ViewAllEmpViewHolder holder, int position, @NonNull ViewAllEmpModel model) {

                holder.nEmp_name.setText(model.getEmp_name());
                holder.nEmp_email.setText(model.getEmp_email());

            }
        };

        nView_emp_list.setHasFixedSize(true);
        nView_emp_list.setLayoutManager(new LinearLayoutManager(this));
        nView_emp_list.setAdapter(adapter);
    }

    private static class ViewAllEmpViewHolder extends RecyclerView.ViewHolder{

        private TextView nEmp_name;
        private TextView nEmp_email;

        public ViewAllEmpViewHolder(@NonNull View itemView) {
            super(itemView);

            nEmp_name = itemView.findViewById(R.id.emp_name);
            nEmp_email = itemView.findViewById(R.id.emp_email);
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