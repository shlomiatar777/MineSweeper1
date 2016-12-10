package com.example.shlomi.minesweeper1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WinnerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        final Intent intentMenu=new Intent(this, ChooseLevelActivity.class);
        final Intent intentReplay=new Intent(this, ChooseLevelActivity.class);
        boolean isBestScore= getIntent().getBooleanExtra("bestScore",false);

        LinearLayout llMain = new LinearLayout(this);
        llMain.setOrientation(LinearLayout.VERTICAL);
        llMain.setGravity(Gravity.CENTER_HORIZONTAL);
        Drawable backGround = ContextCompat.getDrawable(this,R.drawable.parket);
        llMain.setBackgroundDrawable(backGround);
        setContentView(llMain);

        ImageView smily = new ImageView(this);
        smily.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Drawable happySmily = ContextCompat.getDrawable(this, R.drawable.happy);
        Bitmap b = ((BitmapDrawable)happySmily).getBitmap();
        Bitmap bResaize = Bitmap.createScaledBitmap(b,width/2,height/3,false);
        Drawable d = new BitmapDrawable(getResources(),bResaize);
        smily.setBackgroundDrawable(d);
        llMain.addView(smily);
        TextView winLbl = new TextView(this);
        winLbl.setTextSize(48);
        winLbl.setTextColor(Color.WHITE);
        if (isBestScore)
            winLbl.setText("You win! new Score");
        else
            winLbl.setText("You win!");
        winLbl.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        llMain.addView(winLbl);
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
        llBottom.setPadding(0,height/4,0,0);
        llMain.addView(llBottom);

        
    }


}

