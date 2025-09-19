package com.example.listycitylab3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class EditCityActivity extends AppCompatActivity {

    private EditText editCityNameEditText;
    private Button saveButton;
    private Button cancelButton;
    private String originalCityName;
    private int cityPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_city); // We'll create this layout next

        editCityNameEditText = findViewById(R.id.edit_city_name_edittext);
        saveButton = findViewById(R.id.save_button);
        cancelButton = findViewById(R.id.cancel_button);

        // Get the data passed from MainActivity
        Intent intent = getIntent();
        originalCityName = intent.getStringExtra("CITY_NAME");
        cityPosition = intent.getIntExtra("CITY_POSITION", -1);

        if (originalCityName != null) {
            editCityNameEditText.setText(originalCityName);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedCityName = editCityNameEditText.getText().toString().trim();
                if (!updatedCityName.isEmpty()) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("UPDATED_CITY_NAME", updatedCityName);
                    resultIntent.putExtra("CITY_POSITION", cityPosition);
                    setResult(RESULT_OK, resultIntent);
                    finish(); // Close this activity and return to MainActivity
                } else {
                    editCityNameEditText.setError("City name cannot be empty");
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish(); // Close this activity and return to MainActivity
            }
        });
    }
}
