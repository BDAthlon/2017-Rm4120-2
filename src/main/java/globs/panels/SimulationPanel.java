package globs.panels;


import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;

public class SimulationPanel extends JPanel{

  private static final long serialVersionUID = -4796002552031761344L;
  private JFreeChart chart;
  private final XYItemRenderer renderer = ChartFactory.createXYLineChart(null, null, null, null).getXYPlot().getRenderer(0);
  
  public SimulationPanel() {

  }

  public void initUI(XYDataset dataset) {
    createBlobs(dataset);
    chart.removeLegend();
    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    if(this.getComponentCount() > 0)
    {
      this.remove(0);
    }
    add(chartPanel);
  }

  public void plotSimulationTrace(String species,int index,  XYDataset dataset) {
    XYPlot plot = chart.getXYPlot();
    plot.setDataset(index, dataset);
    plot.setRenderer(index, renderer);
  }
  
  public void resetChart()
  {
    for(int i = 1; i < chart.getXYPlot().getDatasetCount(); i++)
    {
      chart.getXYPlot().setDataset(i, null);
    }
  }

  private void createBlobs(XYDataset dataset) 
  {

    chart = ChartFactory.createScatterPlot(
      "Green Blobs", 
      "Time", 
      "Amounts", 
      dataset
        );

    XYPlot plot = chart.getXYPlot();
    plot.getRenderer().setSeriesPaint(0, Color.green);
    plot.setBackgroundPaint(Color.white);
  }



}
