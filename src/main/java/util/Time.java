package util;

import models.Point;
import models.Rectangle;
import models.kd.KDNode;
import operations.HybridTree;
import operations.RegionTree;
import operations.index.KDTree;

import java.util.List;

public class Time {

    public static double constructionTime(RegionTree tree, List<Point> points) {
        System.out.printf("Constructing %s for size %d \n", tree.getClass(), points.size());
        long startTime = System.nanoTime();
        for (Point point: points) {
            tree.insert(Geometry.toRectangle(point));
        }
        long endTime = System.nanoTime();
        return (endTime - startTime);
    }

    public static double constructionTime(KDTree tree, KDNode root, List<Point> points) {
        System.out.printf("Constructing %s for size %d \n", tree.getClass(), points.size());
        long startTime = System.nanoTime();
        for (Point point: points) {
            tree.insert(root, point);
        }
        long endTime = System.nanoTime();
        return (endTime - startTime);
    }

    public static long constructionTime(HybridTree hybridTree, RegionTree gridTree,
                                         List<Rectangle> rectangles, List<Point> points,
                                         Rectangle boundary) {
        long startTime = System.nanoTime();
        for (Rectangle rectangle: rectangles) {
            hybridTree.insert(gridTree, rectangle);
        }
        for (Point point: points) {
            hybridTree.insert(gridTree, boundary, point);
        }
        long endTime = System.nanoTime();
        return (endTime - startTime);
    }

    public static long searchTime(HybridTree hybridTree, RegionTree gridTree, List<Point> points, Rectangle boundary) {
        long startTime = System.nanoTime();
        for (Point point: points) {
            hybridTree.search(gridTree, boundary, point);
        }
        long endTime = System.nanoTime();
        return (endTime - startTime)/points.size();
    }

    public static double searchTime(RegionTree tree, List<Point> points) {
        System.out.printf("Searching %s for size %d \n", tree.getClass(), points.size());
        long startTime = System.nanoTime();
        for (Point point: points) {
            tree.search(Geometry.toRectangle(point));
        }
        long endTime = System.nanoTime();
        return (endTime - startTime);
    }

    public static double searchTime(KDTree tree, KDNode root, List<Point> points) {
        System.out.printf("Searching %s for size %d \n", tree.getClass(), points.size());
        long startTime = System.nanoTime();
        for (Point point: points) {
            boolean isPresent = tree.search(root, point);
            if (!isPresent) {
                System.out.print("Danger!!!!");
            }
        }
        long endTime = System.nanoTime();
        return (endTime - startTime);
    }
}
