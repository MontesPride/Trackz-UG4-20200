package com.montes.trackz.generators.procedural;

import android.content.Context;
import android.util.Log;

import com.montes.trackz.generators.TrackGeneratorImpl;
import com.montes.trackz.pieces.TrackPiece;
import com.montes.trackz.tracks.Track;
import com.montes.trackz.tracks.TrackImpl;
import com.montes.trackz.util.Consts;
import com.montes.trackz.util.Helper;
import com.montes.trackz.util.TestTrackz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        //generatedPoints = testPoints();
        List<TrackPiece> trackPieces = null;
        boolean finishedTrack = false;

        //while (!finishedTrack) {

            Log.d(tag, "[generateTrack] Trying to find a new track!");

            ConvexHull convexHull = new ConvexHull(TestTrackz.getProceduralPointList2());
            List<Point> convexPolygon = convexHull.computePolygon();
            trackPieces = new ArrayList<>();

            for (int i = 0; i < convexPolygon.size() - 1; ++i) {
                Point p1 = convexPolygon.get(i);
                Point p2 = convexPolygon.get(i + 1);
                List <TrackPiece> trackPiecePath = findTrackPiecePathBetweenTwoPoints(p1, p2);
                if (trackPiecePath == null) {
                    Log.d(tag, "[generateTrack] breaking from for loop");
                    break;
                }
                finishedTrack = i == convexPolygon.size() - 2;
                Log.d(tag, String.format("[generateTrack] p1: %s, p2: %s, trackPiecePath: %s", p1, p2, trackPiecePath));
                trackPieces = Stream.concat(trackPieces.stream(), trackPiecePath.stream()).collect(Collectors.toList());
            }

        //}
        Log.d(tag, String.format("[generateTrack] trackPieces: %s", trackPieces));
        return new TrackImpl(trackPieces);
    }

    private List<Point> generatePoints() {
        List<Point> points = new ArrayList<>();
        int numberOfPoints = this.randomGenerator.nextInt(this.numOfGeneratedPointsMax - this.numOfGeneratedPointsMin + 1) + this.numOfGeneratedPointsMin;

        Log.d(tag, String.format("[generatePoints] numberOfPoints: %d", numberOfPoints));

        for (int i = 0; i < numberOfPoints; i++) {
            int x = this.randomGenerator.nextInt(this.fieldSize + 1) - (this.fieldSize / 2);
            int y = this.randomGenerator.nextInt(this.fieldSize + 1) - (this.fieldSize / 2);

            //Log.d(tag, String.format("[generatePoints] x: %d, y: %d", x, y));

            Point point = transformGeneratedPoint(x, y);
            points.add(point);
        }

        Log.d(tag, String.format("[generatePoints] points: %s", points));

        return points;
    }

    private Point transformGeneratedPoint(int x, int y) {

        int magnitude = (int) Math.sqrt(x * x + y * y);
        int multiplier = Math.max((Consts.FIELD_SIZE / Consts.UNIT_MULTIPLIER) * Consts.TRANSFORMATION_MULTIPLIER / (magnitude + 1), 1) * Consts.UNIT_MULTIPLIER;

        Log.d(tag, String.format("[transformGeneratedPoint] x: %d, y: %d, magnitude: %d, multiplier: %d", x, y, magnitude, multiplier));

        x *= multiplier;
        y *= multiplier;

        if (x % Consts.TRANSFORMATION_MODULO != 0) {
            x += Consts.TRANSFORMATION_MODULO - x % Consts.TRANSFORMATION_MODULO;
        }

        if (y % Consts.TRANSFORMATION_MODULO != 0) {
            y += Consts.TRANSFORMATION_MODULO - y % Consts.TRANSFORMATION_MODULO;
        }

        return new Point(x, y);
    }

    private List<TrackPiece> findTrackPiecePathBetweenTwoPoints(Point p1, Point p2) {
        Log.d(tag, String.format("[findTrackPiecePathBetweenTwoPoints] p1: %s, p2: %s", p1, p2));
        if (p1.equals(p2)) {
            Log.d(tag, "Equal points provided");
            return null;
        }

        List<TrackPiece> resultTrackPieces = null;

        int x_diff = Math.abs(p2.getX() - p1.getX());
        int y_diff = Math.abs(p2.getY() - p1.getY());

        int new_x = ((p1.getX() + p2.getX()) / 2);
        int new_y = ((p1.getY() + p2.getY()) / 2);

        int x_direction = p1.getX() <= p2.getX() ? 1 : -1;
        int y_direction = p1.getY() <= p2.getY() ? 1 : -1;

        boolean xy_direction = x_direction * y_direction == 1;

        if (x_diff == 0 || y_diff == 0) {
            TrackPiece straightTrackPiece = Helper.getStraightTrackPieceByLength(x_diff + y_diff);
            Log.d(tag, String.format("[findTrackPiecePathBetweenTwoPoints] straightTrackPiece of length: %d", x_diff + y_diff));
            if (straightTrackPiece != null) {
                return Collections.singletonList(straightTrackPiece);
            }
        } else if (x_diff == y_diff) {
            TrackPiece curvedTrackPiece = Helper.getCurvedTrackPieceByLength(x_diff, xy_direction);
            Log.d(tag, String.format("[findTrackPiecePathBetweenTwoPoints] curvedTrackPiece of length: %d, xy_direction: %s", x_diff, xy_direction));
            if (curvedTrackPiece != null) {
                return Arrays.asList(curvedTrackPiece, Helper.getCurvedTrackPieceByLength(x_diff, xy_direction));
            }
        } else {
            Point tryNewCurvedPoint = tryInputCurvedTrackPiece(p1, p2);
            if (tryNewCurvedPoint != null) {
                new_x = tryNewCurvedPoint.getX();
                new_y = tryNewCurvedPoint.getY();
            }
        }

        Point newPoint = new Point(new_x, new_y);
        Log.d(tag, String.format("[findTrackPiecePathBetweenTwoPoints] newPoint: %s", newPoint));

        if (!(canInputCurvedTrackPiece(p1, newPoint) && canInputCurvedTrackPiece(p2, newPoint)) && (x_diff != 0 && y_diff != 0)) {
            Log.d(tag, "[findTrackPiecePathBetweenTwoPoints] New point is invalid because CurvedPiece cannot be inputted");
            new_x = p1.getX() + Consts.MIN_LENGTH_OF_CURVED_TRACK_PIECE * x_direction;
            newPoint.setX(new_x);
            newPoint.setY(p2.getY());
            Log.d(tag, String.format("[findTrackPiecePathBetweenTwoPoints] after update newPoint: %s", newPoint));
        }

        if (newPoint.equals(p1) || newPoint.equals(p2)) {
            Log.d(tag, "[findTrackPiecePathBetweenTwoPoints] newPoint has not been changed");
            return null;
        }

        List<TrackPiece> trackPiecesToNewPoint = findTrackPiecePathBetweenTwoPoints(p1, newPoint);
        if (trackPiecesToNewPoint == null)
            return null;

        List<TrackPiece> trackPiecesFromNewPoint = findTrackPiecePathBetweenTwoPoints(newPoint, p2);
        if (trackPiecesFromNewPoint == null)
            return null;

        resultTrackPieces = Stream.concat(trackPiecesToNewPoint.stream(), trackPiecesFromNewPoint.stream()).collect(Collectors.toList());
        Log.d(tag, String.format("[findTrackPiecePathBetweenTwoPoints] startPoint: %s, endPoint: %s, resultTrackPieces: %s", p1, p2, resultTrackPieces));
        if (!validateCurrentTrack(p1, p2, resultTrackPieces)) {
            resultTrackPieces = rotateCurrentTrack(p1, p2, trackPiecesToNewPoint, trackPiecesFromNewPoint);
            Log.d(tag, String.format("[findTrackPiecePathBetweenTwoPoints] startPoint: %s, endPoint: %s, VALIDATED resultTrackPieces: %s", p1, p2, resultTrackPieces));
        }
        return resultTrackPieces;

        /*return new ArrayList<>();*/

    }

    private boolean canInputCurvedTrackPiece(Point p1, Point p2) {
        int x_diff = Math.abs(p1.getX() - p2.getX());
        int y_diff = Math.abs(p1.getY() - p2.getY());
        return (x_diff >= Consts.MIN_LENGTH_OF_CURVED_TRACK_PIECE && y_diff >= Consts.MIN_LENGTH_OF_CURVED_TRACK_PIECE) || (x_diff == 0 || y_diff == 0);
    }

    private Point tryInputCurvedTrackPiece(Point p1, Point p2) {
        List<Integer> curvedPiecesLength = Arrays.asList(4, 2);

        int x_diff = Math.abs(p2.getX() - p1.getX());
        int y_diff = Math.abs(p2.getY() - p1.getY());

        int x_direction = p1.getX() <= p2.getX() ? 1 : -1;
        int y_direction = p1.getY() <= p2.getY() ? 1 : -1;

        for (int length : curvedPiecesLength) {
            if ((x_diff == length && y_diff > length) || (x_diff > length && y_diff == length)) {
                Point newPoint = new Point(p1.getX() + length * x_direction, p1.getY() + length * y_direction);
                Log.d(tag, String.format("[tryInputCurvedTrackPiece] returning newPoint: %s", newPoint));
                return newPoint;
            }
        }

        Log.d(tag, "[tryInputCurvedTrackPiece] returning null");
        return null;
    }

    private boolean validateCurrentTrack(Point p1, Point p2, List<TrackPiece> trackPieces) {
        return validateCurrentTrack(p1, p2, trackPieces, null);
    }

    private boolean validateCurrentTrack(Point p1, Point p2, List<TrackPiece> trackPiecesToNewPoint, List<TrackPiece> trackPiecesFromNewPoint) {
        List<TrackPiece> trackPieces;
        if (trackPiecesFromNewPoint == null) {
            trackPieces = trackPiecesToNewPoint;
        } else {
            trackPieces = Stream.concat(trackPiecesToNewPoint.stream(), trackPiecesFromNewPoint.stream()).collect(Collectors.toList());
        }

        Log.d(tag, String.format("[validateCurrentTrack] p1: %s, p2: %s, trackPieces: %s", p1, p2, trackPieces));

        List<Double> angles = new ArrayList<>();

        if (p1.getX() <= p2.getX()) {
            angles.add((double) 0);
        } else {
            angles.add(Math.PI);
        }

        if (p1.getY() <= p2.getY()) {
            angles.add(Math.PI * (1 / 2.f));
        } else {
            angles.add(Math.PI * (3 / 2.f));
        }

        Log.d(tag, String.format("[validateCurrentTrack] angles: %s", angles));

        for (double currentAngle : angles) {
            double x = p1.getX();
            double y = p1.getY();
            double angle = currentAngle;
            for (TrackPiece trackPiece : trackPieces) {
                x += trackPiece.getLength() * Math.cos(angle + trackPiece.getAngle());
                y += trackPiece.getLength() * Math.sin(angle + trackPiece.getAngle());
                angle += 2 * trackPiece.getAngle();
                Log.d(tag, String.format("[validateCurrentTrack] trackPiece: %s, x: %.3f, y: %.3f, angle: %.3f", trackPiece, x, y, angle));
            }
            Log.d(tag, String.format("[validateCurrentTrack] trackPieces: %s, x: %.3f, x_end: %d, y: %.3f, y_end: %d, angle: %.3f", trackPieces, x, p2.getX(), y, p2.getY(), angle));
            if (Helper.compareTwoDoubles(x, p2.getX()) && Helper.compareTwoDoubles(y, p2.getY())) {
                Log.d(tag, "[validateCurrentTrack] Returning true");
                return true;
            }
        }
        Log.d(tag, "[validateCurrentTrack] Returning false");
        return false;
    }

    private List<TrackPiece> rotateCurrentTrack(Point p1, Point p2, List<TrackPiece> trackPiecesToNewPoint, List<TrackPiece> trackPiecesFromNewPoint) {
        List<TrackPiece> rotatedTrackPiecesToNewPoint = rotateCurrentTrack(trackPiecesToNewPoint);
        List<TrackPiece> rotatedTrackPiecesFromNewPoint = rotateCurrentTrack(trackPiecesFromNewPoint);

        if (validateCurrentTrack(p1, p2, rotatedTrackPiecesToNewPoint, trackPiecesFromNewPoint)) {
            return Stream.concat(rotatedTrackPiecesToNewPoint.stream(), trackPiecesFromNewPoint.stream()).collect(Collectors.toList());
        } else if (validateCurrentTrack(p1, p2, trackPiecesToNewPoint, rotatedTrackPiecesFromNewPoint)) {
            return Stream.concat(trackPiecesToNewPoint.stream(), rotatedTrackPiecesFromNewPoint.stream()).collect(Collectors.toList());
        } else if (validateCurrentTrack(p1, p2, rotatedTrackPiecesToNewPoint, rotatedTrackPiecesFromNewPoint)) {
            return Stream.concat(rotatedTrackPiecesToNewPoint.stream(), rotatedTrackPiecesFromNewPoint.stream()).collect(Collectors.toList());
        }
        return null;
    }

    private List<TrackPiece> rotateCurrentTrack(List<TrackPiece> trackPieces) {
        Log.d(tag, String.format("[rotateCurrentTrack] before rotation trackPieces: %s", trackPieces));
        Collections.reverse(trackPieces);
        for (TrackPiece trackPiece : trackPieces) {
            trackPiece.rotate();
        }
        Log.d(tag, String.format("[rotateCurrentTrack] after rotation trackPieces: %s", trackPieces));
        return trackPieces;
    }
}
