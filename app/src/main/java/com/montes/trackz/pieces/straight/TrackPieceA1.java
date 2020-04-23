package com.montes.trackz.pieces.straight;

import android.graphics.Color;

import com.montes.trackz.util.Consts;

public class TrackPieceA1 extends TrackPieceStraight {

    private static final String staticId = "A1";
    private static final String staticName = "Short Straight";
    private static final double staticLength = Consts.UNIT * 2;
    private static final double staticAngle = 0;
    private static final int staticLevels = 0;
    private static final int staticColor = Color.argb(Consts.COLOR_ALPHA, 0x33, 0xBB, 0x33);

    public TrackPieceA1() {
        super(staticId, staticName, staticLength, staticColor);
    }

}
