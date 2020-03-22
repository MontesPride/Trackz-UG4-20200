package com.montes.trackz.pieces.straight;

import com.montes.trackz.pieces.TrackPiece;
import com.montes.trackz.util.Consts;

public class TrackPieceC extends TrackPieceStraight {

    private static final String staticId = "C";
    private static final String staticName = "Medium Straight";
    private static final double staticLength = Consts.UNIT * 8;
    private static final double staticAngle = 0;
    private static final int staticLevels = 0;

    public TrackPieceC() {
        super(staticId, staticName, staticLength);
    }

}
