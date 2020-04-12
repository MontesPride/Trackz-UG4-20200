package com.montes.trackz.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.montes.trackz.pieces.TrackPiece;
import com.montes.trackz.pieces.curved.TrackPieceCurved;
import com.montes.trackz.pieces.straight.TrackPieceStraight;
import com.montes.trackz.pieces.straight.bridges.TrackPieceBridge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class TrackPiecesDataHolder {
    private static final String tag = "TrackPiecesDataHolder";

    private SharedPreferences sharedPreferences;
    private Map<String, Integer> trackPiecesData;
    private Context context;

    public TrackPiecesDataHolder(Context context) {
        Log.d(tag, String.format("[TrackPiecesDataHolder] context: %s", context));
        this.context = context;
    }

    public boolean getSharedPreference(String sharedPreferenceName, boolean defaultValue) {
        return this.sharedPreferences.getBoolean(sharedPreferenceName, defaultValue);
    }

    private SharedPreferences getSharedPreferences() {
        Log.d(tag, String.format("[getSharedPreferences] sharedPreferences: %s", sharedPreferences));
        return this.sharedPreferences != null ? this.sharedPreferences : readSharedPreferences();
    }

    public Map<String, Integer> getTrackPiecesData() {
        Log.d(tag, String.format("[getTrackPiecesData] trackPiecesData: %s", this.trackPiecesData));
        return this.trackPiecesData != null ? this.trackPiecesData : readTrackPiecesData();
    }

    private void setSharedPreferences(SharedPreferences sharedPreferences) {
        Log.d(tag, String.format("[setSharedPreferences] sharedPreferences: %s", sharedPreferences));
        this.sharedPreferences = sharedPreferences;
    }

    public void setTrackPiecesData(Map<String, Integer> trackPiecesData) {
        Log.d(tag, String.format("[setTrackPiecesData] trackPiecesData: %s", trackPiecesData));
        this.trackPiecesData = trackPiecesData;
    }

    public void putSharedPreferences(String sharedPreferenceName, Integer sharedPreferenceValue) {
        Log.d(tag, String.format("[putSharedPreferences] sharedPreferenceName: %s, sharedPreferenceValue: %d", sharedPreferenceName, sharedPreferenceValue));
        if (this.sharedPreferences == null) {
            Log.wtf(tag, "[putSharedPreferences] sharedPreferences is null");
            readSharedPreferences();
        }
        this.sharedPreferences.edit().putInt(sharedPreferenceName, sharedPreferenceValue).apply();
    }

    public void putSharedPreferences(String sharedPreference, boolean sharedPreferenceValue) {
        Log.d(tag, String.format("[putSharedPreferences] sharedPreference: %s, sharedPreferenceValue: %s", sharedPreference, sharedPreferenceValue));
        if (this.sharedPreferences == null) {
            Log.wtf(tag, "[putSharedPreferences] sharedPreferences is null");
            readSharedPreferences();
        }
        this.sharedPreferences.edit().putBoolean(sharedPreference, sharedPreferenceValue).apply();
    }

    public void putTrackPieceData(String trackPiece, Integer count) {
        Log.d(tag, String.format("[putTrackPieceData] trackPiece: %s, count: %d", trackPiece, count));
        if (this.trackPiecesData == null) {
            Log.wtf(tag, "[putTrackPieceData] trackPiecesData is null");
            readTrackPiecesData();
        }
        putSharedPreferences(trackPiece, count);
        this.trackPiecesData.put(trackPiece, count);
    }

    private SharedPreferences readSharedPreferences() {
        Log.d(tag, String.format("[readSharedPreferences] context: %s", this.context));
        SharedPreferences sharedPreferences = this.context.getSharedPreferences(Consts.TRACK_PIECES_DATA_FILE_NAME, Context.MODE_PRIVATE);
        Log.d(tag, String.format("[readSharedPreferences] sharedPreferences: %s", sharedPreferences));
        setSharedPreferences(sharedPreferences);
        return sharedPreferences;
    }

    public Map<String, Integer> readTrackPiecesData() {
        Log.d(tag, String.format("[readTrackPiecesData] context: %s", this.context));
        getSharedPreferences();

        Map<String, Integer> trackPiecesData = new HashMap<>();
        for (Map.Entry<String, ?> sharedPreference : this.sharedPreferences.getAll().entrySet()) {
            if (sharedPreference.getValue() instanceof Integer) {
                TrackPiece trackPiece = Helper.getTrackPieceById(sharedPreference.getKey());
                trackPiecesData.put(trackPiece.getId(), (Integer) sharedPreference.getValue());
            }
        }

        Log.d(tag, String.format("[readTrackPiecesData] trackPiecesData: %s", trackPiecesData));
        if (trackPiecesData.isEmpty()) {
            Log.d(tag, "[readTrackPiecesData] trackPiecesData isEmpty, gettingDefaultTrackPieces()");
            trackPiecesData = Helper.getDefaultTrackPieces();
            Log.d(tag, String.format("[readTrackPiecesData] defaultTrackPiecesData: %s", trackPiecesData));
        }

        Log.d(tag, String.format("[readTrackPiecesData] trackPiecesData: %s", trackPiecesData));
        setTrackPiecesData(trackPiecesData);
        return trackPiecesData;
    }

    public void storeTrackPiecesData(Map<String, Integer> trackPiecesData) {
        Log.d(tag, String.format("[storeTrackPiecesData] trackPiecesData: %s", trackPiecesData));
        this.context.getSharedPreferences(Consts.TRACK_PIECES_DATA_FILE_NAME, Context.MODE_PRIVATE);
        this.trackPiecesData = trackPiecesData;
    }

    public List<TrackPiece> getListOfStraightTrackPieces() {
        Log.d(tag, "[getListOfStraightTrackPieces] Start");
        List<TrackPiece> trackPieceList = Helper.getListOfGenericTrackPieces(this.trackPiecesData, TrackPieceStraight.class);
        Log.d(tag, String.format("[getListOfStraightTrackPieces] End, trackPieceList: %s", trackPieceList));
        return trackPieceList;
    }

    public List<TrackPiece> getListOfCurvedTrackPieces() {
        Log.d(tag, "[getListOfCurvedTrackPieces] Start");
        List<TrackPiece> trackPieceList = Helper.getListOfGenericTrackPieces(this.trackPiecesData, TrackPieceCurved.class);
        Log.d(tag, String.format("[getListOfCurvedTrackPieces] End, trackPieceList: %s", trackPieceList));
        return trackPieceList;
    }

    public int getNumberOfStraightTrackPieces() {
        Log.d(tag, "[getNumberOfStraightTrackPieces] Start");
        int numberOfStraightTrackPieces = Helper.getNumberOfGenericTrackPieces(this.trackPiecesData, TrackPieceStraight.class);
        Log.d(tag, String.format("[getNumberOfStraightTrackPieces] End, numberOfStraightTrackPieces: %d", numberOfStraightTrackPieces));
        return numberOfStraightTrackPieces;
    }

    public int getNumberOfCurvedTrackPieces() {
        Log.d(tag, "[getNumberOfCurvedTrackPieces] Start");
        int numberOfCurvedTrackPieces = Helper.getNumberOfGenericTrackPieces(this.trackPiecesData, TrackPieceCurved.class);
        Log.d(tag, String.format("[getNumberOfCurvedTrackPieces] End, numberOfCurvedTrackPieces: %d", numberOfCurvedTrackPieces));
        return numberOfCurvedTrackPieces;
    }

    public int getNumberOfBridgeTrackPieces() {
        Log.d(tag, "[getNumberOfBridgeTrackPieces] Start");
        int numberOfBridgeTrackPieces = Helper.getNumberOfGenericTrackPieces(this.trackPiecesData, TrackPieceBridge.class);
        Log.d(tag, String.format("[getNumberOfBridgeTrackPieces] End, numberOfBridgeTrackPieces: %d", numberOfBridgeTrackPieces));
        return numberOfBridgeTrackPieces;
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Integer> trackPieceData : this.trackPiecesData.entrySet()) {
            stringBuilder.append(String.format("%s: %d, ", trackPieceData.getKey(), trackPieceData.getValue()));
        }
        //Log.d(tag, String.format("[toString] trackPiecesData: %s", stringBuilder.toString()));
        return stringBuilder.toString();
    }
}
