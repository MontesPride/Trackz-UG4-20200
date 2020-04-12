package com.montes.trackz.pieces.curved;

import com.montes.trackz.util.Consts;

public class TrackPieceE extends TrackPieceCurved {

    private static final String staticId = "E";
    private static final String staticName = "Large Curve";
    private static final double staticLength = 2 * (Consts.UNIT * 8) * Math.cos((Math.PI - Consts.RADIANS_IN_CURVE) / 2);
    private static final double staticAngle = Consts.RADIANS_IN_CURVE / 2;
    private static final int staticLevels = 0;

    public TrackPieceE() {
        super(staticId, staticName, staticLength, staticAngle);
    }

    public TrackPieceE(boolean clockwise) {
        super(staticId, staticName, staticLength, staticAngle, clockwise);
    }

}
