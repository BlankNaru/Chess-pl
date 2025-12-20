package com.example.chesspl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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

public class SearchPlayerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_player_layout);
        new DrawerHelper(this, findViewById(R.id.drawer_layout), findViewById(R.id.nav_view));

        RecyclerView recycler = findViewById(R.id.playersList);
        PlayersAdapter adapter = new PlayersAdapter();
        adapter.setOnItemClickListener(position -> {
            User clicked = adapter.getPlayers().get(position);
            Intent intent = new Intent(SearchPlayerActivity.this, AccountActivity.class);
            intent.putExtra("username", clicked.getUsername());
            startActivity(intent);
        });
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);


        Button search = findViewById(R.id.searchButton);
        EditText searchingBar = findViewById(R.id.searchInput);
        search.setOnClickListener(v -> {

            AuthApi api = ApiClient.getAuthApi(this);

            api.getPlayers(searchingBar.getText().toString()).enqueue(new Callback<List<Map<String, Object>>>() {
                @Override
                public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Map<String, Object>> players = response.body();

                        List<User> serverPlayers = new ArrayList<>();

                        for (Map<String, Object> p : players)
                        {
                            User u = new User(p.get("username").toString(), (int) Double.parseDouble(p.get("elo").toString()));
                            if(!u.getUsername().equals(getSharedPreferences("prefs", MODE_PRIVATE).getString("username", "")))
                                serverPlayers.add(u);
                        }

                        adapter.updateData(serverPlayers);
                    }
                }

                @Override
                public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                    Toast.makeText(SearchPlayerActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
                }
            });
        });

    }
}
