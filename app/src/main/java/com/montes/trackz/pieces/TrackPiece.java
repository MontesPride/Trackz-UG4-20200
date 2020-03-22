package com.montes.trackz.pieces;

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

    public String getName() {
        return this.name;
    }

    public double getLength() {
        return this.length;
    }

    public double getAngle() {
        return 0;
    }

    public int getLevels() {
        return 0;
    }

}
