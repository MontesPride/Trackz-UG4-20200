package com.montes.trackz.generators;

import android.content.Context;
import android.util.Log;

import com.montes.trackz.pieces.*;
import com.montes.trackz.tracks.Track;
import com.montes.trackz.util.Helper;
import com.montes.trackz.util.TrackPiecesDataHolder;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class TrackGeneratorImpl implements TrackGenerator {
    private static final String tag = "TrackGeneratorImpl";

    private TrackPiecesDataHolder trackPiecesDataHolder;
    private List<Track> generatedTracks;
    private int trackPointer;
    private Map<String, Integer> trackPiecesData;
    private boolean includeSymmetricalTracks;
    private int lastPermutationOfCurvedPieces;
    private boolean allTracksGenerated;

    public TrackPiecesDataHolder getTrackPiecesDataHolder() {
        return this.trackPiecesDataHolder;
    }

    public void setTrackPiecesDataHolder(TrackPiecesDataHolder trackPiecesDataHolder) {
        this.trackPiecesDataHolder = trackPiecesDataHolder;
    }

    public List<Track> getGeneratedTracks() {
        return this.generatedTracks;
    }

    public void setGeneratedTracks(List<Track> generatedTracks) {
        this.generatedTracks = generatedTracks;
    }

    public int getTrackPointer() {
        return trackPointer;
    }

    public void setTrackPointer(int trackPointer) {
        this.trackPointer = trackPointer;
    }

    public Map<String, Integer> getTrackPiecesData() {
        return trackPiecesData;
    }

    public void setTrackPiecesData(Map<String, Integer> trackPiecesData) {
        this.trackPiecesData = trackPiecesData;
    }

    public boolean isIncludeSymmetricalTracks() {
        return includeSymmetricalTracks;
    }

    public void setIncludeSymmetricalTracks(boolean includeSymmetricalTracks) {
        this.includeSymmetricalTracks = includeSymmetricalTracks;
    }

    public int getLastPermutationOfCurvedPieces() {
        return lastPermutationOfCurvedPieces;
    }

    public void setLastPermutationOfCurvedPieces(int lastPermutationOfCurvedPieces) {
        this.lastPermutationOfCurvedPieces = lastPermutationOfCurvedPieces;
    }

    public boolean isAllTracksGenerated() {
        return allTracksGenerated;
    }

    public void setAllTracksGenerated(boolean allTracksGenerated) {
        this.allTracksGenerated = allTracksGenerated;
    }

    public TrackGeneratorImpl(Context context) {
        Log.d(tag, String.format("[TrackGeneratorImpl] context: %s", context));
        this.trackPiecesDataHolder = new TrackPiecesDataHolder(context);
    }

    @Override
    public Track generateTrack() {
        return null;
    }

    @Override
    public List<Track> generateTracks() {
        return null;
    }

    @Override
    public Track generateTracks(int count) {
        return null;
    }

    @Override
    public Track getNextTrack() {
        Log.d(tag, String.format("[getNextTrack()] Start, generatedTracks isNull: %s, trackPointer: %d", this.generatedTracks == null, this.trackPointer));
        if (this.generatedTracks == null) {
            generateTracks();
        }
        if (this.generatedTracks.size() == 0) {
            return null;
        }
        if (this.trackPointer >= this.generatedTracks.size()) {
            reshuffle();
            this.trackPointer = 0;
        }
        Log.d(tag, String.format("[getNextTrack()] End, generatedTracks.size(): %s, trackPointer: %d", this.generatedTracks.size(), this.trackPointer));
        return getNextTrack(this.trackPointer);
    }

    @Override
    public Track getNextTrack(int trackPointer) {
        Log.d(tag, String.format("[getNextTrack(int)] Start, trackPointer: %d", trackPointer));
        this.trackPointer++;
        Log.d(tag, String.format("[getNextTrack(int)] End, this.trackPointer: %d", this.trackPointer));
        return this.generatedTracks.get(trackPointer);
    }

    @Override
    public boolean validateTrack(Track track) {
        double x = 0;
        double y = 0;
        double angle = 0;
        int levels = 0;

        double angleInRadians;

        for (TrackPiece trackPiece : track.getTrackPieces()) {

            x += trackPiece.getLength() * Math.cos(angle + trackPiece.getAngle());
            y += trackPiece.getLength() * Math.sin(angle + trackPiece.getAngle());
            if (trackPiece.getAngle() != 0) {
                angle += 2 * trackPiece.getAngle();
            }
            levels += trackPiece.getLevels();
        }
        angleInRadians = angle / Math.PI;
        angle = angleInRadians - (int) angleInRadians;

        Log.d(tag, String.format("[validateTrack] x: %.3f, y: %.3f, angle: %.3f, levels: %d", x, y, angle, levels));
        return (Helper.compareTwoDoubles(x, 0) &&
                Helper.compareTwoDoubles(y, 0) &&
                Helper.compareTwoDoubles(angle, 0) &&
                levels == 0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void reshuffle() {
        Log.d(tag, "[reshuffle] Reshuffling");
        this.generatedTracks = (List<Track>) shuffle(this.generatedTracks);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<?> shuffle(List<?> objectsList) {
        Log.d(tag, "[shuffle] Start");
        Helper.logGeneratedTracks((List<Track>) objectsList);

        Random random = new Random();
        int listSize = objectsList.size();
        for (int i = 0; i < listSize - 1; i++) {
            Collections.swap(objectsList, listSize - 1 - random.nextInt(listSize - i), i);
        }

        Log.d(tag, "[shuffle] End");
        Helper.logGeneratedTracks((List<Track>) objectsList);

        return objectsList;
    }

    public void storeLastIteration(int lastPermutationOfCurvedPieces) {
        this.lastPermutationOfCurvedPieces = lastPermutationOfCurvedPieces;
    }

}
