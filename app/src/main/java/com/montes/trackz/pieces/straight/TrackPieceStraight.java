package com.montes.trackz.pieces.straight;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.montes.trackz.pieces.TrackPiece;
import com.montes.trackz.util.Consts;

public abstract class TrackPieceStraight extends TrackPiece {

    public TrackPieceStraight(String id, String name, double length) {
        super(id, name, length);
    }

    public TrackPieceStraight(String id, String name, double length, int color) {
        super(id, name, length, color);
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
