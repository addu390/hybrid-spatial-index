package operations;

import models.Point;
import models.Rectangle;

import java.util.List;

public interface HybridTree extends Tree {

    List<Rectangle> search(RegionTree tree, Rectangle rectangle);

    void insert(RegionTree tree, Rectangle rectangle);

    boolean search(RegionTree tree, Rectangle boundary, Point point);

    void insert(RegionTree tree, Rectangle boundary, Point point);
}
