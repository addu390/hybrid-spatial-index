package util;

import models.Point;
import models.Rectangle;
import models.quad.BaseRectangle;

import java.util.List;
import java.util.ArrayList;

public class Generator {

    public static List<Point> points(int minimum, int maximum, int count, int density) {
        int densityCount = (int) (count * 0.1 * density);
        List<Point> pointList = new ArrayList<>();

        count = count - densityCount;
        double denseArea = 0.1;
        double paddingX = Math.random() * (maximum - minimum)/2;
        double paddingY = Math.random() * (maximum - minimum)/2;

        for (int i = 0; i < count; i++) {
            int xPoint = (int) (minimum + Math.random() * (maximum - minimum));
            int yPoint = (int) (minimum + Math.random() * (maximum - minimum));
            pointList.add(new Point(xPoint, yPoint));
        }

        for (int j = 0; j < densityCount; j++) {
            int xPoint = (int) (minimum + Math.random() * (maximum - minimum) * denseArea + paddingX);
            int yPoint = (int) (minimum + Math.random() * (maximum - minimum) * denseArea + paddingY);
            pointList.add(new Point(xPoint, yPoint));
        }

        return pointList;
    }

    public static Point point(int minimum, int maximum) {
        int xPoint = (int)(minimum + Math.random() * (maximum - minimum));
        int yPoint = (int)(minimum + Math.random() * (maximum - minimum));
        return new Point(xPoint, yPoint);
    }

    public static List<Rectangle> rectangles(int minimum, int maximum, int count) {
        List<Rectangle> rectangleList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            double xPoint = (int) (minimum + Math.random() * (maximum - minimum));
            double yPoint = (int) (minimum + Math.random() * (maximum - minimum));
            double wPoint = (int) (minimum + Math.random() * (maximum - minimum));
            double hPoint = (int) (minimum + Math.random() * (maximum - minimum));
            rectangleList.add(new BaseRectangle(xPoint, yPoint, wPoint, hPoint));
        }
        return rectangleList;
    }

    public static Rectangle rectangle(int minimum, int maximum) {
        return new BaseRectangle();
    }
}