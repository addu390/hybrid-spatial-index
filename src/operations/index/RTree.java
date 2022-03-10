package operations.index;

import models.RNode;
import models.quad.BaseRectangle;
import models.quad.Rectangle;
import operations.SpaceTree;
import util.Geometry;

import java.util.LinkedList;
import java.util.List;

import static util.Geometry.getArea;

public class RTree implements SpaceTree {

    private final int maxObjects;
    private final int minObjects;
    private RNode parent;

    public RTree(int maxObjects, int minObjects) {
        this.maxObjects = maxObjects;
        this.minObjects = minObjects;
        parent = build(true);
    }

    public RTree() {
        this(50, 2);
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

    private void search(Rectangle rectangle, RNode rNode, LinkedList<Rectangle> results) {
        if (rNode.leaf) {
            for (RNode e : rNode.children) {
                if (Geometry.isOverlap(rectangle, e.rectangle)) {
                    results.add(e.rectangle);
                }
            }
        } else {
            for (RNode c : rNode.children) {
                if (Geometry.isOverlap(rectangle, c.rectangle)) {
                    search(rectangle, c, results);
                }
            }
        }
    }

    private void adjustTree(RNode rNode, RNode nNode) {
        if (rNode == parent) {
            if (nNode != null) {
                parent = build(false);
                parent.children.add(rNode);
                rNode.parent = parent;
                parent.children.add(nNode);
                nNode.parent = parent;
            }
            tighten(parent);
            return;
        }
        tighten(rNode);
        if (nNode != null) {
            tighten(nNode);
            if (rNode.parent.children.size() > maxObjects) {
                RNode[] splits = splitNode(rNode.parent);
                adjustTree(splits[0], splits[1]);
            }
        } else if (rNode.parent != null) {
            adjustTree(rNode.parent, null);
        }
    }

    private RNode[] splitNode(RNode rNode) {
        RNode[] rNodes = new RNode[]{rNode, new RNode(rNode.rectangle, rNode.leaf)};
        rNodes[1].parent = rNode.parent;
        if (rNodes[1].parent != null) {
            rNodes[1].parent.children.add(rNodes[1]);
        }

        LinkedList<RNode> cc = new LinkedList<>(rNode.children);
        rNode.children.clear();
        RNode[] ss = pickSeeds(cc);
        rNodes[0].children.add(ss[0]);
        rNodes[1].children.add(ss[1]);

        while (!cc.isEmpty()) {
            if ((rNodes[0].children.size() >= minObjects) &&
                    (rNodes[1].children.size() + cc.size() == minObjects)) {
                rNodes[1].children.addAll(cc);
                cc.clear();
                return rNodes;
            } else if ((rNodes[1].children.size() >= minObjects) &&
                    (rNodes[1].children.size() + cc.size() == minObjects)) {
                rNodes[0].children.addAll(cc);
                cc.clear();
                return rNodes;
            }
            RNode c = cc.pop();
            RNode preferred;

            double e0 = getExpansion(rNodes[0].rectangle, c);
            double e1 = getExpansion(rNodes[1].rectangle, c);
            if (e0 < e1) {
                preferred = rNodes[0];
            } else if (e0 > e1) {
                preferred = rNodes[1];
            } else {
                double a0 = getArea(rNodes[0].rectangle);
                double a1 = getArea(rNodes[1].rectangle);
                if (a0 < a1) {
                    preferred = rNodes[0];
                } else if (e0 > a1) {
                    preferred = rNodes[1];
                } else {
                    if (rNodes[0].children.size() < rNodes[1].children.size()) {
                        preferred = rNodes[0];
                    } else if (rNodes[0].children.size() > rNodes[1].children.size()) {
                        preferred = rNodes[1];
                    } else {
                        preferred = rNodes[(int) Math.round(Math.random())];
                    }
                }
            }
            preferred.children.add(c);
        }
        tighten(rNodes[0]);
        tighten(rNodes[1]);
        return rNodes;
    }

    private RNode[] pickSeeds(LinkedList<RNode> rNodes) {
        RNode[] bestPair = null;
        double bestSep = 0.0f;
        for (int i = 0; i < 2; i++) {
            double dimLb = Double.MAX_VALUE, dimMinUb = Double.MAX_VALUE;
            double dimUb = -1.0f * Double.MAX_VALUE, dimMaxLb = -1.0f * Double.MAX_VALUE;
            RNode nMaxLb = null, nMinUb = null;
            for (RNode n : rNodes) {
                double[] nCoordinates = new double[]{n.rectangle.getX(), n.rectangle.getY()};
                double[] nDimensions = new double[]{n.rectangle.getW(), n.rectangle.getH()};
                if (nCoordinates[i] < dimLb) {
                    dimLb = nCoordinates[i];
                }
                if (nDimensions[i] + nCoordinates[i] > dimUb) {
                    dimUb = nDimensions[i] + nCoordinates[i];
                }
                if (nCoordinates[i] > dimMaxLb) {
                    dimMaxLb = nCoordinates[i];
                    nMaxLb = n;
                }
                if (nDimensions[i] + nCoordinates[i] < dimMinUb) {
                    dimMinUb = nDimensions[i] + nCoordinates[i];
                    nMinUb = n;
                }
            }
            double sep = Math.abs((dimMinUb - dimMaxLb) / (dimUb - dimLb));
            if (sep >= bestSep) {
                bestPair = new RNode[]{nMaxLb, nMinUb};
                bestSep = sep;
            }
        }
        rNodes.remove(bestPair[0]);
        rNodes.remove(bestPair[1]);
        return bestPair;
    }

    private void tighten(RNode rNode) {
        double[] minCoordinates = new double[2];
        double[] maxDimensions = new double[2];
        for (int i = 0; i < minCoordinates.length; i++) {
            minCoordinates[i] = Double.MAX_VALUE;
            maxDimensions[i] = 0.0f;

            for (RNode c : rNode.children) {
                c.parent = rNode;
                double[] cCoordinates = new double[]{c.rectangle.getX(), c.rectangle.getY()};
                double[] cDimensions = new double[]{c.rectangle.getW(), c.rectangle.getH()};
                if (cCoordinates[i] < minCoordinates[i]) {
                    minCoordinates[i] = cCoordinates[i];
                }
                if ((cCoordinates[i] + cDimensions[i]) > maxDimensions[i]) {
                    maxDimensions[i] = (cCoordinates[i] + cDimensions[i]);
                }
            }
        }
        rNode.rectangle.setX(minCoordinates[0]);
        rNode.rectangle.setY(minCoordinates[1]);
        rNode.rectangle.setW(maxDimensions[0]);
        rNode.rectangle.setH(maxDimensions[1]);
    }

    private RNode chooseLeaf(RNode rNode, RNode nNode) {
        if (rNode.leaf) {
            return rNode;
        }
        double minInc = Double.MAX_VALUE;
        RNode next = null;
        for (RNode c : rNode.children) {
            double[] cDimensions = new double[]{c.rectangle.getW(), c.rectangle.getH()};
            double inc = getExpansion(c.rectangle, nNode);
            if (inc < minInc) {
                minInc = inc;
                next = c;
            } else if (inc == minInc) {
                double curArea = 1.0f;
                double thisArea = 1.0f;
                double[] nextDimensions = new double[]{next.rectangle.getW(), next.rectangle.getH()};
                for (int i = 0; i < cDimensions.length; i++) {
                    curArea *= nextDimensions[i];
                    thisArea *= cDimensions[i];
                }
                if (thisArea < curArea) {
                    next = c;
                }
            }
        }
        return chooseLeaf(next, nNode);
    }

    private double getExpansion(Rectangle rectangle, RNode rNode) {
        double[] coordinates = new double[]{rectangle.getX(), rectangle.getY()};
        double[] dimensions = new double[]{rectangle.getW(), rectangle.getH()};

        double[] eCoordinates = new double[]{rNode.rectangle.getX(), rNode.rectangle.getY()};
        double[] eDimensions = new double[]{rNode.rectangle.getW(), rNode.rectangle.getH()};

        double area = getArea(rectangle);
        double[] deltas = new double[dimensions.length];
        for (int i = 0; i < deltas.length; i++) {
            if (coordinates[i] + dimensions[i] < eCoordinates[i] + eDimensions[i]) {
                deltas[i] = eCoordinates[i] + eDimensions[i] - coordinates[i] - dimensions[i];
            } else if (coordinates[i] + dimensions[i] > eCoordinates[i] + eDimensions[i]) {
                deltas[i] = coordinates[i] - eCoordinates[i];
            }
        }
        double expanded = 1.0f;
        for (int i = 0; i < dimensions.length; i++) {
            area *= dimensions[i] + deltas[i];
        }
        return (expanded - area);
    }

    private RNode build(boolean isLeaf) {
        double coordinate = Math.sqrt(Double.MAX_VALUE);
        double dimension = -2.0f * Math.sqrt(Double.MAX_VALUE);
        Rectangle rectangle = new BaseRectangle(coordinate, coordinate, dimension, dimension);
        return new RNode(rectangle, isLeaf);
    }
}
