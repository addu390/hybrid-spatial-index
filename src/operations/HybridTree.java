package operations;

import models.kd.Point;
import models.quad.Rectangle;

public interface HybridTree extends Tree {

    Rectangle search(Tree tree, Rectangle rectangle);

    void insert(Tree tree, Rectangle rectangle);

    Point search(Tree tree, Rectangle boundary, Point point);

    void insert(Tree tree, Rectangle boundary, Point point);
}
