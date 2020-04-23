package com.montes.trackz.pieces.curved;

import android.graphics.Point;
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
        return String.format("%s%c", this.getId(), this.angle <= 0 ? 'Z' : 'P');
    }

    @Override
    public LineGraphSeries<DataPoint> getTrackPieceAsCurve(double x, double y, double angle, int levels) {
        double x_end = x + this.getLength() * Math.cos(angle + this.getAngle());
        double y_end = y + this.getLength() * Math.sin(angle + this.getAngle());

        DataPoint circleCenter = getCircleCenter(x, y, x_end, y_end, angle);
        Log.d(tag, String.format("[getTrackPieceAsCurve] x_center: %.3f, y_center: %.3f", circleCenter.getX(), circleCenter.getY()));

        double x_start;
        double y_start;

        if (x <= x_end) {
            x_start = x;
            y_start = y;
        } else {
            x_start = x_end;
            x_end = x;
            y_start = y_end;
            y_end = y;
        }

        LineGraphSeries<DataPoint> lineGraphSeries = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(x_start, y_start),
                new DataPoint(x_end, y_end)
        });
        lineGraphSeries.setColor(levels == 0 ? this.getColor() : this.colorUp());
        lineGraphSeries.setThickness(Consts.LINE_THICKNESS);
        lineGraphSeries.setAnimated(true);

        return lineGraphSeries;
    }

    private DataPoint getCircleCenter(double x1, double y1, double x2, double y2, double angle) {
        double x_center;
        double y_center;
        //Log.d(tag, String.format("[getCircleCenter] x1: %.3f, y1: %.3f, x2: %.3f, y2: %.3f", x1, y1, x2, y2));

        if (Helper.compareDoubleAndInteger(x1, (int) x1) || Helper.compareDoubleAndInteger(y1, (int) y1)) {
            Log.d(tag, String.format("[getCircleCenter] x1: %.3f, y1: %.3f", x1, y1));
        } else {
            Log.d(tag, String.format("[getCircleCenter] x2: %.3f, y2: %.3f", x2, y2));
            x1 = x2;
            y1 = y2;
            angle += 2 * this.getAngle();
        }

        double x_middle = x1 + this.getLength() * Math.cos(angle + this.getAngle());
        double y_middle = y1 + this.getLength() * Math.sin(angle + this.getAngle());
        angle += 2 * this.getAngle();
        x2 = x_middle + this.getLength() * Math.cos(angle + this.getAngle());
        y2 = y_middle  + this.getLength() * Math.sin(angle + this.getAngle());
        double x_avg = (x1 + x2) / 2;
        double y_avg = (y1 + y2) / 2;
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

}
