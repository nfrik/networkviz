package core;
/**
 * Created by NF on 3/23/2017.
 */

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.util.Random;


public class PltXYSeries extends  ApplicationFrame{

    public XYSeries getSeries() {
        return series;
    }

    public void setSeries(XYSeries series) {
        this.series = series;
    }

    private XYSeries series;

    private static PltXYSeries instance = null;

//        public static PltXYSeries getInstance(){
//            if(instance==null){
//                instance = new PltXYSeries();
//            }
//
//            return instance;
//        }

//    public static PltXYSeries getInstance(){
//        if(instance == null){
//            instance = new PltXYSeries("Dynamic Line And TimeSeries Chart");
//            instance.pack();
//            RefineryUtilities.centerFrameOnScreen(instance);
//            instance.setVisible(true);
//        }
//
//        return instance;
//    }

    /**
     * A demonstration application showing an XY series containing a null value.
     *
     * @param title  the frame title.
     */
    public PltXYSeries(final String title) {

        super(title);
        series = new XYSeries(title);

        final XYSeriesCollection data = new XYSeriesCollection(series);
        final JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                "X",
                "Y",
                data,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);

        this.pack();
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);

    }

    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(final String[] args) throws InterruptedException {

        final PltXYSeries demo = new PltXYSeries("Cool Test");


        Random rn = new Random();
        for(int i=0;i<1000;i++){

            Thread.sleep(100);
            demo.getSeries().add(i,rn.nextDouble());

        }

    }

}
