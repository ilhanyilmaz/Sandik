package com.digitalwonders.ilhan.sandik;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private int scores[];
    private String[] parties;
    private List<TextView> scoreTVs;
    private List<LinearLayout> layouts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadDisplay();
        loadScores();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id ==R.id.reset_scores) {
            resetScores();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        saveScores();
        super.onDestroy();
    }

    private int getPartyIndex(String name) {
        for(int i=0; i<parties.length; i++)
            if(parties[i].equals(name))
                return i;

        return -1;
    }

    private int getViewIndex(View v) {
        for(int i=0; i<layouts.size(); i++) {
            if (layouts.get(i).equals(v))
                return i;
        }
        return -1;
    }


    private void loadDisplay() {


        scoreTVs= new ArrayList<TextView>();
        layouts= new ArrayList<LinearLayout>();

        layouts.add((LinearLayout) findViewById(R.id.party1layout));
        layouts.add((LinearLayout) findViewById(R.id.party2layout));
        layouts.add((LinearLayout) findViewById(R.id.party3layout));
        layouts.add((LinearLayout) findViewById(R.id.party4layout));
        layouts.add((LinearLayout) findViewById(R.id.party5layout));
        layouts.add((LinearLayout) findViewById(R.id.party6layout));

        scoreTVs.add((TextView) findViewById(R.id.party1text));
        scoreTVs.add((TextView) findViewById(R.id.party2text));
        scoreTVs.add((TextView) findViewById(R.id.party3text));
        scoreTVs.add((TextView) findViewById(R.id.party4text));
        scoreTVs.add((TextView) findViewById(R.id.party5text));
        scoreTVs.add((TextView) findViewById(R.id.party6text));

    }
    private void loadScores() {


        parties = getResources().getStringArray(R.array.partiler);
        scores = new int[parties.length];

        String prefName = this.getString(R.string.preference_file_key);
        SharedPreferences sharedPref = this.getSharedPreferences(
                prefName, Context.MODE_PRIVATE);

        if(sharedPref == null)
            return;
        Map scoreMap = sharedPref.getAll();

        Set keys = scoreMap.keySet();
        Iterator<String> itr = keys.iterator();
        String key;
        int partyIndex;


        for(int i=0; itr.hasNext(); i++) {
            key = itr.next();
            partyIndex = getPartyIndex(key);
            if(partyIndex == -1) {
                // TO-DO what else a key be??
                continue;
            }
            scores[partyIndex] = sharedPref.getInt(key, -1);

        }

        updateDisplayValues();
    }

    private void updateDisplayValues() {
        for(int i=0; i<scores.length; i++) {
            //Log.i("MainActivity", "Oy: " + scores[i]);
            TextView tv =scoreTVs.get(i);
            if(tv != null) {
                tv.setText("Oy: " + scores[i]);
            }
            else
                Log.i("MainActivity", "Why you no see!");

        }
    }

    @Override
    public void onClick(View v) {
        Log.i("MainActivity", "clicked view");
        int viewIndex = getViewIndex(v);
        if(viewIndex != -1) {
            scores[viewIndex]++;
            updateDisplayValues();
        }
    }

    public void saveScores() {


        String prefName = this.getString(R.string.preference_file_key);
        SharedPreferences sharedPref = this.getSharedPreferences(
                prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        String partyName;
        for(int i=0; i<scores.length; i++) {
            partyName = parties[i];
            editor.putInt(partyName, scores[getPartyIndex(partyName)]);
        }

        editor.commit();
    }

    public void resetScores() {
        for(int i=0; i<scores.length; i++)
            scores[i] =0;
        updateDisplayValues();
        saveScores();
    }
}
