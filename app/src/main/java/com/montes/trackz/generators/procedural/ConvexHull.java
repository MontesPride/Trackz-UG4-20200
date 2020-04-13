package com.montes.trackz.generators.procedural;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConvexHull {
    private static final String tag = "ConvexHull";

    List<Point> generatedPoints;

    public ConvexHull(List<Point> generatedPoints) {
        this.generatedPoints = generatedPoints;
    }

    public List<Point> computePolygon() {
        return computePolygon(this.generatedPoints, 0, this.generatedPoints.size());
    }

    public List<Point> computePolygon(List<Point> generatedPoints, int offset, int size) {
        List<Point> sortedGeneratedPoints = sortPoints(generatedPoints);
        List<Point> convexHullPoints = new ArrayList<>();
        int k = 0;

        for (int i = 0; i < size; ++i) {
            while(k >= 2 && ccw(convexHullPoints.get(k - 2), convexHullPoints.get(k - 1), sortedGeneratedPoints.get(i)) <= 0) {
                //Log.d(tag, String.format("[computePolygon][lowerHull] convexHullPoints: %s", convexHullPoints));
                //Log.d(tag, String.format("[computePolygon][lowerHull] removing: %s, i: %d, k: %d", convexHullPoints.get(k-1), i, k));
                convexHullPoints.remove(--k);
                //Log.d(tag, String.format("[computePolygon][lowerHull] convexHullPoints: %s", convexHullPoints));
            }
            convexHullPoints.add(sortedGeneratedPoints.get(i));
            ++k;
            //Log.d(tag, String.format("[computePolygon][lowerHull] convexHullPoints: %s, i: %d, k: %d", convexHullPoints, i, k));
        }

        Log.d(tag, String.format("[computePolygon] lowerHullPoints: %s", convexHullPoints));

        for (int i = size - 2, t = k + 1; i >= 0; --i) {
            while(k >= t && ccw(convexHullPoints.get(k - 2), convexHullPoints.get(k - 1), sortedGeneratedPoints.get(i)) <= 0) {
                //Log.d(tag, String.format("[computePolygon][upperHull] convexHullPoints: %s", convexHullPoints));
                //Log.d(tag, String.format("[computePolygon][upperHull] removing: %s, i: %d, k: %d", convexHullPoints.get(k-1), i, k));
                convexHullPoints.remove(--k);
                //Log.d(tag, String.format("[computePolygon][upperHull] convexHullPoints: %s", convexHullPoints));
            }
            convexHullPoints.add(sortedGeneratedPoints.get(i));
            ++k;
            //Log.d(tag, String.format("[computePolygon][upperHull] convexHullPoints: %s, i: %d, k: %d", convexHullPoints, i, k));
        }

        //Log.d(tag, String.format("[computePolygon] k: %d", k));

        Log.d(tag, String.format("[computePolygon] convexHullPoints: %s", convexHullPoints));

        return convexHullPoints;
    }

    private List<Point> sortPoints(List<Point> points) {

        Log.d(tag, String.format("[sortPoints] preSortedPoints: %s", points));

        Collections.sort(points, Point::compare);

        Log.d(tag, String.format("[sortPoints] sortedPoints: %s", points));

        return points;
    }

    private int ccw(Point O, Point A, Point B) {
        return (A.getX() - O.getX()) * (B.getY() - O.getY()) - (A.getY() - O.getY()) * (B.getX() - O.getX());
    }

}