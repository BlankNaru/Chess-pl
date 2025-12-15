package com.example.chesspl.activities;

import android.app.Activity;
import android.content.Intent;

import androidx.drawerlayout.widget.DrawerLayout;

import com.example.chesspl.R;
import com.google.android.material.navigation.NavigationView;

public class DrawerHelper {

    private Activity activity;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    public DrawerHelper(Activity activity, DrawerLayout drawerLayout, NavigationView navigationView) {
        this.activity = activity;
        this.drawerLayout = drawerLayout;
        this.navigationView = navigationView;
        setupDrawer();
    }

    private void setupDrawer() {
        navigationView.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_account) {
                navigateTo(AccountActivity.class);
            } else if (id == R.id.nav_settings) {
                navigateTo(LocalGameActivity.class);
            } else if (id == R.id.nav_search_player) {
                navigateTo(SearchPlayerActivity.class);
            } else if (id == R.id.nav_game_offline) {
                navigateTo(LocalGameActivity.class);
            } else if (id == R.id.nav_game_online) {
                navigateTo(OnlineGameSearchActivity.class);
            }

            drawerLayout.closeDrawer(androidx.core.view.GravityCompat.START);
            return true;
        });
    }


    private void navigateTo(Class<?> targetActivity) {
        Intent intent = new Intent(activity, targetActivity);
        if (activity instanceof AccountActivity)
            intent.putExtra("username", "");
        activity.startActivity(intent);

    }


    public void openDrawer() {
        drawerLayout.openDrawer(androidx.core.view.GravityCompat.START);
    }

    public void closeDrawer() {
        drawerLayout.closeDrawer(androidx.core.view.GravityCompat.START);
    }
}
