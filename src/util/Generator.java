package util;

import java.util.List;
import java.util.ArrayList;

public class Generator {

    public List<int[]> points(int minimum, int maximum, int count, int density) {
        int densityCount = (int) (count * 0.1 * density);
        List<int[]> pointsList = new ArrayList<>();

        count = count - densityCount;
        double denseArea = 0.1;
        double paddingX = Math.random() * (maximum - minimum)/2;
        double paddingY = Math.random() * (maximum - minimum)/2;

        for (int i = 0; i < count; i++) {
            int[] point = new int[2];
            point[0] = (int)(minimum + Math.random() * (maximum - minimum));
            point[1] = (int)(minimum + Math.random() * (maximum - minimum));
            pointsList.add(point);
        }

        for (int j = 0; j < densityCount; j++) {
            int[] point = new int[2];
            point[0] = (int)(minimum + Math.random() * (maximum - minimum) * denseArea + paddingX);
            point[1] = (int)(minimum + Math.random() * (maximum - minimum) * denseArea + paddingY);
            pointsList.add(point);
        }

        return pointsList;
    }

    public int[] point(int minimum, int maximum) {
        int[] point = new int[2];

        point[0] = (int)(minimum + Math.random() * (maximum - minimum));
        point[1] = (int)(minimum + Math.random() * (maximum - minimum));
        return point;
    }
}