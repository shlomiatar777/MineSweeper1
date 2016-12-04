package com.example.shlomi.minesweeper1;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
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

    //private GoogleApiClient client;
    private TextView timer;
    Board theBoard;
    TableLayout UI_Board;
    boolean isFlagUI=false;
    TextView bombs;
    int numOfFlags=0;
    int rows, cols,  bombsNum, leveltType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        rows= getIntent().getIntExtra("rows",1);
        cols = getIntent().getIntExtra("cols",1);
        bombsNum = getIntent().getIntExtra("bombsNum",1);
        leveltType= getIntent().getIntExtra("levelName",1);
        theBoard = new Board(rows,cols,bombsNum);
        LinearLayout llGame =  new LinearLayout(this);
        llGame.setOrientation(LinearLayout.VERTICAL);
        GridLayout glTblGame = new GridLayout(this);
        /*TextView timeLbl = new TextView(this);
        timeLbl.setText("Time :");
        timeLbl.setTextSize(24);*/
        LinearLayout llTop= new LinearLayout(this);
        LinearLayout llCenter= new LinearLayout(this);
        LinearLayout llBottom= new LinearLayout(this);
        llTop.setOrientation(LinearLayout.HORIZONTAL);
        timer=  new TextView(this);
        timer.setTextSize(24);
        //llGame.addView(timeLbl);
        llTop.addView(timer);
        setContentView(llGame);
        runThread();
        bombs = new TextView(this);
        bombs.setTextSize(24);
        bombs.setText("bombs: "+ theBoard.getBombs());
        // llGame.addView(bombs);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        bombs.setPadding(350,0,0,0);
        llTop.addView(bombs);
        llGame.addView(llTop);

        theBoard.initLogicBoard();
        UI_Board =initTileBoard(rows,cols);
        UI_Board.setPadding(0,50,0,0);
        UI_Board.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        llCenter.setGravity(Gravity.CENTER_HORIZONTAL);
        llGame.addView(UI_Board);
        llGame.addView(llCenter);
        Button flags = new Button(this);
        flags.setTextSize(24);
        flags.setText("Flags");
        flags.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        flags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFlagUI=true;
            }
        });
        // flags.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        Button dicover = new Button(this);
        dicover.setTextSize(24);
        dicover.setText("Discover");
        dicover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFlagUI=false;
            }
        });
        Button quit = new Button(this);
        quit.setTextSize(24);
        quit.setText("Quit");
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToMenu();
            }
        });
        llBottom.setOrientation(LinearLayout.HORIZONTAL);
        llBottom.setPadding(0,100,0,0);
        quit.setPadding(200,0,0,0);
        llBottom.addView(flags);
        llBottom.addView(dicover);
        llBottom.addView(quit);
        llGame.addView(llBottom);




    }


    private void runThread() {
        new Thread(){

            private int countSeconds= 0 ;
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

    public TableLayout initTileBoard (int row, int col){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int with =size.x;
        int high =size.y;
        final TableLayout tileBoard= new TableLayout(this);
        for (int i=0; i<row; i++){
            TableRow allRow= new TableRow(this);
            for ( int j=0 ; j<col; j++){
                Button index=new Button(this);
                index.setId(i*col+j);
                //index.setText(""+theBoard.getAllTiles()[i][j].getType());
                index.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //   int resoult =-1;
                        int resoult =theBoard.makeStep(v.getId(),isFlagUI);
                        if (isFlagUI){
                            makeFlag(v);
                        }
                        else
                            makeDicover(v, resoult);
                    }
                });
                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,(int) (0.021*high) , getResources().getDisplayMetrics());
                int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,(int) (0.033*with), getResources().getDisplayMetrics());
                index.setLayoutParams(new TableRow.LayoutParams(width, height));
                allRow.addView(index);
            }
            tileBoard.addView(allRow);
        }
        return  tileBoard;
    }

    public void makeFlag(View v){
        int indexY= v.getId()%cols;
        int indexX = v.getId()/cols;
        if (theBoard.getAllTiles()[indexX][indexY].isCover()) {
            if (theBoard.getAllTiles()[indexX][indexY].isFlag())
                ((Button) ((TableRow) UI_Board.getChildAt(indexX)).getChildAt(indexY)).setText("F");
            else
                ((Button) ((TableRow) UI_Board.getChildAt(indexX)).getChildAt(indexY)).setText("");

            bombs.setText("Bombs: "+(theBoard.getBombs()-theBoard.getNumOfFlagsLogic()));
        }
    }

    public void makeDicover(View v, int resoult){
        if (resoult==1) {
            final Intent intent = new Intent(this,WinnerActivity.class);
            final  int scene=2;
            startActivityForResult(intent,scene);
        }
        else if (resoult==-1) {
            final Intent intent = new Intent(this, LoserActivity.class);
            final int scene = 2;
            SharedPreferences settings =  getSharedPreferences("sllBestScores",0);
            SharedPreferences.Editor editor = settings.edit();
        //    editor.putInt()
            startActivityForResult(intent, scene);
        }
        else{
            for (int i = 0 ; i < theBoard.getRows() ; i ++){
                for (int j = 0 ; j < theBoard.getCols() ; j ++){
                    if (!theBoard.getAllTiles()[i][j].isCover()){
                        if (theBoard.getAllTiles()[i][j].getType()==10) {
                            ((Button) ((TableRow) UI_Board.getChildAt(i)).getChildAt(j)).setText("X");
                        }
                        else ((Button) ((TableRow) UI_Board.getChildAt(i)).getChildAt(j)).setText(""+theBoard.getAllTiles()[i][j].getType());
                    }
                }
            }
        }
    }

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

    @Override
    public void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }



}

