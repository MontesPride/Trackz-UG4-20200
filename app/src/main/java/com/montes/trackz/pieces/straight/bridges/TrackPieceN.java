package com.montes.trackz.pieces.straight.bridges;

import com.montes.trackz.util.Consts;

public class TrackPieceN extends TrackPieceBridge {

    private static final String staticId = "N";
    private static final String staticName = "Ascending";
    private static final double staticLength = Consts.UNIT * 4;
    private static final double staticAngle = 0;
    private static final int staticLevels = 1;

    public TrackPieceN() {
        super(staticId, staticName, staticLength, staticLevels);
    }

    public TrackPieceN(boolean upOrDown) {
        super(staticId, staticName, staticLength, staticLevels, upOrDown);
    }

}
