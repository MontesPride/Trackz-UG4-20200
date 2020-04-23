package com.montes.trackz.pieces.curved;

import android.graphics.Color;

import com.montes.trackz.util.Consts;

public class TrackPieceE extends TrackPieceCurved {

    private static final String staticId = "E";
    private static final String staticName = "Large Curve";
    private static final double staticLength = 2 * (Consts.UNIT * 4) * Math.cos((Math.PI - Consts.RADIANS_IN_CURVE) / 2);
    private static final double staticStraightLength = Consts.UNIT * 4;
    private static final double staticAngle = Consts.RADIANS_IN_CURVE / 2;
    private static final int staticLevels = 0;
    private static final int staticColor = Color.argb(Consts.COLOR_ALPHA, 0xBB, 0x55, 0x55);

    public TrackPieceE() {
        super(staticId, staticName, staticLength, staticAngle, staticColor);
    }

    public TrackPieceE(boolean clockwise) {
        super(staticId, staticName, staticLength, staticAngle, clockwise, staticColor);
    }

}
