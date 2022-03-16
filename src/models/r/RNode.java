package models.r;

import models.Node;
import models.Rectangle;

import java.util.LinkedList;

public class RNode implements Node {
    public final Rectangle rectangle;
    public final LinkedList<RNode> children;
    public final boolean leaf;

    public RNode parent;

    public RNode(Rectangle rectangle, boolean leaf) {
        this.rectangle = rectangle;
        this.leaf = leaf;
        children = new LinkedList<RNode>();
    }
}
