package com.montes.trackz.pieces.straight;

import android.graphics.Color;

import com.montes.trackz.util.Consts;

public class TrackPieceD extends TrackPieceStraight {

    private static final String staticId = "D";
    private static final String staticName = "Medium Straight";
    private static final double staticLength = Consts.UNIT * 4;
    private static final double staticAngle = 0;
    private static final int staticLevels = 0;
    private static final int staticColor = Color.argb(Consts.COLOR_ALPHA, 0x22, 0xEE, 0x22);

    public TrackPieceD() {
        super(staticId, staticName, staticLength, staticColor);
    }

}
