package com.example.unitconverterthemechage;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class settingsActivity extends AppCompatActivity {
    private Switch themeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply theme before setting content view
        applyTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Set up action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Settings");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Set up theme switch
        themeSwitch = findViewById(R.id.theme_switch);

        // Get current theme preference
        SharedPreferences prefs = getSharedPreferences("UnitConverterPrefs", MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("dark_mode", false);

        // Set switch state based on preference
        themeSwitch.setChecked(isDarkMode);

        // Set up theme switch listener
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save preference
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("dark_mode", isChecked);
            editor.apply();

            // Apply theme change
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            // Recreate activity for theme change to take effect
            recreate();
        });
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}