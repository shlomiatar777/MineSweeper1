package com.example.shlomi.minesweeper1;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import Logic.Winner;

public class ShowHighScoreActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static GoogleMap googleMap;
    private static int selectedRow= 0;
    ArrayList<Winner> theList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_high_score);
        MapFragment mp = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mp.getMapAsync(this);
        String level = getIntent().getExtras().getString("level");
        SharedPreferences sp=getSharedPreferences("all highScore lists", Context.MODE_PRIVATE);
        String listStr= sp.getString(level+"list","");
        theList = new ArrayList<Winner>();
        if(!listStr.equals("")) {
            Type type = new TypeToken<ArrayList<Winner>>() {
            }.getType();
            theList = new Gson().fromJson(listStr, type);
        }
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        Bundle b = new Bundle();
        b.putString("scores", listStr);
        b.putInt("width", width);
        if(!listStr.equals("")) {
            ScoresTableFragment table = new ScoresTableFragment();/*(ScoresTableFragment)
                getFragmentManager().findFragmentById(R.id.table);*/
            table.setArguments(b);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.table, table);
            ft.commit();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap= googleMap;
        setLocations(theList);
    }


    public static class ScoresTableFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
            // Defines the xml file for the fragment

            Bundle b = getArguments();
            String level = b.getString("scores");
            Type type = new TypeToken<ArrayList<Winner>>(){}.getType();
            ArrayList<Winner> scores = new Gson().fromJson(level, type);
            int width = b.getInt("width");

            View v = inflater.inflate(R.layout.high_score_table, parent, false);

            fillTableWithScores(scores, getActivity().getApplicationContext(), v,
                    width);

            return v;
        }


        public void fillTableWithScores(final ArrayList<Winner> highscores, final Context context,
                                        View v, int width)
        {

            final TableLayout table= (TableLayout) v.findViewById(R.id.tablelayout);
            TableRow titlerow = new TableRow(context);
            titlerow.setBackgroundColor(Color.LTGRAY);

            TextView numberCell = setTextViewStyle(context, "number", width/40);
            TextView nameCell = setTextViewStyle(context, "name", width/40);
            TextView scoreCell = setTextViewStyle(context, "score", width/40);

            titlerow.addView(numberCell);
            titlerow.addView(nameCell);
            titlerow.addView(scoreCell);
            titlerow.setClickable(false);
            table.addView(titlerow);

            for (int i = 0; i < highscores.size(); i++)
            {
                TableRow tr = new TableRow(context);
                if(i%2==0){
                    tr.setBackgroundColor(Color.rgb(237,237,237));
                } else {
                    tr.setBackgroundColor(Color.rgb(227,230,233));
                }

                TextView number = setTextViewStyle(context, (i+1) + "", width/50);
                TextView name = setTextViewStyle(context,
                        highscores.get(i).getName(), width/50);
                TextView score = setTextViewStyle(context,
                        highscores.get(i).getScore()+"", width/50);

                tr.addView(number);
                tr.addView(name);
                tr.addView(score);
                tr.setClickable(true);
                tr.setWeightSum(3);

                tr.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        TableRow tablerow = (TableRow)v;
                        int index = table.indexOfChild(tablerow);

                        if(selectedRow%2==0){
                            table.getChildAt(selectedRow).
                                    setBackgroundColor(Color.rgb(237,237,237));
                        } else {
                            table.getChildAt(selectedRow).
                                    setBackgroundColor(Color.rgb(227,230,233));
                        }

                        tablerow.setBackgroundColor(Color.rgb(180,180,230));

                        selectedRow = index;

                        LatLng point = new LatLng(highscores.get(index-1).getLocation()
                                .getLatitude(), highscores.get(index-1).getLocation()
                                .getLongitude());
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(point));
                    }
                });

                table.addView(tr);
            }
        }

        TextView setTextViewStyle(Context context, String name, float fontSize)
        {
            TextView text = new TextView(context);
            TableRow.LayoutParams trlp = new TableRow.LayoutParams
                    (0, TableRow.LayoutParams.MATCH_PARENT, 1);

            text.setLayoutParams(trlp);
            text.setText(name);
            text.setTextColor(Color.BLACK);
            text.setTextSize(fontSize);
            text.setEllipsize(TextUtils.TruncateAt.END);
            return text;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setLocations(ArrayList<Winner> scores)
    {
        for (int i = 0; i < scores.size(); i++)
        {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(scores.get(i).getLocation().getLatitude(),
                            scores.get(i).getLocation().getLongitude()))
                    .title("name: " + scores.get(i).getName())
                    .snippet("score: " + scores.get(i).getScore()));
        }
    }

}


