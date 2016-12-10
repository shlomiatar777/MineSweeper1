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


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;


        LinearLayout ll =(LinearLayout) findViewById(R.id.mainLayout);
        ll.setGravity(Gravity.CENTER_HORIZONTAL);
        Drawable image = ContextCompat.getDrawable(this,R.drawable.parket);
        ll.setBackgroundDrawable(image);

        TextView title = new TextView(this);
        title.setText("Mine Sweeper!");
        title.setTextSize(height/50);
        title.setTextColor(Color.WHITE);
        title.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        ll.addView(title);

        LinearLayout llChooseLvl = new LinearLayout(this);
        llChooseLvl.setOrientation(LinearLayout.HORIZONTAL);
        llChooseLvl.setPadding(0,height/6,0,0);
        LinearLayout llScores = new LinearLayout(this);
        makeScoreLayOut(llScores,height/70);
;       chooseLevelByGroupBtn(height/70);
        llChooseLvl.addView(rg);
        llChooseLvl.addView(llScores);
        ll.addView(llChooseLvl);

        LinearLayout llPlay =new LinearLayout(this);
        llPlay.setGravity(Gravity.CENTER_HORIZONTAL);
        llPlay.setPadding(0,height/5,0,0);
        Button play = new Button(this);
        play.setText("Play");
        play.setTextSize(height/60);
        final Intent intent = new Intent(this, GameActivity.class);
        final  int scene=1;
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
                intent.putExtra("rows",rows);
                intent.putExtra("cols" , cols);
                intent.putExtra("bombsNum", bombs);
                intent.putExtra("levelName",index);
                startActivityForResult(intent,scene);
            }
        });
        play.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        llPlay.addView(play);

       ll.addView(llPlay);
    }

    public void makeScoreLayOut(LinearLayout llScores,int size){

        SharedPreferences settings =  getSharedPreferences("allBestScores",0);
        SharedPreferences.Editor editor = settings.edit();
        int easyScore = settings.getInt("LevelName "+0,0);
        int mediumScore =  settings.getInt("LevelName "+1,0);
        int hardScore =  settings.getInt("LevelName "+2,0);

        llScores.setOrientation(LinearLayout.VERTICAL);
        TextView easyScoreLbl= new TextView(this);
        easyScoreLbl.setTextSize(size);
        easyScoreLbl.setText("      Score: "+ easyScore );
        easyScoreLbl.setTextColor(Color.WHITE);
        TextView mediumScoreLbl= new TextView(this);
        mediumScoreLbl.setTextSize(size);
        mediumScoreLbl.setText("      Score: "+ mediumScore );
        mediumScoreLbl.setTextColor(Color.WHITE);
        TextView hardScoreLbl= new TextView(this);
        hardScoreLbl.setTextSize(size);
        hardScoreLbl.setText("      Score: "+ hardScore );
        hardScoreLbl.setTextColor(Color.WHITE);
        llScores.addView(easyScoreLbl);
        llScores.addView(mediumScoreLbl);
        llScores.addView(hardScoreLbl);
    }

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
    
    public RadioButton makeRadionBtnOption(int size, String lbl){
        RadioButton rbTemp = new RadioButton(this);
        rbTemp.setTextSize(size);
        rbTemp.setText(lbl);
        return rbTemp;
    }
 
}
