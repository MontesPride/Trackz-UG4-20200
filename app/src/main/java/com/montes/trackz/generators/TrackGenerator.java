package com.montes.trackz.generators;

import com.montes.trackz.tracks.Track;

import java.util.List;

public interface TrackGenerator {

    Track generateTrack();

    List<Track> generateTracks();

    Track generateTracks(int count);

    Track getNextTrack();

    Track getNextTrack(int trackPointer);

    void parseTrackPiecesData();

    boolean validateTrack(Track track);

    void reshuffle();

    List<?> shuffle(List<?> objectsList);

    boolean checkValidator();

}
