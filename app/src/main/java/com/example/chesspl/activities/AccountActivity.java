package com.example.chesspl.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.chesspl.R;
import com.example.chesspl.login.ApiClient;
import com.example.chesspl.login.AuthApi;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_layout);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        new DrawerHelper(this, drawerLayout, findViewById(R.id.nav_view));

        findViewById(R.id.menuIcon).setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

        TextView usernameTextView = findViewById(R.id.nicknameText);
        TextView eloTextView = findViewById(R.id.stat1);
        TextView gamesPlayedTextView = findViewById(R.id.stat2);


        AuthApi api = ApiClient.getAuthApi(this);
        String username;
        String tempUsername = getIntent().getStringExtra("username");
        if(tempUsername.isEmpty())
            username = getSharedPreferences("prefs", MODE_PRIVATE).getString("username", "");
        else
            username = tempUsername;

        Button goToFriends = findViewById(R.id.friendsListButton);
        goToFriends.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, FriendListActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        Button addFriendButton = findViewById(R.id.addFriendButton);
        assert username != null;
        if (username.equals(getSharedPreferences("prefs", MODE_PRIVATE).getString("username", "")))
            addFriendButton.setVisibility(View.GONE);
        else
            isUserFriend(api, username, getSharedPreferences("prefs", MODE_PRIVATE).getString("username", ""), this);

        api.getUserByUsername(username).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    usernameTextView.setText(username);
                    Number elo = (Number) response.body().get("elo");
                    eloTextView.setText(elo.toString().substring(0, elo.toString().indexOf('.')));
                    Number gamesPlayed = (Number) response.body().get("gamesPlayed");
                    gamesPlayedTextView.setText(gamesPlayed.toString().substring(0, gamesPlayed.toString().indexOf('.')));

                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(AccountActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void isUserFriend(AuthApi api, String username1, String username2, Activity activity) {
        api.isUserFriend(username1, username2).enqueue(new Callback<Map<String, Object>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Button addFriendButton = findViewById(R.id.addFriendButton);
                    Boolean arefFriends = (Boolean) response.body().get("friends");
                    if (Boolean.FALSE.equals(arefFriends))
                    {
                        addFriendButton.setOnClickListener(v -> addFriend(api, username1, username2, activity));
                        return;
                    }
                    addFriendButton.setBackgroundColor(ContextCompat.getColor(activity, R.color.dark_gray));
                    addFriendButton.setTextColor(ContextCompat.getColor(activity, R.color.gray));
                    addFriendButton.setText("Znajomy");
                    addFriendButton.setOnClickListener(null);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(AccountActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addFriend(AuthApi api, String username1, String username2, Activity activity)
    {
        api.addFriend(username1, username2).enqueue(new Callback<Map<String, Object>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Button addFriendButton = findViewById(R.id.addFriendButton);
                    addFriendButton.setBackgroundColor(ContextCompat.getColor(activity, R.color.dark_gray));
                    addFriendButton.setTextColor(ContextCompat.getColor(activity, R.color.gray));
                    addFriendButton.setText("Znajomy");
                    addFriendButton.setOnClickListener(null);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(AccountActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
