package operations;

import models.quad.Rectangle;
import models.quad.QuadRectangle;
import util.Geometry;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class QuadTree {

    protected static final int THIS_QUADTREE = -1;
    protected static final int NE_CHILD = 0;
    protected static final int NW_CHILD = 1;
    protected static final int SW_CHILD = 2;
    protected static final int SE_CHILD = 3;

    private int maxObjects = 10;
    private int maxLevels = 5;
    private int defaultWidth = 100;
    private int defaultHeight = 100;

    private QuadTree parent;

    private QuadTree[] children;

    private List<Rectangle> rectangles;

    private Integer level;

    private Integer x;
    private Integer y;
    private Integer w;
    private Integer h;

    public QuadTree() {
        this.level = 0;
        this.x = 0;
        this.y = 0;
        this.w = defaultWidth;
        this.h = defaultHeight;
        this.rectangles = new ArrayList<>();
        this.parent = null;
        this.children = new QuadTree[4];
    }

    public QuadTree(int maxObjects, int maxLevels, int level, int x, int y, int w, int h, QuadTree parent) {
        this.maxObjects = maxObjects;
        this.maxLevels = maxLevels;
        this.level = level;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.rectangles = new ArrayList<>();
        this.parent = parent;
        this.children = new QuadTree[4];
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Integer getW() {
        return w;
    }

    public Integer getH() {
        return h;
    }

    public List<Rectangle> search(Rectangle rectangle) {

        List<Rectangle> returnList = new ArrayList<>();
        ListIterator<Rectangle> iterator = search(new ArrayList<Rectangle>(), rectangle).listIterator();
        while (iterator.hasNext()) {
            Rectangle currentRectangle = iterator.next();
            if (Geometry.rectangleObjectsOverlap(currentRectangle, rectangle)) {
                returnList.add(currentRectangle);
            }
        }
        return returnList;
    }

    public void insert(Rectangle rectangle) {
        if (children[0] != null) {
            int indexToPlaceObject = getChildIndexRectangleBelongsIn(rectangle);

            if (indexToPlaceObject != QuadTree.THIS_QUADTREE) {
                children[indexToPlaceObject].insert(rectangle);
                return;
            }
        }
        rectangles.add(rectangle);

        if (rectangles.size() > this.maxObjects && this.level < this.maxLevels) {
            split();

            int i = 0;
            while (i < rectangles.size()) {
                int indexToPlaceObject = getChildIndexRectangleBelongsIn(rectangles.get(i));
                if (indexToPlaceObject != QuadTree.THIS_QUADTREE) {
                    children[indexToPlaceObject].insert(rectangles.remove(i));
                } else {
                    i++;
                }
            }
        }
    }

    private List<Rectangle> search(List<Rectangle> rectangles, Rectangle rectangle) {

        rectangles.addAll(this.rectangles);

        int index = getChildIndexRectangleBelongsIn(rectangle);
        if (index == QuadTree.THIS_QUADTREE || this.children[0] == null) {
            if (this.children[0] != null) {
                for (int i = 0; i < this.children.length; i++) {
                    if (Geometry.rectangleObjectsOverlap(
                            new QuadRectangle(Double.valueOf(this.children[i].getX()),
                                              Double.valueOf(this.children[i].getY()), Double.valueOf(this.children[i].getW()),
                                              Double.valueOf(this.children[i].getH())), rectangle)) {

                        this.children[i].search(rectangles, rectangle);
                    }
                }
            }
        } else if (this.children[index] != null) {
            this.children[index].search(rectangles, rectangle);
        }
        return rectangles;
    }

    private void split() {
        int childWidth = this.w / 2;
        int childHeight = this.h / 2;

        children[QuadTree.NE_CHILD] = new QuadTree(this.maxObjects, this.maxLevels, level + 1, this.x + childWidth, this.y, childWidth, childHeight, this);
        children[QuadTree.NW_CHILD] = new QuadTree(this.maxObjects, this.maxLevels, this.level + 1, this.x, this.y, childWidth, childHeight, this);
        children[QuadTree.SW_CHILD] = new QuadTree(this.maxObjects, this.maxLevels, this.level + 1, this.x, this.y + childHeight, childWidth, childHeight, this);
        children[QuadTree.SE_CHILD] = new QuadTree(this.maxObjects, this.maxLevels, this.level + 1, this.x + childWidth, this.y + childHeight, childWidth, childHeight, this);
    }

    protected int getChildIndexRectangleBelongsIn(Rectangle rectangleObject) {
        int index = -1;
        double verticalDividingLine = getX() + getW() / 2;
        double horizontalDividingLine = getY() + getH() / 2;

        boolean fitsCompletelyInNorthHalf = rectangleObject.getY() < horizontalDividingLine && (rectangleObject.getH() + rectangleObject.getY() < horizontalDividingLine);
        boolean fitsCompletelyInSouthHalf = rectangleObject.getY() > horizontalDividingLine;
        boolean fitsCompletelyInWestHalf = rectangleObject.getX() < verticalDividingLine && (rectangleObject.getX() + rectangleObject.getW() < verticalDividingLine);
        boolean fitsCompletelyInEastHalf = rectangleObject.getX() > verticalDividingLine;

        if (fitsCompletelyInEastHalf) {
            if (fitsCompletelyInNorthHalf) {
                index = QuadTree.NE_CHILD;
            } else if (fitsCompletelyInSouthHalf) {
                index = QuadTree.SE_CHILD;
            }
        } else if (fitsCompletelyInWestHalf) {
            if (fitsCompletelyInNorthHalf) {
                index = QuadTree.NW_CHILD;
            } else if (fitsCompletelyInSouthHalf) {
                index = QuadTree.SW_CHILD;
            }
        }
        return index;
    }
}
