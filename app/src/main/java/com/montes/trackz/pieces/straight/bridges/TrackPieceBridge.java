package com.montes.trackz.pieces.straight.bridges;

import android.annotation.SuppressLint;

import com.montes.trackz.pieces.straight.TrackPieceStraight;

import androidx.annotation.NonNull;

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

    public void setLevels(int levels) {
        this.levels = levels;
    }

    public void setLevels(boolean upOrDown) {
        this.levels = upOrDown ? 1 : -1;
    }

    @NonNull
    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        return String.format("%s%c", this.getId(), this.levels >= 0 ? '+' : '-');
    }
}
