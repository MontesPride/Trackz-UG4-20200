package com.montes.trackz.util;

public class StaticDataHolder {
    private static String fieldSize = Integer.toString(Consts.FIELD_SIZE);
    private static boolean showTrackString = false;
    private static boolean showTrackScore = true;
    public static String getFieldSize() {
        return fieldSize;
    }
    public static void setFieldSize(String fieldSize) {
        StaticDataHolder.fieldSize = fieldSize;
    }
    public static boolean getShowTrackString() {
        return showTrackString;
    }
    public static void setShowTrackString(boolean showTrackString) {
        StaticDataHolder.showTrackString = showTrackString;
    }
    public static boolean getShowTrackScore() {
        return showTrackScore;
    }
    public static void setShowTrackScore(boolean showTrackScore) {
        StaticDataHolder.showTrackScore = showTrackScore;
    }
}
