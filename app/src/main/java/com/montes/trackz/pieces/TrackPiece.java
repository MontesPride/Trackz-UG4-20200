package com.montes.trackz.pieces;

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

    @NonNull
    @Override
    public String toString() {
        return this.id;
    }
}
