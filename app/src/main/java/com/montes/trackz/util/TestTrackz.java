package com.montes.trackz.util;

import com.montes.trackz.pieces.TrackPiece;
import com.montes.trackz.pieces.curved.TrackPieceE;
import com.montes.trackz.pieces.straight.TrackPieceA;
import com.montes.trackz.tracks.Track;
import com.montes.trackz.tracks.TrackImpl;

import java.util.ArrayList;
import java.util.List;

public class TestTrackz {

    public static List<Track> getTestTracks() {
        List<Track> testTracks = new ArrayList<>();

        testTracks.add(getBasicTrack1());
        testTracks.add(getBasicTrack2());
        testTracks.add(getBasicTrack3());
        testTracks.add(getBasicTrack4());

        return testTracks;
    }

    private static Track getBasicTrack1() {
        List<TrackPiece> trackPieceList = new ArrayList<>();

        trackPieceList.add(new TrackPieceE(false));
        trackPieceList.add(new TrackPieceE(false));
        trackPieceList.add(new TrackPieceE(false));
        trackPieceList.add(new TrackPieceE(false));
        trackPieceList.add(new TrackPieceE(false));
        trackPieceList.add(new TrackPieceE(false));
        trackPieceList.add(new TrackPieceE(false));
        trackPieceList.add(new TrackPieceE(false));

        return new TrackImpl(trackPieceList);
    }

    private static Track getBasicTrack2() {
        List<TrackPiece> trackPieceList = new ArrayList<>();

        trackPieceList.add(new TrackPieceE(true));
        trackPieceList.add(new TrackPieceE(true));
        trackPieceList.add(new TrackPieceE(true));
        trackPieceList.add(new TrackPieceE(true));
        trackPieceList.add(new TrackPieceE(true));
        trackPieceList.add(new TrackPieceE(true));
        trackPieceList.add(new TrackPieceE(true));
        trackPieceList.add(new TrackPieceE(true));

        return new TrackImpl(trackPieceList);
    }

    private static Track getBasicTrack3() {
        List<TrackPiece> trackPieceList = new ArrayList<>();

        trackPieceList.add(new TrackPieceA());
        trackPieceList.add(new TrackPieceE(true));
        trackPieceList.add(new TrackPieceE(true));
        trackPieceList.add(new TrackPieceE(true));
        trackPieceList.add(new TrackPieceE(true));
        trackPieceList.add(new TrackPieceA());
        trackPieceList.add(new TrackPieceE(true));
        trackPieceList.add(new TrackPieceE(true));
        trackPieceList.add(new TrackPieceE(true));
        trackPieceList.add(new TrackPieceE(true));

        return new TrackImpl(trackPieceList);
    }

    private static Track getBasicTrack4() {
        List<TrackPiece> trackPieceList = new ArrayList<>();

        trackPieceList.add(new TrackPieceA());
        trackPieceList.add(new TrackPieceE(true));
        trackPieceList.add(new TrackPieceE(false));
        trackPieceList.add(new TrackPieceE(true));
        trackPieceList.add(new TrackPieceE(true));
        trackPieceList.add(new TrackPieceE(true));
        trackPieceList.add(new TrackPieceE(true));
        trackPieceList.add(new TrackPieceA());
        trackPieceList.add(new TrackPieceE(true));
        trackPieceList.add(new TrackPieceE(false));
        trackPieceList.add(new TrackPieceE(true));
        trackPieceList.add(new TrackPieceE(true));
        trackPieceList.add(new TrackPieceE(true));
        trackPieceList.add(new TrackPieceE(true));

        return new TrackImpl(trackPieceList);

    }

}
