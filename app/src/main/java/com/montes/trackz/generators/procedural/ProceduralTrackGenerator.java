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
import java.util.Set;
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

        while (!finishedTrack) {

            Log.d(tag, "[generateTrack] Trying to find a new track!");

            //ConvexHull convexHull = new ConvexHull(TestTrackz.getProceduralPointList2());
            //ConvexHull convexHull = new ConvexHull(TestTrackz.getProceduralPointList3());
            //ConvexHull convexHull = new ConvexHull(TestTrackz.getProceduralPointList4());
            //ConvexHull convexHull = new ConvexHull(TestTrackz.getProceduralPointList5());
            ConvexHull convexHull = new ConvexHull(generatePoints());
            List<Point> convexPolygon = minimiseDirections(setNextAndPreviousPoints(convexHull.computePolygon()));
            if (!checkDirections(convexPolygon))
                continue;
            trackPieces = new ArrayList<>();

            for (int i = 0; i < convexPolygon.size() - 1; ++i) {
                Point p1 = convexPolygon.get(i);
                Point p2 = convexPolygon.get(i + 1);
                List <TrackPiece> trackPiecePath = findTrackPiecePathBetweenTwoPoints(p1, p2);
                if (trackPiecePath == null) {
                    Log.d(tag, "[generateTrack] breaking from for loop because trackPiecePath");
                    break;
                }
                finishedTrack = i == convexPolygon.size() - 2;
                Log.d(tag, String.format("[generateTrack] p1: %s, p2: %s, trackPiecePath: %s", p1, p2, trackPiecePath));
                trackPieces = TrackPiece.joinTwoLists(trackPieces, trackPiecePath);
                //Log.d(tag, String.format("[generateTrack] isCurrentTrackValidBeforeRotation: %s", validateCurrentTrack(p1, p2, trackPieces, trackPiecePath, true)));

                /*if (!validateCurrentTrack(convexPolygon.get(0), p2, trackPieces, trackPiecePath, true)) {
                    Log.d(tag, "[generateTrack] isCurrentTrackValidBeforeRotation: false");
                    trackPieces = rotateCurrentTrack(convexPolygon.get(0), p2, trackPieces, trackPiecePath, true);
                } else {
                    Log.d(tag, "[generateTrack] isCurrentTrackValidBeforeRotation: true");
                    trackPieces = Stream.concat(trackPieces.stream(), trackPiecePath.stream()).collect(Collectors.toList());
                }
                Log.d(tag, String.format("[generateTrack] trackPieces: %s", trackPieces));
                if (trackPieces == null) {
                    Log.d(tag, "[generateTrack] breaking from for loop because trackPieces");
                    break;
                }*/

                //Log.d(tag, String.format("[generateTrack] isCurrentTrackValid: %s", validateCurrentTrack(p1, p2, trackPieces)));
            }

        }
        Log.d(tag, String.format("[generateTrack] trackPieces: %s", trackPieces));
        //Log.d(tag, String.format("[generateTrack] isWholeTrackValid: %s", validateCurrentTrack(convexPolygon.get(0), convexPolygon.get(0), trackPieces, true)));
        TrackImpl track = new TrackImpl(trackPieces);
        validateTrack(track, true);
        return track;
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

    private List<Point> setNextAndPreviousPoints(List<Point> points) {
        points.get(0).setNext(points.get(1));
        points.get(0).setPrevious(points.get(points.size() - 2));

        for (int i = 1; i < points.size() - 1; ++i) {
            points.get(i).setNext(points.get(i + 1));
            points.get(i).setPrevious(points.get(i - 1));
        }

        Log.d(tag, String.format("[setNextAndPreviousPoints] points: %s", points));
        return points;
    }

    private List<Point> minimiseDirections(List<Point> points) {
        boolean finished;
        do {
            finished = true;
            for (Point point : points) {
                finished &= point.minimiseInOutDirections();
            }
            Log.d(tag, String.format("[minimiseDirections] minimisedInOutDirections finished: %s", finished));
        } while (!finished);

        do {
            finished = true;
            for (Point point : points) {
                finished &= point.minimiseDirections();
            }
            Log.d(tag, String.format("[minimiseDirections] minimisedDirections finished: %s", finished));
        } while(!finished);

        return points;
    }

    public boolean checkDirections(List<Point> points) {
        boolean result = true;
        for (Point point : points) {
            result &= point.getDirections().size() == 1;
        }
        Log.d(tag, String.format("[checkDirections] result: %s", result));
        return result;
    }

    private List<TrackPiece> findTrackPiecePathBetweenTwoPoints(Point p1, Point p2) {
        List<TrackPiece> resultTrackPieces = getProceduralTrackPiecePathBetweenTwoPoints(p1, p2);
        if (resultTrackPieces != null)
            return resultTrackPieces;
        return getSimpleTrackPiecePathBetweenTwoPoints(p1, p2);

        //TODO: check if generated track is valid with the directions
        //TODO: if not then call get getSimpleTrackPiecePathBetweenTwoPoints

    }

    private List<TrackPiece> getProceduralTrackPiecePathBetweenTwoPoints(Point p1, Point p2) {
        Log.d(tag, String.format("[getProceduralTrackPiecePathBetweenTwoPoints] p1: %s, p2: %s", p1, p2));
        if (p1.equals(p2)) {
            Log.d(tag, "[getProceduralTrackPiecePathBetweenTwoPoints] Equal points provided");
            return null;
        }

        List<TrackPiece> resultTrackPieces;

        int x_diff = Math.abs(p2.getX() - p1.getX());
        int y_diff = Math.abs(p2.getY() - p1.getY());

        int new_x = ((p1.getX() + p2.getX()) / 2);
        int new_y = ((p1.getY() + p2.getY()) / 2);

        int x_direction = Integer.compare(p2.getX(), p1.getX());
        int y_direction = Integer.compare(p2.getY(), p1.getY());

        boolean xy_direction = p1.getDirection() != null && p2.getDirection() != null ? DirectionClass.isClockwise(p1.getDirection(), p2.getDirection()) : x_direction * y_direction == 1;

        if (x_diff == 0 || y_diff == 0) {
            return getSimpleStraight(p1, p2);
        } else if (x_diff == y_diff && DirectionClass.arePerpendicular(p1.getDirection(), p2.getDirection())) {
            TrackPiece curvedTrackPiece = Helper.getCurvedTrackPieceByLength(x_diff, xy_direction);
            Log.d(tag, String.format("[getProceduralTrackPiecePathBetweenTwoPoints] curvedTrackPiece of length: %d, xy_direction: %s", x_diff, xy_direction));
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
        newPoint.setPrevious(p1);
        newPoint.setNext(p2);
        Log.d(tag, String.format("[getProceduralTrackPiecePathBetweenTwoPoints] newPoint: %s, direction: %s", newPoint, newPoint.getDirection()));

        if (!(canInputCurvedTrackPiece(p1, newPoint) && canInputCurvedTrackPiece(newPoint, p2))) {
            Log.d(tag, "[getProceduralTrackPiecePathBetweenTwoPoints] New point is invalid because CurvedPiece cannot be inputted");

            if (DirectionClass.isXAxis(p1.getDirection())) {
                new_y = p1.getY() + Consts.MIN_LENGTH_OF_CURVED_TRACK_PIECE * y_direction;
                newPoint.setX(p2.getX());
                newPoint.setY(new_y);
            } else {
                new_x = p1.getX() + Consts.MIN_LENGTH_OF_CURVED_TRACK_PIECE * x_direction;
                newPoint.setX(new_x);
                newPoint.setY(p2.getY());
            }
            Log.d(tag, String.format("[getProceduralTrackPiecePathBetweenTwoPoints] after update newPoint: %s", newPoint));
        }

        newPoint.minimisePointDirections();
        if (newPoint.getDirection() == null) {
            Log.d(tag, String.format("[getProceduralTrackPiecePathBetweenTwoPoints] newPoint: %s has null direction!!! previous: %s, next: %s", newPoint, newPoint.getPrevious(), newPoint.getNext()));
        }

        if (newPoint.equals(p1) || newPoint.equals(p2)) {
            Log.d(tag, "[getProceduralTrackPiecePathBetweenTwoPoints] newPoint has not been changed");
            return null;
        }

        List<TrackPiece> trackPiecesToNewPoint = getProceduralTrackPiecePathBetweenTwoPoints(p1, newPoint);
        if (trackPiecesToNewPoint == null)
            return null;

        List<TrackPiece> trackPiecesFromNewPoint = getProceduralTrackPiecePathBetweenTwoPoints(newPoint, p2);
        if (trackPiecesFromNewPoint == null)
            return null;

        resultTrackPieces = TrackPiece.joinTwoLists(trackPiecesToNewPoint, trackPiecesFromNewPoint);
        Log.d(tag, String.format("[getProceduralTrackPiecePathBetweenTwoPoints] startPoint: %s, endPoint: %s, resultTrackPieces: %s", p1, p2, resultTrackPieces));
        if (!validateCurrentTrack(p1, p2, resultTrackPieces)) {
            Log.d(tag, String.format("[getProceduralTrackPiecePathBetweenTwoPoints] TRACK IS INALID resultTrackPieces: %s", resultTrackPieces));
            return null;
            //resultTrackPieces = rotateCurrentTrack(p1, p2, trackPiecesToNewPoint, trackPiecesFromNewPoint);
            //Log.d(tag, String.format("[findTrackPiecePathBetweenTwoPoints] startPoint: %s, endPoint: %s, VALIDATED resultTrackPieces: %s", p1, p2, resultTrackPieces));
        }
        return resultTrackPieces;

    }

    private List<TrackPiece> getSimpleTrackPiecePathBetweenTwoPoints(Point p1, Point p2) {
        List<TrackPiece> resultTrackPieces = new ArrayList<>();

        Log.d(tag, String.format("[getSimpleTrackPiecePathBetweenTwoPoints] p1: %s, p2: %s", p1, p2));

        int x_diff = Math.abs(p2.getX() - p1.getX());
        int y_diff = Math.abs(p2.getY() - p1.getY());

        int x_direction = p1.getX() <= p2.getX() ? 1 : -1;
        int y_direction = p1.getY() <= p2.getY() ? 1 : -1;

        int min_x_y = Point.min(p1, p2);

        if (DirectionClass.arePerpendicular(p1.getDirection(), p2.getDirection()) && min_x_y >= Consts.MIN_LENGTH_OF_CURVED_TRACK_PIECE) {
            return getSimpleTurnOnly(p1, p2);
        } else if (DirectionClass.areEqual(p1.getDirection(), p2.getDirection()) && min_x_y >= Consts.MIN_DOUBLE_TURN_RADIUS) {
            return getSimpleDoubleTurn(p1, p2);
        } else if (x_diff == 0 || y_diff == 0) {
            return getSimpleStraight(p1, p2);
        }

        Log.d(tag, String.format("[getSimpleTrackPiecePathBetweenTwoPoints] Unsupported simple operation p1: %s, direction1: %s, p2: %s, direction2: %s", p1, p1.getDirection(), p2, p2.getDirection()));

        return null;
        //throw new RuntimeException(String.format("[getSimpleTrackPiecePathBetweenTwoPoints] Unsupported simple operation p1: %s, direction1: %s, p2: %s, direction2: %s", p1, p1.getDirection(), p2, p2.getDirection()));
    }

    private List<TrackPiece> getSimpleTurnOnly(Point p1, Point p2) {

        Log.d(tag, String.format("[getSimpleTurnOnly] p1: %s, p2: %s", p1, p2));

        List<TrackPiece> resultTrackPieces = new ArrayList<>();

        int min_x_y = Point.min(p1, p2);

        int x_new_1, y_new_1, x_new_2, y_new_2;

        int maxCurvedTrackPieceLength = min_x_y >= Consts.MAX_LENGTH_OF_CURVED_TRACK_PIECE ? Consts.MAX_LENGTH_OF_CURVED_TRACK_PIECE : Consts.MIN_LENGTH_OF_CURVED_TRACK_PIECE;

        if (DirectionClass.isXAxis(p1.getDirection())) {

            x_new_1 = p2.getX() - maxCurvedTrackPieceLength * DirectionClass.getXDirectionAsInt(p1.getDirection());
            y_new_1 = p1.getY();
            x_new_2 = p2.getX();
            y_new_2 = p1.getY() + maxCurvedTrackPieceLength * DirectionClass.getYDirectionAsInt(p2.getDirection());

        } else {

            x_new_1 = p1.getX();
            y_new_1 = p2.getY() - maxCurvedTrackPieceLength * DirectionClass.getYDirectionAsInt(p1.getDirection());
            x_new_2 = p1.getX() + maxCurvedTrackPieceLength * DirectionClass.getXDirectionAsInt(p2.getDirection());
            y_new_2 = p2.getY();

        }

        Point new_1 = new Point(x_new_1, y_new_1, p1.getDirection());
        Point new_2 = new Point(x_new_2, y_new_2, p2.getDirection());

        Log.d(tag, String.format("[getSimpleTurnOnly] new1: %s, new_2: %s", new_1, new_2));

        List<TrackPiece> trackPieces1 = getProceduralTrackPiecePathBetweenTwoPoints(p1, new_1);
        List<TrackPiece> trackPieces2 = getProceduralTrackPiecePathBetweenTwoPoints(new_1, new_2);
        List<TrackPiece> trackPieces3 = getProceduralTrackPiecePathBetweenTwoPoints(new_2, p2);

        Log.d(tag, String.format("[getSimpleTurnOnly] trackPieces1: %s, trackPieces2: %s, trackPieces3: %s", trackPieces1, trackPieces2, trackPieces3));

        resultTrackPieces = TrackPiece.joinTwoLists(resultTrackPieces, trackPieces1);
        resultTrackPieces = TrackPiece.joinTwoLists(resultTrackPieces, trackPieces2);
        resultTrackPieces = TrackPiece.joinTwoLists(resultTrackPieces, trackPieces3);

        Log.d(tag, String.format("[getSimpleTurnOnly] resultTrackPieces: %s", resultTrackPieces));

        return resultTrackPieces;
    }

    private List<TrackPiece> getSimpleDoubleTurn(Point p1, Point p2) {

        Log.d(tag, String.format("[getSimpleDoubleTurn] p1: %s, p2: %s", p1, p2));

        List<TrackPiece> resultTrackPieces = new ArrayList<>();

        DirectionClass.Direction newDirection;

        if (DirectionClass.isXAxis(p1.getDirection())) {
            if (p1.getY() < p2.getY()) {
                newDirection = DirectionClass.Direction.UP;
            } else {
                newDirection = DirectionClass.Direction.DOWN;
            }
        } else {
            if (p1.getX() < p2.getX()) {
                newDirection = DirectionClass.Direction.RIGHT;
            } else {
                newDirection = DirectionClass.Direction.LEFT;
            }
        }

        int x_avg = (p1.getX() + p2.getX()) / 2;
        int y_avg = (p1.getY() + p2.getY()) / 2;

        int min_x_y = Point.min(p1, p2);

        int maxDoubleTurnRadius = (min_x_y >= Consts.MAX_DOUBLE_TURN_RADIUS ? Consts.MAX_DOUBLE_TURN_RADIUS : Consts.MIN_DOUBLE_TURN_RADIUS) / 2;

        int x_new_1 = DirectionClass.isXAxis(p1.getDirection()) ? x_avg : p1.getX() + maxDoubleTurnRadius * DirectionClass.getXDirectionAsInt(newDirection);
        int y_new_1 = DirectionClass.isYAxis(p1.getDirection()) ? y_avg : p1.getY() + maxDoubleTurnRadius * DirectionClass.getYDirectionAsInt(newDirection);
        int x_new_2 = DirectionClass.isXAxis(p2.getDirection()) ? x_avg : p2.getX() - maxDoubleTurnRadius * DirectionClass.getXDirectionAsInt(newDirection);
        int y_new_2 = DirectionClass.isYAxis(p2.getDirection()) ? y_avg : p2.getY() - maxDoubleTurnRadius * DirectionClass.getYDirectionAsInt(newDirection);


        Point new_1 = new Point(x_new_1, y_new_1, newDirection);
        Point new_2 = new Point(x_new_2, y_new_2, newDirection);

        Log.d(tag, String.format("[getSimpleDoubleTurn] new1: %s, new_2: %s", new_1, new_2));

        List<TrackPiece> trackPieces1 = getSimpleTurnOnly(p1, new_1);
        List<TrackPiece> trackPieces2 = getSimpleStraight(new_1, new_2);
        List<TrackPiece> trackPieces3 = getSimpleTurnOnly(new_2, p2);

        Log.d(tag, String.format("[getSimpleDoubleTurn] trackPieces1: %s, trackPieces2: %s, trackPieces3: %s", trackPieces1, trackPieces2, trackPieces3));

        resultTrackPieces = TrackPiece.joinTwoLists(resultTrackPieces, trackPieces1);
        resultTrackPieces = TrackPiece.joinTwoLists(resultTrackPieces, trackPieces2);
        resultTrackPieces = TrackPiece.joinTwoLists(resultTrackPieces, trackPieces3);

        Log.d(tag, String.format("[getSimpleDoubleTurn] resultTrackPieces: %s", resultTrackPieces));

        return resultTrackPieces;
    }

    private List<TrackPiece> getSimpleStraight(Point p1, Point p2) {
        Log.d(tag, String.format("[getSimpleStraight] p1: %s, p2: %s", p1, p2));
        if (p1.equals(p2)) {
            Log.d(tag, "Equal points provided");
            return null;
        }

        int x_diff = Math.abs(p2.getX() - p1.getX());
        int y_diff = Math.abs(p2.getY() - p1.getY());

        TrackPiece straightTrackPiece = Helper.getStraightTrackPieceByLength(x_diff + y_diff);
        Log.d(tag, String.format("[getSimpleStraight] straightTrackPiece: %s, of length: %d", straightTrackPiece, x_diff + y_diff));
        if (straightTrackPiece != null) {
            return Collections.singletonList(straightTrackPiece);
        }

        List<TrackPiece> resultTrackPieces = new ArrayList<>();

        int x_new = (p1.getX() + p2.getX()) / 2;
        int y_new = (p1.getY() + p2.getY()) / 2;

        Point newPoint = new Point(x_new, y_new, p1.getDirection());

        Log.d(tag, String.format("[getSimpleStraight] newPoint: %s", newPoint));

        List<TrackPiece> trackPieces1 = getSimpleStraight(p1, newPoint);
        List<TrackPiece> trackPieces2 = getSimpleStraight(newPoint, p2);

        Log.d(tag, String.format("[getSimpleStraight] trackPieces1: %s, trackPieces2: %s", trackPieces1, trackPieces2));

        resultTrackPieces = TrackPiece.joinTwoLists(resultTrackPieces, trackPieces1);
        resultTrackPieces = TrackPiece.joinTwoLists(resultTrackPieces, trackPieces2);

        Log.d(tag, String.format("[getSimpleStraight] resultTrackPieces: %s", resultTrackPieces));

        return resultTrackPieces;
    }

    private boolean canInputCurvedTrackPiece(Point p1, Point p2) {
        int x_diff = Math.abs(p1.getX() - p2.getX());
        int y_diff = Math.abs(p1.getY() - p2.getY());
        return (x_diff >= Consts.MIN_LENGTH_OF_CURVED_TRACK_PIECE && y_diff >= Consts.MIN_LENGTH_OF_CURVED_TRACK_PIECE) || (x_diff == 0 || y_diff == 0);
    }

    private Point tryInputCurvedTrackPiece(Point p1, Point p2) {
        List<Integer> curvedPiecesLength = Arrays.asList(Consts.MAX_LENGTH_OF_CURVED_TRACK_PIECE, Consts.MIN_LENGTH_OF_CURVED_TRACK_PIECE);

        int x_diff = Math.abs(p2.getX() - p1.getX());
        int y_diff = Math.abs(p2.getY() - p1.getY());

        int x_direction = Integer.compare(p2.getX(), p1.getX());
        int y_direction = Integer.compare(p2.getY(), p1.getY());

        for (int length : curvedPiecesLength) {
            if ((x_diff == length && y_diff > length) || (x_diff > length && y_diff == length)) {

                int x_new, y_new;

                if (x_diff == length) {
                   if (DirectionClass.isYAxis(p1.getDirection())) {
                       x_new = p1.getX();
                       y_new = p2.getY() - length * y_direction;
                   } else {
                       x_new = p2.getX();
                       y_new = p1.getY() + length * y_direction;
                   }
                } else {
                    if (DirectionClass.isXAxis(p1.getDirection())) {
                        x_new = p2.getX() - length * x_direction;
                        y_new = p1.getY();
                    } else {
                        x_new = p1.getX() + length * x_direction;
                        y_new = p2.getY();
                    }
                }

                Point newPoint = new Point(x_new, y_new, p1, p1);
                Log.d(tag, String.format("[tryInputCurvedTrackPiece] returning newPoint: %s", newPoint));
                return newPoint;
            }
        }

        Log.d(tag, "[tryInputCurvedTrackPiece] returning null");
        return null;
    }

    private boolean validateCurrentTrack(Point p1, Point p2, List<TrackPiece> trackPieces) {
        return validateCurrentTrack(p1, p2, trackPieces,false);
    }

    private boolean validateCurrentTrack(Point p1, Point p2, List<TrackPiece> trackPieces, boolean allDirections) {
        return validateCurrentTrack(p1, p2, trackPieces, null, allDirections);
    }

    private boolean validateCurrentTrack(Point p1, Point p2, List<TrackPiece> trackPiecesToNewPoint, List<TrackPiece> trackPiecesFromNewPoint, boolean allDirections) {
        if (trackPiecesToNewPoint == null) {
            return false;
        }
        List<TrackPiece> trackPieces = TrackPiece.joinTwoLists(trackPiecesToNewPoint, trackPiecesFromNewPoint);
/*        if (trackPiecesFromNewPoint == null) {
            trackPieces = trackPiecesToNewPoint;
        } else {
            trackPieces = Stream.concat(trackPiecesToNewPoint.stream(), trackPiecesFromNewPoint.stream()).collect(Collectors.toList());
        }*/

        Log.d(tag, String.format("[validateCurrentTrack] p1: %s, p1.direction: %s, p2: %s, p2.direction: %s, trackPieces: %s", p1, p1.getDirection(), p2, p2.getDirection(), trackPieces));

        List<Double> angles = new ArrayList<>();
        angles.add(DirectionClass.getDirectionAsAngle(p1.getDirection()));
        double targetAngle = DirectionClass.getDirectionAsAngle(p2.getDirection());

/*        if (allDirections) {
            angles.add((double) 0);
            angles.add(Math.PI * (1 / 2.f));
            angles.add(Math.PI);
            angles.add(Math.PI * (3 / 2.f));
        } else {
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
        }*/

        Log.d(tag, String.format("[validateCurrentTrack] angles: %s", angles));

        for (double currentAngle : angles) {
            double x = p1.getX();
            double y = p1.getY();
            double angle = currentAngle;
            for (TrackPiece trackPiece : trackPieces) {
                x += trackPiece.getLength() * Math.cos(angle + trackPiece.getAngle());
                y += trackPiece.getLength() * Math.sin(angle + trackPiece.getAngle());
                angle += 2 * trackPiece.getAngle();
                Log.d(tag, String.format("[validateCurrentTrack] trackPiece: %s, p: (%.3f, %.3f), x: %.3f, y: %.3f, angle: %.3f", trackPiece, x, y, x, y, angle));
            }
            Log.d(tag, String.format("[validateCurrentTrack] trackPieces: %s, x: %.3f, x_end: %d, y: %.3f, y_end: %d, angle: %.3f", trackPieces, x, p2.getX(), y, p2.getY(), angle));
            if (Helper.compareTwoDoubles(x, p2.getX()) && Helper.compareTwoDoubles(y, p2.getY()) && DirectionClass.compareDirectionWithAngle(p2.getDirection(), angle)) {
                Log.d(tag, "[validateCurrentTrack] Returning true");
                return true;
            }
        }
        Log.d(tag, "[validateCurrentTrack] Returning false");
        return false;
    }

    private List<TrackPiece> rotateCurrentTrack(Point p1, Point p2, List<TrackPiece> trackPiecesToNewPoint, List<TrackPiece> trackPiecesFromNewPoint) {
        return rotateCurrentTrack(p1, p2, trackPiecesToNewPoint, trackPiecesFromNewPoint, false);
    }

    private List<TrackPiece> rotateCurrentTrack(Point p1, Point p2, List<TrackPiece> trackPiecesToNewPoint, List<TrackPiece> trackPiecesFromNewPoint, boolean allDirections) {
        List<TrackPiece> rotatedTrackPiecesToNewPoint = rotateCurrentTrack(trackPiecesToNewPoint);
        List<TrackPiece> rotatedTrackPiecesFromNewPoint = rotateCurrentTrack(trackPiecesFromNewPoint);

        if (validateCurrentTrack(p1, p2, rotatedTrackPiecesToNewPoint, trackPiecesFromNewPoint, allDirections)) {
            return Stream.concat(rotatedTrackPiecesToNewPoint.stream(), trackPiecesFromNewPoint.stream()).collect(Collectors.toList());
        } else if (validateCurrentTrack(p1, p2, trackPiecesToNewPoint, rotatedTrackPiecesFromNewPoint, allDirections)) {
            return Stream.concat(trackPiecesToNewPoint.stream(), rotatedTrackPiecesFromNewPoint.stream()).collect(Collectors.toList());
        } else if (validateCurrentTrack(p1, p2, rotatedTrackPiecesToNewPoint, rotatedTrackPiecesFromNewPoint, allDirections)) {
            return Stream.concat(rotatedTrackPiecesToNewPoint.stream(), rotatedTrackPiecesFromNewPoint.stream()).collect(Collectors.toList());
        }
        return null;
    }

    private List<TrackPiece> rotateCurrentTrack(List<TrackPiece> trackPieces) {
        Log.d(tag, String.format("[rotateCurrentTrack] before rotation trackPieces: %s", trackPieces));

        List<TrackPiece> rotatedTrackPieces = flipCurrentTrack(trackPieces);
        Collections.reverse(rotatedTrackPieces);

        Log.d(tag, String.format("[rotateCurrentTrack] after rotation trackPieces: %s", rotatedTrackPieces));
        return rotatedTrackPieces;
    }

    private List<TrackPiece> flipCurrentTrack(List<TrackPiece> trackPieces) {
        Log.d(tag, String.format("[flipCurrentTrack] before flip trackPieces: %s", trackPieces));
        List<TrackPiece> flippedTrackPieces = new ArrayList<>();

        for (TrackPiece trackPiece : trackPieces) {
            TrackPiece flippedTrackPiece = Helper.getTrackPieceById(trackPiece.getId());
            if (Helper.compareTwoDoubles(flippedTrackPiece.getAngle(), trackPiece.getAngle())) {
                flippedTrackPiece.rotate();
            }
            flippedTrackPieces.add(flippedTrackPiece);
        }

        Log.d(tag, String.format("[flipCurrentTrack] after flip trackPieces: %s", flippedTrackPieces));
        return flippedTrackPieces;
    }
}
