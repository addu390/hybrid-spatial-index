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
        QuadKDTree hQuadKDTree = new QuadKDTree();
        QuadTree hQuadTree = new QuadTree();

        RKDTree hRKDTree = new RKDTree();
        RTree hRTree = new RTree();

        List<Rectangle> rectangles = Generator.rectangles(5, 1000, 300);
        List<Point> points = Generator.points(10, 200, 100, 10);
        Rectangle boundary = new BaseRectangle(10.0, 10.0, 200.0, 200.0);

        long c1 = constructionTime(hQuadKDTree, hQuadTree, rectangles, points, boundary);
        long c2 = constructionTime(hRKDTree, hRTree, rectangles, points, boundary);

        System.out.println("Quad-KD Tree Creation Time: "+ c1);
        System.out.println("R-KD Tree Creation Time: "+ c2);

        long s1 = searchTime(hQuadKDTree, hQuadTree, points, boundary);
        long s2 = searchTime(hRKDTree, hRTree, points, boundary);

        System.out.println("Quad-KD Tree Search Time: "+ s1);
        System.out.println("R-KD Tree Search Time: "+ s2);
    }

    public static long constructionTime(HybridTree hybridTree, SpaceTree gridTree,
                                        List<Rectangle> rectangles, List<Point> points,
                                        Rectangle boundary) {
        long startTimeKDIns = System.nanoTime();
        for (Rectangle rectangle: rectangles) {
            hybridTree.insert(gridTree, rectangle);
        }
        for (Point point: points) {
            hybridTree.insert(gridTree, boundary, point);
        }
        long endTimeKDIns = System.nanoTime();
        return (endTimeKDIns - startTimeKDIns);
    }

    public static long searchTime(HybridTree hybridTree, SpaceTree gridTree, List<Point> points, Rectangle boundary) {
        long startTimeKDSearch = System.nanoTime();
        for (Point point: points) {
            hybridTree.search(gridTree, boundary, point);
        }
        long endTimeKDSearch = System.nanoTime();
        return (endTimeKDSearch - startTimeKDSearch)/points.size();
    }
}
