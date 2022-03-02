package models;

import java.util.LinkedList;

public class RNode {
    public final double[] coordinates;
    public final double[] dimensions;
    public final LinkedList<RNode> children;
    public final boolean leaf;

    public RNode parent;

    public RNode(double[] coordinates, double[] dimensions, boolean leaf) {
        this.coordinates = new double[coordinates.length];
        this.dimensions = new double[dimensions.length];
        System.arraycopy(coordinates, 0, this.coordinates, 0, coordinates.length);
        System.arraycopy(dimensions, 0, this.dimensions, 0, dimensions.length);
        this.leaf = leaf;
        children = new LinkedList<RNode>();
    }
}
