package models;

import models.quad.Rectangle;
import operations.index.KDTree;

import java.util.LinkedList;

public class RNode {
    private KDTree tree;
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
