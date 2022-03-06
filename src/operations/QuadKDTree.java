package operations;

import models.kd.Point;
import models.quad.BaseRectangle;
import models.quad.Rectangle;

public class QuadKDTree implements Tree {

    public boolean search(QuadTree quadTree, Rectangle boundary, Point point) {
        return true;
    }

    public Rectangle search(Rectangle rectangle) {
        return new BaseRectangle();
    }

    public void insert(Rectangle boundary, Point point) {
        // Treat the boundingBox as a regular rectangle (use a flag).
        // Add a KDTreeNode for the bounding box (rectangle).
        // Any successive inserts to the same BBox will insert to the existing KDTreeNode.

        // Bounding boxes cannot overlap.
        // There can be a BBox and Rectangle (Only a flag to differentiate).
    }

    public void insert(Rectangle rectangle) {

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
