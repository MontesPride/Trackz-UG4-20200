package com.montes.trackz.pieces.curved;

import android.graphics.Point;
import android.provider.ContactsContract;
import android.util.Log;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.montes.trackz.pieces.TrackPiece;
import com.montes.trackz.util.Consts;
import com.montes.trackz.util.Helper;

import androidx.annotation.NonNull;

public abstract class TrackPieceCurved extends TrackPiece {
    private static final String tag = "TrackPieceCurved";

    private double angle;

    public TrackPieceCurved(String id, String name, double length, double angle) {
        super(id, name, length);
        this.angle = angle;
    }

    public TrackPieceCurved(String id, String name, double length, double angle, int color) {
        super(id, name, length, color);
        this.angle = angle;
    }

    public TrackPieceCurved(String id, String name, double length, double angle, boolean clockwise) {
        super(id, name, length);
        this.angle = clockwise ? -angle : angle;
    }

    public TrackPieceCurved(String id, String name, double length, double angle, boolean clockwise, int color) {
        super(id, name, length, color);
        this.angle = clockwise ? -angle : angle;
    }

    public double getStraightLength() {
        return this.getLength() / Math.cos((Math.PI - Consts.RADIANS_IN_CURVE) / 2) / 2;
    }

    @Override
    public double getAngle() {
        return this.angle;
    }

    @Override
    public void rotate() {
        this.angle *= -1;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s%c", this.getId(), this.angle <= 0 ? 'R' : 'L');
    }

    @Override
    public LineGraphSeries<DataPoint> getTrackPieceAsCurve(double x, double y, double angle, int levels) {
        double x_end = x + this.getLength() * Math.cos(angle + this.getAngle());
        double y_end = y + this.getLength() * Math.sin(angle + this.getAngle());

        DataPoint circleCenter = getCircleCenter(this, x, y, x_end, y_end, angle);
        //Log.d(tag, String.format("[getTrackPieceAsCurve] x_center: %.3f, y_center: %.3f", circleCenter.getX(), circleCenter.getY()));

        double nextAngle = angle + 2 * this.getAngle();
        //Log.d(tag, String.format("[getTrackPieceAsCurve] angle: %.3f, nextAngle: %.3f", angle, nextAngle));

        boolean upOrDown = Helper.upOrDown(angle, nextAngle);

        double x_temp = x, x_temp_end = x_end;
        x_end = Math.max(x_temp, x_temp_end);
        x = Math.min(x_temp, x_temp_end);
        if (!Helper.compareTwoDoubles(x_end, x_temp_end))
            upOrDown = !upOrDown;

        LineGraphSeries<DataPoint> lineGraphSeries = new LineGraphSeries<>();
        x += Consts.SERIES_CURVE_DISTANCE;
        while (x < x_end - Consts.SERIES_CURVE_DISTANCE) {
            y = getY(x, this.getStraightLength(), circleCenter.getX(), circleCenter.getY(), upOrDown);
            lineGraphSeries.appendData(new DataPoint(x, y), true, Consts.SERIES_MAX_COUNT);
            x += Consts.SERIES_STEP;
        }
        lineGraphSeries.appendData(new DataPoint(x_end, getY(x_end, this.getStraightLength(), circleCenter.getX(), circleCenter.getY(), upOrDown)), true, Consts.SERIES_MAX_COUNT);

        lineGraphSeries.setColor(levels == 0 ? this.getColor() : this.colorUp());
        lineGraphSeries.setThickness(Consts.LINE_THICKNESS);
        lineGraphSeries.setAnimated(true);

        return lineGraphSeries;
    }

    private static DataPoint getCircleCenter(TrackPieceCurved trackPieceCurved, double x1, double y1, double x2, double y2, double angle) {
        double x_center;
        double y_center;
        //Log.d(tag, String.format("[getCircleCenter] x1: %.3f, y1: %.3f, x2: %.3f, y2: %.3f", x1, y1, x2, y2));

        if (!(Helper.compareDoubleAndInteger(x1, (int) x1) || Helper.compareDoubleAndInteger(y1, (int) y1))) {
            x1 = x2;
            y1 = y2;
            angle += 2 * trackPieceCurved.getAngle();
        }

        double x_middle = x1 + trackPieceCurved.getLength() * Math.cos(angle + trackPieceCurved.getAngle());
        double y_middle = y1 + trackPieceCurved.getLength() * Math.sin(angle + trackPieceCurved.getAngle());
        angle += 2 * trackPieceCurved.getAngle();
        x2 = x_middle + trackPieceCurved.getLength() * Math.cos(angle + trackPieceCurved.getAngle());
        y2 = y_middle  + trackPieceCurved.getLength() * Math.sin(angle + trackPieceCurved.getAngle());
        double x_avg = (x1 + x2) / 2;
        //double y_avg = (y1 + y2) / 2;
        if (x1 <= x2) {
            if (y1 <= y2) {
                if (x_middle >= x_avg) {
                    x_center = x1;
                    y_center = y2;
                } else {
                    x_center = x2;
                    y_center = y1;
                }
            } else {
                if (x_middle >= x_avg) {
                    x_center = x1;
                    y_center = y2;
                } else {
                    x_center = x2;
                    y_center = y1;
                }
            }
        } else {
            if (y1 <= y2) {
                if (x_middle >= x_avg) {
                    x_center = x2;
                    y_center = y1;
                } else {
                    x_center = x1;
                    y_center = y2;
                }
            } else {
                if (x_middle >= x_avg) {
                    x_center = x2;
                    y_center = y1;
                } else {
                    x_center = x1;
                    y_center = y2;
                }
            }
        }

        //Log.d(tag, String.format("[getCircleCenter] x1: %.3f, y1: %.3f, x_middle: %.3f, y_middle: %.3f, x2: %.3f, y2: %.3f", x1, y1, x_middle, y_middle, x2, y2));
        //Log.d(tag, String.format("[getCircleCenter] x_center: %.3f, y_center: %.3f", x_center, y_center));

        return new DataPoint(x_center, y_center);
    }

    private static double getY(double x, double r, double x_center, double y_center, boolean upOrDown) {
        //(x_center - x) ^ 2 + (y_center - y) ^ 2 = r^2
        //(y_center - y)^2 = r^2 - (x_center - x)^2
        //(y_center - y) = sqrt(r^2 - (x_center - x)^2)
        //y = y_center +- sqrt(r^2 - (x_center - x)^2)
        double sqrt_squared = r*r - (x_center - x)*(x_center - x);
        if (sqrt_squared < 0)
            sqrt_squared *= -1;
        double sqrt = Math.sqrt(sqrt_squared);
        return y_center + sqrt * (upOrDown ? 1 : -1);
    }

}
