package operations;

import models.kd.Point;
import models.quad.Rectangle;

import java.util.List;

public interface HybridTree extends Tree {

    List<Rectangle> search(SpaceTree tree, Rectangle rectangle);

    void insert(SpaceTree tree, Rectangle rectangle);

    boolean search(SpaceTree tree, Rectangle boundary, Point point);

    void insert(SpaceTree tree, Rectangle boundary, Point point);
}
