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
import java.util.Collections;
import java.util.List;

import static util.Time.constructionTime;
import static util.Time.searchTime;

public class TimeIt {
    static int min = 0;
    static int max = 100000;

    public static void main(String[] args) throws IOException {
        chartOne();
    }
    
    private static void chartOne() throws IOException {
        final XYChart constructionChart = new XYChartBuilder().width(600).height(400)
                .title("Construct and Insert Time").xAxisTitle("Number of Points")
                .yAxisTitle("Time in Milli Seconds").build();

        constructionChart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        constructionChart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

        final XYChart searchChart = new XYChartBuilder().width(600).height(400)
                .title("Search Time").xAxisTitle("Number of Points")
                .yAxisTitle("Time in Milli Seconds").build();

        searchChart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        searchChart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

        double[] countList = new double[] {100.0, 1000.0, 5000.0, 10000.0, 15000.0, 20000.0,
                30000.0, 45000.0, 60000.0, 80000.0};

        List<Double> quadInserts = new ArrayList<>();
        List<Double> rInserts = new ArrayList<>();
        List<Double> kdTimes = new ArrayList<>();

        List<Double> quadSearches = new ArrayList<>();
        List<Double> rSearches = new ArrayList<>();
        List<Double> kdSearches = new ArrayList<>();

        for (Double count: countList) {
            List<Point> points = Generator.points(min, max, count.intValue(), 0);

            QuadTree quadTree = new QuadTree();
            RTree rTree = new RTree();
            KDTree kdTree = new KDTree();

            KDNode rootNode = new KDNode(points.get(0));

            quadInserts.add(constructionTime(quadTree, points)/1000000.0);
            rInserts.add(constructionTime(rTree, points)/1000000.0);
            kdTimes.add(constructionTime(kdTree, rootNode, points)/1000000.0);

            Collections.shuffle(points);

            quadSearches.add(searchTime(quadTree, points)/1000000.0);
            rSearches.add(searchTime(rTree, points)/1000000.0);
            kdSearches.add(searchTime(kdTree, rootNode, points)/1000000.0);
        }

        constructionChart.addSeries("Quad Tree", countList, quadInserts.stream().mapToDouble(Double::doubleValue).toArray());
        constructionChart.addSeries("R Tree", countList, rInserts.stream().mapToDouble(Double::doubleValue).toArray());
        constructionChart.addSeries("KD Tree", countList, kdTimes.stream().mapToDouble(Double::doubleValue).toArray());

        BitmapEncoder.saveBitmap(constructionChart, "images/results/tree-construction", BitmapEncoder.BitmapFormat.PNG);

        searchChart.addSeries("Quad Tree", countList, quadSearches.stream().mapToDouble(Double::doubleValue).toArray());
        searchChart.addSeries("R Tree", countList, rSearches.stream().mapToDouble(Double::doubleValue).toArray());
        searchChart.addSeries("KD Tree", countList, kdSearches.stream().mapToDouble(Double::doubleValue).toArray());

        BitmapEncoder.saveBitmap(searchChart, "images/results/tree-access", BitmapEncoder.BitmapFormat.PNG);
    }
}
