package com.montes.trackz.util;

public class Consts {
    public static final int UNIT = 1; // 18mm
    public static final int CURVES_IN_CIRCLE = 8; // 8 * 45 = 360
    public static final double RADIANS_IN_CURVE = 2 * Math.PI / Consts.CURVES_IN_CIRCLE;
    public static final double COMPARISON_THRESHOLD = .0001;

    public static final String TRACK_PIECES_DATA_FILE_NAME = "TrackPiecesData";
    public static final String INCLUDE_SYMMETRICAL_TRACKS = "includeSymmetricalTracks";

    public static final String PERMUTATION_BINARY_FORMAT_STRING = "%%1$%ds";

    public static final int DEFAULT_NUMBER_OF_TRACK_PIECE_A = 4;
    public static final int DEFAULT_NUMBER_OF_TRACK_PIECE_E = 8;
    public static final int DEFAULT_NUMBER_OF_TRACK_PIECE_N = 2;

}
