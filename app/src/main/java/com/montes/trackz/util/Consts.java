package com.montes.trackz.util;

public class Consts {
    public static final int UNIT = 1; // 18mm
    public static final int CURVES_IN_CIRCLE = 8; // 8 * 45 = 360
    public static final double RADIANS_IN_CURVE = 2 * Math.PI / Consts.CURVES_IN_CIRCLE;
    public static final double COMPARISON_THRESHOLD = .0001;

    public static final String TRACK_PIECES_DATA_FILE_NAME = "TrackPiecesData";
    public static final String INCLUDE_SYMMETRICAL_TRACKS = "includeSymmetricalTracks";

    public static final String PERMUTATION_BINARY_FORMAT_STRING = "%%1$%ds";

    public static final int DEFAULT_NUMBER_OF_TRACK_PIECE_A = 0;
    public static final int DEFAULT_NUMBER_OF_TRACK_PIECE_D = 4;
    public static final int DEFAULT_NUMBER_OF_TRACK_PIECE_E = 12;
    public static final int DEFAULT_NUMBER_OF_TRACK_PIECE_N = 0;

    public static final int FIELD_SIZE = 15;
    public static final int NUM_OF_GENERATED_POINTS_MIN = 8;
    public static final int NUM_OF_GENERATED_POINTS_MAX = 12;

    public static final int TRANSFORMATION_MULTIPLIER = 1;
    public static final int TRANSFORMATION_MODULO = 2;
    public static final int UNIT_MULTIPLIER = 1;

    public static final int MIN_LENGTH_OF_STRAIGHT_TRACK_PIECE = 1;
    public static final int MIN_LENGTH_OF_CURVED_TRACK_PIECE = 2;
    public static final int MAX_LENGTH_OF_CURVED_TRACK_PIECE = 4;

    public static final int MIN_DOUBLE_TURN_RADIUS = 4;
    public static final int MAX_DOUBLE_TURN_RADIUS = 8;

}
