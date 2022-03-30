package operations;

import models.Rectangle;

import java.util.List;

public interface RegionTree extends Tree {

    List<Rectangle> search(Rectangle rectangle);

    void insert(Rectangle rectangle);

    boolean delete(Rectangle rectangle);
}
