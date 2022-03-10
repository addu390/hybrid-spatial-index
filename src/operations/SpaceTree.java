package operations;

import models.quad.Rectangle;

import java.util.List;

public interface SpaceTree extends Tree {

    List<Rectangle> search(Rectangle rectangle);

    void insert(Rectangle rectangle);

    boolean delete(Rectangle rectangle);
}
