package com.montes.trackz.generators.procedural;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Point {

    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public String toString() {
        return String.format("Point(%d, %d)", this.x, this.y);
    }

    public static int compare(Point p1, Point p2) {
        if (p1.x == p2.x) {
            return p1.y - p2.y;
        } else {
            return p1.x - p2.x;
        }
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Point) {
            Point checkPoint = (Point) obj;
            return this.x == checkPoint.x && this.y == checkPoint.y;
        }
        return false;
    }
}
