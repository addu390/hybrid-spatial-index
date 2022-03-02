package util;

import models.quad.Rectangle;

public class Geometry {

    public static Double distanceBetweenPoints(Double x1, Double y1, Double x2, Double y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    public static Boolean rectangleObjectsOverlap(Rectangle r1, Rectangle r2) {
        if (r1.getX() > r2.getX() + r2.getW() || r2.getX() > r1.getX() + r1.getW()) {
            return false;
        }
        if (r1.getY() > r2.getY() + r2.getH() || r2.getY() > r1.getY() + r1.getH()) {
            return false;
        }
        return true;
    }
}