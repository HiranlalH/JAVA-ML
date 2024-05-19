import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;

public class SalesVsProfitScatterPlot {

    public static void main(String[] args) {
        String csvFile = "clndData.csv";

        XYSeries series = new XYSeries("Sales vs. Profit");

        try (FileReader reader = new FileReader(csvFile);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            for (CSVRecord record : csvParser) {
                double sales = Double.parseDouble(record.get("Sales"));
                double profit = Double.parseDouble(record.get("Profit"));

                series.add(sales, profit);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create dataset
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        // Create the scatter plot
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Sales vs. Profit",
                "Sales",
                "Profit",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);

        // Display the chart in a JFrame
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        JFrame frame = new JFrame("Sales vs. Profit");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }
}
