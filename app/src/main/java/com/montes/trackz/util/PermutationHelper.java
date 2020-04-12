package com.montes.trackz.util;

import android.util.Log;

import com.montes.trackz.pieces.TrackPiece;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PermutationHelper {
    private static final String tag = "PermutationHelper";

    private List<TrackPiece> trackPieceListInitial;
    private List<TrackPiece> trackPieceList;
    private int[] c;
    private int i;

    public PermutationHelper(List<TrackPiece> trackPieceList) {
        this.trackPieceListInitial = trackPieceList;
        this.trackPieceList = new ArrayList<>(trackPieceList);
        this.c = new int[trackPieceList.size()];
        this.i = 0;
    }

    public List<TrackPiece> getInitialPermutationAndReset() {
        reset();
        return this.trackPieceListInitial;
    }

    public List<TrackPiece> getNextPermutation() {
        while (this.i < this.trackPieceList.size()) {
            if (this.c[this.i] < this.i) {
                if (this.i % 2 == 0 && shouldSwap(0, this.i)) {
                    Collections.swap(this.trackPieceList, 0, this.i);
                    this.c[this.i]++;
                    this.i = 0;
                    Log.d(tag, String.format("[getNextPermutation] returning trackPieceList: %s", this.trackPieceList));
                    return this.trackPieceList;

                } else if (shouldSwap(this.c[this.i], this.i)){
                    Collections.swap(this.trackPieceList, this.c[this.i], this.i);
                    this.c[this.i]++;
                    this.i = 0;
                    Log.d(tag, String.format("[getNextPermutation] returning trackPieceList: %s", this.trackPieceList));
                    return this.trackPieceList;
                }
                this.c[this.i]++;
                this.i = 0;
            } else {
                this.c[this.i] = 0;
                this.i++;
            }
        }
        Log.d(tag, String.format("[getNextPermutation] Resetting, trackPieceListInitial: %s", this.trackPieceListInitial));
        reset();
        return null;
    }

    private void reset() {
        this.trackPieceList =  new ArrayList<>(this.trackPieceListInitial);
        this.c = new int[trackPieceList.size()];
        this.i = 0;
    }

    private boolean shouldSwap(int startIndex, int currentIndex) {
        return !this.trackPieceList.get(startIndex).getClass().equals(this.trackPieceList.get(currentIndex).getClass());
    }

}
