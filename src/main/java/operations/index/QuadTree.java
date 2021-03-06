package operations.index;

import models.Rectangle;
import models.quad.BaseRectangle;
import operations.RegionTree;
import util.Geometry;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class QuadTree implements RegionTree {

    protected static final int QUADTREE = -1;
    protected static final int NE_CHILD = 0;
    protected static final int NW_CHILD = 1;
    protected static final int SW_CHILD = 2;
    protected static final int SE_CHILD = 3;

    private int maxObjects = 100;
    private int maxLevels = 50;

    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 100;

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
        this.w = DEFAULT_WIDTH;
        this.h = DEFAULT_HEIGHT;
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

    @Override
    public List<Rectangle> search(Rectangle rectangle) {

        List<Rectangle> returnList = new ArrayList<>();
        ListIterator<Rectangle> iterator = search(new ArrayList<Rectangle>(), rectangle).listIterator();
        while (iterator.hasNext()) {
            Rectangle currentRectangle = iterator.next();
            if (Geometry.isOverlap(currentRectangle, rectangle)) {
                returnList.add(currentRectangle);
            }
        }
        return returnList;
    }

    @Override
    public void insert(Rectangle rectangle) {
        if (children[0] != null) {
            int indexToPlaceObject = getChildIndex(rectangle);

            if (indexToPlaceObject != QuadTree.QUADTREE) {
                children[indexToPlaceObject].insert(rectangle);
                return;
            }
        }
        rectangles.add(rectangle);

        if (rectangles.size() > this.maxObjects && this.level < this.maxLevels) {
            split();

            int i = 0;
            while (i < rectangles.size()) {
                int indexToPlaceObject = getChildIndex(rectangles.get(i));
                if (indexToPlaceObject != QuadTree.QUADTREE) {
                    children[indexToPlaceObject].insert(rectangles.remove(i));
                } else {
                    i++;
                }
            }
        }
    }

    @Override
    public boolean delete(Rectangle rectangle) {
        int index = getChildIndex(rectangle);
        if (index == QuadTree.QUADTREE || this.children[index] == null){
            for (int i = 0; i < this.rectangles.size(); i++) {
                if (rectangles.get(i).getId().equals(rectangle.getId())){
                    rectangles.remove(i);
                    return true;
                }
            }
        } else {
            this.children[index].delete(rectangle);
            return true;
        }

        return false;
    }

    private List<Rectangle> search(List<Rectangle> rectangles, Rectangle rectangle) {

        rectangles.addAll(this.rectangles);

        int index = getChildIndex(rectangle);
        if (index == QuadTree.QUADTREE || this.children[0] == null) {
            if (this.children[0] != null) {
                for (int i = 0; i < this.children.length; i++) {
                    if (Geometry.isOverlap(
                            new BaseRectangle(Double.valueOf(this.children[i].getX()),
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

    protected int getChildIndex(Rectangle rectangle) {
        int index = -1;
        double verticalDividingLine = getX() + getW() / 2;
        double horizontalDividingLine = getY() + getH() / 2;

        boolean fitsCompletelyInNorthHalf = rectangle.getY() < horizontalDividingLine && (rectangle.getH() + rectangle.getY() < horizontalDividingLine);
        boolean fitsCompletelyInSouthHalf = rectangle.getY() > horizontalDividingLine;
        boolean fitsCompletelyInWestHalf = rectangle.getX() < verticalDividingLine && (rectangle.getX() + rectangle.getW() < verticalDividingLine);
        boolean fitsCompletelyInEastHalf = rectangle.getX() > verticalDividingLine;

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
