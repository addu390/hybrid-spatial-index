package operations.index;

import models.kd.KDNode;
import models.Point;
import operations.Tree;

import java.util.Objects;

public class KDTree implements Tree {

    public boolean search(KDNode root, Point point) {
        return searchNode(root, new int[]{point.getX(), point.getY()}, 0);
    }

    public KDNode insert(KDNode root, Point point) {
        return insertNode(root, new int[]{point.getX(), point.getY()}, 0);
    }

    public KDNode nearestNeighbour(KDNode root, Point point) {
        return searchNearestNeighbour(root, new int[]{point.getX(), point.getY()}, Integer.MAX_VALUE, root);
    }

    private static boolean searchNode(KDNode root, int[] point, int depth) {
        if (Objects.isNull(root)) {
            return false;
        }
        if (isEqual(root.point, point)) {
            return true;
        }

        int currentDimension = depth % KDNode.DIMENSION;
        if (point[currentDimension] < root.point[currentDimension]) {
            return searchNode(root.left, point, depth + 1);
        } else {
            return searchNode(root.right, point, depth + 1);
        }
    }

    private static KDNode searchNearestNeighbour(KDNode root, int[] dataPoint, double minDist, KDNode bestNode) {
        if (root == null)
            return bestNode;

        double distanceFromNode = euclideanDistance(root.point, dataPoint);
        if (euclideanDistance(root.point, dataPoint) < minDist) {
            minDist = distanceFromNode;
            bestNode = root;
        }

        if (Objects.isNull(root.left)) {
            return searchNearestNeighbour(root.right, dataPoint, minDist, bestNode);
        }

        if (Objects.isNull(root.right)) {
            return searchNearestNeighbour(root.left, dataPoint, minDist, bestNode);
        }

        if (euclideanDistance(root.left.point, dataPoint) < euclideanDistance(root.right.point, dataPoint)) {
            bestNode = searchNearestNeighbour(root.left, dataPoint, minDist, bestNode);
        } else {
            bestNode = searchNearestNeighbour(root.right, dataPoint, minDist, bestNode);
        }

        return bestNode;
    }

    private static KDNode insertNode(KDNode root, int[] dataPoint, int depth) {
        if (root == null) {
            return new KDNode(dataPoint);
        }

        int currentDimension = depth % KDNode.DIMENSION;

        if (dataPoint[currentDimension] < (root.point[currentDimension])) {
            root.left = insertNode(root.left, dataPoint, depth + 1);
        } else {
            root.right = insertNode(root.right, dataPoint, depth + 1);
        }
        return root;
    }

    private static double euclideanDistance(int[] a, int[] b) {
        if (a == null || b == null) {
            return Integer.MAX_VALUE;
        }
        return Math.sqrt((b[1] - a[1]) * (b[1] - a[1]) + (b[0] - a[0]) * (b[0] - a[0]));
    }

    private static boolean isEqual(int[] point1, int[] point2) {
        for (int i = 0; i < KDNode.DIMENSION; ++i) {
            if (point1[i] != point2[i]) {
                return false;
            }
        }
        return true;
    }
}
