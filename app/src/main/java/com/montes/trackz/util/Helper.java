package com.montes.trackz.util;

import android.annotation.SuppressLint;
import android.util.Log;

import com.montes.trackz.pieces.TrackPiece;
import com.montes.trackz.pieces.curved.TrackPieceCurved;
import com.montes.trackz.pieces.curved.TrackPieceE;
import com.montes.trackz.pieces.curved.TrackPieceE1;
import com.montes.trackz.pieces.straight.TrackPieceA;
import com.montes.trackz.pieces.straight.TrackPieceA1;
import com.montes.trackz.pieces.straight.TrackPieceA2;
import com.montes.trackz.pieces.straight.TrackPieceB2;
import com.montes.trackz.pieces.straight.TrackPieceC2;
import com.montes.trackz.pieces.straight.TrackPieceD;
import com.montes.trackz.pieces.straight.TrackPieceStraight;
import com.montes.trackz.pieces.straight.bridges.TrackPieceBridge;
import com.montes.trackz.pieces.straight.bridges.TrackPieceN;
import com.montes.trackz.tracks.Track;

import java.util.ArrayList;
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
            case "A1":
            case "TrackPieceA1":
                Log.d(tag, "[getTrackPieceById] returning TrackPieceA1");
                return new TrackPieceA1();
            case "A2":
            case "TrackPieceA2":
                Log.d(tag, "[getTrackPieceById] returning TrackPieceA2");
                return new TrackPieceA2();
            case "B2":
            case "TrackPieceB2":
                Log.d(tag, "[getTrackPieceById] returning TrackPieceB2");
                return new TrackPieceB2();
            case "C2":
            case "TrackPieceC2":
                Log.d(tag, "[getTrackPieceById] returning TrackPieceC2");
                return new TrackPieceC2();
            case "D":
            case "TrackPieceD":
                return new TrackPieceD();
            case "E":
            case "TrackPieceE":
                Log.d(tag, "[getTrackPieceById] returning TrackPieceE");
                return new TrackPieceE();
            case "E1":
            case "TrackPieceE1":
                Log.d(tag, "[getTrackPieceById] returning TrackPieceE1");
                return new TrackPieceE1();
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

    public static TrackPiece getStraightTrackPieceByLength(int length) {
        switch (length) {
            case 1:
            case -1:
                return new TrackPieceA2();
            case 2:
            case -2:
                return new TrackPieceA1();
            /*case 8:
            case -8:
                return new TrackPieceA();*/
            case 4:
            case -4:
                return new TrackPieceD();
            default:
                return null;
        }
    }

    public static TrackPiece getCurvedTrackPieceByLength(int length) {
        return getCurvedTrackPieceByLength(length, true);
    }

    public static TrackPiece getCurvedTrackPieceByLength(int length, boolean clockwise) {
        switch (length) {
            case 2:
            case -2:
                return new TrackPieceE1(clockwise);
            case 4:
            case -4:
                return new TrackPieceE(clockwise);
            default:
                return null;
        }
    }

    public static Map<String, Integer> getDefaultTrackPieces() {
        Map<String, Integer> trackPiecesData = new HashMap<>();
        trackPiecesData.put(TrackPieceA.class.getSimpleName(), Consts.DEFAULT_NUMBER_OF_TRACK_PIECE_A);
        trackPiecesData.put(TrackPieceD.class.getSimpleName(), Consts.DEFAULT_NUMBER_OF_TRACK_PIECE_D);
        trackPiecesData.put(TrackPieceE.class.getSimpleName(), Consts.DEFAULT_NUMBER_OF_TRACK_PIECE_E);
        trackPiecesData.put(TrackPieceN.class.getSimpleName(), Consts.DEFAULT_NUMBER_OF_TRACK_PIECE_N);

        Log.d(tag, String.format("[getDefaultTrackPieces] trackPiecesData: %s", trackPiecesData));
        return trackPiecesData;
    }

    public static List<TrackPiece> getListOfGenericTrackPieces(Map<String, Integer> trackPiecesData, Class<?> trackPieceClass) {
        Log.d(tag, String.format("[getListOfGenericTrackPieces] Start, trackPieceClass: %s", trackPieceClass.getSimpleName()));
        List<TrackPiece> trackPieceList = new ArrayList<>();
        for (Map.Entry<String, Integer> trackPieceEntry : trackPiecesData.entrySet()) {
            if (trackPieceClass.isInstance(Helper.getTrackPieceById(trackPieceEntry.getKey(), true))) {
                for (int i = 0; i < trackPieceEntry.getValue(); i++) {
                    trackPieceList.add(Helper.getTrackPieceById(trackPieceEntry.getKey()));
                }
            }
        }
        Log.d(tag, String.format("[getListOfGenericTrackPieces] End, trackPieceList: %s", trackPieceList));
        return trackPieceList;
    }

    public static int getNumberOfGenericTrackPieces(Map<String, Integer> trackPiecesData, Class<?> trackPieceClass) {
        Log.d(tag,  String.format("[getNumberOfGenericTrackPieces] Start, trackPieceClass: %s", trackPieceClass.getSimpleName()));
        int numberOfGenericTrackPieces = 0;
        for (Map.Entry<String, Integer> trackPieceEntry : trackPiecesData.entrySet()) {
            if (trackPieceClass.isInstance(Helper.getTrackPieceById(trackPieceEntry.getKey(), true))) {
                numberOfGenericTrackPieces += trackPieceEntry.getValue();
            }
        }
        Log.d(tag, String.format("[getNumberOfGenericTrackPieces] End, numberOfGenericTrackPieces: %d", numberOfGenericTrackPieces));
        return numberOfGenericTrackPieces;
    }

    public static int getNumberOfStraightTrackPieces(Map<String, Integer> trackPiecesData) {
        Log.d(tag, "[getNumberOfStraightTrackPieces] Start");
        int numberOfStraightTrackPieces = getNumberOfGenericTrackPieces(trackPiecesData, TrackPieceStraight.class);
        Log.d(tag, String.format("[getNumberOfStraightTrackPieces] End, numberOfStraightTrackPieces: %d", numberOfStraightTrackPieces));
        return numberOfStraightTrackPieces;
    }

    public static int getNumberOfCurvedTrackPieces(Map<String, Integer> trackPiecesData) {
        Log.d(tag, "[getNumberOfCurvedTrackPieces] Start");
        int numberOfCurvedTrackPieces = getNumberOfGenericTrackPieces(trackPiecesData, TrackPieceCurved.class);
        Log.d(tag, String.format("[getNumberOfCurvedTrackPieces] End, numberOfCurvedTrackPieces: %d", numberOfCurvedTrackPieces));
        return numberOfCurvedTrackPieces;
    }

    public static int getNumberOfBridgeTrackPieces(Map<String, Integer> trackPiecesData) {
        Log.d(tag, "[getNumberOfBridgeTrackPieces] Start");
        int numberOfBridgeTrackPieces = getNumberOfGenericTrackPieces(trackPiecesData, TrackPieceBridge.class);
        Log.d(tag, String.format("[getNumberOfBridgeTrackPieces] End, numberOfBridgeTrackPieces: %d", numberOfBridgeTrackPieces));
        return numberOfBridgeTrackPieces;
    }

    public static String getPermutationBinaryString(int permutationNumber, int numberOfCurvedTrackPieces) {
        //Log.d(tag, String.format("[getPermutationBinaryString] permutationNumber: %d, numberOfCurvedTrackPieces: %d", permutationNumber, numberOfCurvedTrackPieces));
        if (numberOfCurvedTrackPieces <= 0)
            return "";

        @SuppressLint("DefaultLocale")
        String permutationBinaryString = String.format(Consts.PERMUTATION_BINARY_FORMAT_STRING, numberOfCurvedTrackPieces);
        //Log.d(tag, String.format("[getPermutationBinaryString] permutataionBinaryStringPreFormat: %s", permutationBinaryString));
        permutationBinaryString = String.format(permutationBinaryString, Integer.toBinaryString(permutationNumber));
        permutationBinaryString = permutationBinaryString.replace(' ', '0');
        //Log.d(tag, String.format("[getPermutationBinaryString] permutationBinaryString: %s", permutationBinaryString));

        return permutationBinaryString;
    }

    public static void reduceTrackPiecesData(Map<String, Integer> trackPiecesData, Class<?> trackPieceClass) {
        String trackPieceWithLargestAmount = "";
        int largestAmount = 0;

        for (Map.Entry<String, Integer> trackPiece : trackPiecesData.entrySet()) {
            if (trackPieceClass.isInstance(getTrackPieceById(trackPiece.getKey())) && trackPiece.getValue() > largestAmount) {
                trackPieceWithLargestAmount = trackPiece.getKey();
                largestAmount = trackPiece.getValue();
            }
        }

        Log.d(tag, String.format("[reduceTrackPiecesData] trackPieceWithLargestAmount: %s, largestAmount: %d", trackPieceWithLargestAmount, largestAmount));

        if (largestAmount > 0 && !trackPieceWithLargestAmount.isEmpty()) {
            Log.d(tag, String.format("[reduceTrackPiecesData] removing one piece: %s", trackPieceWithLargestAmount));
            trackPiecesData.put(trackPieceWithLargestAmount, largestAmount - 1);
        }

    }

    public static boolean validateCurvedTrackPiecesPermutationBinaryString(String curvedTrackPiecesPermutationBinaryString) {
        long clockwiseCurvedTrackPieces = curvedTrackPiecesPermutationBinaryString.chars().filter(character -> character == '1').count();
        long counterClockwiseCurvedTrackPieces = curvedTrackPiecesPermutationBinaryString.chars().filter(character -> character == '0').count();

        return ((clockwiseCurvedTrackPieces - counterClockwiseCurvedTrackPieces) % Consts.CURVES_IN_CIRCLE == 0) &&
               ((clockwiseCurvedTrackPieces - counterClockwiseCurvedTrackPieces) == Consts.CURVES_IN_CIRCLE);
    }

    public static boolean validateBridgeTrackPiecesPermutationBinaryString(String bridgeTrackPiecesPermutationBinaryString) {
        if (bridgeTrackPiecesPermutationBinaryString.chars().filter(character -> character == '0').count() -
                bridgeTrackPiecesPermutationBinaryString.chars().filter(character -> character == '1').count() != 0)
            return false;

        int level = 0;

        for (int i = 0; i < bridgeTrackPiecesPermutationBinaryString.length(); i++) {
            level += (bridgeTrackPiecesPermutationBinaryString.charAt(i) - ((int) '0')) * (-2) + 1;
            if (level < 0)
                return false;
        }
        return true;
    }

    public static boolean compareTwoDoubles(double a, double b) {
        //Log.d(tag, String.format("[compareTwoDoubles] a: %.3f, b: %.3f", a, b));
        return Math.abs(a - b) <= Consts.COMPARISON_THRESHOLD;
    }

    public static boolean compareDoubleAndInteger(double a, int b) {
        return Math.abs(a - (double)b) <= Consts.COMPARISON_THRESHOLD ||
                Math.abs(a - (double)b - 1) <= Consts.COMPARISON_THRESHOLD ||
                Math.abs(a - (double)b + 1) <= Consts.COMPARISON_THRESHOLD;
    }

    public static void logGeneratedTracks(List<Track> tracks) {
        for (Track track : tracks) {
            Log.d(tag, String.format("[logGeneratedTracks] track: %s", track.getTrackString()));
        }
    }

    public static boolean upOrDown(double angle, double nextAngle) {
        return angle > nextAngle;
    }

}
