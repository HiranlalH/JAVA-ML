import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfitDistributionHistogram {

    public static void main(String[] args) {
        String csvFile = "clndData.csv";
        List<Double> profitList = new ArrayList<>();

        try (FileReader reader = new FileReader(csvFile);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            for (CSVRecord record : csvParser) {
                double profit = Double.parseDouble(record.get("Profit"));
                profitList.add(profit);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        double[] profitArray = profitList.stream().mapToDouble(Double::doubleValue).toArray();

        // Create a dataset for the histogram
        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("Profit", profitArray, 30); // 30 bins

        // Create the histogram chart
        JFreeChart chart = ChartFactory.createHistogram(
                "Distribution of Profit",
                "Profit",
                "Frequency",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);

        // Display the chart in a JFrame
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        JFrame frame = new JFrame("Distribution of Profit");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }
}
