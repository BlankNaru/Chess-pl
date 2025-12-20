package com.example.chesspl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chesspl.R;
import com.example.chesspl.activities.recyclerViewsClasses.PlayersAdapter;
import com.example.chesspl.activities.recyclerViewsClasses.User;
import com.example.chesspl.login.ApiClient;
import com.example.chesspl.login.AuthApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_list_layout);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        new DrawerHelper(this, drawerLayout, findViewById(R.id.nav_view));

        RecyclerView recycler = findViewById(R.id.friendsList);
        PlayersAdapter adapter = new PlayersAdapter();
        adapter.setOnItemClickListener(position -> {
            User clicked = adapter.getPlayers().get(position);
            Intent intent = new Intent(FriendListActivity.this, AccountActivity.class);
            intent.putExtra("username", clicked.getUsername());
            startActivity(intent);
        });
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

        AuthApi api = ApiClient.getAuthApi(this);
        String username = getIntent().getStringExtra("username");


        api.getFriendsOf(username).enqueue(new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Map<String, Object>> players = response.body();

                    List<User> serverPlayers = new ArrayList<>();

                    for (Map<String, Object> p : players)
                    {
                        User u = new User(p.get("username").toString(), (int) Double.parseDouble(p.get("elo").toString()));
                        serverPlayers.add(u);
                    }

                    adapter.updateData(serverPlayers);
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                Toast.makeText(FriendListActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
