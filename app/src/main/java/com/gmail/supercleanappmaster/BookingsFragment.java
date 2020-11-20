package com.gmail.supercleanappmaster;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gmail.supercleanappmaster.SubmissionsHistory.SubmissionsHistoryModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class BookingsFragment extends Fragment {

    private static final int NO_BOOKINGS_LAYOUT = 1;
    private View view;

    private RecyclerView nSub_history_recycler_view;

    // Firstore
    private FirebaseFirestore firestore;
    private FirestoreRecyclerOptions options;
    private FirestoreRecyclerAdapter adapter;

    // firebase
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String uid;

    int checker = 0;
    public BookingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bookings, container, false);
        // current user
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        // Recycler view
        nSub_history_recycler_view = view.findViewById(R.id.sub_history_recycler_view);
        // Firestore
        firestore = FirebaseFirestore.getInstance();
        // Query
        Query  query = firestore.collection("submissions");

        options = new FirestoreRecyclerOptions.Builder<SubmissionsHistoryModel>()
                .setQuery(query, SubmissionsHistoryModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<SubmissionsHistoryModel, SubmissionsHistoryViewHolder>(options) {
            @NonNull
            @Override
            public SubmissionsHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                firestore.collection("submissions").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful())
                                {
                                    uid = mUser.getUid();

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.getId().contains(uid))
                                        {checker++;}

                                    }
                                }
                            }
                        });
                View view;

                if (checker <= 0)
                {
                        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.no_bookings_layout, parent, false);
                        return new SubmissionsHistoryViewHolder(view);
                }
                else
                {
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookings_history_layout, parent, false);
                    return new SubmissionsHistoryViewHolder(view);
                }
            }

            @Override
            protected void onBindViewHolder(@NonNull SubmissionsHistoryViewHolder holder, int position, @NonNull SubmissionsHistoryModel model) {
                if (holder.nDate != null &&
                        holder.nService_type != null &&
                        holder.nBedrooms != null &&
                        holder.nBathrooms != null &&
                        holder.nAddress != null)
                {
                    holder.nDate.setText(model.getSub_client_date());
                    holder.nService_type.setText(model.getSub_client_service_type());
                    holder.nBedrooms.setText(model.getSub_client_bedroom_count());
                    holder.nBathrooms.setText(model.getSub_client_bathroom_count());
                    holder.nAddress.setText(model.getSub_client_address());
                }
                else {holder.nNo_bookings.setText(model.getNo_bookings());}

            }
        };

        //LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        // sets the last message to appear at the bottom of a layout
        //layoutManager.setStackFromEnd(true);

        nSub_history_recycler_view.setHasFixedSize(true);
        nSub_history_recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        nSub_history_recycler_view.setAdapter(adapter);

        return view;
    }

    private boolean containsUID(boolean contains) {
        contains = true;
        return contains;
    }

    public class SubmissionsHistoryViewHolder extends RecyclerView.ViewHolder {

        private TextView nDate;
        private TextView nService_type;
        private TextView nBedrooms;
        private TextView nBathrooms;
        private TextView nAddress;

        private TextView nNo_bookings;

        public SubmissionsHistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            nDate = itemView.findViewById(R.id.ct_date);
            nService_type = itemView.findViewById(R.id.service_type);
            nBedrooms = itemView.findViewById(R.id.bedrooms);
            nBathrooms = itemView.findViewById(R.id.bathrooms);
            nAddress = itemView.findViewById(R.id.ct_address);

            nNo_bookings = itemView.findViewById(R.id.no_bookings);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();

    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();

    }

}