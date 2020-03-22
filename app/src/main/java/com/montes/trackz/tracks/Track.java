package com.montes.trackz.tracks;

import com.montes.trackz.pieces.TrackPiece;

import java.util.List;

public interface Track {

    List<TrackPiece> getTrackPieces();

    void printTrack();

    void printTrackList();

    String getTrackString();

    String getTrackListAsString();

}
