package operations;

import models.kd.Point;
import models.quad.Rectangle;

public class RKDTree implements HybridTree {

    @Override
    public Rectangle search(Tree tree, Rectangle rectangle) {
        RTree rTree = (RTree) tree;
        return null;
    }

    @Override
    public void insert(Tree tree, Rectangle rectangle) {
        RTree rTree = (RTree) tree;
    }

    @Override
    public Point search(Tree tree, Rectangle boundary, Point point) {
        RTree rTree = (RTree) tree;
        return null;
    }

    @Override
    public void insert(Tree tree, Rectangle boundary, Point point) {
        RTree rTree = (RTree) tree;
    }
}
