package com.gmail.supercleanappmaster.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.supercleanappmaster.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AdminAddServiceActivity extends AppCompatActivity implements View.OnClickListener {

    // components
    private ImageButton nItem1_btn, nItem2_btn, nItem3_btn, nItem4_btn, nItem5_btn, nAdd_item_icon_btn;
    public ImageView nImage_preview;
    private TextView nWarning_text_view, nText_preview;
    private Button nAdd_service_btn;
    private TextInputEditText nService_title_edit_text;
    private ConstraintLayout nParent_layout;
    private GridView nGrid_view_btns;

    // drawables for icon picker buttons
    int item1_icon;
    int item2_icon;
    int item3_icon;

    int item4_icon;
    int item5_icon;

    // Firestore
    private FirebaseFirestore firebaseFirestore;
    // Storage refrence
    private StorageReference nImageStorageRef;
    private static final int GALLERY_PICK = 1;
    private Uri resultUri;
    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    // get date
    private Date date;
    private SimpleDateFormat df;

    //
    private String service_title;
    private String item;
    private String link;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_service);

        // components
        // buttons
        //nItem1_btn = findViewById(R.id.item1_btn);
        //nItem1_btn.setOnClickListener(this);
        //nItem2_btn = findViewById(R.id.item2_btn);
        //nItem2_btn.setOnClickListener(this);
        //nItem3_btn = findViewById(R.id.item3_btn);
        //nItem3_btn.setOnClickListener(this);
        //nItem4_btn = findViewById(R.id.item4_btn);
        //nItem4_btn.setOnClickListener(this);
        //nItem5_btn = findViewById(R.id.item5_btn);
        //nItem5_btn.setOnClickListener(this);
        //nAdd_item_icon_btn = findViewById(R.id.add_icon_btn);
        //nAdd_item_icon_btn.setOnClickListener(this);
        nAdd_service_btn = findViewById(R.id.add_service_btn);
        nAdd_service_btn.setOnClickListener(this);

        // textviews
        nWarning_text_view = findViewById(R.id.warning_text_view);
        nText_preview = findViewById(R.id.text_preview);
        // edit text
        nService_title_edit_text = findViewById(R.id.service_title_edit_text);
        // imagepreview
        nImage_preview = findViewById(R.id.image_preview);
        // parent layout
        nParent_layout = findViewById(R.id.parent_layout);
        minimizeKeyboardOnScreenTouched();

        // Firestore
        firebaseFirestore = FirebaseFirestore.getInstance();
        // Storage for image
        nImageStorageRef = FirebaseStorage.getInstance().getReference();
        // firebase
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        // get date
        date = Calendar.getInstance().getTime();
        df = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());

        nService_title_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String textBoxContent = String.valueOf(nService_title_edit_text.getText());
                nText_preview.setText(textBoxContent);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void displayIconsOnImageButton(String item1 , String item2 , String item3, String item4, String item5) {

        firebaseFirestore.collection("icons").document(item1)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String link4 = String.valueOf(documentSnapshot.get("icon"));
                if (documentSnapshot.get("icon") != null)
                {
                    //nItem1_btn.setVisibility(View.VISIBLE);
                    //Picasso.get().load(link4).into(nItem1_btn);

                }
            }
        });
        firebaseFirestore.collection("icons").document(item2)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String link4 = String.valueOf(documentSnapshot.get("icon"));
                if (documentSnapshot.get("icon") != null)
                {
                    //nItem2_btn.setVisibility(View.VISIBLE);
                    //Picasso.get().load(link4).into(nItem2_btn);

                }
            }
        });
        firebaseFirestore.collection("icons").document(item3)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String link4 = String.valueOf(documentSnapshot.get("icon"));
                if (documentSnapshot.get("icon") != null)
                {
                    //nItem3_btn.setVisibility(View.VISIBLE);
                    //Picasso.get().load(link4).into(nItem3_btn);

                }
            }
        });
        firebaseFirestore.collection("icons").document(item4)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String link4 = String.valueOf(documentSnapshot.get("icon"));
                if (documentSnapshot.get("icon") != null)
                {
                    //nItem4_btn.setVisibility(View.VISIBLE);
                    //Picasso.get().load(link4).into(nItem4_btn);

                }
            }
        });
        firebaseFirestore.collection("icons").document(item5)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String link5 = String.valueOf(documentSnapshot.get("icon"));
                if (documentSnapshot.get("icon") != null)
                {
                    //nItem5_btn.setVisibility(View.VISIBLE);
                    //Picasso.get().load(link5).into(nItem5_btn);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        itemCounter();
        displayIconsOnImageButton("item1", "item2", "item3", "item4", "item5");
    }

    private void itemCounter() {
        firebaseFirestore.collection("all_services_list")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int cntr = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        cntr++;
                    }
                    nWarning_text_view.setText("your current service list has ("+cntr+
                            ") items, maximum is 5" +
                            "\nto to increase your list of services\n" +
                            "please contact your devoloper");
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        // drawables for icon picker buttons
        item1_icon = R.drawable.ic_icons_condo;
        item2_icon = R.drawable.ic_icons_house;
        item3_icon = R.drawable.ic_icons_apt;

        item4_icon = 0; // set icon
        item5_icon = 0; // set icon

        switch (view.getId())
        {
            case R.id.add_service_btn:

                if (!TextUtils.isEmpty(nService_title_edit_text.getText()))
                {
                    firebaseFirestore.collection("all_services_list")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        int cntr = 0;
                                        for (QueryDocumentSnapshot document : task.getResult()){cntr++;}
                                        String title = String.valueOf(nService_title_edit_text.getText());
                                        if (cntr < 6)
                                        {
                                            switch (cntr)
                                            {
                                                case 1:
                                                    addServiceTofirestore(title, "item1");
                                                    break;
                                                case 2:
                                                    addServiceTofirestore(title, "item2");
                                                    break;
                                                case 3:
                                                    addServiceTofirestore(title, "item3");
                                                    break;
                                                case 4:
                                                    addServiceTofirestore(title, "item4");
                                                    break;
                                                case 5:
                                                    addServiceTofirestore(title, "item5");
                                                    break;
                                            }
                                        }
                                        else
                                        {
                                            Toast.makeText(
                                                    AdminAddServiceActivity.this,
                                                    "you reached the maximum amount of items, " +
                                                            "please contact your developer for extension"
                                                    , Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(
                                                AdminAddServiceActivity.this,
                                                "Something wrong! please try again"
                                                , Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void addServiceTofirestore(final String title, final String item) {

        firebaseFirestore.collection("all_services_list").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int cntr = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {cntr++;}
                            if (cntr != 5) {
                                Map<String, String> serviceMap = new HashMap<>();
                                serviceMap.put("service_title", title);

                                firebaseFirestore.collection("all_services_list")
                                        .document(item).set(serviceMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(AdminAddServiceActivity.this,
                                                        "list added successfully!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    }
                });
    }

    private void pikIconFromDevice() {
        Intent galleryIntenet = new Intent();
        galleryIntenet.setType("image/*");
        galleryIntenet.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntenet, "SELECT IMAGE"),GALLERY_PICK);
    }

    private void getIconFromfirestore(String item) {

        firebaseFirestore.collection("icons").document(item)
                .get().getResult().get("icon");
    }

    private void setIconToImagePreview(String item) {
        firebaseFirestore.collection("icons").document(item).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        link = String.valueOf(documentSnapshot.get("icon"));
                        Picasso.get().load(link).into(nImage_preview);
                    }
                });

    }

    public void minimizeKeyboardOnScreenTouched()
    {
        nParent_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                UIUtil.hideKeyboard(AdminAddServiceActivity.this);
                return false;
            }
        });

    }

    private void openDeviceGalleryToPickAnIcon() {
        firebaseFirestore.collection("icons").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int cntr = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                cntr++;
                            }
                            if (cntr < 5)
                            {
                                pikIconFromDevice();
                            }
                            else
                            {
                                Toast.makeText(AdminAddServiceActivity.this,
                                        "you reached maximum number of icons, " +
                                                "contact your developer to increase number of icons",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(AdminAddServiceActivity.this,
                                    "something went wrong! please try again",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK)
        {
            // crop the image
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(1,1).setRequestedSize(150,150)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            final CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();



                // get date
                date = Calendar.getInstance().getTime();
                df = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
                String formattedDate = df.format(date);

                nImageStorageRef = FirebaseStorage.getInstance().getReference();
                StorageReference filepath = nImageStorageRef.child("service_icons").child(formattedDate + ".jpg");
                filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(AdminAddServiceActivity.this,
                                "success!", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminAddServiceActivity.this,
                                "failed!", Toast.LENGTH_LONG).show();
                    }
                });

                firebaseFirestore.collection("icons").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    int cntr = 0;
                                    //final String downoladUrl = resultUri.toString();
                                    for (QueryDocumentSnapshot document : task.getResult()) {cntr++;}
                                    if (cntr <= 5)
                                    {
                                        if (cntr == 1) {
                                            item = "item1";
                                            addIconTofirestore(item, resultUri.toString());
                                            nItem1_btn.setVisibility(View.VISIBLE);
                                            Picasso.get().load(resultUri).into(nItem1_btn);
                                        }
                                        if (cntr == 2) {
                                            item = "item2";
                                            addIconTofirestore(item, resultUri.toString());
                                            nItem2_btn.setVisibility(View.VISIBLE);
                                            Picasso.get().load(resultUri).into(nItem2_btn);
                                        }
                                        if (cntr == 3) {
                                            item = "item3";
                                            addIconTofirestore(item, resultUri.toString());
                                            nItem3_btn.setVisibility(View.VISIBLE);
                                            Picasso.get().load(resultUri).into(nItem3_btn);
                                        }
                                        if (cntr == 4) {
                                            item = "item4";
                                            addIconTofirestore(item, resultUri.toString());
                                            nItem4_btn.setVisibility(View.VISIBLE);
                                            Picasso.get().load(resultUri).into(nItem4_btn);
                                        }
                                        if (cntr == 5) {
                                            item = "item5";
                                            addIconTofirestore(item, resultUri.toString());
                                            nItem5_btn.setVisibility(View.VISIBLE);
                                            Picasso.get().load(resultUri).into(nItem5_btn);
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(AdminAddServiceActivity.this,
                                                "you reached maximum number of icons, " +
                                                        "contact your developer to increase number of icons",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(AdminAddServiceActivity.this,
                                            "Something went wrong, please try again",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }

    }

    private void addIconTofirestore(String item, String link) {

        Map<String, String> iconMap = new HashMap<>();
        iconMap.put("icon",link);
        firebaseFirestore.collection("icons").document(item)
                .set(iconMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AdminAddServiceActivity.this,
                        "icon added successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
}