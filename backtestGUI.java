import eu.verdelhan.ta4j.TimeSeries;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;

import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.*;
import org.jfree.ui.ApplicationFrame;
import org.jfree.chart.plot.PlotOrientation;


import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class backtestGUI extends ApplicationFrame {
    TimeSeries temp;
    ArrayList<Double> annout;
    ArrayList<trade> tradeHistory;

    public backtestGUI(String applicationTitle , String chartTitle, TimeSeries series, ArrayList<Double> ANNoutIn,ArrayList<trade> orders) {
        super(applicationTitle);
        temp =series;
        annout = ANNoutIn;
        tradeHistory = orders;

        JFreeChart lineChart = ChartFactory.createXYLineChart(chartTitle, "Time","price", createDataset(), PlotOrientation.VERTICAL, true,true,false);
        lineChart.setBackgroundPaint(Color.white);

        final XYPlot plot = lineChart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);

        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesShapesVisible(0,false);
        renderer.setSeriesShapesVisible(1,false);
        renderer.setSeriesOutlineStroke(1, new BasicStroke(4.0f));

        for(int i =0;i<plot.getSeriesCount();i++) {
            renderer.setSeriesVisibleInLegend(i+2, false);
            renderer.setSeriesShapesVisible(i+2, true);
            Shape square = new Rectangle2D.Double(-2.0, -2.0, 1.0, 1.0);
            renderer.setSeriesShape(i+3, square);
            renderer.setSeriesPaint(i+3, Color.green);
            renderer.setSeriesLinesVisible(i+2, false);
            renderer.setSeriesVisible(i+2,false);


        }

        plot.setRenderer(renderer);


        ChartPanel chartPanel = new ChartPanel( lineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 1000 , 600 ) );
        setContentPane( chartPanel );
    }

    private XYDataset createDataset( ) throws NullPointerException {
        XYSeries timeSeries = new XYSeries(temp.getName());
        XYSeries ANN = new XYSeries("Neural Network Prediction");
ArrayList<XYSeries> trades = new ArrayList<XYSeries>();
        XYSeries buy;



        double y = 1;
        for(int i =0;i<temp.getEnd();i++) {
            y += 1;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
            LocalDate localDate = LocalDate.parse(temp.getTick(i).getSimpleDateName(), formatter);

            timeSeries.add(y, temp.getTick(i).getClosePrice().toDouble());
            ANN.add(y,annout.get(i));


            for(int z = 0;z<tradeHistory.size();z++){
try {
    if (localDate.compareTo(tradeHistory.get(z).getOpenDate()) >= 0 && localDate.compareTo(tradeHistory.get(z).getCloseDate()) <= 0) {
        //System.out.println("Current " + temp.getTick(i).getSimpleDateName() + "  " + tradeHistory.get(z) );
        buy = new XYSeries(String.valueOf(Math.random()));
        buy.add(y, temp.getTick(i).getClosePrice().toDouble() - 5);
        trades.add(buy);


    }
}
                catch(NullPointerException e){

    }

            }


        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(ANN);
        dataset.addSeries(timeSeries);
        for(int i =0;i<trades.size();i++) {
            dataset.addSeries(trades.get(i));
            //System.out.println(i);
        }

        return dataset;
    }


}