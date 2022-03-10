package operations.index;

import models.kd.Point;
import models.quad.Rectangle;
import operations.HybridTree;
import operations.Tree;

import java.util.List;

public class RKDTree implements HybridTree {

    @Override
    public List<Rectangle> search(Tree tree, Rectangle rectangle) {
        RTree rTree = (RTree) tree;
        return rTree.search(rectangle);
    }

    @Override
    public void insert(Tree tree, Rectangle rectangle) {
        RTree rTree = (RTree) tree;
        rTree.insert(rectangle);
    }

    @Override
    public boolean search(Tree tree, Rectangle boundary, Point point) {
        RTree rTree = (RTree) tree;
        return true;
    }

    @Override
    public void insert(Tree tree, Rectangle boundary, Point point) {
        RTree rTree = (RTree) tree;
    }
}
