package com.montes.trackz.generators.basic;

import android.content.Context;
import android.util.Log;

import com.montes.trackz.generators.TrackGeneratorImpl;
import com.montes.trackz.pieces.TrackPiece;
import com.montes.trackz.pieces.curved.TrackPieceE;
import com.montes.trackz.pieces.straight.TrackPieceStraight;
import com.montes.trackz.pieces.straight.bridges.TrackPieceBridge;
import com.montes.trackz.tracks.Track;
import com.montes.trackz.tracks.TrackImpl;
import com.montes.trackz.util.Helper;
import com.montes.trackz.util.PermutationHelper;

import java.util.ArrayList;
import java.util.List;

public class BasicTrackGenerator extends TrackGeneratorImpl {
    private static final String tag = "BasicTrackGenerator";

    private List<TrackPiece> trackPieces;

    private PermutationHelper straightTrackPiecesListPermutationHelper;

    private int numberOfPermutationsOfCurvedTrackPieces;
    private int numberOfPermutationsOfBridgeTrackPieces;

    private int curvedTrackPiecesPermutationIterator;
    private int bridgeTrackPiecesPermutationIterator;

    private int[] straightPieces;
    private int straightCounter;
    private int curvedCounter;
    private int bridgeCounter;

    public BasicTrackGenerator(Context context) {
        super(context);
        this.straightTrackPiecesListPermutationHelper = new PermutationHelper(this.getStraightTrackPiecesList());
        this.numberOfPermutationsOfCurvedTrackPieces = (int) Math.pow(2, this.isIncludeSymmetricalTracks() ? this.getNumberOfCurvedTrackPieces() : this.getNumberOfCurvedTrackPieces() - 1);
        this.numberOfPermutationsOfBridgeTrackPieces = (int) Math.pow(2, Math.max(this.getNumberOfBridgeTrackPieces() - 1, 0));
        this.curvedTrackPiecesPermutationIterator = 0;
        this.bridgeTrackPiecesPermutationIterator = 0;

        this.straightPieces = new int[this.getNumberOfStraightTrackPieces()];
        this.trackPieces = new ArrayList<>();
        this.straightCounter = 0;
        this.curvedCounter = 0;
        this.bridgeCounter = 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Track> generateTracks() {
        Log.d(tag, String.format("[generateTracks] Start, trackPiecesDataHolder: %s", this.getTrackPiecesDataHolder()));
        List<Track> tracks = new ArrayList<>();

        PermutationHelper straightTrackPiecesListPermutationHelper = new PermutationHelper(this.getStraightTrackPiecesList());

        int numberOfPermutationsOfCurvedTrackPieces = (int) Math.pow(2, this.isIncludeSymmetricalTracks() ? this.getNumberOfCurvedTrackPieces() : this.getNumberOfCurvedTrackPieces() - 1);
        Log.d(tag, String.format("[generateTracks] numberOfPermutationsOfCurvedTrackPieces: %d", numberOfPermutationsOfCurvedTrackPieces));

        int numberOfPermutationsOfBridgeTrackPieces = (int) Math.pow(2, Math.max(this.getNumberOfBridgeTrackPieces() - 1, 0));
        Log.d(tag, String.format("[generateTracks] numberOfPermutationsOfBridgeTrackPieces: %d", numberOfPermutationsOfBridgeTrackPieces));

        for (int curvedTrackPiecesPermutationIterator = 0; curvedTrackPiecesPermutationIterator < numberOfPermutationsOfCurvedTrackPieces; curvedTrackPiecesPermutationIterator++) {

            String curvedTrackPiecesPermutationBinaryString = Helper.getPermutationBinaryString(curvedTrackPiecesPermutationIterator, this.getNumberOfCurvedTrackPieces());
            Log.d(tag, String.format("[generateTracks] curvedTrackPiecesPermutationBinaryString: %s", curvedTrackPiecesPermutationBinaryString));

            if (!Helper.validateCurvedTrackPiecesPermutationBinaryString(curvedTrackPiecesPermutationBinaryString)) {
                Log.d(tag, "[generateTracks] invalid curvedTrackPiecesPermutationBinaryString");
                continue;
            }

            Track track;
            List<TrackPiece> trackPieces;
            TrackPiece trackPiece;
            int[] straightPieces;
            int straightCounter;
            int curvedCounter;
            int bridgeCounter;
            boolean terminate;

            while (this.getStraightTrackPiecesList() != null) {

                Log.d(tag, String.format("[generateTracks] straightTrackPiecesList: %s", this.getStraightTrackPiecesList()));

                for (int bridgeTrackPiecesPermutationIterator = 0; bridgeTrackPiecesPermutationIterator < numberOfPermutationsOfBridgeTrackPieces; bridgeTrackPiecesPermutationIterator++) {

                    String bridgeTrackPiecesPermutationBinaryString = Helper.getPermutationBinaryString(bridgeTrackPiecesPermutationIterator, this.getNumberOfBridgeTrackPieces());
                    Log.d(tag, String.format("[generateTracks] bridgeTrackPiecesPermutationBinaryString: %s", bridgeTrackPiecesPermutationBinaryString));

                    if (!Helper.validateBridgeTrackPiecesPermutationBinaryString(bridgeTrackPiecesPermutationBinaryString)) {
                        Log.d(tag, "[generateTracks] invalid bridgeTrackPiecesPermutationBinaryString");
                        continue;
                    }

                    straightPieces = new int[this.getNumberOfStraightTrackPieces()];

                    do {

                        //Log.d(tag, "[generateTracks] while, for, do");

                        trackPieces = new ArrayList<>();
                        straightCounter = 0;
                        curvedCounter = 0;
                        bridgeCounter = 0;

                        while (straightCounter < this.getNumberOfStraightTrackPieces() || curvedCounter < this.getNumberOfCurvedTrackPieces()) {
                            //Log.d(tag, String.format("[generateTracks] straightCounter: %d, curvedCounter: %d", straightCounter, curvedCounter));
                            if (straightCounter < this.getNumberOfStraightTrackPieces()) {
                                if (straightPieces[straightCounter] == curvedCounter) {
                                    //Log.d(tag, String.format("[generateTracks] Adding straight piece, straightCounter: %d", straightCounter));
                                    trackPiece = this.getStraightTrackPiecesList().get(straightCounter);
                                    if (trackPiece instanceof TrackPieceBridge) {
                                        ((TrackPieceBridge) trackPiece).setLevels(bridgeTrackPiecesPermutationBinaryString.charAt(bridgeCounter) == '0');
                                        trackPieces.add(trackPiece);
                                        bridgeCounter++;
                                    } else if (trackPiece instanceof TrackPieceStraight) {
                                        trackPieces.add(trackPiece);
                                    } else {
                                        throw new RuntimeException(String.format("Unknown TrackPiece Type found: %s", trackPiece));
                                    }
                                    straightCounter++;
                                    continue;
                                }
                            }

                            if (curvedCounter < this.getNumberOfCurvedTrackPieces()) {
                                //Log.d(tag, String.format("[generateTracks] Adding curved piece, curvedCounter: %d", curvedCounter));
                                trackPieces.add(new TrackPieceE(curvedTrackPiecesPermutationBinaryString.charAt(curvedCounter) == '0'));
                                curvedCounter++;
                            }
                        }

                        track = new TrackImpl(trackPieces);
                        //Log.d(tag, String.format("[generateTracks] track: %s, curvedTrackPiecesPermutationBinaryString: %s, bridgeTrackPiecesPermutationBinaryString: %s", track.getTrackString(), curvedTrackPiecesPermutationBinaryString, bridgeTrackPiecesPermutationBinaryString));

                        for (int i = 0; i < this.getNumberOfStraightTrackPieces(); i++) {
                            int j = this.getNumberOfStraightTrackPieces() - 1 - i;
                            if (straightPieces[j] < this.getNumberOfCurvedTrackPieces()) {
                                straightPieces[j]++;
                                break;
                            } else {
                                straightPieces[j] = -1;
                            }
                        }

                        for (int i = 1; i < this.getNumberOfStraightTrackPieces(); i++) {
                            if (straightPieces[i] == -1) {
                                straightPieces[i] = straightPieces[i - 1];
                            }
                        }

                        terminate = true;

                        for (int i = 0; i < this.getNumberOfStraightTrackPieces(); i++) {
                            if (straightPieces[i] != this.getNumberOfCurvedTrackPieces()) {
                                terminate = false;
                                break;
                            }
                        }

                        if (validateTrack(track)) {
                            Log.d(tag, String.format("[generateTracks] validatedTrack: %s, curvedTrackPiecesPermutationBinaryString: %s, bridgeTrackPiecesPermutationBinaryString: %s", track.getTrackString(), curvedTrackPiecesPermutationBinaryString, bridgeTrackPiecesPermutationBinaryString));
                            tracks.add(new TrackImpl(new ArrayList<>(track.getTrackPieces())));
                        }

                    } while (!terminate);

                }

                this.setStraightTrackPiecesList(straightTrackPiecesListPermutationHelper.getNextPermutation());
            }
            this.setStraightTrackPiecesList(straightTrackPiecesListPermutationHelper.getNextPermutation());

        }

        Helper.logGeneratedTracks(tracks);
        this.setGeneratedTracks((List<Track>) shuffle(tracks));
        this.setTrackPointer(0);
        Log.d(tag, String.format("[generateTracks] End, generatedTracks.size(): %d, trackPointer: %d", this.getGeneratedTracks().size(), this.getTrackPointer()));
        //Helper.logGeneratedTracks(this.getGeneratedTracks());
        return this.getGeneratedTracks();
    }

    @Override
    public Track generateTrack() {

        Log.d(tag, String.format("[generateTrack] Start, trackPiecesDataHolder: %s", this.getTrackPiecesDataHolder()));

        Log.d(tag, String.format("[generateTrack] numberOfPermutationsOfCurvedTrackPieces: %d", this.numberOfPermutationsOfCurvedTrackPieces));

        Log.d(tag, String.format("[generateTrack] numberOfPermutationsOfBridgeTrackPieces: %d", this.numberOfPermutationsOfBridgeTrackPieces));

        for (this.curvedTrackPiecesPermutationIterator = 0; this.curvedTrackPiecesPermutationIterator < this.numberOfPermutationsOfCurvedTrackPieces; this.curvedTrackPiecesPermutationIterator++) {

            String curvedTrackPiecesPermutationBinaryString = Helper.getPermutationBinaryString(this.curvedTrackPiecesPermutationIterator, this.getNumberOfCurvedTrackPieces());
            Log.d(tag, String.format("[generateTrack] curvedTrackPiecesPermutationBinaryString: %s", curvedTrackPiecesPermutationBinaryString));
            //curvedTrackPiecesPermutationBinaryString = "010000010000";

            if (!Helper.validateCurvedTrackPiecesPermutationBinaryString(curvedTrackPiecesPermutationBinaryString)) {
                Log.d(tag, "[generateTrack] invalid curvedTrackPiecesPermutationBinaryString");
                continue;
            } else {
                Log.d(tag, String.format("[generateTrack] valid curvedTrackPiecesPermutationBinaryString: %s", curvedTrackPiecesPermutationBinaryString));
            }

            //
            //Track track;
            //List<TrackPiece> trackPieces;
            TrackPiece trackPiece;
            //int[] straightPieces;
            //int straightCounter;
            //int curvedCounter;
            //int bridgeCounter;
            boolean terminate;
            //

            this.setStraightTrackPiecesList(straightTrackPiecesListPermutationHelper.getInitialPermutationAndReset());

            //Log.d(tag, String.format("[generateTrack] straightTrackPiecesList: %s", this.getStraightTrackPiecesList()));

            while (this.getStraightTrackPiecesList() != null) {

                for (this.bridgeTrackPiecesPermutationIterator = 0; this.bridgeTrackPiecesPermutationIterator < this.numberOfPermutationsOfBridgeTrackPieces; this.bridgeTrackPiecesPermutationIterator++) {

                    String bridgeTrackPiecesPermutationBinaryString = Helper.getPermutationBinaryString(this.bridgeTrackPiecesPermutationIterator, this.getNumberOfBridgeTrackPieces());
                    //Log.d(tag, String.format("[generateTrack] bridgeTrackPiecesPermutationBinaryString: %s", bridgeTrackPiecesPermutationBinaryString));

                    if (!Helper.validateBridgeTrackPiecesPermutationBinaryString(bridgeTrackPiecesPermutationBinaryString)) {
                        //Log.d(tag, "[generateTrack] invalid bridgeTrackPiecesPermutationBinaryString");
                        continue;
                    }

                    do {

                        //Log.d(tag, "[generateTrack] while, for, do");

                        while (this.straightCounter < this.getNumberOfStraightTrackPieces() || this.curvedCounter < this.getNumberOfCurvedTrackPieces()) {
                            //Log.d(tag, String.format("[generateTrack] straightCounter: %d, curvedCounter: %d", this.straightCounter, this.curvedCounter));
                            if (this.straightCounter < this.getNumberOfStraightTrackPieces()) {
                                if (this.straightPieces[this.straightCounter] == this.curvedCounter) {
                                    //Log.d(tag, String.format("[generateTrack] Adding straight piece, this.straightCounter: %d", this.straightCounter));
                                    trackPiece = this.getStraightTrackPiecesList().get(this.straightCounter);
                                    if (trackPiece instanceof TrackPieceBridge) {
                                        ((TrackPieceBridge) trackPiece).setLevels(bridgeTrackPiecesPermutationBinaryString.charAt(this.bridgeCounter) == '0');
                                        this.trackPieces.add(trackPiece);
                                        this.bridgeCounter++;
                                    } else if (trackPiece instanceof TrackPieceStraight) {
                                        this.trackPieces.add(trackPiece);
                                    } else {
                                        throw new RuntimeException(String.format("Unknown TrackPiece Type found: %s", trackPiece));
                                    }
                                    this.straightCounter++;
                                    continue;
                                }
                            }

                            if (this.curvedCounter < this.getNumberOfCurvedTrackPieces()) {
                                //Log.d(tag, String.format("[generateTrack] Adding curved piece, curvedCounter: %d", this.curvedCounter));
                                this.trackPieces.add(new TrackPieceE(curvedTrackPiecesPermutationBinaryString.charAt(this.curvedCounter) == '0'));
                                this.curvedCounter++;
                            }
                        }

                        Track track = new TrackImpl(this.trackPieces);
                        //Log.d(tag, String.format("[generateTrack] track: %s, curvedTrackPiecesPermutationBinaryString: %s, bridgeTrackPiecesPermutationBinaryString: %s", track.getTrackString(), curvedTrackPiecesPermutationBinaryString, bridgeTrackPiecesPermutationBinaryString));

                        for (int i = 0; i < this.getNumberOfStraightTrackPieces(); i++) {
                            int j = this.getNumberOfStraightTrackPieces() - 1 - i;
                            if (this.straightPieces[j] < this.getNumberOfCurvedTrackPieces()) {
                                this.straightPieces[j]++;
                                break;
                            } else {
                                this.straightPieces[j] = -1;
                            }
                        }

                        for (int i = 1; i < this.getNumberOfStraightTrackPieces(); i++) {
                            if (this.straightPieces[i] == -1) {
                                this.straightPieces[i] = this.straightPieces[i - 1];
                            }
                        }

                        terminate = true;

                        for (int i = 0; i < this.getNumberOfStraightTrackPieces(); i++) {
                            if (this.straightPieces[i] != this.getNumberOfCurvedTrackPieces()) {
                                terminate = false;
                                break;
                            }
                        }

                        this.trackPieces = new ArrayList<>();
                        this.straightCounter = 0;
                        this.curvedCounter = 0;
                        this.bridgeCounter = 0;

                        if (validateTrack(track)) {
                            Log.d(tag, String.format("[generateTrack] validatedTrack: %s, curvedTrackPiecesPermutationBinaryString: %s, bridgeTrackPiecesPermutationBinaryString: %s", track.getTrackString(), curvedTrackPiecesPermutationBinaryString, bridgeTrackPiecesPermutationBinaryString));
                            return track;
                        }

                    } while (!terminate);

                    this.straightPieces = new int[this.getNumberOfStraightTrackPieces()];

                }

                this.setStraightTrackPiecesList(straightTrackPiecesListPermutationHelper.getNextPermutation());
            }
            this.setStraightTrackPiecesList(straightTrackPiecesListPermutationHelper.getNextPermutation());

        }

        this.setTrackPointer(0);

        return null;
    }

}
