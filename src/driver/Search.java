package driver;

import models.kd.KDNode;
import models.kd.Point;
import operations.index.KDTree;
import operations.index.QuadTree;
import models.quad.BaseRectangle;
import models.quad.Rectangle;
import operations.index.RTree;

import java.util.List;

public class Search {

    public static void main(String[] args) {
        QuadTree quadTree = new QuadTree();
        quadTree.insert(new BaseRectangle(5.0, 5.0, 10.0, 10.0, "1"));
        quadTree.insert(new BaseRectangle(25.0, 25.0, 10.0, 10.0, "2"));
        quadTree.insert(new BaseRectangle(5.0, 5.0, 12.0, 10.0, "3"));
        quadTree.insert(new BaseRectangle(25.0, 25.0, 10.0, 10.0, "4"));
        quadTree.insert(new BaseRectangle(5.0, 25.0, 20.0, 10.0, "5"));
        quadTree.insert(new BaseRectangle(25.0, 5.0, 10.0, 10.0, "6"));
        quadTree.insert(new BaseRectangle(2.0, 2.0, 2.0, 2.0, "6"));

        List<Rectangle> qRectangles = quadTree.search(new BaseRectangle(3.0, 3.0, 3.0, 3.0));

        RTree rtree = new RTree();
        rtree.insert(new BaseRectangle(5.0, 5.0, 10.0, 10.0, "1"));
        rtree.insert(new BaseRectangle(25.0, 25.0, 10.0, 10.0, "2"));
        rtree.insert(new BaseRectangle(5.0, 5.0, 12.0, 10.0, "3"));
        rtree.insert(new BaseRectangle(25.0, 25.0, 10.0, 10.0, "4"));
        rtree.insert(new BaseRectangle(5.0, 25.0, 20.0, 10.0, "5"));
        rtree.insert(new BaseRectangle(25.0, 5.0, 10.0, 10.0, "6"));
        rtree.insert(new BaseRectangle(2.0, 2.0, 2.0, 2.0, "6"));

        List<Rectangle> rRectangles = rtree.search(new BaseRectangle(3.0, 3.0, 3.0, 3.0));

        KDTree kdTree = new KDTree();
        KDNode root = new KDNode(new Point(0, 0));
        kdTree.insert(root, new Point(1, 1));
        kdTree.insert(root, new Point(1, 2));
        kdTree.insert(root, new Point(3, 2));
        kdTree.insert(root, new Point(3, 3));
        kdTree.insert(root, new Point(2, 6));
        kdTree.insert(root, new Point(4, 5));
        kdTree.insert(root, new Point(8, 1));

        kdTree.search(root, new Point(1, 2));

        KDTree kdTree2 = new KDTree();
    }
}
