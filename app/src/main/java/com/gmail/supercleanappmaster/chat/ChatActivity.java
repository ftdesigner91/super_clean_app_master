package com.gmail.supercleanappmaster.chat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gmail.supercleanappmaster.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    // parent layout
    private ConstraintLayout nChat_parent_layout;

    // Tool bar
    private Toolbar nToolbar;
    // Firebase
    private  FirebaseDatabase db;
    private DatabaseReference DBref;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    // Firestore
    private FirebaseFirestore firebaseFirestore;

    // chat layout components
    private EditText nSender_editText;
    private ImageButton nSenderBtn;
    private RecyclerView nMessageList;
    private TextView nSenderName;
    private CircleImageView nSender_image;

    // get date and time
    private DateTimeFormatter dtf;
    private LocalDateTime currentTime;
    private SimpleDateFormat df;
    private Date date;

    // user level
    private boolean client;
    private boolean employee;


    private FirebaseRecyclerOptions <ChatMessageModel> options;
    private ChatAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        nToolbar =findViewById(R.id.toolbar);
        setSupportActionBar(nToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        // Firebase
        db = FirebaseDatabase.getInstance();
        DBref = db.getReference("client_emp_messages");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        // Firestroe
        firebaseFirestore = FirebaseFirestore.getInstance();

        // chat layout components
        nSender_editText = findViewById(R.id.sender_editText);
        nSenderBtn = findViewById(R.id.senderBtn);
        nSender_image =findViewById(R.id.sender_image);
        nSenderName = findViewById(R.id.senderName);

        // recycler view
        nMessageList = findViewById(R.id.messageList);

        // user information
        final String uid = mUser.getUid();
        String username = mUser.getDisplayName();
        Uri userImg = mUser.getPhotoUrl();
        final boolean b = true;

        // Date and time
        dtf = DateTimeFormatter.ofPattern("hhmmssddmmyy");
        currentTime = LocalDateTime.now();
        df = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
        date = Calendar.getInstance().getTime();
        final String formattedDate = df.format(date);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // sets the last message to appear at the bottom of a layout
        layoutManager.setStackFromEnd(true);

        nMessageList.setHasFixedSize(true);
        nMessageList.setLayoutManager(layoutManager);

        employee = mUser.getEmail().contains("@emp.com");
        nSenderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check if no effect delete
                nMessageList.smoothScrollToPosition(nMessageList.getAdapter().getItemCount());


                CharSequence senderTextBox = nSender_editText.getText().toString();
                if (employee)
                {
                    if (!TextUtils.isEmpty(senderTextBox))
                    {sendEmployeeMessageToRealtimeDatabase(uid);}
                }
                else
                {
                    if (!TextUtils.isEmpty(senderTextBox))
                    {snedClientMessageToRealtimeDatabase(uid, formattedDate);}
                }

            }
        });

        if (employee)
        {
            Bundle bundle = getIntent().getExtras();
            String d = bundle.getString("path_to_client_realtime_db");

            // change DBref if not working
            options = new FirebaseRecyclerOptions.Builder<ChatMessageModel>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("client_emp_messages")
                            .child(d), ChatMessageModel.class)
                    .build();
            // fetch client information
            firebaseFirestore.collection("paird_client_and_emp").document(d)
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String client_name = documentSnapshot.get("client_name").toString();
                    String client_image = documentSnapshot.get("client_image").toString();
                    Picasso.get().load(client_image).into(nSender_image);


                }
            });
        }

        else // if client
        {
            // change DBref if not working
            options = new FirebaseRecyclerOptions.Builder<ChatMessageModel>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("client_emp_messages")
                            .child(formattedDate+uid), ChatMessageModel.class)
                    .build();

            // fetch employee information
            firebaseFirestore.collection("paird_client_and_emp").document(formattedDate+uid)
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String employee_name = String.valueOf(documentSnapshot.get("emp_name"));
                    String employee_image = String.valueOf(documentSnapshot.get("emp_image"));
                    if (documentSnapshot.get("emp_image") != null)
                    {Picasso.get().load(employee_image).into(nSender_image);}
                    else {nSender_image.setImageResource(R.drawable.ic_icons_emp_persona_w_bg_icon);}
                    getSupportActionBar().setTitle(employee_name);
                }
            });
        }
        adapter = new ChatAdapter(options);
        nMessageList.setAdapter(adapter);
    }

    private void sendEmployeeMessageToRealtimeDatabase(final String uid) {
        // get client path in realtime database from SubmissionsActivity.java
        Bundle bundle = getIntent().getExtras();
        final String path_to_client_realtime_db =
                bundle.getString("path_to_client_realtime_db");

        // get employees information to display in chat
        firebaseFirestore.collection("employees").document(uid)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                        String employeeName = String.valueOf(value.get("emp_name"));
                        String employeeText = String.valueOf(nSender_editText.getText());

                        HashMap<String, String> empMsgMap = new HashMap<>();
                        empMsgMap.put("employee_name", employeeName);
                        empMsgMap.put("message", employeeText);
                        empMsgMap.put("employe_id", uid);
                        empMsgMap.put("sender_id", uid);

                        // add to client node in realtime database
                        DBref.child(path_to_client_realtime_db).push().setValue(empMsgMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // clear text box after message has sent
                                nSender_editText.getText().clear();
                            }
                        });

                    }
                });
    }

    private void snedClientMessageToRealtimeDatabase(final String uid, final String formattedDate) {

        // get client information to display in chat
        firebaseFirestore.collection("clients").document(uid)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                        String clientName = String.valueOf(value.get("client_name"));
                        String clientText = String.valueOf(nSender_editText.getText());
                        String client_db_path = formattedDate+uid;

                        HashMap<String, String> clientMsgMap = new HashMap<>();
                        clientMsgMap.put("client_name", clientName);
                        clientMsgMap.put("message", clientText);
                        clientMsgMap.put("client_id", uid);
                        clientMsgMap.put("sender_id", uid);

                        // add message to client node in realtime database
                        DBref.child(client_db_path).push().setValue(clientMsgMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // clear text box after message has sent
                                nSender_editText.getText().clear();
                            }
                        });

                    }
                });
    }


    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();

    }

    @Override
    public void onStart() {
        super.onStart();

        getSupportActionBar().setTitle(null);
        // display name and image of sender
        if (!employee)
        {
            // Date and time
            dtf = DateTimeFormatter.ofPattern("hhmmssddmmyy");
            currentTime = LocalDateTime.now();
            df = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
            date = Calendar.getInstance().getTime();
            final String formattedDate = df.format(date);

            // fetch employee information
            firebaseFirestore.collection("paird_client_and_emp").document(formattedDate
                    +FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String employee_name = documentSnapshot.get("emp_name").toString();
                    String employee_image = documentSnapshot.get("emp_image").toString();
                    Picasso.get().load(employee_image).into(nSender_image);
                    nSenderName.setText(employee_name);
                }
            });
        }
        else // if client
        {
            Bundle bundle = getIntent().getExtras();
            String d = bundle.getString("path_to_client_realtime_db");

            // fetch client information
            firebaseFirestore.collection("paird_client_and_emp").document(d)
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String client_name = documentSnapshot.get("client_name").toString();
                    String client_image = documentSnapshot.get("client_image").toString();
                    Picasso.get().load(client_image).into(nSender_image);
                    nSenderName.setText(client_name);
                }
            });
        }

        adapter.startListening();
    }
}