package operations;

import models.KDNode;

import java.util.Objects;

public class KDTree {

    public static boolean search(KDNode root, int[] dataPoint) {
        return searchNode(root, dataPoint, 0);
    }

    public static KDNode insert(KDNode root, int[] dataPoint) {
        return insertNode(root, dataPoint, 0);
    }

    public static KDNode nearestNeighbour(KDNode root, int[] dataPoint) {
        return searchNearestNeighbour(root, dataPoint, Integer.MAX_VALUE, root);
    }

    private static boolean searchNode(KDNode root, int[] dataPoint, int depth) {
        if (Objects.isNull(root)) {
            return false;
        }
        if (isEqual(root.dataPoint, dataPoint)) {
            return true;
        }

        int currentDimension = depth % KDNode.DIMENSION;
        if (dataPoint[currentDimension] < root.dataPoint[currentDimension]) {
            return searchNode(root.left, dataPoint, depth + 1);
        } else {
            return searchNode(root.right, dataPoint, depth + 1);
        }
    }

    private static KDNode searchNearestNeighbour(KDNode root, int[] dataPoint, double minDist, KDNode bestNode) {
        if (root == null)
            return bestNode;

        double distanceFromNode = euclideanDistance(root.dataPoint, dataPoint);
        if (euclideanDistance(root.dataPoint, dataPoint) < minDist) {
            minDist = distanceFromNode;
            bestNode = root;
        }

        if (Objects.isNull(root.left)) {
            return searchNearestNeighbour(root.right, dataPoint, minDist, bestNode);
        }

        if (Objects.isNull(root.right)) {
            return searchNearestNeighbour(root.left, dataPoint, minDist, bestNode);
        }

        if (euclideanDistance(root.left.dataPoint, dataPoint) < euclideanDistance(root.right.dataPoint, dataPoint)) {
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

        if (dataPoint[currentDimension] < (root.dataPoint[currentDimension])) {
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
