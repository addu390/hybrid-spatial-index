package util;

import models.Rectangle;

public class Geometry {

    public static Double distance(Double x1, Double y1, Double x2, Double y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    public static Boolean isEqual(Rectangle r1, Rectangle r2) {
        if (r1.getX().equals(r2.getX()) && r1.getX().equals(r2.getX()) && r1.getX().equals(r2.getX()) && r1.getX().equals(r2.getX())) {
            return true;
        }
        return false;
    }

    public static Boolean isOverlap(Rectangle r1, Rectangle r2) {
        if (r1.getX() > r2.getX() + r2.getW() || r2.getX() > r1.getX() + r1.getW()) {
            return false;
        }
        if (r1.getY() > r2.getY() + r2.getH() || r2.getY() > r1.getY() + r1.getH()) {
            return false;
        }
        return true;
    }

    public static double getArea(Rectangle rectangle) {
        double area = 1.0f;
        area *= (rectangle.getW() * rectangle.getY());
        return area;
    }
}