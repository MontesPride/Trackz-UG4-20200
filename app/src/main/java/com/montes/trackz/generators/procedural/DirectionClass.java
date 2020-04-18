package com.montes.trackz.generators.procedural;

import com.montes.trackz.util.Helper;

public class DirectionClass {

    public enum Direction {
        RIGHT,
        UP,
        LEFT,
        DOWN
    }

    public static boolean areEqual(Direction direction1, Direction direction2) {
        return direction1 == direction2;
    }

    public static boolean areOpposite(Direction direction1, Direction direction2) {
        return (direction1 == Direction.RIGHT && direction2 == Direction.LEFT) ||
                (direction1 == Direction.LEFT && direction2 == Direction.RIGHT) ||
                (direction1 == Direction.UP && direction2 == Direction.DOWN) ||
                (direction1 == Direction.DOWN && direction2 == Direction.UP);
    }

    public static boolean arePerpendicular(Direction direction1, Direction direction2) {
        return (direction1 == Direction.RIGHT && direction2 == Direction.UP) ||
                (direction1 == Direction.RIGHT && direction2 == Direction.DOWN) ||
                (direction1 == Direction.LEFT && direction2 == Direction.UP) ||
                (direction1 == Direction.LEFT && direction2 == Direction.DOWN) ||
                (direction2 == Direction.RIGHT && direction1 == Direction.UP) ||
                (direction2 == Direction.RIGHT && direction1 == Direction.DOWN) ||
                (direction2 == Direction.LEFT && direction1 == Direction.UP) ||
                (direction2 == Direction.LEFT && direction1 == Direction.DOWN);
    }

    public static Direction getXDirection(int direction) {
        if (direction == 1)
            return Direction.RIGHT;
        if (direction == -1)
            return Direction.LEFT;
        throw new RuntimeException(String.format("[getXDirection] invalid direction provided: %d", direction));
    }

    public static Direction getYDirection(int direction) {
        if (direction == 1)
            return Direction.UP;
        if (direction == -1)
            return Direction.DOWN;
        throw new RuntimeException(String.format("[getYDirection] invalid direction provided: %d", direction));
    }

    public static int getXDirectionAsInt(Direction direction) {
        if (direction == Direction.RIGHT)
            return 1;
        if (direction == Direction.LEFT)
            return -1;
        throw new RuntimeException(String.format("[getXDirectionAsInt] invalid direction provided: %s", direction));
    }

    public static int getYDirectionAsInt(Direction direction) {
        if (direction == Direction.UP)
            return 1;
        if (direction == Direction.DOWN)
            return -1;
        throw new RuntimeException(String.format("[getYDirectionAsInt] invalid direction provided: %s", direction));
    }

    public static double getDirectionAsAngle(Direction direction) {
        switch (direction) {
            case RIGHT:
                return 0;
            case UP:
                return Math.PI * (1 / 2.f);
            case LEFT:
                return Math.PI;
            case DOWN:
                return Math.PI * (3 / 2.f);
            default:
                throw new RuntimeException(String.format("[getDirectionAsAngle] WTF! unexpected direction encountered: %s", direction));
        }
    }

    public static boolean compareDirectionWithAngle(Direction direction, double angle) {
        angle = angle % (2 * Math.PI);
        if (angle < 0)
            angle += 2 * Math.PI;
        return Helper.compareTwoDoubles(getDirectionAsAngle(direction), angle);
    }

    public static boolean isXAxis(Direction direction) {
        return direction == Direction.RIGHT || direction == Direction.LEFT;
    }

    public static boolean isYAxis(Direction direction) {
        return direction == Direction.UP || direction == Direction.DOWN;
    }

    public static boolean isClockwise(Direction direction1, Direction direction2) {
        return (direction1 == Direction.RIGHT && direction2 == Direction.DOWN) ||
                (direction1 == Direction.UP && direction2 == Direction.RIGHT) ||
                (direction1 == Direction.LEFT && direction2 == Direction.UP) ||
                (direction1 == Direction.DOWN && direction2 == Direction.LEFT);
    }

}