package com.montes.trackz.util;

import com.montes.trackz.generators.procedural.Point;
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
        testTracks.add(getBasicTrack5());

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

    private static Track getBasicTrack5() {
        List<TrackPiece> trackPieceList = new ArrayList<>();

        trackPieceList.add(new TrackPieceE(true));
        trackPieceList.add(new TrackPieceE(false));

        return new TrackImpl(trackPieceList);
    }

    public static List<Point> getProceduralPointList1() {
        /*Point(-22, 18), Point(-20, 20), Point(-14, -8), Point(-14, 18), Point(-14, 26), Point(-13, -12), Point(13, -14), Point(13, 10), Point(18, -14), Point(18, 12), Point(24, 8)*/
        Point p1 = new Point(-22, 18);
        Point p2 = new Point(-20, 20);
        Point p3 = new Point(-14, -8);
        Point p4 = new Point(-14, 18);
        Point p5 = new Point(-14, 26);
        Point p6 = new Point(-13, -12);
        Point p7 = new Point(13, -14);
        Point p8 = new Point(13, 10);
        Point p9 = new Point(18, -14);
        Point p10 = new Point(18, 12);
        Point p11 = new Point(24, 8);
        List<Point> points = new ArrayList<>();
        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);
        points.add(p5);
        points.add(p6);
        points.add(p7);
        points.add(p8);
        points.add(p9);
        points.add(p10);
        points.add(p11);

        return points;
    }

    public static List<Point> getProceduralPointList2() {
        /*Point(-12, 0), Point(-6, -8), Point(8, -10), Point(12, 6), Point(8, 10), Point(0, 8), Point(-6, 6), Point(-12, 0)*/
        Point p1 = new Point(-12, 0);
        Point p2 = new Point(-6, -8);
        Point p3 = new Point(8, -10);
        Point p4 = new Point(12, 6);
        Point p5 = new Point(8, 10);
        Point p6 = new Point(0, 8);
        Point p7 = new Point(-6, 6);
        Point p8 = new Point(-12, 0);
        List<Point> points = new ArrayList<>();
        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);
        points.add(p5);
        points.add(p6);
        points.add(p7);
        points.add(p8);

        return points;
    }

}
