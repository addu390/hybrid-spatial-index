package operations;

import models.RNode;
import models.quad.BaseRectangle;
import models.quad.Rectangle;
import util.Geometry;

import java.util.LinkedList;
import java.util.List;

public class RTree {

    private final int maxObjects;
    private final int minObjects;
    private final int dimension;
    private RNode parent;

    public RTree(int maxObjects, int minObjects, int dimension) {
        this.dimension = dimension;
        this.maxObjects = maxObjects;
        this.minObjects = minObjects;
        parent = buildRoot(true);
    }

    public RTree() {
        this(50, 2, 2);
    }

    public List<Rectangle> search(Rectangle rectangle) {
        LinkedList<Rectangle> results = new LinkedList<>();
        search(rectangle, parent, results);
        return results;
    }

    public void insert(Rectangle rectangle) {
        RNode e = new RNode(rectangle, true);
        RNode l = chooseLeaf(parent, e);
        l.children.add(e);
        e.parent = l;
        if (l.children.size() > maxObjects) {
            RNode[] splits = splitNode(l);
            adjustTree(splits[0], splits[1]);
        } else {
            adjustTree(l, null);
        }
    }

    private void search(Rectangle rectangle, RNode n, LinkedList<Rectangle> results) {
        if (n.leaf) {
            for (RNode e : n.children) {
                if (Geometry.rectangleObjectsOverlap(rectangle, e.rectangle)) {
                    results.add(e.rectangle);
                }
            }
        } else {
            for (RNode c : n.children) {
                if (Geometry.rectangleObjectsOverlap(rectangle, c.rectangle)) {
                    search(rectangle, c, results);
                }
            }
        }
    }

    private void adjustTree(RNode n, RNode nn) {
        if (n == parent) {
            if (nn != null) {
                parent = buildRoot(false);
                parent.children.add(n);
                n.parent = parent;
                parent.children.add(nn);
                nn.parent = parent;
            }
            tighten(parent);
            return;
        }
        tighten(n);
        if (nn != null) {
            tighten(nn);
            if (n.parent.children.size() > maxObjects) {
                RNode[] splits = splitNode(n.parent);
                adjustTree(splits[0], splits[1]);
            }
        } else if (n.parent != null) {
            adjustTree(n.parent, null);
        }
    }

    private RNode[] splitNode(RNode n) {
        RNode[] nn = new RNode[]{n, new RNode(n.rectangle, n.leaf)};
        nn[1].parent = n.parent;
        if (nn[1].parent != null) {
            nn[1].parent.children.add(nn[1]);
        }
        LinkedList<RNode> cc = new LinkedList<RNode>(n.children);
        n.children.clear();
        RNode[] ss = pickSeeds(cc);
        nn[0].children.add(ss[0]);
        nn[1].children.add(ss[1]);
        while (!cc.isEmpty()) {
            if ((nn[0].children.size() >= minObjects) &&
                    (nn[1].children.size() + cc.size() == minObjects)) {
                nn[1].children.addAll(cc);
                cc.clear();
                return nn;
            } else if ((nn[1].children.size() >= minObjects) &&
                    (nn[1].children.size() + cc.size() == minObjects)) {
                nn[0].children.addAll(cc);
                cc.clear();
                return nn;
            }
            RNode c = cc.pop();
            RNode preferred;
            double e0 = getRequiredExpansion(nn[0].rectangle, c);
            double e1 = getRequiredExpansion(nn[1].rectangle, c);
            if (e0 < e1) {
                preferred = nn[0];
            } else if (e0 > e1) {
                preferred = nn[1];
            } else {
                double a0 = getArea(nn[0].rectangle);
                double a1 = getArea(nn[1].rectangle);
                if (a0 < a1) {
                    preferred = nn[0];
                } else if (e0 > a1) {
                    preferred = nn[1];
                } else {
                    if (nn[0].children.size() < nn[1].children.size()) {
                        preferred = nn[0];
                    } else if (nn[0].children.size() > nn[1].children.size()) {
                        preferred = nn[1];
                    } else {
                        preferred = nn[(int) Math.round(Math.random())];
                    }
                }
            }
            preferred.children.add(c);
        }
        tighten(nn[0]);
        tighten(nn[1]);
        return nn;
    }

    private RNode[] pickSeeds(LinkedList<RNode> nn) {
        RNode[] bestPair = null;
        double bestSep = 0.0f;
        for (int i = 0; i < dimension; i++) {
            double dimLb = Double.MAX_VALUE, dimMinUb = Double.MAX_VALUE;
            double dimUb = -1.0f * Double.MAX_VALUE, dimMaxLb = -1.0f * Double.MAX_VALUE;
            RNode nMaxLb = null, nMinUb = null;
            for (RNode n : nn) {
                double[] n_coordinates = new double[]{n.rectangle.getX(), n.rectangle.getY()};
                double[] n_dimensions = new double[]{n.rectangle.getW(), n.rectangle.getH()};
                if (n_coordinates[i] < dimLb) {
                    dimLb = n_coordinates[i];
                }
                if (n_dimensions[i] + n_coordinates[i] > dimUb) {
                    dimUb = n_dimensions[i] + n_coordinates[i];
                }
                if (n_coordinates[i] > dimMaxLb) {
                    dimMaxLb = n_coordinates[i];
                    nMaxLb = n;
                }
                if (n_dimensions[i] + n_coordinates[i] < dimMinUb) {
                    dimMinUb = n_dimensions[i] + n_coordinates[i];
                    nMinUb = n;
                }
            }
            double sep = Math.abs((dimMinUb - dimMaxLb) / (dimUb - dimLb));
            if (sep >= bestSep) {
                bestPair = new RNode[]{nMaxLb, nMinUb};
                bestSep = sep;
            }
        }
        nn.remove(bestPair[0]);
        nn.remove(bestPair[1]);
        return bestPair;
    }

    private void tighten(RNode n) {
        double[] minCoordinates = new double[2];
        double[] maxDimensions = new double[2];
        for (int i = 0; i < minCoordinates.length; i++) {
            minCoordinates[i] = Double.MAX_VALUE;
            maxDimensions[i] = 0.0f;

            for (RNode c : n.children) {
                c.parent = n;
                double[] c_coordinates = new double[]{c.rectangle.getX(), c.rectangle.getY()};
                double[] c_dimensions = new double[]{c.rectangle.getW(), c.rectangle.getH()};
                if (c_coordinates[i] < minCoordinates[i]) {
                    minCoordinates[i] = c_coordinates[i];
                }
                if ((c_coordinates[i] + c_dimensions[i]) > maxDimensions[i]) {
                    maxDimensions[i] = (c_coordinates[i] + c_dimensions[i]);
                }
            }
        }
        n.rectangle.setX(minCoordinates[0]);
        n.rectangle.setY(minCoordinates[1]);
        n.rectangle.setW(maxDimensions[0]);
        n.rectangle.setH(maxDimensions[1]);
    }

    private RNode chooseLeaf(RNode n, RNode e) {
        if (n.leaf) {
            return n;
        }
        double minInc = Double.MAX_VALUE;
        RNode next = null;
        for (RNode c : n.children) {
            double[] c_dimensions = new double[]{c.rectangle.getW(), c.rectangle.getH()};
            double inc = getRequiredExpansion(c.rectangle, e);
            if (inc < minInc) {
                minInc = inc;
                next = c;
            } else if (inc == minInc) {
                double curArea = 1.0f;
                double thisArea = 1.0f;
                double[] next_dimensions = new double[]{next.rectangle.getW(), next.rectangle.getH()};
                for (int i = 0; i < c_dimensions.length; i++) {
                    curArea *= next_dimensions[i];
                    thisArea *= c_dimensions[i];
                }
                if (thisArea < curArea) {
                    next = c;
                }
            }
        }
        return chooseLeaf(next, e);
    }

    private double getRequiredExpansion(Rectangle rectangle, RNode e) {
        double[] coordinates = new double[]{rectangle.getX(), rectangle.getY()};
        double[] dimensions = new double[]{rectangle.getW(), rectangle.getH()};

        double[] e_coordinates = new double[]{e.rectangle.getX(), e.rectangle.getY()};
        double[] e_dimensions = new double[]{e.rectangle.getW(), e.rectangle.getH()};

        double area = getArea(rectangle);
        double[] deltas = new double[dimensions.length];
        for (int i = 0; i < deltas.length; i++) {
            if (coordinates[i] + dimensions[i] < e_coordinates[i] + e_dimensions[i]) {
                deltas[i] = e_coordinates[i] + e_dimensions[i] - coordinates[i] - dimensions[i];
            } else if (coordinates[i] + dimensions[i] > e_coordinates[i] + e_dimensions[i]) {
                deltas[i] = coordinates[i] - e_coordinates[i];
            }
        }
        double expanded = 1.0f;
        for (int i = 0; i < dimensions.length; i++) {
            area *= dimensions[i] + deltas[i];
        }
        return (expanded - area);
    }

    private double getArea(Rectangle rectangle) {
        double area = 1.0f;
        area *= (rectangle.getW() * rectangle.getY());
        return area;
    }

    private RNode buildRoot(boolean asLeaf) {
        double coordinate = Math.sqrt(Double.MAX_VALUE);
        double dimension = -2.0f * Math.sqrt(Double.MAX_VALUE);
        Rectangle rectangle = new BaseRectangle(coordinate, coordinate, dimension, dimension);
        return new RNode(rectangle, asLeaf);
    }
}
