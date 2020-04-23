package com.montes.trackz.tracks;

import android.util.Log;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.montes.trackz.generators.procedural.DirectionClass;
import com.montes.trackz.generators.procedural.Point;
import com.montes.trackz.pieces.TrackPiece;

import java.util.ArrayList;
import java.util.List;

public class TrackImpl implements Track {
    private static final String tag = "TrackImpl";

    private List<TrackPiece> trackPieces;
    private Point startingPoint;
    private Point bottomLeft;
    private Point topRight;

    public TrackImpl(List<TrackPiece> trackPieces) {
        this(trackPieces, new Point(0,0, DirectionClass.Direction.RIGHT));
        //Log.d(tag, String.format("[TrackImpl] trackPieces: %s", trackPieces));
    }

    public TrackImpl(List<TrackPiece> trackPieces, Point startingPoint) {
        this(trackPieces, startingPoint, null, null);
    }

    public TrackImpl(List<TrackPiece> trackPieces, Point startingPoint, Point bottomLeft, Point topRight) {
        this.trackPieces = trackPieces;
        this.startingPoint = startingPoint;
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
    }

    public Point getBottomLeft() {
        return bottomLeft;
    }

    public void setBottomLeft(Point bottomLeft) {
        this.bottomLeft = bottomLeft;
    }

    public Point getTopRight() {
        return topRight;
    }

    public void setTopRight(Point topRight) {
        this.topRight = topRight;
    }

    @Override
    public List<TrackPiece> getTrackPieces() {
        return this.trackPieces;
    }

    @Override
    public void printTrack() {
        Log.d(tag, "[printTrack]");
        System.out.println(getTrackString());
    }

    @Override
    public void printTrackList() {
        Log.d(tag, "[printTrackList]");
        System.out.println(getTrackListAsString());
    }

    @Override
    public String getTrackString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (TrackPiece trackPiece : this.trackPieces) {
            stringBuilder.append(trackPiece.toString());
        }
        //Log.d(tag, String.format("[getTrackString] trackString: %s", stringBuilder.toString()));
        return stringBuilder.toString();
    }

    @Override
    public String getTrackListAsString() {
        Log.d(tag, "[getTrackListAsString]");
        return this.trackPieces.toString();
    }

    @Override
    public List<LineGraphSeries<DataPoint>> getTrackAsCurve() {
        Log.d(tag, "[getTrackAsCurve] Getting Track as Curve");

        List<LineGraphSeries<DataPoint>> lineGraphSeriesList = new ArrayList<>();
        double x = this.startingPoint.getX();
        double y = this.startingPoint.getY();
        double angle = DirectionClass.getDirectionAsAngle(this.startingPoint.getDirection());
        int levels = 0;
        for (TrackPiece trackPiece : this.trackPieces) {
            //Log.d(tag, String.format("[getTrackAsCurve] (%.3f, %.3f) x: %.3f, y: %.3f, angle: %.3f, levels: %d", x, y, x, y, angle, levels));

            lineGraphSeriesList.add(trackPiece.getTrackPieceAsCurve(x, y, angle, levels));

            x += trackPiece.getLength() * Math.cos(angle + trackPiece.getAngle());
            y += trackPiece.getLength() * Math.sin(angle + trackPiece.getAngle());
            if (trackPiece.getAngle() != 0) {
                angle += 2 * trackPiece.getAngle();
            }
            levels += trackPiece.getLevels();
        }
        //Log.d(tag, String.format("[getTrackAsCurve] (%.3f, %.3f) x: %.3f, y: %.3f, angle: %.3f, levels: %d", x, y, x, y, angle, levels));
        return lineGraphSeriesList;
    }

}
