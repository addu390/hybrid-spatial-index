package driver;

import models.Point;
import models.kd.KDNode;
import operations.index.*;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import util.Generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static util.Time.constructionTime;

public class TimeIt {
    static int min = 0;
    static int max = 100000;

    public static void main(String[] args) throws IOException {
        chartOne();
    }
    
    private static void chartOne() throws IOException {
        final XYChart chart = new XYChartBuilder().width(600).height(400)
                .title("Construct and Insert Time").xAxisTitle("Number of Points")
                .yAxisTitle("Time in Milli Seconds").build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

        double[] countList = new double[] {100.0, 1000.0, 5000.0, 10000.0, 15000.0, 20000.0,
                30000.0, 45000.0, 60000.0, 80000.0};

        List<Double> quadTimes = new ArrayList<>();
        List<Double> rTimes = new ArrayList<>();
        List<Double> kdTimes = new ArrayList<>();

        for (Double count: countList) {
            List<Point> points = Generator.points(min, max, count.intValue(), 0);

            QuadTree quadTree = new QuadTree();
            RTree rTree = new RTree();
            KDTree kdTree = new KDTree();

            quadTimes.add(constructionTime(quadTree, points));
            rTimes.add(constructionTime(rTree, points));
            kdTimes.add(constructionTime(kdTree, new KDNode(points.get(0)), points));
        }

        chart.addSeries("Quad Tree", countList, quadTimes.stream().mapToDouble(Double::doubleValue).toArray());
        chart.addSeries("R Tree", countList, rTimes.stream().mapToDouble(Double::doubleValue).toArray());
        chart.addSeries("KD Tree", countList, kdTimes.stream().mapToDouble(Double::doubleValue).toArray());

        BitmapEncoder.saveBitmap(chart, "images/results/tree-construction", BitmapEncoder.BitmapFormat.PNG);
    }
}
