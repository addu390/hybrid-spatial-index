package models.kd;

import models.Node;
import models.Point;

public class KDNode implements Node {

    public int[] point;
    public final static int DIMENSION = 2;

    public KDNode left;
    public KDNode right;

    public KDNode(int[] point) {
        this.point = new int[DIMENSION];
        System.arraycopy(point, 0, this.point, 0, point.length);
        left = null;
        right = null;
    }

    public KDNode(Point point) {
        this.point = new int[]{point.getX(), point.getY()};
        left = null;
        right = null;
    }

}
