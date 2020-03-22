package com.montes.trackz.pieces.curves;

import com.montes.trackz.pieces.TrackPiece;

public abstract class TrackPieceCurve extends TrackPiece {

    private double angle;

    public TrackPieceCurve(String id, String name, double length, double angle) {
        super(id, name, length);
        this.angle = angle;
    }

    public TrackPieceCurve(String id, String name, double length, double angle, boolean clockwise) {
        super(id, name, length);
        this.angle = clockwise ? angle : -angle;
    }

    @Override
    public double getAngle() {
        return this.angle;
    }
}
