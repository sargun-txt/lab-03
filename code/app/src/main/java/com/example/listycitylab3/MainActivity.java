package com.example.listycitylab3; // Make sure this matches your package name

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton; // Correct import
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> dataList;
    private ListView cityList;
    private ArrayAdapter<String> cityAdapter;
    private FloatingActionButton addCityFab;

    private static final int EDIT_CITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] cities = {
                "Edmonton", "Vancouver", "Moscow",
                "Sydney", "Berlin", "Vienna",
                "Tokyo", "Beijing", "Osaka", "New Delhi"
        };

        dataList = new ArrayList<>();
        dataList.addAll(Arrays.asList(cities));

        cityList = findViewById(R.id.city_list);
        addCityFab = findViewById(R.id.add_city_fab); // Initialize FAB

        cityAdapter = new ArrayAdapter<>(this, R.layout.content, dataList); // Assuming R.layout.content is your list item layout
        cityList.setAdapter(cityAdapter);

        // Listener for adding a new city
        addCityFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddCityDialog();
            }
        });

        // Listener for editing an existing city
        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cityToEdit = dataList.get(position);
                Intent intent = new Intent(MainActivity.this, EditCityActivity.class);
                intent.putExtra("CITY_NAME", cityToEdit);
                intent.putExtra("CITY_POSITION", position);
                startActivityForResult(intent, EDIT_CITY_REQUEST_CODE);
            }
        });
    }

    private void showAddCityDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New City");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        input.setHint("Enter city name");
        builder.setView(input);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newCity = input.getText().toString().trim();
                if (!newCity.isEmpty()) {
                    if (!dataList.contains(newCity)) { // Optional: Prevent duplicate cities
                        dataList.add(newCity);
                        cityAdapter.notifyDataSetChanged();
                    } else {
                        // Optionally, show a toast or message that the city already exists
                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_CITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String updatedCityName = data.getStringExtra("UPDATED_CITY_NAME");
            int cityPosition = data.getIntExtra("CITY_POSITION", -1);

            if (updatedCityName != null && !updatedCityName.isEmpty() && cityPosition != -1) {
                // Optional: Check if the new name duplicates another existing city (excluding itself)
                boolean isDuplicate = false;
                for (int i = 0; i < dataList.size(); i++) {
                    if (i != cityPosition && dataList.get(i).equalsIgnoreCase(updatedCityName)) {
                        isDuplicate = true;
                        break;
                    }
                }

                if (!isDuplicate) {
                    dataList.set(cityPosition, updatedCityName);
                    cityAdapter.notifyDataSetChanged();
                } else {
                    // Optionally show a message that the city name would cause a duplicate
                }
            }
        }
    }
}
