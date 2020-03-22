package com.montes.trackz.util;

import android.annotation.SuppressLint;
import android.util.Log;

import com.montes.trackz.pieces.*;
import com.montes.trackz.pieces.straight.TrackPieceStraight;
import com.montes.trackz.pieces.straight.bridges.TrackPieceN;
import com.montes.trackz.pieces.curves.TrackPieceCurve;
import com.montes.trackz.pieces.curves.TrackPieceE;
import com.montes.trackz.pieces.straight.TrackPieceA;
import com.montes.trackz.pieces.straight.TrackPieceB;
import com.montes.trackz.pieces.straight.TrackPieceC;
import com.montes.trackz.tracks.Track;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Helper {
    private static final String tag = "Helper";

    public static TrackPiece getTrackPieceById(String trackPieceId, boolean throwRuntimeException) {
        Log.d(tag, String.format("[getTrackPieceById] Start, trackPieceId: %s, throwRuntimeException: %s", trackPieceId, throwRuntimeException));
        switch(trackPieceId) {
            case "A":
            case "TrackPieceA":
                Log.d(tag, "[getTrackPieceById] returning TrackPieceA");
                return new TrackPieceA();
            case "B":
            case "TrackPieceB":
                Log.d(tag, "[getTrackPieceById] returning TrackPieceB");
                return new TrackPieceB();
            case "C":
            case "TrackPieceC":
                Log.d(tag, "[getTrackPieceById] returning TrackPieceC");
                return new TrackPieceC();
            case "E":
            case "TrackPieceE":
                Log.d(tag, "[getTrackPieceById] returning TrackPieceE");
                return new TrackPieceE();
            case "N":
            case "TrackPieceN":
                Log.d(tag, "[getTrackPieceById] returning TrackPieceN");
                return new TrackPieceN();
            default:
                if (throwRuntimeException)
                    throw new RuntimeException(String.format("[getTrackPieceById] Unsupported TrackPiece encountered: %s", trackPieceId));
                Log.d(tag, String.format("[getTrackPieceById] Unsupported TrackPiece encountered: %s, returning null", trackPieceId));
                return null;
        }
    }

    public static TrackPiece getTrackPieceById(String trackPieceId) {
        return getTrackPieceById(trackPieceId, false);
    }

    public static Map<String, Integer> getDefaultTrackPieces() {
        Map<String, Integer> trackPiecesData = new HashMap<>();
        trackPiecesData.put(TrackPieceA.class.getSimpleName(), Consts.DEFAULT_NUMBER_OF_TRACK_PIECE_A);
        trackPiecesData.put(TrackPieceE.class.getSimpleName(), Consts.DEFAULT_NUMBER_OF_TRACK_PIECE_E);

        Log.d(tag, String.format("[getDefaultTrackPieces] trackPiecesData: %s", trackPiecesData));
        return trackPiecesData;
    }

    public static int getNumberOfStraightTrackPieces(Map<String, Integer> trackPiecesData) {
        Log.d(tag, "[getNumberOfStraightTrackPieces] Start");
        int numberOfStraightTrackPieces = 0;
        for (Map.Entry<String, Integer> trackPieceEntry : trackPiecesData.entrySet()) {
            if (Helper.getTrackPieceById(trackPieceEntry.getKey(), true) instanceof TrackPieceStraight) {
                numberOfStraightTrackPieces += trackPieceEntry.getValue();
            }
        }
        Log.d(tag, String.format("[getNumberOfStraightTrackPieces] End, numberOfStraightTrackPieces: %d", numberOfStraightTrackPieces));
        return numberOfStraightTrackPieces;
    }

    public static int getNumberOfCurvedTrackPieces(Map<String, Integer> trackPiecesData) {
        Log.d(tag, "[getNumberOfCurvedTrackPieces] Start");
        int numberOfCurvedTrackPieces = 0;
        for (Map.Entry<String, Integer> trackPieceEntry : trackPiecesData.entrySet()) {
            if (Helper.getTrackPieceById(trackPieceEntry.getKey(), true) instanceof TrackPieceCurve) {
                numberOfCurvedTrackPieces += trackPieceEntry.getValue();
            }
        }
        Log.d(tag, String.format("[getNumberOfCurvedTrackPieces] End, numberOfCurvedTrackPieces: %d", numberOfCurvedTrackPieces));
        return numberOfCurvedTrackPieces;
    }

    public static String getPermutationBinaryString(int permutationNumber, int numberOfCurvedTrackPieces) {
        Log.d(tag, String.format("[getPermutationBinaryString] permutationNumber: %d, numberOfCurvedTrackPieces: %d", permutationNumber, numberOfCurvedTrackPieces));

        @SuppressLint("DefaultLocale")
        String permutationBinaryString = String.format(Consts.PERMUTATION_BINARY_FORMAT_STRING, numberOfCurvedTrackPieces);
        permutationBinaryString = String.format(permutationBinaryString, Integer.toBinaryString(permutationNumber));
        permutationBinaryString = permutationBinaryString.replace(' ', '0');
        Log.d(tag, String.format("[getPermutationBinaryString] permutationBinaryString: %s", permutationBinaryString));

        return permutationBinaryString;
    }

    public static boolean validateCurvedTrackPiecesPermutationBinaryString(String curvedTrackPiecesPermutationBinaryString) {
        long clockwiseCurvedTrackPieces = curvedTrackPiecesPermutationBinaryString.chars().filter(character -> character == '1').count();
        long counterClockwiseCurvedTrackPieces = curvedTrackPiecesPermutationBinaryString.chars().filter(character -> character == '0').count();

        return ((clockwiseCurvedTrackPieces - counterClockwiseCurvedTrackPieces) % Consts.CURVES_IN_CIRCLE == 0) &&
               ((clockwiseCurvedTrackPieces - counterClockwiseCurvedTrackPieces) != 0);
    }

    public static boolean validateBridgeTrackPiecesPermutationBinaryString(String bridgeTrackPiecesPermutationBinaryString) {
        return bridgeTrackPiecesPermutationBinaryString.chars().filter(character -> character == '0').count() -
               bridgeTrackPiecesPermutationBinaryString.chars().filter(character -> character == '1').count() == 0;
    }

    public static boolean compareTwoDoubles(double a, double b) {
        return Math.abs(a - b) <= Consts.COMPARISON_THRESHOLD;
    }

    public static void logGeneratedTracks(List<Track> tracks) {
        for (Track track : tracks) {
            Log.d(tag, String.format("[logGeneratedTracks] track: %s", track.getTrackString()));
        }
    }

}
