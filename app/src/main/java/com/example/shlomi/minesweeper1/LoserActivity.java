package com.example.shlomi.minesweeper1;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loser);
        final Intent intentMenu = new Intent(this, ChooseLevelActivity.class);
        final Intent intentReplay = new Intent(this, ChooseLevelActivity.class);

        LinearLayout llMain = new LinearLayout(this);
        llMain.setOrientation(LinearLayout.VERTICAL);
        llMain.setGravity(Gravity.CENTER_HORIZONTAL);
        setContentView(llMain);
        TextView loseLbl = new TextView(this);
        loseLbl.setTextSize(48);
        loseLbl.setText("You Lose!");
        loseLbl.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        llMain.addView(loseLbl);
        LinearLayout llBottom = new LinearLayout(this);
        llBottom.setOrientation(LinearLayout.HORIZONTAL);
        llBottom.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Button replay = new Button(this);
        replay.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        replay.setText("Replay");
        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Button menu = new Button(this);
        menu.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        menu.setText("Menu");
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentMenu);
            }
        });
        llBottom.addView(replay);
        llBottom.addView(menu);
        llBottom.setPadding(0, 800, 0, 0);
        llMain.addView(llBottom);

    }
}
