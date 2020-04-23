package com.montes.trackz.pieces;

import android.graphics.Color;
import android.graphics.Paint;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.montes.trackz.util.Consts;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import androidx.annotation.NonNull;

public abstract class TrackPiece {

    private String id;
    private String name;
    private double length;
    private int color;

    public TrackPiece(String id, String name, double length) {
        this.id = id;
        this.name = name;
        this.length = length;
        this.color = Color.argb(0x00, 0x00, 0x00, 0x00);
    }

    public TrackPiece(String id, String name, double length, int color) {
        this.id = id;
        this.name = name;
        this.length = length;
        this.color = color;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLength() {
        return this.length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getAngle() {
        return 0;
    }

    public int getLevels() {
        return 0;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int colorUp() {
        return this.colorUp(Consts.COLOR_UPDATE, Consts.COLOR_UPDATE, Consts.COLOR_UPDATE);
    }

    public int colorUp(int red, int green, int blue) {
        return this.updateColor(red, green, blue, 1);
    }

    public int colorDown() {
        return this.colorDown(Consts.COLOR_UPDATE, Consts.COLOR_UPDATE, Consts.COLOR_UPDATE);
    }

    public int colorDown(int red, int green, int blue) {
        return this.updateColor(red, green, blue, -1);
    }

    public int updateColor(int red, int green, int blue, int flag) {
        return this.color + Color.argb(Consts.COLOR_ALPHA, red, green, blue) * flag;
    }

    public void rotate() {};

    @NonNull
    @Override
    public String toString() {
        return this.id;
    }

    public static List<TrackPiece> joinTwoLists(List<TrackPiece> trackPieces1, List<TrackPiece> trackPieces2) {
        if (trackPieces2 == null)
            return trackPieces1;
        return Stream.concat(trackPieces1.stream(), trackPieces2.stream()).collect(Collectors.toList());
    }

    public LineGraphSeries<DataPoint> getTrackPieceAsCurve(double x, double y, double angle, int levels) {
        return null;
    }

}
