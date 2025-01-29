import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class UnitConverter {
    private JFrame frame;
    private JTextField inputField, outputField;
    private JComboBox<String> inputUnit, outputUnit, conversionType;
    private JLabel inputLabel, outputLabel;

    public UnitConverter() {
        initializeComponents();
    }

    private void initializeComponents() {
        frame = new JFrame("Unit Converter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 350);
        frame.setLayout(new BorderLayout());

        // Create the components
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        inputLabel = new JLabel("Input Value:");
        outputLabel = new JLabel("Output Value:");

        inputField = new JTextField(10);
        outputField = new JTextField(10);
        outputField.setEditable(false);

        // ComboBox for selecting conversion type (Length, Temperature, Weight, Time)
        conversionType = new JComboBox<>();
        conversionType.addItem("Length");
        conversionType.addItem("Temperature");
        conversionType.addItem("Weight");
        conversionType.addItem("Time");
        conversionType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUnitOptions();
            }
        });

        // Initialize unit ComboBoxes for conversion type (length, temperature, etc.)
        inputUnit = new JComboBox<>();
        outputUnit = new JComboBox<>();
        updateUnitOptions();

        // Add ActionListeners for inputField, inputUnit, and outputUnit
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performConversion();
            }
        });

        inputUnit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performConversion();
            }
        });

        outputUnit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performConversion();
            }
        });

        // Add components to the panel
        panel.add(inputLabel);
        panel.add(inputField);
        panel.add(conversionType);
        panel.add(inputUnit);
        panel.add(outputUnit);
        panel.add(outputField);
        panel.add(outputLabel);

        // Add the panel to the frame
        frame.add(panel, BorderLayout.CENTER);

        // Display the frame
        frame.setVisible(true);
    }

    private void updateUnitOptions() {
        String selectedType = (String) conversionType.getSelectedItem();
        inputUnit.removeAllItems();
        outputUnit.removeAllItems();

        switch (selectedType) {
            case "Length":
                addLengthUnits();
                break;
            case "Temperature":
                addTemperatureUnits();
                break;
            case "Weight":
                addWeightUnits();
                break;
            case "Time":
                addTimeUnits();
                break;
        }
    }

    private void addLengthUnits() {
        inputUnit.addItem("Meters");
        inputUnit.addItem("Kilometers");
        inputUnit.addItem("Centimeters");
        inputUnit.addItem("Millimeters");
        inputUnit.addItem("Miles");
        inputUnit.addItem("Yards");
        inputUnit.addItem("Feet");
        inputUnit.addItem("Inches");

        outputUnit.addItem("Meters");
        outputUnit.addItem("Kilometers");
        outputUnit.addItem("Centimeters");
        outputUnit.addItem("Millimeters");
        outputUnit.addItem("Miles");
        outputUnit.addItem("Yards");
        outputUnit.addItem("Feet");
        outputUnit.addItem("Inches");
    }

    private void addTemperatureUnits() {
        inputUnit.addItem("Celsius");
        inputUnit.addItem("Fahrenheit");
        inputUnit.addItem("Kelvin");

        outputUnit.addItem("Celsius");
        outputUnit.addItem("Fahrenheit");
        outputUnit.addItem("Kelvin");
    }

    private void addWeightUnits() {
        inputUnit.addItem("Kilograms");
        inputUnit.addItem("Grams");
        inputUnit.addItem("Pounds");
        inputUnit.addItem("Ounces");

        outputUnit.addItem("Kilograms");
        outputUnit.addItem("Grams");
        outputUnit.addItem("Pounds");
        outputUnit.addItem("Ounces");
    }

    private void addTimeUnits() {
        inputUnit.addItem("Seconds");
        inputUnit.addItem("Minutes");
        inputUnit.addItem("Hours");
        inputUnit.addItem("Days");

        outputUnit.addItem("Seconds");
        outputUnit.addItem("Minutes");
        outputUnit.addItem("Hours");
        outputUnit.addItem("Days");
    }

    private void performConversion() {
        try {
            double input = Double.parseDouble(inputField.getText());
            String selectedType = (String) conversionType.getSelectedItem();
            String inputUnitValue = (String) inputUnit.getSelectedItem();
            String outputUnitValue = (String) outputUnit.getSelectedItem();

            String result = Logic.convert(selectedType, inputUnitValue, input, outputUnitValue);
            outputField.setText(result);
        } catch (NumberFormatException e) {
            outputField.setText("Invalid Input");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UnitConverter();
            }
        });
    }
}

class Logic {

    private static final Map<String, Double> lengthTable = new HashMap<>();
    private static final Map<String, Double> temperatureTable = new HashMap<>();
    private static final Map<String, Double> weightTable = new HashMap<>();
    private static final Map<String, Double> timeTable = new HashMap<>();

    static {
        // Length Conversion Factors (relative to meters)
        lengthTable.put("Meters", 1.0);
        lengthTable.put("Kilometers", 0.001);
        lengthTable.put("Centimeters", 100.0);
        lengthTable.put("Millimeters", 1000.0);
        lengthTable.put("Miles", 0.0006213689);
        lengthTable.put("Yards", 1.0936132983);
        lengthTable.put("Feet", 3.280839895);
        lengthTable.put("Inches", 39.37007874);

        // Temperature Conversion Factors
        temperatureTable.put("Celsius", 1.0);
        temperatureTable.put("Fahrenheit", 1.8);
        temperatureTable.put("Kelvin", 1.0);

        // Weight Conversion Factors (relative to kilograms)
        weightTable.put("Kilograms", 1.0);
        weightTable.put("Grams", 1000.0);
        weightTable.put("Pounds", 2.20462);
        weightTable.put("Ounces", 35.274);

        // Time Conversion Factors (relative to seconds)
        timeTable.put("Seconds", 1.0);
        timeTable.put("Minutes", 1.0 / 60);
        timeTable.put("Hours", 1.0 / 3600);
        timeTable.put("Days", 1.0 / 86400);
    }

    public static String convert(String type, String inputUnit, double input, String outputUnit) {
        double inputFactor = 0;
        double outputFactor = 0;

        switch (type) {
            case "Length":
                inputFactor = lengthTable.get(inputUnit);
                outputFactor = lengthTable.get(outputUnit);
                break;
            case "Temperature":
                if ("Celsius".equals(inputUnit)) {
                    input += 273.15; // Convert to Kelvin for calculation
                }
                if ("Celsius".equals(outputUnit)) {
                    outputFactor = 273.15; // Convert back from Kelvin
                }
                break;
            case "Weight":
                inputFactor = weightTable.get(inputUnit);
                outputFactor = weightTable.get(outputUnit);
                break;
            case "Time":
                inputFactor = timeTable.get(inputUnit);
                outputFactor = timeTable.get(outputUnit);
                break;
        }

        double result = input * (outputFactor / inputFactor);
        return String.format("%.4f", result);
    }
}
