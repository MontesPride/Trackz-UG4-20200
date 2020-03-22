package com.montes.trackz.pieces.straight.bridges;

import com.montes.trackz.pieces.straight.TrackPieceStraight;

public abstract class TrackPieceBridge extends TrackPieceStraight {

    private int levels;

    TrackPieceBridge(String id, String name, double length, int levels) {
        super(id, name, length);
        this.levels = levels;
    }

    TrackPieceBridge(String id, String name, double length, int levels, boolean upOrDown) {
        super(id, name, length);
        this.levels = upOrDown ? levels : -levels;
    }

    @Override
    public int getLevels() {
        return this.levels;
    }

}
