package operations.index;

import models.kd.Point;
import models.quad.Rectangle;
import operations.HybridTree;
import operations.Tree;

public class QuadKDTree implements HybridTree {

    @Override
    public Rectangle search(Tree tree, Rectangle rectangle) {
        QuadTree quadTree = (QuadTree) tree;
        return null;
    }

    @Override
    public void insert(Tree tree, Rectangle rectangle) {
        QuadTree quadTree = (QuadTree) tree;
    }

    @Override
    public Point search(Tree tree, Rectangle boundary, Point point) {
        QuadTree quadTree = (QuadTree) tree;
        return null;
    }

    @Override
    public void insert(Tree tree, Rectangle boundary, Point point) {
        QuadTree quadTree = (QuadTree) tree;
    }

    private boolean isPoint(Rectangle rectangle) {
        if (rectangle.getX().equals(rectangle.getY())
                && rectangle.getW().equals(rectangle.getH())
                && rectangle.getX().equals(rectangle.getW())) {
            return true;
        }
        return false;
    }
}
