package operations;

import models.kd.KDNode;
import models.Point;
import models.Rectangle;
import operations.index.KDTree;

import java.util.List;

public class AbstractHybridTree implements HybridTree {

    public boolean search(SpaceTree tree, Rectangle boundary, Point point) {
        List<Rectangle> rectangles = tree.search(boundary);

        for (Rectangle rectangle: rectangles) {
            if (rectangle.getBoundary()) {
                KDTree kdTree = new KDTree();
                KDNode kdNode = (KDNode) rectangle.getNode();
                if (kdTree.search(kdNode, point)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void insert(SpaceTree tree, Rectangle boundary, Point point) {
        List<Rectangle> rectangles = search(tree, boundary);

        if (!rectangles.isEmpty()) {
            for (Rectangle rectangle : rectangles) {
                if (rectangle.getBoundary()) {
                    KDTree kdTree = new KDTree();
                    KDNode kdNode = (KDNode) rectangle.getNode();
                    kdTree.insert(kdNode, point);
                    return;
                }
            }
        }
        boundary.setBoundary(true);
        boundary.setNode(new KDNode(point));
        tree.insert(boundary);
    }

    public List<Rectangle> search(SpaceTree tree, Rectangle rectangle) {
        return tree.search(rectangle);
    }

    public void insert(SpaceTree tree, Rectangle rectangle) {
        tree.insert(rectangle);
    }
}
