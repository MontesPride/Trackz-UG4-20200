package com.montes.trackz.generators.procedural;

import android.annotation.SuppressLint;

import com.montes.trackz.util.Consts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Point {

    private int x;
    private int y;

    private Set<DirectionClass.Direction> directions;
    private Set<DirectionClass.Direction> directionsIn;
    private Set<DirectionClass.Direction> directionsOut;

    private Point next;
    private Point previous;

    public Point(int x, int y) {
        this(x, y, true);
    }

    public Point(int x, int y, Point next, Point previous) {
        this.x = x;
        this.y = y;

        this.directions = EnumSet.allOf(DirectionClass.Direction.class);
        this.directionsIn = EnumSet.allOf(DirectionClass.Direction.class);
        this.directionsOut = EnumSet.allOf(DirectionClass.Direction.class);

        this.next = next;
        this.previous = previous;
        minimiseDirections();
    }

    public Point(int x, int y, DirectionClass.Direction direction) {
        this.x = x;
        this.y = y;

        this.directions = EnumSet.of(direction);
        this.directionsIn = EnumSet.of(direction);
        this.directionsOut = EnumSet.of(direction);

        this.next = null;
        this.previous = null;
    }

    public Point(int x, int y, boolean setDirections) {
        this.x = x;
        this.y = y;

        if (setDirections) {
            this.directions = EnumSet.allOf(DirectionClass.Direction.class);
            this.directionsIn = EnumSet.allOf(DirectionClass.Direction.class);
            this.directionsOut = EnumSet.allOf(DirectionClass.Direction.class);
        } else {
            this.directions = null;
            this.directionsIn = null;
            this.directionsOut = null;
        }

        this.next = null;
        this.previous = null;
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

    public Set<DirectionClass.Direction> getDirections() {
        return directions;
    }

    public void setDirections(Set<DirectionClass.Direction> directions) {
        this.directions = directions;
    }

    public Set<DirectionClass.Direction> getDirectionsIn() {
        return directionsIn;
    }

    public void setDirectionsIn(Set<DirectionClass.Direction> directionsIn) {
        this.directionsIn = directionsIn;
    }

    public Set<DirectionClass.Direction> getDirectionsOut() {
        return directionsOut;
    }

    public void setDirectionsOut(Set<DirectionClass.Direction> directionsOut) {
        this.directionsOut = directionsOut;
    }

    public Point getNext() {
        return next;
    }

    public void setNext(Point next) {
        this.next = next;
    }

    public Point getPrevious() {
        return previous;
    }

    public void setPrevious(Point previous) {
        this.previous = previous;
    }

    public DirectionClass.Direction getDirection() {
        if (this.directions != null && this.directions.size() > 0) {
            return this.directions.iterator().next();
        }
        return null;
    }

    public boolean minimiseDirections() {
        if (this.directions.size() <= 1)
            return true;

        if (this.directions.size() == this.next.getDirections().size() && this.directions.size() == this.previous.getDirections().size())
            return true;

        int sizeBefore = this.directions.size();

        if (straightOnly(this, this.next)) {
            this.removePerpendicularDirections(this.next);
        }
        if (straightOnly(this, this.previous)) {
            this.removePerpendicularDirections(this.previous);
        }
        if (turnOnly(this, this.next) && this.directions.size() > this.next.getDirections().size()) {
            this.removeEqualDirections(this.next);
        }
        if (turnOnly(this, this.previous) && this.directions.size() > this.previous.getDirections().size()) {
            this.removeEqualDirections(this.previous);
        }

        return sizeBefore == this.directions.size();
    }

    public boolean minimiseInOutDirections() {
        int inSizeBefore = this.directionsIn.size();
        int outSizeBefore = this.directionsOut.size();

        int xOutDirection = Integer.compare(next.getX(), this.x);
        int yOutDirection = Integer.compare(next.getY(), this.y);

        int xInDirection = Integer.compare(this.x, previous.getX());
        int yInDirecion  = Integer.compare(this.y, previous.getY());

        removeXDirections(this.directionsOut, xOutDirection);
        removeYDirections(this.directionsOut, yOutDirection);
        removeXDirections(this.directionsIn, xInDirection);
        removeYDirections(this.directionsIn, yInDirecion);

        //minimiseDirections(this.directionsIn, this.directionsOut);
        //minimiseDirections(this.directionsOut, this.directionsIn);
        minimiseDirections(this.directionsOut, next.getDirectionsIn());
        minimiseDirections(this.directionsIn, previous.getDirectionsOut());

        minimiseDirections(this.directions, this.directionsIn);
        minimiseDirections(this.directions, this.directionsOut);

        return inSizeBefore == this.directionsIn.size() && outSizeBefore == this.directionsOut.size();
    }

    public void minimiseDirections(Set<DirectionClass.Direction> thisPointDirections, Set<DirectionClass.Direction> otherPointDirections) {
        thisPointDirections.retainAll(otherPointDirections);
    }

    public void removeXDirections(Set<DirectionClass.Direction> directions, int direction) {
        if (direction >= 0)
            directions.remove(DirectionClass.Direction.LEFT);
        if (direction <= 0)
            directions.remove(DirectionClass.Direction.RIGHT);
    }

    public void removeYDirections(Set<DirectionClass.Direction> directions, int direction) {
        if (direction >= 0)
            directions.remove(DirectionClass.Direction.DOWN);
        if (direction <= 0)
            directions.remove(DirectionClass.Direction.UP);
    }

    public void removeEqualDirections(Point p) {
        for (DirectionClass.Direction direction : p.directions) {
            this.directions.remove(direction);
        }
    }

    public void removePerpendicularDirections(Point p) {
        this.directions.retainAll(p.directions);
    }

    public static boolean straightOnly(Point p1, Point p2) {
        int x_diff = Math.abs(p2.getX() - p1.getX());
        int y_diff = Math.abs(p2.getY() - p1.getY());

        return x_diff == 0 || y_diff == 0;
    }

    public static boolean turnOnly(Point p1, Point p2) {
        int x_diff = Math.abs(p2.getX() - p1.getX());
        int y_diff = Math.abs(p2.getY() - p1.getY());
        int min = Math.min(x_diff, y_diff);

        return min >= Consts.MIN_LENGTH_OF_CURVED_TRACK_PIECE && min < Consts.MAX_LENGTH_OF_CURVED_TRACK_PIECE;
    }

    public static boolean turnAndStraight(Point p1, Point p2) {
        int x_diff = Math.abs(p2.getX() - p1.getX());
        int y_diff = Math.abs(p2.getY() - p1.getY());
        int min = Math.min(x_diff, y_diff);

        return min >= Consts.MAX_LENGTH_OF_CURVED_TRACK_PIECE;
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public String toString() {
        return String.format("Point(%d, %d)", this.x, this.y);
    }

    public static int compare(Point p1, Point p2) {
        if (p1.getX() == p2.getX()) {
            return p1.getY() - p2.getY();
        } else {
            return p1.getX() - p2.getX();
        }
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Point) {
            Point checkPoint = (Point) obj;
            return this.x == checkPoint.getX() && this.y == checkPoint.getY();
        }
        return false;
    }

    public boolean equals(int x, int y) {
        return this.x == x && this.y == y;
    }

    public static int min(Point p1, Point p2) {
        int x_diff = Math.abs(p2.getX() - p1.getX());
        int y_diff = Math.abs(p2.getY() - p1.getY());

        return Math.min(x_diff, y_diff);
    }

}
