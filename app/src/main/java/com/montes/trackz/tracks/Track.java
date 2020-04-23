package com.montes.trackz.tracks;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.montes.trackz.pieces.TrackPiece;

import java.util.List;

public interface Track {

    List<TrackPiece> getTrackPieces();

    void printTrack();

    void printTrackList();

    String getTrackString();

    String getTrackListAsString();

    List<LineGraphSeries<DataPoint>> getTrackAsCurve();

    int getTrackScore();

}
