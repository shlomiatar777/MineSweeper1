package com.example.shlomi.minesweeper1;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import Logic.Board;
import Logic.Winner;

public class GameActivity extends AppCompatActivity {

    private TextView timer, bombs;
    Board theBoard;
    TableLayout UI_Board;
    Drawable old;
    boolean isFlagUI = false;
    int rows, cols, bombsNum, levelType;
    int width, height;
    private int countSeconds = 0;
    private Intent intentService;
    RelativeLayout rlMain;
    Location theLocation;
    LocationManager locationMng;
    LocationListener locationLsn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
// load parameters from other activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        rows = getIntent().getIntExtra("rows", 1);
        cols = getIntent().getIntExtra("cols", 1);
        bombsNum = getIntent().getIntExtra("bombsNum", 1);
        levelType = getIntent().getIntExtra("levelName", -1);
        theBoard = new Board(rows, cols, bombsNum);
        theBoard.initLogicBoard();
        rlMain = (RelativeLayout) findViewById(R.id.activity_game);
        //rlMain.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
//  screen size parameters
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

//main LayOut of this Activity
        LinearLayout llGame = new LinearLayout(this);
        llGame.setOrientation(LinearLayout.VERTICAL);
        llGame.setGravity(Gravity.CENTER_HORIZONTAL);
        // setContentView(llGame);
        Drawable backGround = ContextCompat.getDrawable(this, R.drawable.parket);
        llGame.setBackgroundDrawable(backGround);
        llGame.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        rlMain.addView(llGame);


        LinearLayout llTop = new LinearLayout(this);
        makeParamsGame(llTop);
        llGame.addView(llTop);
// make the tiles board
        UI_Board = initTileBoard(rows, cols, height, width);
        UI_Board.setPadding(0, height / 20, 0, 0);
        UI_Board.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        llGame.addView(UI_Board);

        LinearLayout llBottom = new LinearLayout(this);
        makeOptionsGame(llBottom);
        llGame.addView(llBottom);
        Intent intent = new Intent(this,MyService.class);
        startService(intent);



        Intent i = new Intent(this, MyService.class);
        startService(i);
        intentService = new Intent(this, MyService.class);





//init the gps objects
        locationMng = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        theLocation  = new Location("");
        theLocation.setLatitude(0);
        theLocation.setLongitude(0);

        locationLsn = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location!= null) {
                    theLocation.setLatitude(location.getLatitude());
                    theLocation.setLongitude(location.getLongitude());
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
            }
        };

       //get permmision to gps
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String []{android.Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET},10);
                return;
            }
            else
                locationMng.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5555, 50, locationLsn);
        }
        else {
            locationMng.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 100, locationLsn);
        }

    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };


    private void updateUI(Intent intent) {
        float firstx = intent.getFloatExtra("firstx",0);
        float firsty = intent.getFloatExtra("firsty",0);
        float firstz = intent.getFloatExtra("firstz",0);
        float x = intent.getFloatExtra("x",0);
        float y = intent.getFloatExtra("y",0);
        float z = intent.getFloatExtra("z",0);

        if (Math.abs(firstx-x) > 10 || Math.abs(firsty-y) > 10|| Math.abs(firstz-z) > 10)
        {
            theBoard.addBomb();
            bombs.setText("Bombs: "+ theBoard.getBombs());
            for (int i = 0 ; i < theBoard.getRows() ; i ++){
                TableRow row= (TableRow)UI_Board.getChildAt(i);
                for (int j = 0 ; j < theBoard.getCols() ; j ++){
                    if (theBoard.getAllTiles()[i][j].isCover()){
                        ((Button) row.getChildAt(j)).setBackground(old);
                        ((Button) row.getChildAt(j)).setText("");
                    }
                }
            }
        }
    }



    //get resoult for location
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case 10:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    configo();

            default:
                theLocation.setLongitude(0);
                theLocation.setLatitude(0);
        }

    }

    private void configo(){

        locationMng.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 100, locationLsn);
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
                                timer.setText("Timer: "+countSeconds);
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
                old = index.getBackground();

                index.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //get the current status of the game (1: win , -1: lose , 0: still play)
                        int resoult =theBoard.makeStep(v.getId(),isFlagUI);
                        if (isFlagUI)
                            makeFlag(v,heightBtn,widthBtn,old);
                        else
                            makeDiscover(v, resoult,heightBtn,widthBtn, old);
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
    public void makeParamsGame(LinearLayout llTop){
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
        bombs.setText("Bombs: "+ theBoard.getBombs());
        bombs.setPadding(width/8,0,0,0);
        llTop.addView(bombs);
    }

    //the options of the game (discover Tile , make a flag or quit)
    public void  makeOptionsGame(LinearLayout llBottom){
        llBottom.setOrientation(LinearLayout.HORIZONTAL);
        llBottom.setGravity(Gravity.CENTER_HORIZONTAL);
        llBottom.setPadding(0,height/20,0,0);

        Button flags = new Button(this);
        flags.setTextSize(height/70);
        flags.setText(R.string.flags);
        flags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFlagUI=true;
            }
        });
        Button dicover = new Button(this);
        dicover.setTextSize(height/70);
        dicover.setText(R.string.discover);
        dicover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFlagUI=false;
            }
        });
        Button quit = new Button(this);
        quit.setTextSize(height/70);
        quit.setText(R.string.quit);
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
    public void makeDiscover(View v, int resoult,int heightBtn, int widthBtn,Drawable old){
        if (resoult==1) {
            final Intent intent = new Intent(this,WinnerActivity.class);
            final  int scene=2;

            String levelName="";
            switch(levelType){
                case 0:
                    levelName="easy";
                    break;
                case 1:
                    levelName="medium";
                    break;
                case 2:
                    levelName="hard";
                    break;
            }

            Winner currentWin =  new Winner("",countSeconds,theLocation);
            String winnerToGson = new Gson().toJson(currentWin,Winner.class);
            intent.putExtra("levelName",levelName);
            intent.putExtra("winnerToGson",winnerToGson);
            winAnimation(UI_Board,rows,cols);
            int time = 2000;
            Runnable runnable = new Runnable() {
                public void run() {
                    startActivityForResult(intent,scene);
                }
            };
            Handler handler = new Handler();
            handler.postDelayed(runnable, time);
        }
        else if (resoult==-1) {
            final Intent intent = new Intent(this, LoserActivity.class);
            final int scene = 2;
            loseAnimation(rlMain,UI_Board);
            int time = 3000;
            Runnable runnable = new Runnable() {
                public void run() {
                    startActivityForResult(intent, scene);
                }
            };
            Handler handler = new Handler();
            handler.postDelayed(runnable, time);

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
        quitDialog.setMessage(R.string.return_to_menu);
        quitDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GameActivity.this.finish();
            }
        });

        quitDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
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
        Button btn=((Button) ((TableRow) UI_Board.getChildAt(x)).getChildAt(y));
       btn.setBackgroundDrawable(d);

        int type= theBoard.getAllTiles()[x][y].getType();
        switch (type) {
            case 1:
                btn.setTextColor(Color.BLUE);
                break;
            case 2:
                btn.setTextColor(Color.rgb(20, 100, 40));
                break;
            case 3:
                btn.setTextColor(Color.RED);
                break;
            case 4:
                btn.setTextColor(Color.MAGENTA);
                break;
            case 5:
                btn.setTextColor(Color.CYAN);
                break;
            case 6:
                btn.setTextColor(Color.YELLOW);
                break;
            case 7:
                btn.setTextColor(Color.GRAY);
                break;
            case 8:
                btn.setTextColor(Color.rgb(170, 170, 40));
                break;
        }
        if (type != 0)
            btn.setText("" + theBoard.getAllTiles()[x][y].getType());
    }

    void loseAnimation(RelativeLayout rl, TableLayout table) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(table, "translationY", height);
        animator.setDuration(3000);
        animator.setInterpolator(new AccelerateInterpolator());

        ImageView image = new ImageView(this);
        image.setImageDrawable(getDrawable(R.drawable.animation));
        AnimationDrawable imgAnimation = (AnimationDrawable) image.getDrawable();
        imgAnimation.setOneShot(false);
        image.setX(width / 2 - 400);
        image.setY(height / 2 - 400);
        image.setImageAlpha(128);
        rl.addView(image);

        animator.start();

        imgAnimation.start();
    }

    void winAnimation(TableLayout table, int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            TableRow row = (TableRow) table.getChildAt(i);
            for (int j = 0; j < cols; j++) {
                Button b;
                if (i % 2 == 0) {
                    b = (Button) row.getChildAt(j);
                } else {
                    b = (Button) row.getChildAt(9 - j);
                }

                RotateAnimation rotate = new RotateAnimation(0, 360,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                        0.5f);
                rotate.setDuration(2000);

                AlphaAnimation animation1 = new AlphaAnimation(1, 0);
                animation1.setDuration(2000);

                AnimationSet a = new AnimationSet(false);
                a.addAnimation(rotate);
                a.addAnimation(animation1);
                a.setFillAfter(true);
                b.startAnimation(a);

            }
        }
    }

    @Override
    public void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }


    @Override
    public void onResume() {
        super.onResume();
        startService(intentService);
        registerReceiver(broadcastReceiver, new IntentFilter(MyService.BROADCAST_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        stopService(intentService);
    }

}


