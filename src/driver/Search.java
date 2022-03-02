package driver;

import operations.QuadTree;
import models.quad.QuadRectangle;
import models.quad.Rectangle;
import operations.RTree;

import java.util.List;

public class Search {

    public static void main(String[] args) {
        QuadTree quadTree = new QuadTree();
        quadTree.insert(new QuadRectangle(5.0, 5.0, 10.0, 10.0));
        quadTree.insert(new QuadRectangle(25.0, 25.0, 10.0, 10.0));
        quadTree.insert(new QuadRectangle(5.0, 5.0, 12.0, 10.0));
        quadTree.insert(new QuadRectangle(25.0, 25.0, 10.0, 10.0));
        quadTree.insert(new QuadRectangle(5.0, 25.0, 20.0, 10.0));
        quadTree.insert(new QuadRectangle(25.0, 5.0, 10.0, 10.0));
        quadTree.insert(new QuadRectangle(2.0, 2.0, 2.0, 2.0));

        List<Rectangle> q_rectangles = quadTree.search(new QuadRectangle(3.0, 3.0, 3.0, 3.0));

        RTree<Object> rtree = new RTree<Object>();
        rtree.insert(new double[]{5.0, 5.0}, new double[]{10.0, 10.0}, "1");
        rtree.insert(new double[]{25.0, 25.0}, new double[]{10.0, 10.0}, "2");
        rtree.insert(new double[]{5.0, 5.0}, new double[]{12.0, 10.0}, "3");
        rtree.insert(new double[]{25.0, 25.0}, new double[]{10.0, 10.0}, "4");
        rtree.insert(new double[]{5.0, 25.0}, new double[]{20.0, 10.0}, "5");
        rtree.insert(new double[]{25.0, 5.0}, new double[]{10.0, 10.0}, "6");
        rtree.insert(new double[]{2.0, 2.0}, new double[]{2.0, 2.0}, "7");

        List<Object> r_rectangles = rtree.search(new double[]{3.0, 3.0}, new double[]{3.0, 3.0});
    }
}
