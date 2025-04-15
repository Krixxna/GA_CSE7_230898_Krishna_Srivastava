package com.example.unitconverterthemechage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {
    private Spinner fromUnitSpinner;
    private Spinner toUnitSpinner;
    private EditText fromValueEditText;
    private TextView resultTextView;

    // Length units
    private final String[] lengthUnits = {"Feet", "Inches", "Centimeters", "Meters", "Yards"};

    // Conversion factors to meters (standard unit)
    private final double[] toMeterFactors = {
            0.3048,    // Feet to Meters
            0.0254,    // Inches to Meters
            0.01,      // Centimeters to Meters
            1.0,       // Meters to Meters
            0.9144     // Yards to Meters
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply theme before setting content view
        applyTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        fromUnitSpinner = findViewById(R.id.from_unit_spinner);
        toUnitSpinner = findViewById(R.id.to_unit_spinner);
        fromValueEditText = findViewById(R.id.from_value);
        resultTextView = findViewById(R.id.result_text);

        // Set up unit spinners
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, lengthUnits);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fromUnitSpinner.setAdapter(unitAdapter);
        toUnitSpinner.setAdapter(unitAdapter);

        // Set default selections
        fromUnitSpinner.setSelection(0); // Feet
        toUnitSpinner.setSelection(2);   // Centimeters

        // Set up listeners
        setupListeners();
    }

    private void setupListeners() {
        // Text change listener for automatic conversion
        fromValueEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                performConversion();
            }
        });

        // Unit selection listeners
        AdapterView.OnItemSelectedListener unitListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                performConversion();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Not needed
            }
        };

        fromUnitSpinner.setOnItemSelectedListener(unitListener);
        toUnitSpinner.setOnItemSelectedListener(unitListener);
    }

    private void performConversion() {
        String input = fromValueEditText.getText().toString();
        if (input.isEmpty()) {
            resultTextView.setText("0");
            return;
        }

        try {
            double inputValue = Double.parseDouble(input);
            int fromUnitPosition = fromUnitSpinner.getSelectedItemPosition();
            int toUnitPosition = toUnitSpinner.getSelectedItemPosition();

            // Convert from source unit to meters, then from meters to target unit
            double valueInMeters = inputValue * toMeterFactors[fromUnitPosition];
            double result = valueInMeters / toMeterFactors[toUnitPosition];

            // Format and display result
            resultTextView.setText(String.format("%.4f", result));

        } catch (NumberFormatException e) {
            resultTextView.setText("Invalid input");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            // Open settings activity
            Intent intent = new Intent(this, settingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void applyTheme() {
        // Get saved theme preference
        SharedPreferences prefs = getSharedPreferences("UnitConverterPrefs", MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("dark_mode", false);

        // Apply theme
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reapply theme in case it changed in settings
        applyTheme();
    }
}