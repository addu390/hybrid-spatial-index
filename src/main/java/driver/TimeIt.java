package driver;

import models.Point;
import models.Rectangle;
import models.quad.BaseRectangle;
import operations.HybridTree;
import operations.SpaceTree;
import operations.index.QuadKDTree;
import operations.index.QuadTree;
import operations.index.RKDTree;
import operations.index.RTree;
import util.Generator;

import java.util.List;

public class TimeIt {
    public static void main(String[] args) {
        System.out.printf("%-15s %-20s %-20s %n", "Hybrid Tree", "Creation Time (ns)", "Search Time (ns)");
        String format = "%-15s %-20d %-20d %n";

        QuadKDTree hQuadKDTree = new QuadKDTree();
        QuadTree hQuadTree = new QuadTree();

        RKDTree hRKDTree = new RKDTree();
        RTree hRTree = new RTree();

        List<Rectangle> rectangles = Generator.rectangles(5, 1000, 300);
        List<Point> points = Generator.points(10, 200, 100, 10);
        Rectangle boundary = new BaseRectangle(10.0, 10.0, 200.0, 200.0);

        long c1 = constructionTime(hQuadKDTree, hQuadTree, rectangles, points, boundary);
        long c2 = constructionTime(hRKDTree, hRTree, rectangles, points, boundary);

        long s1 = searchTime(hQuadKDTree, hQuadTree, points, boundary);
        long s2 = searchTime(hRKDTree, hRTree, points, boundary);

        System.out.printf(format, "Quad-KD", s1, c1);
        System.out.printf(format, "R-KD", s2, c2);
    }

    private static long constructionTime(HybridTree hybridTree, SpaceTree gridTree,
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

    private static long searchTime(HybridTree hybridTree, SpaceTree gridTree, List<Point> points, Rectangle boundary) {
        long startTime = System.nanoTime();
        for (Point point: points) {
            hybridTree.search(gridTree, boundary, point);
        }
        long endTime = System.nanoTime();
        return (endTime - startTime)/points.size();
    }
}
