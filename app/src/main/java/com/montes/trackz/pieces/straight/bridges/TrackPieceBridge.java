package com.montes.trackz.pieces.straight.bridges;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.montes.trackz.pieces.straight.TrackPieceStraight;
import com.montes.trackz.util.Consts;
import com.montes.trackz.util.Helper;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public abstract class TrackPieceBridge extends TrackPieceStraight {

    private int levels;

    TrackPieceBridge(String id, String name, double length, int levels) {
        super(id, name, length);
        this.levels = levels;
    }

    TrackPieceBridge(String id, String name, double length, int levels, int color) {
        super(id, name, length, color);
        this.levels = levels;
    }

    TrackPieceBridge(String id, String name, double length, int levels, boolean upOrDown) {
        super(id, name, length);
        this.levels = upOrDown ? levels : -levels;
    }

    TrackPieceBridge(String id, String name, double length, int levels, boolean upOrDown, int color) {
        super(id, name, length, color);
        this.levels = upOrDown ? levels : -levels;
    }

    @Override
    public int getLevels() {
        return this.levels;
    }

    public void setLevels(int levels) {
        this.levels = levels;
    }

    public void setLevels(boolean upOrDown) {
        this.levels = upOrDown ? 1 : -1;
    }

    private int setAndReturnColorUp() {
        return setAndReturnColor(1);
    }

    private int setAndRetrunColorDown() {
        return setAndReturnColor(-1);
    }

    private int setAndReturnColor(int flag) {
        int color = this.getColor();
        this.setColor(color + Color.argb(0, 0x02, 0x02, 0x02) * flag);
        return color;
    }

    @NonNull
    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        return String.format("%s%c", this.getId(), this.levels >= 0 ? '+' : '-');
    }

    public List<LineGraphSeries<DataPoint>> getTrackPieceBridgeAsCurve(double x, double y, double angle, int levels) {
        double x_end = x + this.getLength() * Math.cos(angle + this.getAngle());
        double y_end = y + this.getLength() * Math.sin(angle + this.getAngle());

        double pieces = Consts.COLOR_UPDATE / 2.f;

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

        if (Helper.compareTwoDoubles(x_start, x_end)) {
            if (y_start < y_end) {
                y_start += Consts.SERIES_STRAIGHT_DISTANCE;
                y_end -= Consts.SERIES_STRAIGHT_DISTANCE;
            } else {
                y_start -= Consts.SERIES_STRAIGHT_DISTANCE;
                y_end += Consts.SERIES_STRAIGHT_DISTANCE;
            }
        } else {
            x_start += Consts.SERIES_STRAIGHT_DISTANCE;
            x_end -= Consts.SERIES_STRAIGHT_DISTANCE;
        }

        double x_delta = (x_end - x_start) / pieces;
        double y_delta = (y_end - y_start) / pieces;
        x = x_start;
        y = y_start;
        List<LineGraphSeries<DataPoint>> lineGraphSeriesList = new ArrayList<>();
        if (levels > 0) {
            this.setColor(this.colorUp());
        }
        while (x + x_delta * 2 < x_end && y + y_delta * 2 < y_end) {
            LineGraphSeries<DataPoint> lineGraphSeries = new LineGraphSeries<>(new DataPoint[] {
                    new DataPoint(x, y),
                    new DataPoint(x + x_delta, y + y_delta)
            });
            lineGraphSeries.setColor(levels == 0 ? setAndReturnColorUp() : setAndRetrunColorDown());
            lineGraphSeries.setThickness(Consts.LINE_THICKNESS);
            lineGraphSeries.setAnimated(true);
            lineGraphSeriesList.add(lineGraphSeries);
            x += x_delta * 2;
            y += y_delta * 2;
            //Log.d(tag, String.format("[getTrackPieceBridgeAsCurve] x: %.3f, "))
        }
        LineGraphSeries<DataPoint> lineGraphSeries = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(x_end, y_end),
        });
        lineGraphSeries.setColor(levels == 0 ? this.getColor() : this.colorUp());
        lineGraphSeries.setThickness(Consts.LINE_THICKNESS);
        lineGraphSeries.setAnimated(true);
        lineGraphSeriesList.add(lineGraphSeries);

        return lineGraphSeriesList;
    }

}
