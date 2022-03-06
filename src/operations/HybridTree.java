package operations;

import models.quad.Rectangle;

public class HybridTree {

    public void insert(Rectangle boundingBox, int[] point) {

        // Treat the boundingBox as a regular rectangle (use a flag).
        // Add a KDTreeNode for the bounding box (rectangle).
        // Any successive inserts to the same BBox will insert to the existing KDTreeNode.

        // Bounding boxes cannot overlap.
        // There can be a BBox and Rectangle (Only a flag to differentiate).
    }

    public void insert(Rectangle rectangle) {

    }

    public void search(Rectangle boundingBox, int[] point) {

    }

    public void search(Rectangle rectangle) {

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
