package com.example.shlomi.minesweeper1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import Logic.Winner;

public class WinnerActivity extends AppCompatActivity {
    SharedPreferences sp;
    ArrayList<Winner> theList;
    Winner theWinner;
    String listStr;
    String level;
    EditText winnerName;
    static boolean isBestScore= false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);

        //  screen size parameters
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        final Intent intentMenu=new Intent(this, ChooseLevelActivity.class);
        String winnerAsStr = getIntent().getStringExtra("winnerToGson");
        level = getIntent().getStringExtra("levelName");
        theWinner = new Gson().fromJson(winnerAsStr,Winner.class);
        sp=getSharedPreferences("all highScore lists", Context.MODE_PRIVATE);
        listStr= sp.getString(level+"list","");
        theList = new ArrayList<Winner>();
        Comparator<Winner> sortByScore = new Comparator<Winner>() {
            @Override
            public int compare(Winner o1, Winner o2) {
                return o1.getScore()-o2.getScore();
            }
        };
        if(!listStr.equals(""))
        {
            Type type = new TypeToken<ArrayList<Winner>>(){}.getType();
            theList = new Gson().fromJson(listStr, type);
        }

        if (theList.size()<10) {
            theList.add(theWinner);
            isBestScore=true;
            Collections.sort(theList,sortByScore);
        }

        else if( theList.get(theList.size()-1).getScore()> theWinner.getScore()){
            theList.set(theList.size()-1,theWinner);
              isBestScore=true;
            Collections.sort(theList,sortByScore);
        }




        //  main LayOut of this Activity
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


        String currentWin= getIntent().getStringExtra("win");

        if (isBestScore) {
            winLbl.setText(R.string.new_score);
            winnerName= new EditText(this);
            winnerName.setTextColor(Color.WHITE);
            winnerName.setTextSize(height/70);
            llMain.addView(winnerName);
        }
        else
            winLbl.setText(R.string.you_win);
        winLbl.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        llMain.addView(winLbl);

        LinearLayout llBottom = new LinearLayout(this);
        llBottom.setOrientation(LinearLayout.HORIZONTAL);
        llBottom.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Button replay = new Button(this);
        replay.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        replay.setText(R.string.replay);
        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBestScore){
                    theWinner.setName(winnerName.getText()+"");
                    SharedPreferences.Editor editor = sp.edit();
                     listStr = new Gson().toJson(theList);
                    editor.putString(level+"list",listStr);
                    editor.commit();
                }
                finish();
            }
        });

        final Button menu = new Button(this);
        menu.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        menu.setText(R.string.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBestScore){
                    theWinner.setName(winnerName.getText()+"");
                    SharedPreferences.Editor editor = sp.edit();
                    listStr = new Gson().toJson(theList);
                    editor.putString(level+"list",listStr);
                    editor.commit();
                }
                intentMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentMenu);
            }
        });
        llBottom.addView(replay);
        llBottom.addView(menu);
        llBottom.setPadding(0,height/8,0,0);
        llMain.addView(llBottom);

        
    }
}

