package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.R;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.ArduinoRobot;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.builders.RuleEntityBuilder;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.stepper.StepMotorEntity;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.exception.IncompatibleStepMotorArguments;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.fragments.ForwardBackwardFragment;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.fragments.TurningFragment;

public class CreateRuleActivity extends AppCompatActivity {

    private static final String TAG = "CreateRuleActivity";

    public static final String RULE_ENTITY_CODE = "ruleEntity";

    private RuleEntityBuilder ruleEntityBuilder;

    private final String[] directionStringOptions = {"Forward", "Turning"};

    private ForwardBackwardFragment forwardBackwardFragment;
    private TurningFragment turningFragment;

    private NumberInputPopupDialog numberInputPopupDialog;
    private Button btnPanDegree;
    private Button btnPanExecutionTime;
    private Button btnTiltDegree;
    private Button btnTiltExecutionTime;

    private AlertDialog validationWarningDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_rule);
        setTitle(R.string.title_create_rule_activity);

        ArduinoRobot arduinoRobot = (ArduinoRobot) getIntent().getSerializableExtra("arduinoRobot");
        assert arduinoRobot != null : "arduinoRobot extra is null";

        TextView textView_ruleNumber = findViewById(R.id.textView_ruleNumber);
        textView_ruleNumber.setText(getString(R.string.label_rule_number, String.valueOf(arduinoRobot.getRulesManagerEntity().size() + 1)));
        ruleEntityBuilder = new RuleEntityBuilder(arduinoRobot);

        AutoCompleteTextView directionAutoComplete = findViewById(R.id.auto_complete_txt);
        directionAutoComplete.setAdapter(new ArrayAdapter<>(this, R.layout.text_option_item, directionStringOptions));
        directionAutoComplete.setOnItemClickListener(this::directionOnItemClickListener);

        numberInputPopupDialog = new NumberInputPopupDialog(CreateRuleActivity.this, true);

        forwardBackwardFragment = ForwardBackwardFragment.newInstance(numberInputPopupDialog, ruleEntityBuilder);
        turningFragment = new TurningFragment();


        btnPanDegree = findViewById(R.id.btnPanDegree);
        btnPanDegree.setOnClickListener(view -> {
            String popupTitle = getString(R.string.label_pan) + " " + getString(R.string.label_degree);
            clickListener_measurementData(numberInputPopupDialog, btnPanDegree, btnPanExecutionTime, popupTitle, ruleEntityBuilder.getPanMotor());
        });

        btnPanExecutionTime = findViewById(R.id.btnPanExecutionTime);
        btnPanExecutionTime.setOnClickListener(view -> {
            String popupTitle = getString(R.string.label_pan) + " " + getString(R.string.label_execution_time);
            clickListener_executionTime(numberInputPopupDialog, btnPanExecutionTime, popupTitle, ruleEntityBuilder.getPanMotor());
        });

        btnTiltDegree = findViewById(R.id.btnTiltDegree);
        btnTiltDegree.setOnClickListener(view -> {
            String popupTitle = getString(R.string.label_tilt) + " " + getString(R.string.label_degree);
            clickListener_measurementData(numberInputPopupDialog, btnTiltDegree, btnTiltExecutionTime, popupTitle, ruleEntityBuilder.getTiltMotor());
        });

        btnTiltExecutionTime = findViewById(R.id.btnTiltExecutionTime);
        btnTiltExecutionTime.setOnClickListener((view -> {
            String popupTitle = getString(R.string.label_tilt) + " " + getString(R.string.label_execution_time);
            clickListener_executionTime(numberInputPopupDialog, btnTiltExecutionTime, popupTitle, ruleEntityBuilder.getTiltMotor());
        }));

        Button buttonCancle = findViewById(R.id.btn_cancle);
        buttonCancle.setOnClickListener(this::cancleButton);

        Button buttonApply = findViewById(R.id.btn_apply);
        buttonApply.setOnClickListener(this::applyButton);
    }

    private void cancleButton(View view) {
        finish();
    }

    private void applyButton(View view) {
        String warnings = validateInputs();
        if (warnings.isEmpty()) {
            applyRule();
            return;
        }

        if (validationWarningDialog != null) {
            validationWarningDialog.setMessage(warnings);
            validationWarningDialog.show();
            return;
        }

        validationWarningDialog = new AlertDialog.Builder(CreateRuleActivity.this)
                .setTitle("Warning:")
                .setMessage(warnings)
                .setOnCancelListener(dialogInterface -> {
                })
                .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton("Yes", (dialogInterface, i) -> applyRule()).create();
        validationWarningDialog.show();
    }

    public void applyRule() {
        Intent intent = getIntent();
        intent.putExtra(CreateRuleActivity.RULE_ENTITY_CODE, ruleEntityBuilder.build());
        setResult(Activity.RESULT_OK, intent);

        finish();
    }

    private String validateInputs() {
        String warnings = "";
        int warningsCount = 0;

        if (ruleEntityBuilder.getLeftMotor().getMeasurementValue() == 0 && ruleEntityBuilder.getRightMotor().getMeasurementValue() == 0
                && ruleEntityBuilder.getPanMotor().getMeasurementValue() == 0 && ruleEntityBuilder.getTiltMotor().getMeasurementValue() == 0
        ) {
            double totalExecutionTime = ruleEntityBuilder.getLeftMotor().getExecutionTime() + ruleEntityBuilder.getRightMotor().getExecutionTime()
                    + ruleEntityBuilder.getPanMotor().getExecutionTime() + ruleEntityBuilder.getTiltMotor().getExecutionTime();
            if (totalExecutionTime == 0) {
                warnings += ++warningsCount + ". This rule will not make anything.\n";
            } else {
                warnings += ++warningsCount + ". This rule will be used only as delay, since the movement and rotations are not specified.\n";
            }
        }

        return warningsCount == 0 ? "" : warnings + "\nCreate the rule anyways?";
    }

    private void directionOnItemClickListener(AdapterView<?> adapterView, View view, int index, long id) {
        if (index == 0) {
            //forwardBackwardFragment.setDirection(true);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.directionTypeFragment, forwardBackwardFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.directionTypeFragment, turningFragment).commit();
        }
    }

    /**
     *
     * @param numberInputPopupDialog instance of NumberInputPopupDialog which will manage the measurement input.
     * @param currentTextView text view on which the measurement input will be displayed
     * @param correspondingTvExecTime text view which holds the execution time value
     * @param popupTitle title of the input popup dialog
     * @param stepMotorEntity motor on which the inputs will be applied
     */
    private void clickListener_measurementData(NumberInputPopupDialog numberInputPopupDialog, TextView currentTextView, TextView correspondingTvExecTime, String popupTitle, StepMotorEntity stepMotorEntity) {
        numberInputPopupDialog.setTitle(popupTitle);
        numberInputPopupDialog.setMinValue(0);
        numberInputPopupDialog.setMaxValue(360);
        numberInputPopupDialog.setValue(0);
        numberInputPopupDialog.setNegativeNumbers(true);
        numberInputPopupDialog.addNumberSelectedListener(value -> {
            try {
                int minExecTime = getMinimalExecutionTimeCelled(stepMotorEntity, value);
                stepMotorEntity.setData(value, minExecTime);

                Context context = currentTextView.getContext();
                currentTextView.setText(context.getString(R.string.degree, value));
                correspondingTvExecTime.setText(context.getString(R.string.execution_time, minExecTime));

            } catch (IncompatibleStepMotorArguments e) {
                e.printStackTrace();
            }
        });
        numberInputPopupDialog.show();
    }

    /**
     *
     * @param numberInputPopupDialog instance of NumberInputPopupDialog which will manage the measurement input.
     * @param currentTextView  text view on which the execution time inputted result will be displayed
     * @param popupTitle title of the input popup dialog
     * @param stepMotorEntity  motor on which the input will be applied
     */
    private void clickListener_executionTime(NumberInputPopupDialog numberInputPopupDialog, TextView currentTextView, String popupTitle, StepMotorEntity stepMotorEntity) {
        int minExecTimeCelled = getMinimalExecutionTimeCelled(stepMotorEntity, (int) stepMotorEntity.getMeasurementValue());

        numberInputPopupDialog.setTitle(popupTitle);
        numberInputPopupDialog.setMinValue(minExecTimeCelled);
        numberInputPopupDialog.setMaxValue(Short.MAX_VALUE);
        numberInputPopupDialog.setValue(minExecTimeCelled);
        numberInputPopupDialog.setNegativeNumbers(false);
        numberInputPopupDialog.addNumberSelectedListener(value -> {
            try {
                stepMotorEntity.setData(stepMotorEntity.getMeasurementValue(), value);

                Context context = currentTextView.getContext();
                currentTextView.setText(context.getString(R.string.execution_time, value));
            } catch (IncompatibleStepMotorArguments e) {
                e.printStackTrace();
            }
        });
        numberInputPopupDialog.show();
    }

    private int getMinimalExecutionTimeCelled(StepMotorEntity stepMotorEntity, int val) {
        double minExecTime = stepMotorEntity.getMinimalTimeRequired(Math.abs(val));
        return (int) Math.ceil(minExecTime);
    }
}