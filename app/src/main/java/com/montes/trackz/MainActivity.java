package com.montes.trackz;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.montes.trackz.generators.TrackGeneratorImpl;
import com.montes.trackz.generators.basic.BasicTrackGenerator;
import com.montes.trackz.generators.procedural.ProceduralTrackGenerator;
import com.montes.trackz.generators.TrackGenerator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String tag = "MainActivity";
    private List<TrackGenerator> trackGenerators;

    public List<TrackGenerator> getTrackGenerators() {
        if (this.trackGenerators == null)
            return new ArrayList<>();
        return this.trackGenerators;
    }

    public TrackGenerator getTrackGenerator(Class<?> trackGeneratorClass) {
        for (TrackGenerator trackGenerator : this.trackGenerators) {
            if (trackGeneratorClass.isInstance(trackGenerator))
                return trackGenerator;
        }
        return null;
    }

    public void setTrackGenerators(List<TrackGenerator> trackGenerators) {
        Log.d(tag, String.format("[setTrackGenerator] trackGenerators: %s", trackGenerators));
        this.trackGenerators = trackGenerators;
    }

    public void setTrackGenerator(TrackGenerator trackGenerator) {
        List<TrackGenerator> trackGenerators = this.getTrackGenerators();
        for (int i = 0; i < trackGenerators.size(); ++i) {
            if (trackGenerators.get(i).getClass().isInstance(trackGenerator)) {
                trackGenerators.add(i, trackGenerator);
                setTrackGenerators(trackGenerators);
                return;
            }
        }
        trackGenerators.add(trackGenerator);
        setTrackGenerators(trackGenerators);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(tag, "[onCreate] Start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabGenerateBasicTrack = findViewById(R.id.fabGenerateBasicTrack);
        fabGenerateBasicTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Generating Track using BasicTrackGenerator", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                getTrackGenerator(BasicTrackGenerator.class).getNextTrack();
            }
        });

        FloatingActionButton fabGenerateProceduralTrack = findViewById(R.id.fabGenerateProceduralTrack);
        fabGenerateProceduralTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Generating Track using ProceduralTrackGenerator", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                getTrackGenerator(ProceduralTrackGenerator.class).getNextTrack();
            }
        });

        FloatingActionButton fabTrackValidator = findViewById(R.id.fabTrackValidator);
        fabTrackValidator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Checking Validator", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                getTrackGenerator(BasicTrackGenerator.class).checkValidator();
            }
        });

        Log.d(tag, "[onCreate] End");
    }

    @Override
    protected void onStart() {
        Log.d(tag, "[onStart] Start");
        super.onStart();
        setTrackGenerator(new BasicTrackGenerator(this.getApplicationContext()));
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
