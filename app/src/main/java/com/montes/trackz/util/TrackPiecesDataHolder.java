package com.montes.trackz.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class TrackPiecesDataHolder {
    private static final String tag = "TrackPiecesDataHolder";

    private SharedPreferences trackPiecesData;
    private Context context;

    public TrackPiecesDataHolder(Context context) {
        Log.d(tag, String.format("[TrackPiecesDataHolder] context: %s", context));
        this.context = context;
    }

    public SharedPreferences getTrackPiecesData() {
        Log.d(tag, String.format("[getTrackData] trackPiecesData: %s", this.trackPiecesData));
        return this.trackPiecesData != null ? this.trackPiecesData : readTrackPiecesData();
    }

    public void setTrackPiecesData(SharedPreferences trackPiecesData) {
        Log.d(tag, String.format("[setTrackData] trackPiecesData: %s", trackPiecesData));
        this.trackPiecesData = trackPiecesData;
    }

    public void putTrackPieceData(String trackPiece, Integer count) {
        Log.d(tag, String.format("[putTrackData] trackPiece: %s, count: %d", trackPiece, count));
        if (this.trackPiecesData == null) {
            Log.wtf(tag, "[putTrackData] trackPiecesData is null");
            readTrackPiecesData();
        }
        this.trackPiecesData.edit().putInt(trackPiece, count).apply();
    }

    public SharedPreferences readTrackPiecesData() {
        Log.d(tag, String.format("[readTrackData] context: %s", this.context));
        SharedPreferences trackPiecesData = this.context.getSharedPreferences(Consts.TRACK_PIECES_DATA_FILE_NAME, Context.MODE_PRIVATE);
        Log.d(tag, String.format("[readTrackData] trackPiecesData: %s", trackPiecesData));
        setTrackPiecesData(trackPiecesData);
        return trackPiecesData;
    }

    public void storeTrackPiecesData(SharedPreferences trackPiecesData) {
        Log.d(tag, String.format("[storeTrackData] trackPiecesData: %s", trackPiecesData));
        this.context.getSharedPreferences(Consts.TRACK_PIECES_DATA_FILE_NAME, Context.MODE_PRIVATE);
        this.trackPiecesData = trackPiecesData;
    }

}
