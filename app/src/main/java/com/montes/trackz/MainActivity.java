package com.montes.trackz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.montes.trackz.generators.TrackGenerator;
import com.montes.trackz.generators.procedural.ProceduralTrackGenerator;
import com.montes.trackz.tracks.Track;
import com.montes.trackz.tracks.TrackImpl;
import com.montes.trackz.util.Consts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String tag = "MainActivity";
    private List<TrackGenerator> trackGenerators;
    private GraphView graphView;
    private SharedPreferences preferences;

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

    public SharedPreferences getPreferences() {
        if (this.preferences == null)
            this.preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        return this.preferences;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(tag, "[onCreate] Start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        boolean showTrackscore = preferences.getBoolean(getResources().getString(R.string.show_track_score), true);
        String fieldSize = preferences.getString(getResources().getString(R.string.track_size), Integer.toString(Consts.FIELD_SIZE));
        preferences.edit().putBoolean(getResources().getString(R.string.show_track_score), showTrackscore).putString(getResources().getString(R.string.track_size), fieldSize).apply();
        for (Map.Entry mapEntry : preferences.getAll().entrySet()) {
            Log.d(tag, String.format("[onCreate] preference mapEntry: %s, value: %s", mapEntry.getKey(), mapEntry.getValue()));
        }

        setTrackGenerator(new ProceduralTrackGenerator(this.getApplicationContext()));
        this.graphView = findViewById(R.id.graph);
        this.graphView.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        this.graphView.getGridLabelRenderer().setVerticalLabelsVisible(false);
        this.graphView.getGridLabelRenderer().setGridColor(Color.argb(0x00, 0xFF, 0xFF, 0xFF));

        Track track = getTrackGenerator(ProceduralTrackGenerator.class).getNextTrack();
        setTrackScore(track.getTrackScore());
        setTrackString(track.getTrackListAsString());
        renderTrack(track);

        FloatingActionButton fabGenerateProceduralTrack = findViewById(R.id.fabGenerateProceduralTrack);
        fabGenerateProceduralTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Track track = getTrackGenerator(ProceduralTrackGenerator.class).getNextTrack();
                setTrackScore(track.getTrackScore());
                setTrackString(track.getTrackListAsString());
                renderTrack(track);
            }
        });

        FloatingActionButton fabGoToSettings = findViewById(R.id.fabSettings);
        fabGoToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

        Log.d(tag, "[onCreate] End");
    }

    @Override
    protected void onStart() {
        Log.d(tag, "[onStart] Start");
        super.onStart();
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

    public void renderTrack(Track track) {
        List<LineGraphSeries<DataPoint>> lineGraphSeriesList = track.getTrackAsCurve();
        Log.d(tag, "[renderTrack] renderingTrack");
        this.graphView.removeAllSeries();
        setViewportSize((TrackImpl) track);
        for (LineGraphSeries<DataPoint> lineGraphSeries : lineGraphSeriesList) {
            if (lineGraphSeries != null) {
                this.graphView.addSeries(lineGraphSeries);
            } else {
                Log.d(tag, "[renderTrack] null lineGraphSeries encountered");
            }
        }
    }

    public void setViewportSize(TrackImpl track) {
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        Log.d(tag, String.format("[setViewport] display.x: %d, display.y: %d", point.x, point.y));

        double ratio;
        double middle;
        double width;
        if (point.x >= point.y) {
            ratio = point.x / (double) point.y;

            middle = (track.getTopRight().getX() + track.getBottomLeft().getX()) / 2.f;
            width = (track.getTopRight().getY() - track.getBottomLeft().getY() + 2 * Consts.VIEWPORT_MARGIN) / 2.f;

            this.graphView.getViewport().setXAxisBoundsManual(true);
            this.graphView.getViewport().setMinX(middle - width * ratio);
            this.graphView.getViewport().setMaxX(middle + width * ratio);
            this.graphView.getViewport().setYAxisBoundsManual(true);
            this.graphView.getViewport().setMinY(track.getBottomLeft().getY() - Consts.VIEWPORT_MARGIN);
            this.graphView.getViewport().setMaxY(track.getTopRight().getY() + Consts.VIEWPORT_MARGIN);

        } else {
            ratio = point.y / (double) point.x;

            middle = (track.getTopRight().getY() + track.getBottomLeft().getY()) / 2.f;
            width = (track.getTopRight().getX() - track.getBottomLeft().getX() + 2 * Consts.VIEWPORT_MARGIN) / 2.f;

            this.graphView.getViewport().setXAxisBoundsManual(true);
            this.graphView.getViewport().setMinX(track.getBottomLeft().getX() - Consts.VIEWPORT_MARGIN);
            this.graphView.getViewport().setMaxX(track.getTopRight().getX() + Consts.VIEWPORT_MARGIN);
            this.graphView.getViewport().setYAxisBoundsManual(true);
            this.graphView.getViewport().setMinY(middle - width * ratio);
            this.graphView.getViewport().setMaxY(middle + width * ratio);

        }

        Log.d(tag, String.format("[setViewportSize] ratio: %.2f, middle: %.2f, width: %.2f", ratio, middle, width));

        this.graphView.getViewport().setScalable(true);
        this.graphView.getViewport().setScalableY(true);
        Log.d(tag, "[setViewportSize] end");
    }

    public void setTrackScore(int trackScore) {
        TextView scoreView = findViewById(R.id.scoreView);
        if (!this.getPreferences().getBoolean(getResources().getString(R.string.show_track_score), true)) {
            scoreView.setText("");
            return;
        }

        scoreView.setText(String.format("Score: %d/10", trackScore));
        scoreView.setTextSize(Consts.TEXT_BASE + trackScore * Consts.TEXT_SCALE);
        if (trackScore == 10) {
            scoreView.setTypeface(null, Typeface.BOLD);
        } else {
            scoreView.setTypeface(null, Typeface.NORMAL);
        }
    }

    public void setTrackString(String trackString) {
        TextView trackStringView = findViewById(R.id.trackStringView);
        if (!this.getPreferences().getBoolean(getResources().getString(R.string.show_track_string), false)) {
            trackStringView.setText("");
        } else {
            trackStringView.setText(trackString);
        }
    }

}
