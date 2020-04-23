package com.montes.trackz.pieces.curved;

import android.util.Log;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.montes.trackz.generators.procedural.Point;
import com.montes.trackz.pieces.TrackPiece;
import com.montes.trackz.util.Consts;

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

}
