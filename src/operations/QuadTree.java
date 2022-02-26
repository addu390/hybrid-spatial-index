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

    //config
    private int maxObjects = 10;
    private int maxLevels = 5;
    private int defaultWidth = 100;
    private int defaultHeight = 100;

    //link to parent
    private QuadTree parent;
    
    //children
    private QuadTree[] children;
    
    //objects in node
    private List<Rectangle> rectangles;

    //how deep this QuadTree is
    private Integer level;

    //bounds
    private Integer x;
    private Integer y;
    private Integer w;
    private Integer h;

    //create a QuadTree with no parent, intended for creating root node
    public QuadTree(){
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

    public void insert(Rectangle rectangle) {
        //if this QuadTree has children (only need to check the first one to be sure of that)
        if (children[0] != null) {
            int indexToPlaceObject = getChildIndexRectangleBelongsIn(rectangle);

            //if it doesn't belong on this QuadTree, let one of the children find where to put it
            if (indexToPlaceObject != QuadTree.THIS_QUADTREE) {
                children[indexToPlaceObject].insert(rectangle);
                return;
            }
        }
        //add the object to the list for this QuadTree
        rectangles.add(rectangle);

        //if we need to split up this QuadTree
        if (rectangles.size() > this.maxObjects && this.level < this.maxLevels) {
            //code I am stealing had a check right here, which I think is unnecessary
            split();

            //just split QuadTree, lets put the objects that are on this QuadTree where they belong
            int i = 0;
            while (i < rectangles.size()) {
                int indexToPlaceObject = getChildIndexRectangleBelongsIn(rectangles.get(i));
                //if the object does not belong on this QuadTree
                if (indexToPlaceObject != QuadTree.THIS_QUADTREE) {
                    //if we remove an object don't increment i, the next object bumps down to where i is
                    children[indexToPlaceObject].insert(rectangles.remove(i));
                } else {
                    i++;
                }
            }
        }
    }
    
    //probably should add a re-balance function to quadtrees where it takes all its kids and re-inserts them
    public Rectangle remove(Rectangle rectangle) {
        int index = getChildIndexRectangleBelongsIn(rectangle);
        if (index == QuadTree.THIS_QUADTREE || this.children[index] == null) {
            for (int i = 0; i < this.rectangles.size(); i++) {
                if (rectangles.get(i).getId().equals(rectangle.getId())) {
                    return rectangles.remove(i);
                }
            }
        } else {
            return this.children[index].remove(rectangle);
        }

        return null;
    }
    
    public void update(Rectangle rectangleBefore, Rectangle rectangleAfter){

    	QuadTree quadTree = this;
    	int index = quadTree.getChildIndexRectangleBelongsIn(rectangleBefore);

    	while (index != QuadTree.THIS_QUADTREE && quadTree.children[index] != null) {
    		quadTree = quadTree.children[index];
    		index = quadTree.getChildIndexRectangleBelongsIn(rectangleBefore);
    	}

        for (int i = 0; i < quadTree.getRectangleObjects().size(); i++) {
            if (quadTree.getRectangleObjects().get(i).getId().equals(rectangleBefore.getId())) {

            	Rectangle rectangleObjectToUpdate = quadTree.getRectangleObjects().remove(i);
            	rectangleObjectToUpdate.setX(rectangleAfter.getX());
            	rectangleObjectToUpdate.setY(rectangleAfter.getY());
            	rectangleObjectToUpdate.setW(rectangleAfter.getW());
            	rectangleObjectToUpdate.setH(rectangleAfter.getH());

            	if (Geometry.rectangleObjectIsInside(new QuadRectangle(Double.valueOf(quadTree.getX()), Double.valueOf(quadTree.getY()), Double.valueOf(quadTree.getW()), Double.valueOf(quadTree.getH())),rectangleAfter)) {
            		quadTree.insert(rectangleObjectToUpdate);
            	} else {
            		quadTree = quadTree.getParent();
            		while(quadTree.getParent() != null && !Geometry.rectangleObjectIsInside(new QuadRectangle(Double.valueOf(quadTree.getX()), Double.valueOf(quadTree.getY()), Double.valueOf(quadTree.getW()), Double.valueOf(quadTree.getH())), rectangleObjectToUpdate)){
            			quadTree = quadTree.getParent();
            		}
            		if (Geometry.rectangleObjectIsInside(new QuadRectangle(Double.valueOf(quadTree.getX()), Double.valueOf(quadTree.getY()), Double.valueOf(quadTree.getW()), Double.valueOf(quadTree.getH())), rectangleObjectToUpdate)) {
                		quadTree.insert(rectangleObjectToUpdate);
            		}
            		
            	}
            	//will never be updating more than one, so top going through the list once we find a match
            	break;
            }
        }
    }

    public List<Rectangle> search(Rectangle rectangleObject){

        List<Rectangle> returnList = new ArrayList<>();
        //here I will need to filter through these and only return the objects that are in the search area (even if they are partially in the search area)
        ListIterator<Rectangle> iterator = search(new ArrayList<Rectangle>(), rectangleObject).listIterator();
        while (iterator.hasNext()){
            Rectangle currentRectangleObject = iterator.next();
            if (Geometry.rectangleObjectsOverlap(currentRectangleObject, rectangleObject)){
                returnList.add(currentRectangleObject);
            }
        }
        return returnList;
    }

    public Integer getDepth() {
        return 1 + Math.max(
                Math.max(this.children[QuadTree.NE_CHILD] == null ? 0 : this.children[QuadTree.NE_CHILD].getDepth(), this.children[QuadTree.NW_CHILD] == null ? 0 : this.children[QuadTree.NW_CHILD].getDepth()),
                Math.max(this.children[QuadTree.SW_CHILD] == null ? 0 : this.children[QuadTree.SW_CHILD].getDepth(), this.children[QuadTree.SE_CHILD] == null ? 0 : this.children[QuadTree.SE_CHILD].getDepth())
        );
    }

    public Integer getTotalObjects() {
        return this.rectangles.size() +
                (this.children[QuadTree.NE_CHILD] == null ? 0 : this.children[QuadTree.NE_CHILD].getTotalObjects()) +
                (this.children[QuadTree.NW_CHILD] == null ? 0 : this.children[QuadTree.NW_CHILD].getTotalObjects()) +
                (this.children[QuadTree.SW_CHILD] == null ? 0 : this.children[QuadTree.SW_CHILD].getTotalObjects()) +
                (this.children[QuadTree.SE_CHILD] == null ? 0 : this.children[QuadTree.SE_CHILD].getTotalObjects());
    }

    //recursively remove this quadtree's children
    public void clear(){
        this.rectangles.clear();
        for(int i = 0; i < children.length; i++) {
            if(children[i] != null){
                children[i].clear();
                children[i] = null;
            }
        }
    }

    private List<Rectangle> search(List<Rectangle> rectangleObjects, Rectangle rectangleObject) {

        rectangleObjects.addAll(this.rectangles);

        int index = getChildIndexRectangleBelongsIn(rectangleObject);
        //if the search area does not fit into any of the children perfectly
        if(index == QuadTree.THIS_QUADTREE || this.children[0] == null){
            //add anything that is on this QuadTree, may need to recurse down and add more
            if(this.children[0] != null){
                //for each of the children, if the search area overlaps with the child area, search the child
                for(int i = 0; i < this.children.length; i++){
                    if(Geometry.rectangleObjectsOverlap(new QuadRectangle(Double.valueOf(this.children[i].getX()), Double.valueOf(this.children[i].getY()),Double.valueOf(this.children[i].getW()), Double.valueOf(this.children[i].getH())), rectangleObject)){
                        this.children[i].search(rectangleObjects, rectangleObject);
                    }
                }
            }
        } else if(this.children[index] != null){
            //search area is in one of the children totally, but we still can't exclude the objects on this node, because that search area could include one
            this.children[index].search(rectangleObjects, rectangleObject);
        }
        return rectangleObjects;
    }

    //instantiate the four children
    private void split(){
        int childWidth = this.w / 2;
        int childHeight = this.h / 2;

        children[QuadTree.NE_CHILD] = new QuadTree(this.maxObjects, this.maxLevels,level + 1, this.x + childWidth, this.y, childWidth, childHeight, this);
        children[QuadTree.NW_CHILD] = new QuadTree(this.maxObjects, this.maxLevels,this.level + 1, this.x, this.y, childWidth, childHeight, this);
        children[QuadTree.SW_CHILD] = new QuadTree(this.maxObjects, this.maxLevels,this.level + 1, this.x, this.y + childHeight, childWidth, childHeight, this);
        children[QuadTree.SE_CHILD] = new QuadTree(this.maxObjects, this.maxLevels,this.level + 1, this.x + childWidth, this.y + childHeight, childWidth, childHeight, this);
    }
    
    protected int getChildIndexRectangleBelongsIn(Rectangle rectangleObject){
        //-1 means the object does not fit in any of the children, keep it on the parent
        int index = -1;
        double verticalDividingLine = getX() + getW() / 2;
        double horizontalDividingLine = getY() + getH() / 2;

        //its funny, here you might be tempted to think about what happens if it goes over the bounds, but if it did...we wouldn't have gotten this far, it would be on a parent QuadTree!
        boolean fitsCompletelyInNorthHalf = rectangleObject.getY() < horizontalDividingLine && (rectangleObject.getH() + rectangleObject.getY() < horizontalDividingLine);
        boolean fitsCompletelyInSouthHalf = rectangleObject.getY() > horizontalDividingLine;
        boolean fitsCompletelyInWestHalf = rectangleObject.getX() < verticalDividingLine && (rectangleObject.getX() + rectangleObject.getW() < verticalDividingLine);
        boolean fitsCompletelyInEastHalf = rectangleObject.getX() > verticalDividingLine;

        if(fitsCompletelyInEastHalf){
            if(fitsCompletelyInNorthHalf){
                index = QuadTree.NE_CHILD;
            }else if(fitsCompletelyInSouthHalf){
                index = QuadTree.SE_CHILD;
            }
        }else if(fitsCompletelyInWestHalf){
            if(fitsCompletelyInNorthHalf){
                index = QuadTree.NW_CHILD;
            }else if(fitsCompletelyInSouthHalf){
                index = QuadTree.SW_CHILD;
            }
        }
        return index;
    }

    public int getMaxObjects() {
        return maxObjects;
    }

    public void setMaxObjects(int maxObjects) {
        this.maxObjects = maxObjects;
    }

    public int getMaxLevels() {
        return maxLevels;
    }

    public void setMaxLevels(int maxLevels) {
        this.maxLevels = maxLevels;
    }

    public int getDefaultWidth() {
        return defaultWidth;
    }

    public void setDefaultWidth(int defaultWidth) {
        this.defaultWidth = defaultWidth;
    }

    public int getDefaultHeight() {
        return defaultHeight;
    }

    public void setDefaultHeight(int defaultHeight) {
        this.defaultHeight = defaultHeight;
    }

    public List<Rectangle> getRectangleObjects() {
        return rectangles;
    }

    public void setRectangleObjects(List<Rectangle> rectangleObjects) {
        this.rectangles = rectangleObjects;
    }
    
    public void addRectangleObject(Rectangle rectangleObject) {
    	if(this.rectangles == null){
    		this.rectangles = new ArrayList<>();
    	}
    	this.rectangles.add(rectangleObject);
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getW() {
        return w;
    }

    public void setW(Integer w) {
        this.w = w;
    }

    public Integer getH() {
        return h;
    }

    public void setH(Integer h) {
        this.h = h;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
    
    public QuadTree getParent(){
    	return this.parent;
    }
    
    public void setParent(QuadTree parent){
    	this.parent = parent;
    }

    public QuadTree[] getChildren() {
        return children;
    }

    public void setChildren(QuadTree[] children) {
        this.children = children;
    }
}
