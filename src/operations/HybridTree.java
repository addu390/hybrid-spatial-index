package operations;

import models.kd.Point;
import models.quad.Rectangle;

import java.util.List;

public interface HybridTree extends Tree {

    List<Rectangle> search(Tree tree, Rectangle rectangle);

    void insert(Tree tree, Rectangle rectangle);

    boolean search(Tree tree, Rectangle boundary, Point point);

    void insert(Tree tree, Rectangle boundary, Point point);
}
