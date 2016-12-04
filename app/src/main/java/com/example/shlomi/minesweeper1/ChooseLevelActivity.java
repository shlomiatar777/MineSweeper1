package com.example.shlomi.minesweeper1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        LinearLayout ll =(LinearLayout) findViewById(R.id.mainLayout);




        TextView title = new TextView(this);
        title.setText("Shlomi");
        title.setTextSize(36);
        title.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        ll.setGravity(Gravity.CENTER_HORIZONTAL);
        ll.addView(title);
        final LinearLayout llEasy=makeLevel("Easy");
        ll.addView(llEasy);
        LinearLayout llMedium=makeLevel("Medium");
        ll.addView(llMedium);
        LinearLayout llHard=makeLevel("Hard");
        ll.addView(llHard);
        Button play = new Button(this);
        play.setText("Play");
        play.setTextSize(30);
        final Intent intent = new Intent(this, GameActivity.class);
        final  int scene=1;
        RadioButton rbEasy = new RadioButton(this);
        rbEasy.setTextSize(24);
        rbEasy.setText("Easy");
        RadioButton rbMedium = new RadioButton(this);
        rbMedium.setTextSize(24);
        rbMedium.setText("Medium");
        RadioButton rbHard = new RadioButton(this);
        rbHard.setTextSize(24);
        rbHard.setText("Hard");
        rg = new RadioGroup(this);
        rg. addView(rbEasy);
        rg.addView(rbMedium);
        rg.addView((rbHard));
        rbEasy.setChecked(true);
        ll.addView(rg);
        play.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View v) {
                int rows,cols,bombs;
                int index = rg.indexOfChild(findViewById(rg.getCheckedRadioButtonId()));
                if (index==0){
                    rows =5;
                    cols= 5;
                    bombs = 5;
                }
                else if (index==1){
                    rows=7;
                    cols=7;
                    bombs=7;
                }
                else {
                    rows = 10;
                    cols= 10;
                    bombs=10;
                }
                intent.putExtra("rows",rows);
                intent.putExtra("cols" , cols);
                intent.putExtra("bombsNum", bombs);
                intent.putExtra("levelName",index);
                startActivityForResult(intent,scene);
            }
        });
        ll.addView(play);
        SharedPreferences settings =  getSharedPreferences("sllBestScores",0);
        SharedPreferences.Editor editor = settings.edit();

    }

    public LinearLayout makeLevel(String levelName){
        LinearLayout temp = new LinearLayout(this);
        temp.setOrientation(LinearLayout.HORIZONTAL);
        RadioButton rb = new RadioButton(this);
        TextView LabelLevel= new TextView(this);
        LabelLevel.setText(levelName);
        LabelLevel.setTextSize(30);
        TextView scoreLable= new TextView(this);
        scoreLable.setText("      Score: "+ 0);
        scoreLable.setTextSize(24);
        temp.addView(rb);
        temp.addView(LabelLevel);
        temp.addView(scoreLable);
        return temp;
    }
}
