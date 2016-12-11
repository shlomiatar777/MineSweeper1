package com.example.shlomi.minesweeper1;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import Logic.Board;

public class GameActivity extends AppCompatActivity {

    private TextView timer, bombs;
    Board theBoard;
    TableLayout UI_Board;
    boolean isFlagUI=false;
    int rows, cols,  bombsNum, levelType;
    private int countSeconds= 0 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
// load parameters from other activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        rows= getIntent().getIntExtra("rows",1);
        cols = getIntent().getIntExtra("cols",1);
        bombsNum = getIntent().getIntExtra("bombsNum",1);
        levelType= getIntent().getIntExtra("levelName",-1);
        theBoard = new Board(rows,cols,bombsNum);
        theBoard.initLogicBoard();

//  screen size parameters
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

//main LayOut of this Activity
        LinearLayout llGame =  new LinearLayout(this);
        llGame.setOrientation(LinearLayout.VERTICAL);
        llGame.setGravity(Gravity.CENTER_HORIZONTAL);
        setContentView(llGame);
        Drawable backGround = ContextCompat.getDrawable(this,R.drawable.parket);
        llGame.setBackgroundDrawable(backGround);

        LinearLayout llTop= new LinearLayout(this);
        makeParamsGame(llTop,height,width);
        llGame.addView(llTop);
// make the tiles board
        UI_Board =initTileBoard(rows,cols,height,width);
        UI_Board.setPadding(0,height/20,0,0);
        UI_Board.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        llGame.addView(UI_Board);

        LinearLayout llBottom= new LinearLayout(this);
        makeOptionsGame(llBottom,height);
        llGame.addView(llBottom);
    }

//update the time's TextView
    private void updateTime() {
        new Thread(){


            public void run() {
                while (true) {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timer.setText(" Timer: "+countSeconds);
                                countSeconds++;
                            }
                        });
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    // initialize the board of Tiles
    public TableLayout initTileBoard (int row, int col,int height, int width){

        final TableLayout tileBoard= new TableLayout(this);
        for (int i=0; i<row; i++){
            TableRow allRow= new TableRow(this);
            for ( int j=0 ; j<col; j++){
                Button index=new Button(this);
                index.setId(i*col+j);
                final int heightBtn = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,(int) (0.21*height/row) , getResources().getDisplayMetrics());
                final int widthBtn = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,(int) (0.33*width/col), getResources().getDisplayMetrics());
                index.setTextSize(heightBtn/7);

                //Resize the image to fit in the Button size
                Drawable fullTile = ContextCompat.getDrawable(this, R.drawable.full);
                Bitmap b = ((BitmapDrawable)fullTile).getBitmap();
                Bitmap bResaize = Bitmap.createScaledBitmap(b,heightBtn,widthBtn,false);
                Drawable d = new BitmapDrawable(getResources(),bResaize);
                index.setBackgroundDrawable(d);

                //keep the unCoverd backgrond in case i press on flag and then unflag
                final Drawable old = index.getBackground();

                index.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //get the current status of the game (1: win , -1: lose , 0: still play)
                        int resoult =theBoard.makeStep(v.getId(),isFlagUI);
                        if (isFlagUI)
                            makeFlag(v,heightBtn,widthBtn,old);
                        else
                            makeDicover(v, resoult,heightBtn,widthBtn, old);
                    }
                });
                index.setLayoutParams(new TableRow.LayoutParams(widthBtn, heightBtn));
                allRow.addView(index);
            }

            tileBoard.addView(allRow);
        }
        return  tileBoard;
    }
// show the current time pass and the num of optional bombs
    public void makeParamsGame(LinearLayout llTop,int height, int width){
        llTop.setOrientation(LinearLayout.HORIZONTAL);
        llTop.setGravity(Gravity.CENTER_HORIZONTAL);
        timer=  new TextView(this);
        timer.setTextSize(height/50);
        timer.setTextColor(Color.RED);
        timer.setBackgroundColor(Color.BLACK);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/digital-7.ttf");
        timer.setTypeface(face);
        llTop.addView(timer);
        updateTime();
        bombs = new TextView(this);
        bombs.setTextSize(height/50);
        bombs.setTextColor(Color.RED);
        bombs.setBackgroundColor(Color.BLACK);
        bombs.setTypeface(face);
        bombs.setText("bombs: "+ theBoard.getBombs());
        bombs.setPadding(width/8,0,0,0);
        llTop.addView(bombs);
    }

    //the options of the game (discover Tile , make a flag or quit)
    public void  makeOptionsGame(LinearLayout llBottom,int height){
        llBottom.setOrientation(LinearLayout.HORIZONTAL);
        llBottom.setGravity(Gravity.CENTER_HORIZONTAL);
        llBottom.setPadding(0,height/20,0,0);

        Button flags = new Button(this);
        flags.setTextSize(height/70);
        flags.setText("Flags");
        flags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFlagUI=true;
            }
        });
        Button dicover = new Button(this);
        dicover.setTextSize(height/70);
        dicover.setText("Discover");
        dicover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFlagUI=false;
            }
        });
        Button quit = new Button(this);
        quit.setTextSize(height/70);
        quit.setText("Quit");
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToMenu();
            }
        });

        llBottom.addView(flags);
        llBottom.addView(dicover);
        llBottom.addView(quit);
    }

    //make a flag in case it valid step
    public void makeFlag(View v , int height, int width,Drawable old){
        int indexY= v.getId()%cols;
        int indexX = v.getId()/cols;
        if (theBoard.getAllTiles()[indexX][indexY].isCover()) {
            if (theBoard.getAllTiles()[indexX][indexY].isFlag()) {
               Drawable flag = ContextCompat.getDrawable(this, R.drawable.flag);
                Bitmap b = ((BitmapDrawable)flag).getBitmap();
                Bitmap bResaize = Bitmap.createScaledBitmap(b,height,width,false);
                Drawable d = new BitmapDrawable(getResources(),bResaize);
                ((Button) ((TableRow) UI_Board.getChildAt(indexX)).getChildAt(indexY)).setBackgroundDrawable(d);
            }
            else
                ((Button) ((TableRow) UI_Board.getChildAt(indexX)).getChildAt(indexY)).setBackground(old);

            bombs.setText("Bombs: "+(theBoard.getBombs()+theBoard.getNumOfFlagsLogic()));
        }
    }


    //discover a Tile in case it valid step and check the current status of game (1: win , -1: lose , 0: still play)
    public void makeDicover(View v, int resoult,int heightBtn, int widthBtn,Drawable old){
        if (resoult==1) {
            final Intent intent = new Intent(this,WinnerActivity.class);
            final  int scene=2;
            boolean isBestScore=false;

            // save the best score
            SharedPreferences settings =  getSharedPreferences("allBestScores",0);
            SharedPreferences.Editor editor = settings.edit();
            int bestScore= settings.getInt("LevelName "+levelType,0);
            if (bestScore == 0 || bestScore > countSeconds) {
                bestScore = countSeconds;
                isBestScore=true;
            }
            intent.putExtra("bestScore",isBestScore);
            editor.putInt("LevelName "+levelType,bestScore);
            editor.commit();

            startActivityForResult(intent,scene);
        }
        else if (resoult==-1) {
            final Intent intent = new Intent(this, LoserActivity.class);
            final int scene = 2;
            startActivityForResult(intent, scene);
        }
        else{
            for (int i = 0 ; i < theBoard.getRows() ; i ++){
                for (int j = 0 ; j < theBoard.getCols() ; j ++){
                    if (!theBoard.getAllTiles()[i][j].isCover()){
                        if (theBoard.getAllTiles()[i][j].getType()==10)
                            ((Button) ((TableRow) UI_Board.getChildAt(i)).getChildAt(j)).setText("X");
                        else
                            updateNumOfBombs(i,j,heightBtn,widthBtn);
                    }
                }
            }
        }
    }

    //the quit option
    public  void returnToMenu(){
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(GameActivity.this);
        quitDialog.setMessage("Are you sure you want return to menu?");
        quitDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GameActivity.this.finish();
            }
        });

        quitDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = quitDialog.create();
        alert.show();
    }
    //update the type and color of each discover Tale
    public void updateNumOfBombs(int x,int y, int heightBtn, int widthBtn){
        Drawable emptyTile = ContextCompat.getDrawable(this, R.drawable.empty);
        Bitmap b = ((BitmapDrawable)emptyTile).getBitmap();
        Bitmap bResaize = Bitmap.createScaledBitmap(b,heightBtn,widthBtn,false);
        Drawable d = new BitmapDrawable(getResources(),bResaize);
        ((Button) ((TableRow) UI_Board.getChildAt(x)).getChildAt(y)).setBackgroundDrawable(d);

        int type= theBoard.getAllTiles()[x][y].getType();
        if (type==1)
            ((Button)((TableRow) UI_Board.getChildAt(x)).getChildAt(y)).setTextColor(Color.BLUE);
        if (type==2)
            ((Button)((TableRow) UI_Board.getChildAt(x)).getChildAt(y)).setTextColor(Color.rgb(20,100,40));
        if (type==3)
            ((Button)((TableRow) UI_Board.getChildAt(x)).getChildAt(y)).setTextColor(Color.RED);
        if (type==4)
            ((Button)((TableRow) UI_Board.getChildAt(x)).getChildAt(y)).setTextColor(Color.MAGENTA);
        if (type==5)
            ((Button)((TableRow) UI_Board.getChildAt(x)).getChildAt(y)).setTextColor(Color.CYAN);
        if (type==6)
            ((Button)((TableRow) UI_Board.getChildAt(x)).getChildAt(y)).setTextColor(Color.YELLOW);
        if (type==7)
            ((Button)((TableRow) UI_Board.getChildAt(x)).getChildAt(y)).setTextColor(Color.GRAY);
        if (type==8)
            ((Button)((TableRow) UI_Board.getChildAt(x)).getChildAt(y)).setTextColor(Color.rgb(170,170,40) );
        if(type!=0)
         ((Button)((TableRow) UI_Board.getChildAt(x)).getChildAt(y)).setText("" + theBoard.getAllTiles()[x][y].getType());
    }

    @Override
    public void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}

