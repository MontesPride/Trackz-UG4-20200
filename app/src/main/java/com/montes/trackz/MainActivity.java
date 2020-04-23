package com.montes.trackz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
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
import com.montes.trackz.util.StaticDataHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String tag = "MainActivity";
    private List<TrackGenerator> trackGenerators;
    private GraphView graphView;

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

        FloatingActionButton fabGoToInfo = findViewById(R.id.fabInfo);
        fabGoToInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, InfoActivity.class));
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
        if (!StaticDataHolder.getShowTrackScore())
            return;
        TextView scoreView = findViewById(R.id.scoreView);
        scoreView.setText(String.format("Score: %d/10", trackScore));
        scoreView.setTextSize(Consts.TEXT_BASE + trackScore * Consts.TEXT_SCALE);
        if (trackScore == 10) {
            scoreView.setTypeface(null, Typeface.BOLD);
        } else {
            scoreView.setTypeface(null, Typeface.NORMAL);
        }
    }

    public void setTrackString(String trackString) {
        if (!StaticDataHolder.getShowTrackString())
            return;
        TextView trackStringView = findViewById(R.id.trackStringView);
        trackStringView.setText(trackString);
    }

}
