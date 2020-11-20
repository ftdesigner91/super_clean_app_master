package com.gmail.supercleanappmaster;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private View view;

    // timer
    private Thread timer;

    // Clickable layouts
    private LinearLayout nHouse_layout;
    private LinearLayout nApartment_layout;
    private LinearLayout nCondo_layout;

    // components
    private CardView nList_card_view;
    private ImageButton nDropdown_btn;

    private ConstraintLayout nSingle_item_list,
    nExpandable_all_items_list, nItem1, nItem2,
    nItem3, nItem4, nItem5;

    private ImageView nFeatured_list_image,
    nItem1_image, nItem2_image, nItem3_image, nItem4_image, nItem5_image;

    private TextView nFeatured_list_title, nItem1_title, nItem2_title,
    nItem3_title, nItem4_title, nItem5_title;

    // items showcase timer
    private static final int TOTAL_NUMBER_OF_ITEMS = 5;
    private static final int SECONDS = 3;
    private int cntr;
    private static final int item = SECONDS;

    // Firestore
    private FirebaseFirestore firebaseFirestore;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        // Buttons
        nDropdown_btn = view.findViewById(R.id.dropdown_btn);
        // Layouts
        nList_card_view = view.findViewById(R.id.list_card_view);
        nExpandable_all_items_list = view.findViewById(R.id.expandable_all_items_list);
        nSingle_item_list = view.findViewById(R.id.single_item_list);
        nSingle_item_list.setOnClickListener(this);
        nItem1 = view.findViewById(R.id.item1);
        nItem1.setOnClickListener(this);
        nItem2 = view.findViewById(R.id.item2);
        nItem2.setOnClickListener(this);
        nItem3 = view.findViewById(R.id.item3);
        nItem3.setOnClickListener(this);
        nItem4 = view.findViewById(R.id.item4);
        nItem4.setOnClickListener(this);
        nItem5 = view.findViewById(R.id.item5);
        nItem5.setOnClickListener(this);
        nHouse_layout = view.findViewById(R.id.house_layout);
        nHouse_layout.setOnClickListener(this);
        nApartment_layout = view.findViewById(R.id.apartment_layout);
        nApartment_layout.setOnClickListener(this);
        nCondo_layout = view.findViewById(R.id.condo_layout);
        nCondo_layout.setOnClickListener(this);

        // ImageViews
        nFeatured_list_image = view.findViewById(R.id.featured_list_image);
        nItem1_image = view.findViewById(R.id.itme1_image);
        nItem2_image = view.findViewById(R.id.item2_image);
        nItem3_image = view.findViewById(R.id.item3_image);
        nItem4_image = view.findViewById(R.id.item4_image);
        nItem5_image = view.findViewById(R.id.item5_image);

        // TextView
        nFeatured_list_title = view.findViewById(R.id.featured_list_title);
        nItem1_title = view.findViewById(R.id.item1_title);
        nItem2_title = view.findViewById(R.id.item2_title);
        nItem3_title = view.findViewById(R.id.item3_title);
        nItem4_title = view.findViewById(R.id.item4_title);
        nItem5_title = view.findViewById(R.id.item5_title);

        // Firestore
        firebaseFirestore = FirebaseFirestore.getInstance();

        nDropdown_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nExpandable_all_items_list.getVisibility() == View.GONE){openDropdownList();}
                else{closeDropdownList();}
            }
        });

        // set icon+title for dropdown lists from firestore
        //setIconForDropdownListsFromFirestroe("item1", nItem1_image, nItem1);
        getServiceNameFromFirestore("item1", nItem1_title);
        //setIconForDropdownListsFromFirestroe("item2", nItem2_image, nItem2);
        getServiceNameFromFirestore("item2", nItem2_title);
        //setIconForDropdownListsFromFirestroe("item3", nItem3_image, nItem3);
        getServiceNameFromFirestore("item3", nItem3_title);
        //setIconForDropdownListsFromFirestroe("item4", nItem4_image, nItem4);
        getServiceNameFromFirestore("item4", nItem4_title);
        //setIconForDropdownListsFromFirestroe("item5", nItem5_image, nItem5);
        getServiceNameFromFirestore("item5", nItem5_title);
        // Showcase services on the list
        showCaseServices();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    private void showCaseServices() {
        timer = new Thread()
        {
            @Override
            public void run() {
                super.run();
                while (!interrupted())
                {
                    try {
                        // 1 SECONDS = 1000 millis
                        int millis = 1000;

                        millis = SECONDS * millis; // convert milliSECONDSonds to SECONDSonds
                        sleep(millis);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cntr++;

                                if(cntr <= SECONDS*TOTAL_NUMBER_OF_ITEMS)
                                {
                                    if (cntr %SECONDS == 0)
                                    {
                                        switch (cntr)
                                        // each case represents an item,
                                        // case:(value) increases by number of SECONDS
                                        {
                                            case item:
                                                if (nItem1.getVisibility() == View.GONE){cntr=item*2;}
                                                else
                                                {
                                                    // service name
                                                    nFeatured_list_title.setText(nItem1_title.getText());
                                                    // service icon
                                                    nFeatured_list_image.setImageDrawable(nItem1_image.getDrawable());
                                                    break;
                                                }
                                            case item*2:
                                                if (nItem2.getVisibility() == View.GONE){cntr=item*3;}
                                                else
                                                {
                                                    // service name
                                                    nFeatured_list_title.setText(nItem2_title.getText());
                                                    // service icon
                                                    nFeatured_list_image.setImageDrawable(nItem2_image.getDrawable());
                                                    break;
                                                }
                                            case item*3:
                                                if (nItem3.getVisibility() == View.GONE){cntr=item*4;}
                                                else
                                                {
                                                    // service name
                                                    nFeatured_list_title.setText(nItem3_title.getText());
                                                    // service icon
                                                    nFeatured_list_image.setImageDrawable(nItem3_image.getDrawable());
                                                    break;
                                                }
                                            case item*4:
                                                if (nItem4.getVisibility() == View.GONE){cntr=item*5;}
                                                else
                                                {
                                                    // service name
                                                    nFeatured_list_title.setText(nItem4_title.getText());
                                                    // service icon
                                                    nFeatured_list_image.setImageDrawable(nItem4_image.getDrawable());
                                                break;
                                                }
                                            case item*5:
                                                if (nItem5.getVisibility() == View.GONE){cntr=item;}
                                                else
                                                {
                                                    // service name
                                                    nFeatured_list_title.setText(nItem5_title.getText());
                                                    // service icon
                                                    nFeatured_list_image.setImageDrawable(nItem5_image.getDrawable());
                                                    break;
                                                }
                                        }
                                    }
                                }
                                else
                                {
                                    cntr = 0; // reset timer
                                }
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }; timer.start();

    }

    private void setIconForDropdownListsFromFirestroe(String item, final ImageView imageView, final ConstraintLayout layout) {

        firebaseFirestore.collection("icons").document(item)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String iconPath = String.valueOf(documentSnapshot.get("icon"));
                if (documentSnapshot.contains("icon"))
                {
                    layout.setVisibility(View.VISIBLE);
                    Picasso.get().load(iconPath).into(imageView);
                }
            }
        });
    }

    private void getServiceNameFromFirestore(String items, final TextView textView) {

        firebaseFirestore.collection("all_services_list").document(items)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String item_title = String.valueOf(documentSnapshot.get("service_title"));

                textView.setText(item_title);
            }
        });
    }

    private void closeDropdownList() {
        TransitionManager.beginDelayedTransition(nList_card_view, new AutoTransition());
        nExpandable_all_items_list.setVisibility(View.GONE);
    }

    private void openDropdownList() {
        TransitionManager.beginDelayedTransition(nList_card_view, new AutoTransition());
        nExpandable_all_items_list.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        String house = "house", apartment = "apartment", condo = "condo";
        String get_item4_title = String.valueOf(nItem4_title.getText());
        String get_item5_title = String.valueOf(nItem5_title.getText());
        Intent checkoutIntent = new Intent(getContext(), CheckoutActivity.class);
        switch (view.getId())
        {
            // navigate to checkout after list is selected
            case R.id.item1:
                checkoutIntent.putExtra("house", house);
                startActivity(checkoutIntent);
                break;
            case R.id.item2:
                checkoutIntent.putExtra("apartment", apartment);
                startActivity(checkoutIntent);
                break;
            case R.id.item3:
                checkoutIntent.putExtra("condo", condo);
                startActivity(checkoutIntent);
                break;
            case R.id.item4:
                checkoutIntent.putExtra("item4", get_item4_title);
                startActivity(checkoutIntent);
                break;
            case R.id.item5:
                checkoutIntent.putExtra("item5", get_item5_title);
                startActivity(checkoutIntent);
                break;

            case R.id.house_layout:
                checkoutIntent.putExtra("house", house);
                startActivity(checkoutIntent);
                break;
            case R.id.apartment_layout:
                checkoutIntent.putExtra("apartment", apartment);
                startActivity(checkoutIntent);
                break;
            case R.id.condo_layout:
                checkoutIntent.putExtra("condo", condo);
                startActivity(checkoutIntent);
                break;
            case R.id.single_item_list:
                if (nExpandable_all_items_list.getVisibility() == View.GONE){openDropdownList();}
                else{closeDropdownList();}
                break;

        }
    }
}
// showcase list from firebase
/*
private void showCaseServices() {
        timer = new Thread()
        {
            @Override
            public void run() {
                super.run();
                while (!interrupted())
                {
                    try {
                        // 1 SECONDS = 1000 millis
                        int millis = 1000;

                        millis = SECONDS * millis; // convert milliSECONDSonds to SECONDSonds
                        sleep(millis);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cntr++;

                                if(cntr <= SECONDS*TOTAL_NUMBER_OF_ITEMS)
                                {
                                    if (cntr %SECONDS == 0)
                                    {
                                        switch (cntr)
                                        // each case represents an item,
                                        // case:(value) increases by number of SECONDS
                                        {
                                            case item:
                                                if (nItem1.getVisibility() == View.GONE){cntr=item*2;}
                                                else
                                                {
                                                    // service name
                                                    firebaseFirestore.collection("all_services_list")
                                                            .document("item1").get()
                                                            .addOnSuccessListener(
                                                                    new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                            String item_title = String.valueOf(documentSnapshot.get("service_title"));
                                                            if (documentSnapshot.contains("service_title"))
                                                            {
                                                                nFeatured_list_title.setText(item_title);

                                                                // service icon
                                                                firebaseFirestore.collection("icons")
                                                                        .document("item1").get()
                                                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                                                String iconLink = String.valueOf(documentSnapshot.get("icon"));
                                                                                if (documentSnapshot.contains("icon"))
                                                                                {
                                                                                    Picasso.get().load(iconLink).into(nFeatured_list_image);
                                                                                }
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    });
                                                    break;
                                                }
                                            case item*2:
                                                if (nItem2.getVisibility() == View.GONE){cntr=item*3;}
                                                else
                                                {
                                                    // service name
                                                    firebaseFirestore.collection("all_services_list")
                                                            .document("item2").get()
                                                            .addOnSuccessListener(
                                                                    new OnSuccessListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                                            String item_title = String.valueOf(documentSnapshot.get("service_title"));
                                                                            if (documentSnapshot.contains("service_title"))
                                                                            {
                                                                                nFeatured_list_title.setText(item_title);

                                                                                // service icon
                                                                                firebaseFirestore.collection("icons")
                                                                                        .document("item2").get()
                                                                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                            @Override
                                                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                                                                String iconLink = String.valueOf(documentSnapshot.get("icon"));
                                                                                                if (documentSnapshot.contains("icon"))
                                                                                                {
                                                                                                    Picasso.get().load(iconLink).into(nFeatured_list_image);
                                                                                                }
                                                                                            }
                                                                                        });
                                                                            }
                                                                        }
                                                                    });
                                                    break;
                                                }
                                            case item*3:
                                                if (nItem3.getVisibility() == View.GONE){cntr=item*4;}
                                                else
                                                {
                                                    // service name
                                                    firebaseFirestore.collection("all_services_list")
                                                            .document("item3").get()
                                                            .addOnSuccessListener(
                                                                    new OnSuccessListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                                            String item_title = String.valueOf(documentSnapshot.get("service_title"));
                                                                            if (documentSnapshot.contains("service_title"))
                                                                            {
                                                                                nFeatured_list_title.setText(item_title);

                                                                                // service icon
                                                                                firebaseFirestore.collection("icons")
                                                                                        .document("item3").get()
                                                                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                            @Override
                                                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                                                                String iconLink = String.valueOf(documentSnapshot.get("icon"));
                                                                                                if (documentSnapshot.contains("icon"))
                                                                                                {
                                                                                                    Picasso.get().load(iconLink).into(nFeatured_list_image);
                                                                                                }
                                                                                            }
                                                                                        });
                                                                            }
                                                                        }
                                                                    });
                                                    break;
                                                }
                                            case item*4:
                                                if (nItem4.getVisibility() == View.GONE){cntr=item*5;}
                                                else
                                                {
                                                    // service name
                                                    firebaseFirestore.collection("all_services_list")
                                                            .document("item4").get()
                                                            .addOnSuccessListener(
                                                                    new OnSuccessListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                                            String item_title = String.valueOf(documentSnapshot.get("service_title"));
                                                                            if (documentSnapshot.contains("service_title"))
                                                                            {
                                                                                nFeatured_list_title.setText(item_title);

                                                                                // service icon
                                                                                firebaseFirestore.collection("icons")
                                                                                        .document("item4").get()
                                                                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                            @Override
                                                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                                                                String iconLink = String.valueOf(documentSnapshot.get("icon"));
                                                                                                if (documentSnapshot.contains("icon"))
                                                                                                {
                                                                                                    Picasso.get().load(iconLink).into(nFeatured_list_image);
                                                                                                }
                                                                                            }
                                                                                        });
                                                                            }
                                                                        }
                                                                    });
                                                break;
                                                }
                                            case item*5:
                                                if (nItem5.getVisibility() == View.GONE){cntr=item;}
                                                else
                                                {
                                                    // service name
                                                    firebaseFirestore.collection("all_services_list")
                                                            .document("item5").get()
                                                            .addOnSuccessListener(
                                                                    new OnSuccessListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                                            String item_title = String.valueOf(documentSnapshot.get("service_title"));
                                                                            if (documentSnapshot.contains("service_title"))
                                                                            {
                                                                                nFeatured_list_title.setText(item_title);

                                                                                // service icon
                                                                                firebaseFirestore.collection("icons")
                                                                                        .document("item5").get()
                                                                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                            @Override
                                                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                                                                String iconLink = String.valueOf(documentSnapshot.get("icon"));
                                                                                                if (documentSnapshot.contains("icon"))
                                                                                                {
                                                                                                    Picasso.get().load(iconLink).into(nFeatured_list_image);
                                                                                                }
                                                                                            }
                                                                                        });
                                                                            }
                                                                        }
                                                                    });
                                                    break;
                                                }
                                        }
                                    }
                                }
                                else
                                {
                                    cntr = 0; // reset timer
                                }

                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }; timer.start();

    }
 */