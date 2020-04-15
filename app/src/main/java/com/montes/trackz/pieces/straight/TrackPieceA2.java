package com.montes.trackz.pieces.straight;

import com.montes.trackz.util.Consts;

public class TrackPieceA2 extends TrackPieceStraight {

    private static final String staticId = "A2";
    private static final String staticName = "Mini Straight";
    private static final double staticLength = Consts.UNIT * 1;
    private static final double staticAngle = 0;
    private static final int staticLevels = 0;

    public TrackPieceA2() {
        super(staticId, staticName, staticLength);
    }

}