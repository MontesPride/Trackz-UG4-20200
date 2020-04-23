package com.montes.trackz;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
        //setTrackGenerator(new BasicTrackGenerator(this.getApplicationContext()));
        setTrackGenerator(new ProceduralTrackGenerator(this.getApplicationContext()));
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        this.graphView = findViewById(R.id.graph);
        //GraphView graph = findViewById(R.id.graph);
/*        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(0, 2),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6),
                new DataPoint(5, 15)
        });
        series.setColor(Color.argb(0xFF, 0xAA, 0x44, 0x88));
        series.setThickness(20);
        series.setAnimated(true);*/
        //this.graphView.getGridLabelRenderer().setVerticalLabelsVisible(false);
        //this.graphView.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        this.graphView.getGridLabelRenderer().setGridColor(Color.argb(0x00, 0xFF, 0xFF, 0xFF));
        //this.graphView.addSeries(series);

        renderTrack(getTrackGenerator(ProceduralTrackGenerator.class).getNextTrack());

/*        FloatingActionButton fabGenerateBasicTrack = findViewById(R.id.fabGenerateBasicTrack);
        fabGenerateBasicTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Generating Track using BasicTrackGenerator", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                getTrackGenerator(BasicTrackGenerator.class).getNextTrack();
            }
        });*/

        FloatingActionButton fabGenerateProceduralTrack = findViewById(R.id.fabGenerateProceduralTrack);
        fabGenerateProceduralTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*                Snackbar.make(view, "Generating Track using ProceduralTrackGenerator", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                //getTrackGenerator(ProceduralTrackGenerator.class).getNextTrack();
                renderTrack(getTrackGenerator(ProceduralTrackGenerator.class).getNextTrack());
            }
        });

/*        FloatingActionButton fabTrackValidator = findViewById(R.id.fabTrackValidator);
        fabTrackValidator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Checking Validator", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                getTrackGenerator(BasicTrackGenerator.class).checkValidator();
            }
        });*/

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

        //double viewportSize = Consts.FIELD_SIZE * Consts.VIEWPORT_SCALE + 2;
        //Log.d(tag, String.format("[setViewport] viewportSize: %.1f", viewportSize));
        //this.graphView.getViewport().setXAxisBoundsManual(true);
        //this.graphView.getViewport().setYAxisBoundsManual(true);

        this.graphView.getViewport().setScalable(true);
        this.graphView.getViewport().setScalableY(true);
        Log.d(tag, "[setViewportSize] end");
    }
}
