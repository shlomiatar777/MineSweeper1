package com.example.shlomi.minesweeper1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ChooseLevelActivity extends AppCompatActivity {

    RadioGroup rg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_level);
        final Intent intentPlay = new Intent(this, GameActivity.class);
        final Intent intentHighScore = new Intent(this, ShowHighScoreActivity.class);
        //final Intent intent = new Intent(this, CompassActivity.class);
        final  int scene=1;
      /*  SharedPreferences sp = getSharedPreferences("all highScore lists",0);
        sp.edit().remove("easylist").commit();
        sp.edit().remove("mediumlist").commit();
        sp.edit().remove("hardlist").commit();*/

    //  screen size parameters
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

    // the main layout of this activity
        LinearLayout ll =(LinearLayout) findViewById(R.id.mainLayout);
        ll.setGravity(Gravity.CENTER_HORIZONTAL);
        Drawable image = ContextCompat.getDrawable(this,R.drawable.parket);
        ll.setBackgroundDrawable(image);

        TextView title = new TextView(this);
        title.setText(R.string.app_name);
        title.setTextSize(height/50);
        title.setTextColor(Color.WHITE);
        title.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        ll.addView(title);

        LinearLayout llChooseLvl = new LinearLayout(this);
        llChooseLvl.setOrientation(LinearLayout.HORIZONTAL);
        llChooseLvl.setPadding(0,height/6,0,0);

        LinearLayout llScores = new LinearLayout(this);
;       chooseLevelByGroupBtn(height/70);
        llChooseLvl.addView(rg);
        llChooseLvl.addView(llScores);
        ll.addView(llChooseLvl);

        LinearLayout llPlay =new LinearLayout(this);
        llPlay.setGravity(Gravity.CENTER_HORIZONTAL);
        llPlay.setPadding(0,height/5,0,0);

        Button play = new Button(this);
        play.setText(R.string.play);
        play.setTextSize(height/60);
        play.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View v) {
                int rows,cols,bombs;
                int index = rg.indexOfChild(findViewById(rg.getCheckedRadioButtonId()));
                if (index==0){
                    rows =10;
                    cols= 10;
                    bombs = 5;
                }
                else if (index==1){
                    rows=10;
                    cols=10;
                    bombs=10;
                }
                else {
                    rows = 5;
                    cols= 5;
                    bombs=10;
                }
                intentPlay.putExtra("rows",rows);
                intentPlay.putExtra("cols" , cols);
                intentPlay.putExtra("bombsNum", bombs);
                intentPlay.putExtra("levelName",index);
                startActivityForResult(intentPlay,scene);
            }
        });
        play.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        llPlay.addView(play);


        Button highScore = new Button(this);
        highScore.setText(R.string.HighScore);
        highScore.setTextSize(height/60);
        highScore.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                int index = rg.indexOfChild(findViewById(rg.getCheckedRadioButtonId()));
                if (index==0)
                    intentHighScore.putExtra("level","easy");

                else if (index==1)
                    intentHighScore.putExtra("level","medium");

                else
                    intentHighScore.putExtra("level","hard");

                startActivityForResult(intentHighScore,scene);
            }


        });
        highScore.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        llPlay.addView(highScore);

       ll.addView(llPlay);
    }



    // make RadioGroup with all levels
     public void chooseLevelByGroupBtn(int size){
         RadioButton rbEasy = makeRadionBtnOption(size, "Easy");
         rbEasy.setTextColor(Color.WHITE);
         RadioButton rbMedium = makeRadionBtnOption(size, "Medium");
         rbMedium.setTextColor(Color.WHITE);
         RadioButton rbHard = makeRadionBtnOption(size, "Hard");
         rbHard.setTextColor(Color.WHITE);
         rg = new RadioGroup(this);
         rg. addView(rbEasy);
         rg.addView(rbMedium);
         rg.addView((rbHard));
         rbEasy.setChecked(true); 
     }

    // make RadioButton for each level
    public RadioButton makeRadionBtnOption(int size, String lbl){
        RadioButton rbTemp = new RadioButton(this);
        rbTemp.setTextSize(size);
        rbTemp.setText(lbl);
        return rbTemp;
    }
 
}
