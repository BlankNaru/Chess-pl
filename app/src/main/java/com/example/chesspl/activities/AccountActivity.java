package com.example.chesspl.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.chesspl.R;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_layout);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        DrawerHelper drawerHelper = new DrawerHelper(this, drawerLayout, findViewById(R.id.nav_view));



        findViewById(R.id.menuIcon).setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

    }
}
