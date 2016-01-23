package org.mobiledevsberkeley.calories;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String TAG = "cs160";
    private int workoutValue = 1;
    TextView vUnits, vCalsBurned, vEquivCalsBurned;
    EditText vNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] workoutNames = getResources().getStringArray(R.array.workout_name_array);
        final String[] unitNames = {getResources().getString(R.string.reps), getResources().getString(R.string.minutes)};
        final int[] workoutVals = getResources().getIntArray(R.array.workout_value_array);
        final int[] workoutUnits = getResources().getIntArray(R.array.workout_unit_array);
        workoutValue = workoutVals[0];

        vUnits = (TextView)findViewById(R.id.units_text);
        vNumber = (EditText) findViewById(R.id.num_text);
        vCalsBurned = (TextView)findViewById(R.id.calories_burned_amount);
        vEquivCalsBurned = (TextView)findViewById(R.id.equivalent_amount);

        //Initialize button to calculate
        Button calculateButton = (Button)findViewById(R.id.calculate_button);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strAmount = vNumber.getText().toString();
                int inputAmount = Integer.parseInt(strAmount);
                inputAmount = (100 * inputAmount) / workoutValue;

                if(vCalsBurned != null)
                    vCalsBurned.setText(String.valueOf(inputAmount));
            }
        });

        //Initialize Spinner to choose workout type
        Spinner workoutSpinner = (Spinner) findViewById(R.id.workout_spinner);
        ArrayAdapter<CharSequence> workoutSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.workout_name_array, android.R.layout.simple_spinner_item);
        workoutSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workoutSpinner.setAdapter(workoutSpinnerAdapter);
        workoutSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(vUnits != null)
                    vUnits.setText(String.valueOf(unitNames[workoutUnits[position]]));
                workoutValue = workoutVals[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Initialize Spinner to choose equivalent workout to compare
        Spinner equivalentSpinner = (Spinner) findViewById(R.id.equivalent_workout_spinner);
        ArrayAdapter<CharSequence> equivalentAdapter = ArrayAdapter.createFromResource(this,
                R.array.workout_name_array, android.R.layout.simple_spinner_item);
        equivalentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        equivalentSpinner.setAdapter(equivalentAdapter);


    }

}
