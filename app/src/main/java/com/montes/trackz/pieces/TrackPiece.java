package com.montes.trackz.pieces;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import androidx.annotation.NonNull;

public abstract class TrackPiece {

    private String id;
    private String name;
    private double length;

    public TrackPiece(String id, String name, double length) {
        this.id = id;
        this.name = name;
        this.length = length;
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

}
