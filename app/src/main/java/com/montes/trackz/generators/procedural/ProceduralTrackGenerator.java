package com.montes.trackz.generators.procedural;

import android.content.Context;
import android.util.Log;

import com.montes.trackz.generators.TrackGeneratorImpl;
import com.montes.trackz.tracks.Track;
import com.montes.trackz.util.Consts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProceduralTrackGenerator extends TrackGeneratorImpl {
    private static final String tag = "ProceduralTrackGenerator";

    private int fieldSize;
    private int numOfGeneratedPointsMin;
    private int numOfGeneratedPointsMax;

    private Random randomGenerator;

    public ProceduralTrackGenerator(Context context) {
        super(context);

        this.fieldSize = Consts.FIELD_SIZE * Consts.UNIT;
        this.numOfGeneratedPointsMin = Consts.NUM_OF_GENERATED_POINTS_MIN;
        this.numOfGeneratedPointsMax = Consts.NUM_OF_GENERATED_POINTS_MAX;

        this.randomGenerator = new Random();
    }

    @Override
    public Track generateTrack() {

        Log.d(tag, String.format("[generateTrack] Start, trackPiecesDataHolder: %s", this.getTrackPiecesDataHolder()));

        List<Point> generatedPoints = generatePoints();
        ConvexHull convexHull = new ConvexHull(generatedPoints);
        convexHull.computePolygon();


        return null;
    }

    private List<Point> generatePoints() {
        List<Point> points = new ArrayList<>();
        int numberOfPoints = this.randomGenerator.nextInt(this.numOfGeneratedPointsMax - this.numOfGeneratedPointsMin + 1) + this.numOfGeneratedPointsMin;

        Log.d(tag, String.format("[generatePoints] numberOfPoints: %d", numberOfPoints));

        for (int i = 0; i < numberOfPoints; i++) {
            int x = this.randomGenerator.nextInt(this.fieldSize + 1) - (this.fieldSize / 2);
            int y = this.randomGenerator.nextInt(this.fieldSize + 1) - (this.fieldSize / 2);

            /*int magnitude = x * x + y * y;
            int multiplier = Consts.FIELD_SIZE * Consts.TRANSFORMATION_MULTIPLIER / (magnitude + 1);
            x *= multiplier;
            y *= multiplier;*/

            Point point = new Point(x, y);
            points.add(point);
        }

        Log.d(tag, String.format("[generatePoints] points: %s", points));

        return points;
    }

}
