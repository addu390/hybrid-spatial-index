package driver;

import operations.QuadTree;
import models.quad.QuadRectangle;
import models.quad.Rectangle;

import java.util.List;

public class Search {

	public static void main(String[] args) {
		QuadTree quadTree = new QuadTree();
		quadTree.insert(new QuadRectangle(5.0, 5.0, 10.0, 10.0));
		quadTree.insert(new QuadRectangle(25.0, 25.0, 10.0, 10.0));
		quadTree.insert(new QuadRectangle(5.0, 5.0, 12.0, 10.0));
		quadTree.insert(new QuadRectangle(25.0, 25.0, 10.0, 10.0));
		quadTree.insert(new QuadRectangle(5.0, 25.0, 20.0, 10.0));
		quadTree.insert(new QuadRectangle(25.0, 5.0, 10.0, 10.0));
		quadTree.insert(new QuadRectangle(2.0, 2.0, 2.0, 2.0));

		List<Rectangle> rectangles = quadTree.search(new QuadRectangle(3.0, 3.0, 3.0, 3.0));
	}
}
