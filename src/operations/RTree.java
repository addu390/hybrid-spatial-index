package operations;

import models.RNode;

import java.util.LinkedList;
import java.util.List;

public class RTree<T> {

    private final int maxEntries;
    private final int minEntries;
    private final int numDims;
    private RNode root;

    public RTree(int maxEntries, int minEntries, int numDims) {
        assert (minEntries <= (maxEntries / 2));
        this.numDims = numDims;
        this.maxEntries = maxEntries;
        this.minEntries = minEntries;
        root = buildRoot(true);
    }

    public RTree() {
        this(50, 2, 2);
    }

    public List<T> search(double[] coords, double[] dimensions) {
        assert (coords.length == numDims);
        assert (dimensions.length == numDims);
        LinkedList<T> results = new LinkedList<T>();
        search(coords, dimensions, root, results);
        return results;
    }

    public void insert(double[] coords, double[] dimensions, T entry) {
        assert (coords.length == numDims);
        assert (dimensions.length == numDims);
        Entry e = new Entry(coords, dimensions, entry);
        RNode l = chooseLeaf(root, e);
        l.children.add(e);
        e.parent = l;
        if (l.children.size() > maxEntries) {
            RNode[] splits = splitNode(l);
            adjustTree(splits[0], splits[1]);
        } else {
            adjustTree(l, null);
        }
    }

    private void search(double[] coords, double[] dimensions, RNode n, LinkedList<T> results) {
        if (n.leaf) {
            for (RNode e : n.children) {
                if (isOverlap(coords, dimensions, e.coordinates, e.dimensions)) {
                    results.add(((Entry) e).entry);
                }
            }
        } else {
            for (RNode c : n.children) {
                if (isOverlap(coords, dimensions, c.coordinates, c.dimensions)) {
                    search(coords, dimensions, c, results);
                }
            }
        }
    }

    private void adjustTree(RNode n, RNode nn) {
        if (n == root) {
            if (nn != null) {
                root = buildRoot(false);
                root.children.add(n);
                n.parent = root;
                root.children.add(nn);
                nn.parent = root;
            }
            tighten(root);
            return;
        }
        tighten(n);
        if (nn != null) {
            tighten(nn);
            if (n.parent.children.size() > maxEntries) {
                RNode[] splits = splitNode(n.parent);
                adjustTree(splits[0], splits[1]);
            }
        } else if (n.parent != null) {
            adjustTree(n.parent, null);
        }
    }

    private RNode[] splitNode(RNode n) {
        @SuppressWarnings("unchecked")
        RNode[] nn = new RNode[]{n, new RNode(n.coordinates, n.dimensions, n.leaf)};
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
            if ((nn[0].children.size() >= minEntries) &&
                    (nn[1].children.size() + cc.size() == minEntries)) {
                nn[1].children.addAll(cc);
                cc.clear();
                return nn;
            } else if ((nn[1].children.size() >= minEntries) &&
                    (nn[1].children.size() + cc.size() == minEntries)) {
                nn[0].children.addAll(cc);
                cc.clear();
                return nn;
            }
            RNode c = cc.pop();
            RNode preferred;
            double e0 = getRequiredExpansion(nn[0].coordinates, nn[0].dimensions, c);
            double e1 = getRequiredExpansion(nn[1].coordinates, nn[1].dimensions, c);
            if (e0 < e1) {
                preferred = nn[0];
            } else if (e0 > e1) {
                preferred = nn[1];
            } else {
                double a0 = getArea(nn[0].dimensions);
                double a1 = getArea(nn[1].dimensions);
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
        for (int i = 0; i < numDims; i++) {
            double dimLb = Double.MAX_VALUE, dimMinUb = Double.MAX_VALUE;
            double dimUb = -1.0f * Double.MAX_VALUE, dimMaxLb = -1.0f * Double.MAX_VALUE;
            RNode nMaxLb = null, nMinUb = null;
            for (RNode n : nn) {
                if (n.coordinates[i] < dimLb) {
                    dimLb = n.coordinates[i];
                }
                if (n.dimensions[i] + n.coordinates[i] > dimUb) {
                    dimUb = n.dimensions[i] + n.coordinates[i];
                }
                if (n.coordinates[i] > dimMaxLb) {
                    dimMaxLb = n.coordinates[i];
                    nMaxLb = n;
                }
                if (n.dimensions[i] + n.coordinates[i] < dimMinUb) {
                    dimMinUb = n.dimensions[i] + n.coordinates[i];
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
        double[] minCoords = new double[n.coordinates.length];
        double[] maxDimensions = new double[n.dimensions.length];
        for (int i = 0; i < minCoords.length; i++) {
            minCoords[i] = Double.MAX_VALUE;
            maxDimensions[i] = 0.0f;

            for (RNode c : n.children) {
                c.parent = n;
                if (c.coordinates[i] < minCoords[i]) {
                    minCoords[i] = c.coordinates[i];
                }
                if ((c.coordinates[i] + c.dimensions[i]) > maxDimensions[i]) {
                    maxDimensions[i] = (c.coordinates[i] + c.dimensions[i]);
                }
            }
        }
        System.arraycopy(minCoords, 0, n.coordinates, 0, minCoords.length);
        System.arraycopy(maxDimensions, 0, n.dimensions, 0, maxDimensions.length);
    }

    private RNode chooseLeaf(RNode n, RTree<T>.Entry e) {
        if (n.leaf) {
            return n;
        }
        double minInc = Double.MAX_VALUE;
        RNode next = null;
        for (RNode c : n.children) {
            double inc = getRequiredExpansion(c.coordinates, c.dimensions, e);
            if (inc < minInc) {
                minInc = inc;
                next = c;
            } else if (inc == minInc) {
                double curArea = 1.0f;
                double thisArea = 1.0f;
                for (int i = 0; i < c.dimensions.length; i++) {
                    curArea *= next.dimensions[i];
                    thisArea *= c.dimensions[i];
                }
                if (thisArea < curArea) {
                    next = c;
                }
            }
        }
        return chooseLeaf(next, e);
    }

    private double getRequiredExpansion(double[] coords, double[] dimensions, RNode e) {
        double area = getArea(dimensions);
        double[] deltas = new double[dimensions.length];
        for (int i = 0; i < deltas.length; i++) {
            if (coords[i] + dimensions[i] < e.coordinates[i] + e.dimensions[i]) {
                deltas[i] = e.coordinates[i] + e.dimensions[i] - coords[i] - dimensions[i];
            } else if (coords[i] + dimensions[i] > e.coordinates[i] + e.dimensions[i]) {
                deltas[i] = coords[i] - e.coordinates[i];
            }
        }
        double expanded = 1.0f;
        for (int i = 0; i < dimensions.length; i++) {
            area *= dimensions[i] + deltas[i];
        }
        return (expanded - area);
    }

    private double getArea(double[] dimensions) {
        double area = 1.0f;
        for (int i = 0; i < dimensions.length; i++) {
            area *= dimensions[i];
        }
        return area;
    }

    private boolean isOverlap(double[] scoords, double[] sdimensions, double[] coords, double[] dimensions) {
        for (int i = 0; i < scoords.length; i++) {
            boolean overlapInThisDimension = false;
            if (scoords[i] == coords[i]) {
                overlapInThisDimension = true;
            } else if (scoords[i] < coords[i]) {
                if (scoords[i] + sdimensions[i] >= coords[i]) {
                    overlapInThisDimension = true;
                }
            } else if (scoords[i] > coords[i]) {
                if (coords[i] + dimensions[i] >= scoords[i]) {
                    overlapInThisDimension = true;
                }
            }
            if (!overlapInThisDimension) {
                return false;
            }
        }
        return true;
    }

    private RNode buildRoot(boolean asLeaf) {
        double[] initCoords = new double[numDims];
        double[] initDimensions = new double[numDims];
        for (int i = 0; i < this.numDims; i++) {
            initCoords[i] = Math.sqrt(Double.MAX_VALUE);
            initDimensions[i] = -2.0f * Math.sqrt(Double.MAX_VALUE);
        }
        return new RNode(initCoords, initDimensions, asLeaf);
    }

    private class Entry extends RNode {
        final T entry;

        public Entry(double[] coords, double[] dimensions, T entry) {
            super(coords, dimensions, true);
            this.entry = entry;
        }
    }
}
