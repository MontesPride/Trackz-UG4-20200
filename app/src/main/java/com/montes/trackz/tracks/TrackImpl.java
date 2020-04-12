package com.montes.trackz.tracks;

import android.util.Log;

import com.montes.trackz.pieces.TrackPiece;

import java.util.List;

public class TrackImpl implements Track {
    private static final String tag = "TrackImpl";

    private List<TrackPiece> trackPieces;

    public TrackImpl(List<TrackPiece> trackPieces) {
        //Log.d(tag, String.format("[TrackImpl] trackPieces: %s", trackPieces));
        this.trackPieces = trackPieces;
    }

    @Override
    public List<TrackPiece> getTrackPieces() {
        return this.trackPieces;
    }

    @Override
    public void printTrack() {
        Log.d(tag, "[printTrack]");
        System.out.println(getTrackString());
    }

    @Override
    public void printTrackList() {
        Log.d(tag, "[printTrackList]");
        System.out.println(getTrackListAsString());
    }

    @Override
    public String getTrackString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (TrackPiece trackPiece : this.trackPieces) {
            stringBuilder.append(trackPiece.toString());
        }
        //Log.d(tag, String.format("[getTrackString] trackString: %s", stringBuilder.toString()));
        return stringBuilder.toString();
    }

    @Override
    public String getTrackListAsString() {
        Log.d(tag, "[getTrackListAsString]");
        return this.trackPieces.toString();
    }

}
