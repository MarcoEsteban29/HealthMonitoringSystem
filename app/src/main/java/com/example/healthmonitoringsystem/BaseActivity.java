package com.example.healthmonitoringsystem;



import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class BaseActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    ImageView imageView;
    TextView tvName;
    String avatarURL,name,email,cp,age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View nView = navigationView.getHeaderView(0);
        SharedPreferences preferences = getSharedPreferences("Information",MODE_PRIVATE);
        Uri uri  = Uri.parse("android.resource://"+this.getPackageName()+"/"+R.drawable.logo);
        avatarURL = preferences.getString("URI",uri.toString());
        name = preferences.getString("name","");
        email = preferences.getString("email","");
        cp = preferences.getString("cp","");
        age = preferences.getString("age","");
        imageView = nView.findViewById(R.id.ProfileimageView);
        tvName = (TextView) nView.findViewById(R.id.NameNall);
        tvName.setText(name+"\n"+email+"\n"+cp+"\n"+age);
        Picasso.with(this).load(avatarURL).into(imageView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(BaseActivity.this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                final String appPackageName = getPackageName();

                switch (item.getItemId()) {

                    case R.id.nav_dashboard:
                        Intent dash = new Intent(getApplicationContext(), MonitorActivity.class);
                        startActivity(dash);
                        finish();
                        drawerLayout.closeDrawers();
                        break;


                    case R.id.nav_about_us:
                        Intent anIntent = new Intent(getApplicationContext(), LineFollowerActivity.class);
                        startActivity(anIntent);
                        finish();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_chatbot:
                        Intent aIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(aIntent);
                        finish();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_temp:
                        Intent intent = new Intent(getApplicationContext(), Temperature.class);
                        startActivity(intent);
                        finish();
                        drawerLayout.closeDrawers();
                        break;
                }
                return false;
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        actionBarDrawerToggle.syncState();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransitionExit();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransitionEnter();
    }


    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
