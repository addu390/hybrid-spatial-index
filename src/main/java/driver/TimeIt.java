package driver;

import models.Point;
import models.Rectangle;
import models.kd.KDNode;
import models.quad.BaseRectangle;
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
    static Rectangle boundary = new BaseRectangle(0.0, 0.0, 100000.0, 100000.0);

    public static void main(String[] args) throws IOException {
        chartOne();
        chartTwo();
        chartThree();
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

        double[] countList = new double[] {100, 1000.0, 2500.0, 3500.0, 5000.0, 7500.0, 10000.0, 12500.0, 15000.0, 17500.0, 20000.0};

        List<Double> quadInserts = new ArrayList<>();
        List<Double> rInserts = new ArrayList<>();
        List<Double> kdTimes = new ArrayList<>();
        List<Double> quadKdInserts = new ArrayList<>();
        List<Double> rKdInserts = new ArrayList<>();

        List<Double> quadSearches = new ArrayList<>();
        List<Double> rSearches = new ArrayList<>();
        List<Double> kdSearches = new ArrayList<>();
        List<Double> quadKdSearches = new ArrayList<>();
        List<Double> rKdSearches = new ArrayList<>();

        for (Double count: countList) {
            List<Point> points = Generator.points(min, max, count.intValue(), 0);

            QuadTree quadTree = new QuadTree();
            RTree rTree = new RTree();
            KDTree kdTree = new KDTree();

            KDNode rootNode = new KDNode(points.get(0));

            QuadKDTree quadKDTree = new QuadKDTree();
            QuadTree hQuadTree = new QuadTree();

            RKDTree rkdTree = new RKDTree();
            RTree hRTree = new RTree();

            quadInserts.add(constructionTime(quadTree, points)/1000000.0);
            rInserts.add(constructionTime(rTree, points)/1000000.0);
            kdTimes.add(constructionTime(kdTree, rootNode, points)/1000000.0);
            quadKdInserts.add(constructionTime(quadKDTree, hQuadTree, new ArrayList<>(), points, boundary)/1000000.0);
            rKdInserts.add(constructionTime(rkdTree, hRTree, new ArrayList<>(), points, boundary)/1000000.0);

            Collections.shuffle(points);

            quadSearches.add(searchTime(quadTree, points)/1000000.0);
            rSearches.add(searchTime(rTree, points)/1000000.0);
            kdSearches.add(searchTime(kdTree, rootNode, points)/1000000.0);
            quadKdSearches.add(searchTime(quadKDTree, hQuadTree, points, boundary)/1000000.0);
            rKdSearches.add(searchTime(rkdTree, hRTree, points, boundary)/1000000.0);
        }

        constructionChart.addSeries("Quad Tree", countList, quadInserts.stream().mapToDouble(Double::doubleValue).toArray());
        constructionChart.addSeries("R Tree", countList, rInserts.stream().mapToDouble(Double::doubleValue).toArray());
        constructionChart.addSeries("KD Tree", countList, kdTimes.stream().mapToDouble(Double::doubleValue).toArray());
        constructionChart.addSeries("Quad KD Tree", countList, quadKdInserts.stream().mapToDouble(Double::doubleValue).toArray());
        constructionChart.addSeries("R KD Tree", countList, rKdInserts.stream().mapToDouble(Double::doubleValue).toArray());

        BitmapEncoder.saveBitmap(constructionChart, "images/results/tree-construction-5-20000", BitmapEncoder.BitmapFormat.PNG);

        searchChart.addSeries("Quad Tree", countList, quadSearches.stream().mapToDouble(Double::doubleValue).toArray());
        searchChart.addSeries("R Tree", countList, rSearches.stream().mapToDouble(Double::doubleValue).toArray());
        searchChart.addSeries("KD Tree", countList, kdSearches.stream().mapToDouble(Double::doubleValue).toArray());
        searchChart.addSeries("Quad KD Tree", countList, quadKdSearches.stream().mapToDouble(Double::doubleValue).toArray());
        searchChart.addSeries("R KD Tree", countList, rKdSearches.stream().mapToDouble(Double::doubleValue).toArray());

        BitmapEncoder.saveBitmap(searchChart, "images/results/tree-access-5-20000", BitmapEncoder.BitmapFormat.PNG);
    }

    private static void chartTwo() throws IOException {
        final XYChart constructionChart = new XYChartBuilder().width(600).height(400)
                .title("Construct and Insert Time").xAxisTitle("Density")
                .yAxisTitle("Time in Milli Seconds").build();

        constructionChart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        constructionChart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

        final XYChart searchChart = new XYChartBuilder().width(600).height(400)
                .title("Search Time").xAxisTitle("Density")
                .yAxisTitle("Time in Milli Seconds").build();

        searchChart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        searchChart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

        double[] densityList = new double[] {0, 2, 4, 6, 8, 10};

        List<Double> quadInserts = new ArrayList<>();
        List<Double> rInserts = new ArrayList<>();
        List<Double> quadKdInserts = new ArrayList<>();
        List<Double> rKdInserts = new ArrayList<>();

        List<Double> quadSearches = new ArrayList<>();
        List<Double> rSearches = new ArrayList<>();
        List<Double> quadKdSearches = new ArrayList<>();
        List<Double> rKdSearches = new ArrayList<>();

        Rectangle boundary = new BaseRectangle(0.0, 0.0, 25000.0, 25000.0);

        for (Double density: densityList) {

            List<Point> points = Generator.points(min, 25000, 50000/2, density.intValue());
            List<Rectangle> rectangles = Generator.rectangles(min, max, 50000/2);

            QuadTree quadTree = new QuadTree();
            RTree rTree = new RTree();

            QuadKDTree quadKDTree = new QuadKDTree();
            QuadTree hQuadTree = new QuadTree();

            RKDTree rkdTree = new RKDTree();
            RTree hRTree = new RTree();

            quadInserts.add((constructionTime(quadTree, rectangles, points))/1000000.0);
            rInserts.add(constructionTime(rTree, rectangles, points)/1000000.0);
            quadKdInserts.add(constructionTime(quadKDTree, hQuadTree, rectangles, points, boundary)/1000000.0);
            rKdInserts.add(constructionTime(rkdTree, hRTree, rectangles, points, boundary)/1000000.0);

            Collections.shuffle(points);

            quadSearches.add(searchTime(quadTree, rectangles, points)/1000000.0);
            rSearches.add(searchTime(rTree, rectangles, points)/1000000.0);
            quadKdSearches.add((searchTime(quadKDTree, hQuadTree, rectangles) + searchTime(quadKDTree, hQuadTree, points, boundary))/1000000.0);
            rKdSearches.add((searchTime(rkdTree, hRTree, rectangles) + searchTime(rkdTree, hRTree, points, boundary))/1000000.0);
        }

        constructionChart.addSeries("Quad Tree", densityList, quadInserts.stream().mapToDouble(Double::doubleValue).toArray());
        constructionChart.addSeries("R Tree", densityList, rInserts.stream().mapToDouble(Double::doubleValue).toArray());
        constructionChart.addSeries("Quad KD Tree", densityList, quadKdInserts.stream().mapToDouble(Double::doubleValue).toArray());
        constructionChart.addSeries("R KD Tree", densityList, rKdInserts.stream().mapToDouble(Double::doubleValue).toArray());

        BitmapEncoder.saveBitmap(constructionChart, "images/results/tree-construction-density-4-50000", BitmapEncoder.BitmapFormat.PNG);

        searchChart.addSeries("Quad Tree", densityList, quadSearches.stream().mapToDouble(Double::doubleValue).toArray());
        searchChart.addSeries("R Tree", densityList, rSearches.stream().mapToDouble(Double::doubleValue).toArray());
        searchChart.addSeries("Quad KD Tree", densityList, quadKdSearches.stream().mapToDouble(Double::doubleValue).toArray());
        searchChart.addSeries("R KD Tree", densityList, rKdSearches.stream().mapToDouble(Double::doubleValue).toArray());

        BitmapEncoder.saveBitmap(searchChart, "images/results/tree-access-density-4-50000", BitmapEncoder.BitmapFormat.PNG);
    }

    private static void chartThree() throws IOException {
        final XYChart constructionChart = new XYChartBuilder().width(600).height(400)
                .title("Construct and Insert Time").xAxisTitle("Number of Rectangles")
                .yAxisTitle("Time in Milli Seconds").build();

        constructionChart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        constructionChart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

        final XYChart searchChart = new XYChartBuilder().width(600).height(400)
                .title("Search Time").xAxisTitle("Number of Rectangles")
                .yAxisTitle("Time in Milli Seconds").build();

        searchChart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        searchChart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

        double[] countList = new double[] {100.0, 1000.0, 5000.0, 10000.0, 15000.0, 20000.0,
                30000.0, 45000.0, 60000.0, 80000.0};

        List<Double> quadInserts = new ArrayList<>();
        List<Double> rInserts = new ArrayList<>();
        List<Double> quadKdInserts = new ArrayList<>();
        List<Double> rKdInserts = new ArrayList<>();

        List<Double> quadSearches = new ArrayList<>();
        List<Double> rSearches = new ArrayList<>();
        List<Double> quadKdSearches = new ArrayList<>();
        List<Double> rKdSearches = new ArrayList<>();

        for (Double count: countList) {
            List<Point> points = Generator.points(min, max, count.intValue()/2, 0);
            List<Rectangle> rectangles = Generator.rectangles(min, max, count.intValue());

            QuadTree quadTree = new QuadTree();

            RTree rTree = new RTree();

            QuadKDTree quadKDTree = new QuadKDTree();
            QuadTree hQuadTree = new QuadTree();

            RKDTree rkdTree = new RKDTree();
            RTree hRTree = new RTree();

            quadInserts.add((constructionTime(quadTree, rectangles, points))/1000000.0);
            rInserts.add(constructionTime(rTree, rectangles, points)/1000000.0);
            quadKdInserts.add(constructionTime(quadKDTree, hQuadTree, rectangles, points, boundary)/1000000.0);
            rKdInserts.add(constructionTime(rkdTree, hRTree, rectangles, points, boundary)/1000000.0);

            Collections.shuffle(points);

            quadSearches.add(searchTime(quadTree, rectangles, points)/1000000.0);
            rSearches.add(searchTime(rTree, rectangles, points)/1000000.0);
            quadKdSearches.add((searchTime(quadKDTree, hQuadTree, rectangles) + searchTime(quadKDTree, hQuadTree, points, boundary))/1000000.0);
            rKdSearches.add((searchTime(rkdTree, hRTree, rectangles) + searchTime(rkdTree, hRTree, points, boundary))/1000000.0);
        }

        constructionChart.addSeries("Quad Tree", countList, quadInserts.stream().mapToDouble(Double::doubleValue).toArray());
        constructionChart.addSeries("R Tree", countList, rInserts.stream().mapToDouble(Double::doubleValue).toArray());
        constructionChart.addSeries("Quad KD Tree", countList, quadKdInserts.stream().mapToDouble(Double::doubleValue).toArray());
        constructionChart.addSeries("R KD Tree", countList, rKdInserts.stream().mapToDouble(Double::doubleValue).toArray());

        BitmapEncoder.saveBitmap(constructionChart, "images/results/tree-construction-rect-2-80000", BitmapEncoder.BitmapFormat.PNG);

        searchChart.addSeries("Quad Tree", countList, quadSearches.stream().mapToDouble(Double::doubleValue).toArray());
        searchChart.addSeries("R Tree", countList, rSearches.stream().mapToDouble(Double::doubleValue).toArray());
        searchChart.addSeries("Quad KD Tree", countList, quadKdSearches.stream().mapToDouble(Double::doubleValue).toArray());
        searchChart.addSeries("R KD Tree", countList, rKdSearches.stream().mapToDouble(Double::doubleValue).toArray());

        BitmapEncoder.saveBitmap(searchChart, "images/results/tree-access-rect-2-80000", BitmapEncoder.BitmapFormat.PNG);
    }
}
