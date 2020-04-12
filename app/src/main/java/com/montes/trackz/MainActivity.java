package com.montes.trackz;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.montes.trackz.generators.procedural.ProceduralTrackGenerator;
import com.montes.trackz.generators.TrackGenerator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private static final String tag = "MainActivity";
    private TrackGenerator trackGenerator;

    public TrackGenerator getTrackGenerator() {
        return this.trackGenerator;
    }

    public void setTrackGenerator(TrackGenerator trackGenerator) {
        Log.d(tag, String.format("[setTrackGenerator] trackGenerator: %s", trackGenerator));
        this.trackGenerator = trackGenerator;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(tag, "[onCreate] Start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Generating Tracks", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                getTrackGenerator().getNextTrack();
            }
        });

        FloatingActionButton fabGenerateTrack = findViewById(R.id.fabGenerateTrack);
        fabGenerateTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Generating Track", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                getTrackGenerator().checkValidator();
            }
        });

        Log.d(tag, "[onCreate] End");
    }

    @Override
    protected void onStart() {
        Log.d(tag, "[onStart] Start");
        super.onStart();
        setTrackGenerator(new ProceduralTrackGenerator(this.getApplicationContext()));
        Log.d(tag, "[onStart] End");
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
            Log.d(tag, "[onOptionsItemSelected] I am here");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
