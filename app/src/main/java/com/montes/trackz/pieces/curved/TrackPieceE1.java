package com.montes.trackz.pieces.curved;

import com.montes.trackz.util.Consts;

public class TrackPieceE1 extends TrackPieceCurved {

    private static final String staticId = "E1";
    private static final String staticName = "Short Curve";
    private static final double staticLength = 2 * (Consts.UNIT * 2) * Math.cos((Math.PI - Consts.RADIANS_IN_CURVE) / 2);
    private static final double staticAngle = Consts.RADIANS_IN_CURVE / 2;
    private static final int staticLevels = 0;

    public TrackPieceE1() {
        super(staticId, staticName, staticLength, staticAngle);
    }

    public TrackPieceE1(boolean clockwise) {
        super(staticId, staticName, staticLength, staticAngle, clockwise);
    }
}
