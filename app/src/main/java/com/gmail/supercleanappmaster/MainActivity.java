package com.gmail.supercleanappmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.gmail.supercleanappmaster.Admin.AdminActivity;
import com.gmail.supercleanappmaster.employees.EmpActivity;
import com.gmail.supercleanappmaster.registerlogin.LoginActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    // FIREBASE
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    // users levels
    boolean employee;
    boolean admin;

    // Drawer + navigation view
    private DrawerLayout nDrawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle nActionBarDrawerToggle;

    // tabs + tab items + view pager + pages adapter
    private TabLayout nTabLayout;
    private ViewPager nViewPager;
    private TabItem firstItem, secondItem, thirdItem;
    private ViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar nToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(nToolbar);

        /* INITIALIZATIONS */
        // Firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        // Slider menu
        nDrawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        // View pager
        nViewPager = findViewById(R.id.viewPager);
        // Tabs
        nTabLayout = findViewById(R.id.tabLayout);
        firstItem = findViewById(R.id.firstItem);
        //secondItem = findViewById(R.id.secondItem);
        thirdItem = findViewById(R.id.thirdItem);


        // listen to items of navigation when clicked
        navigationView.setNavigationItemSelectedListener(this);
        // add hamburger icon to action bar (nToolbar)
        nActionBarDrawerToggle = new ActionBarDrawerToggle(this, nDrawerLayout, nToolbar, R.string.open, R.string.close);
        // listen to navigation menu (when open/close)
        nDrawerLayout.addDrawerListener(nActionBarDrawerToggle);
        // hamburger icon enabled
        nActionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        // sync the state of navigation menu (open or close)
        nActionBarDrawerToggle.syncState();
        // pager adapter for slide through pages in main activity
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, nTabLayout.getTabCount());
        // setting adapter
        nViewPager.setAdapter(pagerAdapter);
        // tabs settings
        nTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                nViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        nViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(nTabLayout));


    }


    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        updateUI(null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUI(currentUser);
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
        // set email domain levels
        String domainLevel = currentUser.getEmail();
        employee = domainLevel.contains("@emp.com");
        return employee;
    }

    private void updateUI(FirebaseUser currentUser ) {
        if (currentUser == null)
        {
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
        else if(isAdmin())
        {
            Intent adminIntent = new Intent(MainActivity.this, AdminActivity.class);
            startActivity(adminIntent);
            finish();
        }
        else if(isEmployee())
        {
            Intent employeeIntent = new Intent(MainActivity.this, EmpActivity.class);
            startActivity(employeeIntent);
            finish();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        nDrawerLayout.closeDrawer(GravityCompat.START);
        if (item.getItemId() == R.id.profile)
        {
            Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
        }
        if (item.getItemId() == R.id.log_out)
        {
            signOut();
        }
        return false;
    }
}