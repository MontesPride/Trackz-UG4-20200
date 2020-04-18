package com.montes.trackz.pieces.curved;

import com.montes.trackz.pieces.TrackPiece;

import androidx.annotation.NonNull;

public abstract class TrackPieceCurved extends TrackPiece {

    private double angle;

    public TrackPieceCurved(String id, String name, double length, double angle) {
        super(id, name, length);
        this.angle = angle;
    }

    public TrackPieceCurved(String id, String name, double length, double angle, boolean clockwise) {
        super(id, name, length);
        this.angle = clockwise ? -angle : angle;
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
}
