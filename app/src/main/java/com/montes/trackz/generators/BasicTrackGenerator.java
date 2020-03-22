package com.montes.trackz.generators;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.montes.trackz.pieces.TrackPiece;
import com.montes.trackz.pieces.curves.TrackPieceE;
import com.montes.trackz.pieces.straight.TrackPieceA;
import com.montes.trackz.tracks.Track;
import com.montes.trackz.tracks.TrackImpl;
import com.montes.trackz.util.Consts;
import com.montes.trackz.util.Helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasicTrackGenerator extends TrackGeneratorImpl {
    private static final String tag = "BasicTrackGenerator";

    public BasicTrackGenerator(Context context) {
        super(context);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Track> generateTracks() {
        Log.d(tag, String.format("[generateTracks] Start, trackPiecesDataHolder: %s", this.getTrackPiecesDataHolder()));
        List<Track> tracks = new ArrayList<>();

        parseTrackPiecesData();

        int numberOfStraightTrackPieces = Helper.getNumberOfStraightTrackPieces(this.getTrackPiecesData());
        Log.d(tag, String.format("[generateTracks] numberOfStraightTrackPieces: %d", numberOfStraightTrackPieces));

        int numberOfCurvedTrackPieces = Helper.getNumberOfCurvedTrackPieces(this.getTrackPiecesData());
        Log.d(tag, String.format("[generateTracks] numberOfCurvedTrackPieces: %d", numberOfCurvedTrackPieces));

        int numberOfPermutationsOfCurvedTrackPieces = (int) Math.pow(2, this.isIncludeSymmetricalTracks() ? numberOfCurvedTrackPieces : numberOfCurvedTrackPieces - 1);
        Log.d(tag, String.format("[generateTracks] numberOfPermutationsOfCurvedTrackPieces: %d", numberOfPermutationsOfCurvedTrackPieces));

        for (int curvedTrackPiecesPermutationIterator = 0; curvedTrackPiecesPermutationIterator < numberOfPermutationsOfCurvedTrackPieces; curvedTrackPiecesPermutationIterator++) {

            String curvedTrackPiecesPermutationBinaryString = Helper.getPermutationBinaryString(curvedTrackPiecesPermutationIterator, numberOfCurvedTrackPieces);
            Log.d(tag, String.format("[generateTracks] curvedTrackPiecesPermutationBinaryString: %s", curvedTrackPiecesPermutationBinaryString));

            if (!Helper.validateCurvedTrackPiecesPermutationBinaryString(curvedTrackPiecesPermutationBinaryString)) {
                Log.d(tag, "[generateTracks] invalid curvedTrackPiecesPermutationBinaryString");
                continue;
            }

            Track track;
            List<TrackPiece> trackPieces;
            int[] straightPieces = new int[numberOfStraightTrackPieces];
            int straightCounter;
            int curveCounter;
            boolean terminate;

            do {

                trackPieces = new ArrayList<>();
                straightCounter = 0;
                curveCounter = 0;

                while (straightCounter < numberOfStraightTrackPieces || curveCounter < numberOfCurvedTrackPieces) {
                    //Log.d(tag, String.format("[generateTracks] straightCounter: %d, curveCounter: %d", straightCounter, curveCounter));
                    if (straightCounter < numberOfStraightTrackPieces) {
                        if (straightPieces[straightCounter] == curveCounter) {
                            trackPieces.add(new TrackPieceA());
                            straightCounter++;
                            continue;
                        }
                    }

                    if (curveCounter < numberOfCurvedTrackPieces) {
                        trackPieces.add(new TrackPieceE(curvedTrackPiecesPermutationBinaryString.charAt(curveCounter) == '0'));
                        curveCounter++;
                    }
                }

                track = new TrackImpl(trackPieces);
                Log.d(tag, String.format("[generateTracks] track: %s, permutationBinaryString: %s", track.getTrackString(), curvedTrackPiecesPermutationBinaryString));

                for (int i = 0; i < numberOfStraightTrackPieces; i++) {
                    int j = numberOfStraightTrackPieces - 1 - i;
                    if (straightPieces[j] < numberOfCurvedTrackPieces) {
                        straightPieces[j]++;
                        break;
                    } else {
                        straightPieces[j] = -1;
                    }
                }

                for (int i = 1; i < numberOfStraightTrackPieces; i++) {
                    if (straightPieces[i] == -1) {
                        straightPieces[i] = straightPieces[i - 1];
                    }
                }

                terminate = true;

                for (int i = 0; i < numberOfStraightTrackPieces; i++) {
                    if (straightPieces[i] != numberOfCurvedTrackPieces) {
                        terminate = false;
                        break;
                    }
                }

                if (validateTrack(track)) {
                    tracks.add(track);
                }

            } while (!terminate);

        }

        this.setGeneratedTracks((List<Track>) shuffle(tracks));
        this.setTrackPointer(0);
        Log.d(tag, String.format("[generateTracks] End, generatedTracks.size(): %d, trackPointer: %d", this.getGeneratedTracks().size(), this.getTrackPointer()));
        Helper.logGeneratedTracks(this.getGeneratedTracks());
        return this.getGeneratedTracks();
    }

    private void parseTrackPiecesData() {
        Log.d(tag, "[parseTrackPiecesData] Start");
        SharedPreferences sharedPreferences = this.getTrackPiecesDataHolder().getTrackPiecesData();

        Map<String, Integer> trackPiecesData = new HashMap<>();
        for (Map.Entry<String, ?> sharedPreference : sharedPreferences.getAll().entrySet()) {
            if (sharedPreference.getValue() instanceof Integer) {
                TrackPiece trackPiece = Helper.getTrackPieceById(sharedPreference.getKey());
                trackPiecesData.put(trackPiece.getId(), (Integer) sharedPreference.getValue());
            }
        }
        Log.d(tag, String.format("[parseTrackPiecesData] trackPiecesData: %s", trackPiecesData));
        if (trackPiecesData.isEmpty()) {
            Log.d(tag, "[parseTrackPiecesData] trackPiecesData isEmpty, gettingDefaultTrackPieces()");
            trackPiecesData = Helper.getDefaultTrackPieces();
        }

        this.setTrackPiecesData(trackPiecesData);
        this.setIncludeSymmetricalTracks(sharedPreferences.getBoolean(Consts.INCLUDE_SYMMETRICAL_TRACKS, false));

        Log.d(tag, String.format("[parseTrackPiecesData] End, trackPiecesData: %s, includeSymmetricalTracks: %s", this.getTrackPiecesData(), this.isIncludeSymmetricalTracks()));
    }

}
