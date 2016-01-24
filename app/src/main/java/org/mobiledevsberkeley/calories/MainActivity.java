package org.mobiledevsberkeley.calories;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String TAG = "cs160";
    private int currentWorkoutValue, currentWorkoutPos, equivWorkoutPos;
    private TextView vUnits, vEquivUnits, vCalsBurned, vEquivCalsBurned;
    private ImageView vFlame, vShine;
    private EditText vNumber;
    private Spinner equivalentSpinner;
    private String[] workoutNames, unitNames = new String[2];
    private int[] workoutVals, workoutUnits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        workoutNames = getResources().getStringArray(R.array.workout_name_array);
        unitNames[0] = getResources().getString(R.string.reps);unitNames[1]=getResources().getString(R.string.minutes);
        workoutVals = getResources().getIntArray(R.array.workout_value_array);
        workoutUnits = getResources().getIntArray(R.array.workout_unit_array);
        currentWorkoutPos = 0; equivWorkoutPos = 1;
        currentWorkoutValue = workoutVals[currentWorkoutPos];

        vUnits = (TextView)findViewById(R.id.units_text);
        vEquivUnits = (TextView)findViewById(R.id.equiv_units_text);
        vFlame = (ImageView)findViewById(R.id.flame_image);
        vShine = (ImageView)findViewById(R.id.shine_image);
        vFlame.setAlpha((float)0);
        vShine.setAlpha((float)0);
        vCalsBurned = (TextView)findViewById(R.id.calories_burned_amount);
        vEquivCalsBurned = (TextView)findViewById(R.id.equivalent_amount);
        vNumber = (EditText) findViewById(R.id.num_text);
        vNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateResults();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //Initialize Spinner to choose workout type
        Spinner workoutSpinner = (Spinner) findViewById(R.id.workout_spinner);
        ArrayAdapter<CharSequence> workoutSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.workout_name_array, R.layout.my_spinner_item);
        workoutSpinnerAdapter.setDropDownViewResource(R.layout.my_spinner_dropdown_item);
        workoutSpinner.setAdapter(workoutSpinnerAdapter);
        workoutSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vUnits.setText(String.valueOf(unitNames[workoutUnits[position]]));
                currentWorkoutPos = position;
                currentWorkoutValue = workoutVals[position];
                updateResults();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Initialize Spinner to choose equivalent workout to compare
        equivalentSpinner = (Spinner) findViewById(R.id.equivalent_workout_spinner);
        ArrayAdapter<CharSequence> equivalentAdapter = ArrayAdapter.createFromResource(this,
                R.array.workout_name_array, R.layout.my_spinner_item);
        equivalentAdapter.setDropDownViewResource(R.layout.my_spinner_dropdown_item);
        equivalentSpinner.setAdapter(equivalentAdapter);
        equivalentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                equivWorkoutPos = position;
                int equivalentAmount = getEquivAmount();
                vEquivCalsBurned.setText(String.valueOf(equivalentAmount));
                String units = unitNames[workoutUnits[position]] + " of";
                vEquivUnits.setText(units);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void updateResults()
    {
        int calsBurned = getCalsBurned();
        vCalsBurned.setText(String.valueOf(calsBurned));
        int color = getTrafficlightColor((float) calsBurned / 100.0);
        vFlame.setColorFilter(color);
        vCalsBurned.setTextColor(color);
        if(calsBurned > Math.pow(10, 7))
            vCalsBurned.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);
        else if(calsBurned > Math.pow(10, 4))
            vCalsBurned.setTextSize(TypedValue.COMPLEX_UNIT_SP, 70);
        else
            vCalsBurned.setTextSize(TypedValue.COMPLEX_UNIT_SP, 100);

        //Set Flame
        if (calsBurned > 50)
        {
            vFlame.setAlpha((float)1);
            vFlame.setColorFilter(getTrafficlightColor((float) (calsBurned - 50) / 100.0));
        }
        else {
            if(vFlame.getAlpha() > 0)
                vFlame.setAlpha((float) 0);
            vFlame.setColorFilter(getTrafficlightColor((float)0));
        }

        //Set shine
        if (calsBurned > 100)
        {
            vShine.setAlpha((float)1);
            vShine.setColorFilter(getTrafficlightColor((float) (calsBurned - 100) / 100.0));
        }
        else {
            if(vShine.getAlpha() > 0)
                vShine.setAlpha((float) 0);
            vShine.setColorFilter(getTrafficlightColor((float)0));
        }

        //Set equivalent to next workout amount
        if(currentWorkoutPos == equivWorkoutPos) {
            equivWorkoutPos = (currentWorkoutPos + 1) % workoutNames.length;
            equivalentSpinner.setSelection(equivWorkoutPos, true);
        }
        int equivalentAmount = getEquivAmount();
        vEquivCalsBurned.setText(String.valueOf(equivalentAmount));
        String units = unitNames[workoutUnits[equivWorkoutPos]] + " of";
        vEquivUnits.setText(units);
    }

    private int getCalsBurned()
    {
        String inputText = vNumber.getText().toString();
        if(inputText.isEmpty())
            return 0;
        double inputAmount = Integer.parseInt(inputText);
        double calsBurned = (100 * inputAmount) / currentWorkoutValue;
        return (int)Math.ceil(calsBurned);
    }

    private int getEquivAmount()
    {
        double calsBurned = getCalsBurned();
        double equivalentWorkoutValue = workoutVals[equivWorkoutPos];
        double equivalentAmount = (calsBurned * equivalentWorkoutValue) / 100;
        return (int)Math.ceil(equivalentAmount);
    }

    private int getTrafficlightColor(double value){
        value = 1 - value;
        return android.graphics.Color.HSVToColor(new float[]{(float)value*58f,1f,1f});
    }

}
