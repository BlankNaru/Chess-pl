package com.example.chesspl.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.chesspl.R;
import com.example.chesspl.chessClasses.onlineGameClasses.OnlineGameHelper;
import com.google.android.material.button.MaterialButtonToggleGroup;

public class OnlineGameSearchActivity  extends AppCompatActivity {

    String time = "10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        new DrawerHelper(this, drawerLayout, findViewById(R.id.nav_view));

        MaterialButtonToggleGroup toggleGroup = findViewById(R.id.segment_temp);
        toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked) {
                    if (checkedId == R.id.option1) {
                        time = "1";
                    } else if (checkedId == R.id.option5) {
                        time = "5";
                    } else if (checkedId == R.id.option10) {
                        time = "10";
                    }
                }
            }
        });

        Button searchButton = findViewById(R.id.submitButton);
        searchButton.setOnClickListener(v ->{
            View view = getLayoutInflater().inflate(R.layout.popup_search, null);
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setView(view)
                    .setCancelable(false)
                    .create();

            dialog.show();
            OnlineGameHelper onlineGameHelper = new OnlineGameHelper();
            WebSocketManager.getInstance().setHelper(onlineGameHelper);
            onlineGameHelper.setContext(this);

            view.findViewById(R.id.cancelBtn).setOnClickListener(v1 -> {
                dialog.dismiss();
                onlineGameHelper.disconnect();
            });


            onlineGameHelper.connect();
            onlineGameHelper.setOnColorDefinedListener(color -> {
                dialog.dismiss();
                Intent intent = new Intent(OnlineGameSearchActivity.this, OnlineGameActivity.class);
                intent.putExtra("time", time);
                startActivity(intent);
            });
        });

    }

}
