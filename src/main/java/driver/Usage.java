package driver;

import models.Point;
import models.Rectangle;
import models.kd.KDNode;
import models.quad.BaseRectangle;
import operations.index.*;
import util.Generator;

import java.util.List;

import static util.Time.constructionTime;
import static util.Time.searchTime;

public class Usage {
    public static void main(String[] args) {
        quadTree();
        rTree();
        kdTree();
        quadKdTree();
        rKdTree();

        Generator.points(100, 1000, 30, 10);
        Generator.rectangles(100, 1000, 30);

        timing();
    }

    public static void timing() {
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

    public static void quadTree() {
        QuadTree quadTree = new QuadTree();
        quadTree.insert(new BaseRectangle(5.0, 5.0, 10.0, 10.0, "1"));
        quadTree.insert(new BaseRectangle(25.0, 25.0, 10.0, 10.0, "2"));
        quadTree.insert(new BaseRectangle(5.0, 5.0, 12.0, 10.0, "3"));
        quadTree.insert(new BaseRectangle(25.0, 25.0, 10.0, 10.0, "4"));
        quadTree.insert(new BaseRectangle(5.0, 25.0, 20.0, 10.0, "5"));
        quadTree.insert(new BaseRectangle(25.0, 5.0, 10.0, 10.0, "6"));
        quadTree.insert(new BaseRectangle(2.0, 2.0, 2.0, 2.0, "7"));

        List<Rectangle> rectangles = quadTree.search(new BaseRectangle(3.0, 3.0, 3.0, 3.0));
    }

    public static void rTree() {
        RTree rtree = new RTree();
        rtree.insert(new BaseRectangle(5.0, 5.0, 10.0, 10.0, "1"));
        rtree.insert(new BaseRectangle(25.0, 25.0, 10.0, 10.0, "2"));
        rtree.insert(new BaseRectangle(5.0, 5.0, 12.0, 10.0, "3"));
        rtree.insert(new BaseRectangle(25.0, 25.0, 10.0, 10.0, "4"));
        rtree.insert(new BaseRectangle(5.0, 25.0, 20.0, 10.0, "5"));
        rtree.insert(new BaseRectangle(25.0, 5.0, 10.0, 10.0, "6"));
        rtree.insert(new BaseRectangle(2.0, 2.0, 2.0, 2.0, "7"));

        List<Rectangle> rectangles = rtree.search(new BaseRectangle(3.0, 3.0, 3.0, 3.0));
    }

    public static void kdTree() {
        KDTree kdTree = new KDTree();
        KDNode root = new KDNode(new Point(0, 0));
        kdTree.insert(root, new Point(1, 1));
        kdTree.insert(root, new Point(1, 2));
        kdTree.insert(root, new Point(3, 2));
        kdTree.insert(root, new Point(3, 3));
        kdTree.insert(root, new Point(2, 6));
        kdTree.insert(root, new Point(4, 5));
        kdTree.insert(root, new Point(8, 1));

        boolean isPresent = kdTree.search(root, new Point(1, 2));
    }

    public static void quadKdTree() {
        QuadKDTree hQuadKDTree = new QuadKDTree();
        QuadTree hQuadTree = new QuadTree();
        hQuadKDTree.insert(hQuadTree, new BaseRectangle(10.0, 10.0, 10.0, 10.0, "0"));
        hQuadKDTree.insert(hQuadTree, new BaseRectangle(5.0, 5.0, 10.0, 10.0, "1"));
        hQuadKDTree.insert(hQuadTree, new BaseRectangle(25.0, 25.0, 10.0, 10.0, "2"));
        hQuadKDTree.insert(hQuadTree, new BaseRectangle(5.0, 5.0, 12.0, 10.0, "3"));
        hQuadKDTree.insert(hQuadTree, new BaseRectangle(25.0, 25.0, 10.0, 10.0, "4"));
        hQuadKDTree.insert(hQuadTree, new BaseRectangle(5.0, 25.0, 20.0, 10.0, "5"));
        hQuadKDTree.insert(hQuadTree, new BaseRectangle(25.0, 5.0, 10.0, 10.0, "6"));
        hQuadKDTree.insert(hQuadTree, new BaseRectangle(2.0, 2.0, 2.0, 2.0, "7"));

        List<Rectangle> rectangles = hQuadKDTree.search(hQuadTree, new BaseRectangle(3.0, 3.0, 3.0, 3.0));

        Rectangle qBoundary = new BaseRectangle(5.0, 5.0, 5.0, 5.0);
        hQuadKDTree.insert(hQuadTree, qBoundary, new Point(1, 1));
        hQuadKDTree.insert(hQuadTree, qBoundary, new Point(2, 2));
        hQuadKDTree.insert(hQuadTree, qBoundary, new Point(1, 2));

        boolean isPresent = hQuadKDTree.search(hQuadTree, qBoundary, new Point(1, 1));
    }

    public static void rKdTree() {
        RKDTree hRKDTree = new RKDTree();
        RTree hRTree = new RTree();
        hRKDTree.insert(hRTree, new BaseRectangle(10.0, 10.0, 10.0, 10.0, "0"));
        hRKDTree.insert(hRTree, new BaseRectangle(5.0, 5.0, 10.0, 10.0, "1"));
        hRKDTree.insert(hRTree, new BaseRectangle(25.0, 25.0, 10.0, 10.0, "2"));
        hRKDTree.insert(hRTree, new BaseRectangle(5.0, 5.0, 12.0, 10.0, "3"));
        hRKDTree.insert(hRTree, new BaseRectangle(25.0, 25.0, 10.0, 10.0, "4"));
        hRKDTree.insert(hRTree, new BaseRectangle(5.0, 25.0, 20.0, 10.0, "5"));
        hRKDTree.insert(hRTree, new BaseRectangle(25.0, 5.0, 10.0, 10.0, "6"));
        hRKDTree.insert(hRTree, new BaseRectangle(2.0, 2.0, 2.0, 2.0, "7"));

        List<Rectangle> rectangles = hRKDTree.search(hRTree, new BaseRectangle(3.0, 3.0, 3.0, 3.0));

        Rectangle rBoundary = new BaseRectangle(5.0, 5.0, 5.0, 5.0);
        hRKDTree.insert(hRTree, rBoundary, new Point(1, 1));
        hRKDTree.insert(hRTree, rBoundary, new Point(2, 2));
        hRKDTree.insert(hRTree, rBoundary, new Point(1, 2));

        boolean isPresent = hRKDTree.search(hRTree, rBoundary, new Point(1, 1));
    }
}
