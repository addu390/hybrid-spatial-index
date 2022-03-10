package operations.index;

import models.kd.KDNode;
import models.kd.Point;
import models.quad.Rectangle;
import operations.HybridTree;
import operations.Tree;

import java.util.List;

public class QuadKDTree implements HybridTree {

    @Override
    public List<Rectangle> search(Tree tree, Rectangle rectangle) {
        QuadTree quadTree = (QuadTree) tree;
        return quadTree.search(rectangle);
    }

    @Override
    public void insert(Tree tree, Rectangle rectangle) {
        QuadTree quadTree = (QuadTree) tree;
        quadTree.insert(rectangle);
    }

    @Override
    public boolean search(Tree tree, Rectangle boundary, Point point) {
        QuadTree quadTree = (QuadTree) tree;
        List<Rectangle> rectangles = quadTree.search(boundary);

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

    @Override
    public void insert(Tree tree, Rectangle boundary, Point point) {
        QuadTree quadTree = (QuadTree) tree;
        List<Rectangle> rectangles = search(tree, boundary);

        if (rectangles.isEmpty()) {
            boundary.setBoundary(true);
            boundary.setNode(new KDNode(point));
            quadTree.insert(boundary);
        } else {
            for (Rectangle rectangle: rectangles) {
                if (rectangle.getBoundary()) {
                    KDTree kdTree = new KDTree();
                    KDNode kdNode = (KDNode) rectangle.getNode();
                    kdTree.insert(kdNode, point);
                }
            }
        }
    }
}
