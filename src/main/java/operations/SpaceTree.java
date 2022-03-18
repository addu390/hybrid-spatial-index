package operations;

import models.Rectangle;

import java.util.List;

public interface SpaceTree extends Tree {

    List<Rectangle> search(Rectangle rectangle);

    void insert(Rectangle rectangle);

    boolean delete(Rectangle rectangle);
}
