import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BRCADataAnalyzer {
    public static void main(String[] args) {
        String csvFile = "data.csv";
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";

        try {
            br = new BufferedReader(new FileReader(csvFile));
            String[] headers = br.readLine().split(csvSplitBy);
            int numColumns = headers.length;
            List<String[]> data = new ArrayList<>();
            List<String[]> cleanedData = new ArrayList<>();
            Map<String, Integer> missingValuesCount = new HashMap<>();
            Map<String, String> dataTypes = new HashMap<>();
            Map<String, List<String>> columnValues = new HashMap<>();

            // Initialize the maps
            for (String header : headers) {
                missingValuesCount.put(header, 0);
                dataTypes.put(header, "String"); // Default type is String
                columnValues.put(header, new ArrayList<>());
            }

            // Read the file line by line and collect data
            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSplitBy, -1); // -1 to include trailing empty strings
                if (values.length != numColumns) {
                    System.err.println("Skipping row due to column count mismatch: " + Arrays.toString(values));
                    continue; // Skip rows with incorrect number of columns
                }
                data.add(values);
                boolean hasMissingValue = false;
                for (int i = 0; i < values.length; i++) {
                    if (values[i].isEmpty()) {
                        missingValuesCount.put(headers[i], missingValuesCount.get(headers[i]) + 1);
                        hasMissingValue = true;
                    }
                }
                if (!hasMissingValue) {
                    cleanedData.add(values);
                    for (int i = 0; i < headers.length; i++) {
                        columnValues.get(headers[i]).add(values[i]);
                        // Infer data types
                        if (dataTypes.get(headers[i]).equals("String")) {
                            if (isInteger(values[i])) {
                                dataTypes.put(headers[i], "Integer");
                            } else if (isDouble(values[i])) {
                                dataTypes.put(headers[i], "Double");
                            }
                        } else if (dataTypes.get(headers[i]).equals("Integer")) {
                            if (!isInteger(values[i])) {
                                if (isDouble(values[i])) {
                                    dataTypes.put(headers[i], "Double");
                                } else {
                                    dataTypes.put(headers[i], "String");
                                }
                            }
                        } else if (dataTypes.get(headers[i]).equals("Double")) {
                            if (!isDouble(values[i])) {
                                dataTypes.put(headers[i], "String");
                            }
                        }
                    }
                }
            }

            // Print missing values count before cleaning
            System.out.println("Missing Values Count Before Cleaning:");
            for (Map.Entry<String, Integer> entry : missingValuesCount.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

            // Print the whole cleaned data
            System.out.println("\nCleaned Data:");
            System.out.println(String.join(",", headers));
            for (String[] row : cleanedData) {
                System.out.println(String.join(",", row));
            }

            // Print data types for each column
            System.out.println("\nData Types:");
            for (Map.Entry<String, String> entry : dataTypes.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

            // Calculate and print missing values count after cleaning
            System.out.println("\nMissing Values Count After Cleaning:");
            for (String header : headers) {
                System.out.println(header + ": 0"); // No missing values in cleaned data
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Helper methods to check data types
    private static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
