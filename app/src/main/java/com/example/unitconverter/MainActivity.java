package com.example.unitconverter;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    // UI Components
    private Spinner converterTypeSpinner;
    private Spinner fromSpinner;
    private Spinner toSpinner;
    private EditText inputValue;
    private ImageButton swapButton;
    private Button convertButton;
    private TextView outputTextView;

    // Conversion types and units
    private final String[] converterTypes = {"Currency", "Fuel", "Temperature"};
    private final String[] currencyUnits = {"USD", "AUD", "EUR", "JPY", "GBP"};
    private final String[] fuelUnits = {"mpg", "km/L", "Gallon", "Liter", "Nautical Mile", "Kilometer"};
    private final String[] temperatureUnits = {"Celsius", "Fahrenheit", "Kelvin"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enables edge-to-edge layout
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Connect all the UI elements
        converterTypeSpinner = findViewById(R.id.converterTypeSpinner);
        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);
        inputValue = findViewById(R.id.inputValue);
        swapButton = findViewById(R.id.swapButton);
        convertButton = findViewById(R.id.convertButton);
        outputTextView = findViewById(R.id.outputTextView);

        // Load the main category spinner
        setupSpinner(converterTypeSpinner, converterTypes);

        // Set default conversion category when app starts
        updateUnitSpinners("Currency");

        // Handles padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // When user changes converter type, update the unit spinners
        converterTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = converterTypeSpinner.getSelectedItem().toString();

                // Update dropdowns based on selected category
                updateUnitSpinners(selectedCategory);

                // Reset result display
                outputTextView.setText("0.00");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nothing to do here
            }
        });

        // Swaps From/To units
        swapButton.setOnClickListener(v -> swapUnits());

        // Convert button performs the calculation
        convertButton.setOnClickListener(v -> performConversion());
    }


    // Helper method to load items into a spinner
    private void setupSpinner(Spinner spinner, String[] items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                items
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }


    // Updates the unit dropdowns depending on selected category
    private void updateUnitSpinners(String category) {
        switch (category) {
            case "Currency":
                setupSpinner(fromSpinner, currencyUnits);
                setupSpinner(toSpinner, currencyUnits);

                fromSpinner.setSelection(getIndex(currencyUnits, "USD"));
                toSpinner.setSelection(getIndex(currencyUnits, "AUD"));
                break;

            case "Fuel":
                setupSpinner(fromSpinner, fuelUnits);
                setupSpinner(toSpinner, fuelUnits);

                fromSpinner.setSelection(getIndex(fuelUnits, "mpg"));
                toSpinner.setSelection(getIndex(fuelUnits, "km/L"));
                break;

            case "Temperature":
                setupSpinner(fromSpinner, temperatureUnits);
                setupSpinner(toSpinner, temperatureUnits);

                fromSpinner.setSelection(getIndex(temperatureUnits, "Celsius"));
                toSpinner.setSelection(getIndex(temperatureUnits, "Fahrenheit"));
                break;
        }
    }


    // Finds the index of a value in an array (for default from/to selections)
    private int getIndex(String[] items, String value) {
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(value)) {
                return i;
            }
        }
        return 0;
    }


    // Swaps selected values between our two spinners
    private void swapUnits() {
        int fromPosition = fromSpinner.getSelectedItemPosition();
        int toPosition = toSpinner.getSelectedItemPosition();

        fromSpinner.setSelection(toPosition);
        toSpinner.setSelection(fromPosition);
    }


    // Main conversion logic triggered by Convert button
    private void performConversion() {
        String input = inputValue.getText().toString().trim();

        // Check if input is empty
        if (input.isEmpty()) {
            Toast.makeText(this, "Please enter a value", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert input to a number
        double value;
        try {
            value = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get current spinner selections
        String category = converterTypeSpinner.getSelectedItem().toString();
        String fromUnit = fromSpinner.getSelectedItem().toString();
        String toUnit = toSpinner.getSelectedItem().toString();

        // Validate input
        String validationError = validateInput(category, fromUnit, value);
        if (validationError != null) {
            Toast.makeText(this, validationError, Toast.LENGTH_SHORT).show();
            return;
        }

        // Perform the conversion
        double result = convert(category, fromUnit, toUnit, value);

        // Output the result
        outputTextView.setText(formatResult(category, toUnit, result));
    }


    // Validates input based on selected category
    private String validateInput(String category, String fromUnit, double value) {
        if (category.equals("Currency") && value < 0) {
            return "Currency value cannot be negative";
        }

        if (category.equals("Fuel") && value < 0) {
            return "Fuel value cannot be negative";
        }

        if (category.equals("Temperature")) {
            if (fromUnit.equals("Kelvin") && value < 0) {
                return "Kelvin cannot be negative";
            }
            if (fromUnit.equals("Celsius") && value < -273.15) {
                return "Celsius cannot be below absolute zero";
            }
            if (fromUnit.equals("Fahrenheit") && value < -459.67) {
                return "Fahrenheit cannot be below absolute zero";
            }
        }

        return null;
    }


    // Performs the conversion based on selected category
    private double convert(String category, String fromUnit, String toUnit, double value) {
        if (fromUnit.equals(toUnit)) {
            return value;
        }

        switch (category) {
            case "Currency":
                return convertCurrency(fromUnit, toUnit, value);

            case "Fuel":
                return convertFuel(fromUnit, toUnit, value);

            case "Temperature":
                return convertTemperature(fromUnit, toUnit, value);

            default:
                return value;
        }
    }


    // Converts currency values based on selected units
    private double convertCurrency(String fromUnit, String toUnit, double value) {
        // Fixed currency rates:
        // 1 USD = 1.55 AUD
        // 1 USD = 0.92 EUR
        // 1 USD = 148.50 JPY
        // 1 USD = 0.78 GBP

        double usdValue;

        switch (fromUnit) {
            case "USD":
                usdValue = value;
                break;
            case "AUD":
                usdValue = value / 1.55;
                break;
            case "EUR":
                usdValue = value / 0.92;
                break;
            case "JPY":
                usdValue = value / 148.50;
                break;
            case "GBP":
                usdValue = value / 0.78;
                break;
            default:
                usdValue = value;
        }

        switch (toUnit) {
            case "USD":
                return usdValue;
            case "AUD":
                return usdValue * 1.55;
            case "EUR":
                return usdValue * 0.92;
            case "JPY":
                return usdValue * 148.50;
            case "GBP":
                return usdValue * 0.78;
            default:
                return usdValue;
        }
    }


    // Converts fuel values based on selected units
    private double convertFuel(String fromUnit, String toUnit, double value) {
        // Fuel efficiency
        if (fromUnit.equals("mpg") && toUnit.equals("km/L")) {
            return value * 0.425;
        }

        if (fromUnit.equals("km/L") && toUnit.equals("mpg")) {
            return value / 0.425;
        }

        // Volume
        if (fromUnit.equals("Gallon") && toUnit.equals("Liter")) {
            return value * 3.785;
        }

        if (fromUnit.equals("Liter") && toUnit.equals("Gallon")) {
            return value / 3.785;
        }

        // Distance
        if (fromUnit.equals("Nautical Mile") && toUnit.equals("Kilometer")) {
            return value * 1.852;
        }

        if (fromUnit.equals("Kilometer") && toUnit.equals("Nautical Mile")) {
            return value / 1.852;
        }

        Toast.makeText(this, "That unit pair cannot be converted", Toast.LENGTH_SHORT).show();
        return value;
    }


    // Converts temperature values based on selected units
    private double convertTemperature(String fromUnit, String toUnit, double value) {
        if (fromUnit.equals("Celsius") && toUnit.equals("Fahrenheit")) {
            return (value * 1.8) + 32;
        }

        if (fromUnit.equals("Fahrenheit") && toUnit.equals("Celsius")) {
            return (value - 32) / 1.8;
        }

        if (fromUnit.equals("Celsius") && toUnit.equals("Kelvin")) {
            return value + 273.15;
        }

        if (fromUnit.equals("Kelvin") && toUnit.equals("Celsius")) {
            return value - 273.15;
        }

        if (fromUnit.equals("Fahrenheit") && toUnit.equals("Kelvin")) {
            return ((value - 32) / 1.8) + 273.15;
        }

        if (fromUnit.equals("Kelvin") && toUnit.equals("Fahrenheit")) {
            return ((value - 273.15) * 1.8) + 32;
        }

        return value;
    }


    // Formats the result based on selected category and unit
    private String formatResult(String category, String toUnit, double value) {
        if (category.equals("Currency")) {
            return formatCurrency(toUnit, value);
        }

        if (category.equals("Temperature")) {
            switch (toUnit) {
                case "Celsius":
                    return String.format(Locale.US, "%.2f °C", value);
                case "Fahrenheit":
                    return String.format(Locale.US, "%.2f °F", value);
                case "Kelvin":
                    return String.format(Locale.US, "%.2f K", value);
            }
        }

        return String.format(Locale.US, "%.2f %s", value, toUnit);
    }


    // Formats currency values based on selected currency
    private String formatCurrency(String currencyCode, double value) {
        Locale locale;

        switch (currencyCode) {
            case "USD":
                locale = Locale.US;
                break;
            case "AUD":
                locale = new Locale("en", "AU");
                break;
            case "EUR":
                locale = Locale.GERMANY;
                break;
            case "JPY":
                locale = Locale.JAPAN;
                break;
            case "GBP":
                locale = Locale.UK;
                break;
            default:
                locale = Locale.US;
        }

        NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);

        try {
            formatter.setCurrency(Currency.getInstance(currencyCode));
        } catch (Exception e) {
            return String.format(Locale.US, "%.2f %s", value, currencyCode);
        }

        return formatter.format(value);
    }
}
