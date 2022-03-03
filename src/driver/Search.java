package driver;

import operations.QuadTree;
import models.quad.BaseRectangle;
import models.quad.Rectangle;
import operations.RTree;

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

        List<Rectangle> q_rectangles = quadTree.search(new BaseRectangle(3.0, 3.0, 3.0, 3.0));

        RTree<String> rtree = new RTree<String>();
        rtree.insert(new BaseRectangle(5.0, 5.0, 10.0, 10.0, "1"));
        rtree.insert(new BaseRectangle(25.0, 25.0, 10.0, 10.0, "2"));
        rtree.insert(new BaseRectangle(5.0, 5.0, 12.0, 10.0, "3"));
        rtree.insert(new BaseRectangle(25.0, 25.0, 10.0, 10.0, "4"));
        rtree.insert(new BaseRectangle(5.0, 25.0, 20.0, 10.0, "5"));
        rtree.insert(new BaseRectangle(25.0, 5.0, 10.0, 10.0, "6"));
        rtree.insert(new BaseRectangle(2.0, 2.0, 2.0, 2.0, "6"));

        List<String> r_rectangles = rtree.search(new BaseRectangle(3.0, 3.0, 3.0, 3.0));
    }
}
