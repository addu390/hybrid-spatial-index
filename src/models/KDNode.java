package models;

public class KDNode {

	public int[] dataPoint;
	public final static int DIMENSION = 2;
	
	public KDNode left;
	public KDNode right;

	public KDNode(int[] point) {
		dataPoint = new int[DIMENSION];
		System.arraycopy(point, 0, dataPoint, 0, point.length);
		left = null;
		right = null;
	}
	
}
