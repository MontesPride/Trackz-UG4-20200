package com.montes.trackz.pieces.straight;

import com.montes.trackz.util.Consts;

public class TrackPieceA1 extends TrackPieceStraight {

    private static final String staticId = "A1";
    private static final String staticName = "Short Straight";
    private static final double staticLength = Consts.UNIT * 2;
    private static final double staticAngle = 0;
    private static final int staticLevels = 0;

    public TrackPieceA1() {
        super(staticId, staticName, staticLength);
    }

}
