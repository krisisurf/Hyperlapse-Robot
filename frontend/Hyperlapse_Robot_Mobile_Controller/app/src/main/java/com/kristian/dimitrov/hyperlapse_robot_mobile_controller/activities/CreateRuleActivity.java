package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.R;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.ArduinoRobot;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.RuleEntity;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.TurnEntity;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.builders.RuleEntityBuilder;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.stepper.StepMotorEntity;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.exception.IncompatibleStepMotorArguments;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.fragments.ForwardBackwardFragment;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.fragments.TurningFragment;

public class CreateRuleActivity extends AppCompatActivity {

    public static final String ARDUINO_ROBOT_CODE = "ar";
    public static final String RULE_ENTITY_CODE = "re";
    public static final String RULE_ENTITY_INDEX_CODE = "reind";

    public static final int RESULT_CREATED_OK = 2000;
    public static final int RESULT_EDITED_OK = 2001;


    private boolean isEditingExistingRule;
    private RuleEntity ruleEntity;

    private final String[] directionStringOptions = {"Straight", "Turning"};

    private ArduinoRobot arduinoRobot;
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

        arduinoRobot = (ArduinoRobot) getIntent().getSerializableExtra(ARDUINO_ROBOT_CODE);
        assert arduinoRobot != null : "arduinoRobot extra is null";

        int indexOfRuleEntity = getIntent().getIntExtra(RULE_ENTITY_INDEX_CODE, -1);

        // Getting rule number
        int ruleNumber;
        if (indexOfRuleEntity == -1) {
            ruleEntity = new RuleEntityBuilder(arduinoRobot).build();
            ruleNumber = arduinoRobot.getRulesManagerEntity().size() + 1;
            isEditingExistingRule = false;
        } else {
            try {
                ruleEntity = arduinoRobot.getRulesManagerEntity().getRules().get(indexOfRuleEntity);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            ruleNumber = indexOfRuleEntity + 1;
            isEditingExistingRule = true;
        }
        // end getting rule number

        TextView textView_ruleNumber = findViewById(R.id.textView_ruleNumber);
        textView_ruleNumber.setText(getString(R.string.label_rule_number, String.valueOf(ruleNumber)));

        AutoCompleteTextView directionAutoComplete = findViewById(R.id.auto_complete_txt);
        directionAutoComplete.setAdapter(new ArrayAdapter<>(this, R.layout.text_option_item, directionStringOptions));
        directionAutoComplete.setOnItemClickListener((adapterView, view, index, l) -> {
            if (index == 0)
                setDirectionTypeFragmentView(true);
            else if (index == 1)
                setDirectionTypeFragmentView(false);
        });

        numberInputPopupDialog = new NumberInputPopupDialog(CreateRuleActivity.this, true);

        forwardBackwardFragment = ForwardBackwardFragment.newInstance(numberInputPopupDialog, ruleEntity);
        turningFragment = TurningFragment.newInstance(numberInputPopupDialog, ruleEntity, arduinoRobot);


        btnPanDegree = findViewById(R.id.btnPanDegree);
        btnPanDegree.setOnClickListener(view -> {
            String popupTitle = getString(R.string.label_pan) + " " + getString(R.string.label_degree);
            clickListener_measurementData(numberInputPopupDialog, btnPanDegree, btnPanExecutionTime, popupTitle, ruleEntity.getPanMotor());
        });

        btnPanExecutionTime = findViewById(R.id.btnPanExecutionTime);
        btnPanExecutionTime.setOnClickListener(view -> {
            String popupTitle = getString(R.string.label_pan) + " " + getString(R.string.label_execution_time);
            clickListener_executionTime(numberInputPopupDialog, btnPanExecutionTime, popupTitle, ruleEntity.getPanMotor());
        });

        btnTiltDegree = findViewById(R.id.btnTiltDegree);
        btnTiltDegree.setOnClickListener(view -> {
            String popupTitle = getString(R.string.label_tilt) + " " + getString(R.string.label_degree);
            clickListener_measurementData(numberInputPopupDialog, btnTiltDegree, btnTiltExecutionTime, popupTitle, ruleEntity.getTiltMotor());
        });

        btnTiltExecutionTime = findViewById(R.id.btnTiltExecutionTime);
        btnTiltExecutionTime.setOnClickListener((view -> {
            String popupTitle = getString(R.string.label_tilt) + " " + getString(R.string.label_execution_time);
            clickListener_executionTime(numberInputPopupDialog, btnTiltExecutionTime, popupTitle, ruleEntity.getTiltMotor());
        }));

        Button buttonCancle = findViewById(R.id.btn_cancle);
        buttonCancle.setOnClickListener(this::cancleButton);

        Button buttonApply = findViewById(R.id.btn_apply);
        buttonApply.setOnClickListener(this::applyButton);

        setupDefaultValues();
    }

    private void setupDefaultValues() {
        if (ruleEntity.getLeftMotor().equalsByMeasurementValueAndExecutionTime(ruleEntity.getRightMotor())) {
            if (ruleEntity.getLeftMotor().getExecutionTime() > 0) {
                setDirectionTypeFragmentView(true);
            }
        } else {
            setDirectionTypeFragmentView(false);
        }

        btnPanDegree.setText(getString(R.string.degree, (int) ruleEntity.getPanMotor().getMeasurementValue()));
        btnPanExecutionTime.setText(getString(R.string.execution_time, (int) ruleEntity.getPanMotor().getExecutionTime()));

        btnTiltDegree.setText(getString(R.string.degree, (int) ruleEntity.getTiltMotor().getMeasurementValue()));
        btnTiltExecutionTime.setText(getString(R.string.execution_time, (int) ruleEntity.getTiltMotor().getExecutionTime()));
    }

    /**
     * Sets the direction fragment view.
     *
     * @param isForwardBackwardDirection 'true' if the selected direction is for forwardBackwardFragment,
     *                                   otherwise 'false' if the selected direction is turningFragment.
     */
    private void setDirectionTypeFragmentView(boolean isForwardBackwardDirection) {
        if (isForwardBackwardDirection) {
            if (turningFragment.isVisible()) {
                try {
                    ruleEntity.getLeftMotor().setData(0, 0);
                    ruleEntity.getRightMotor().setData(0, 0);
                } catch (IncompatibleStepMotorArguments e) {
                    e.printStackTrace();
                }
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.directionTypeFragment, forwardBackwardFragment).commit();
        } else {
            if (forwardBackwardFragment.isVisible()) {
                if (ruleEntity.getTurnEntity() == null) {
                    ruleEntity.setTurnEntity(new TurnEntity(arduinoRobot));
                }
                ruleEntity.getTurnEntity().setTurnRadius(0);
                ruleEntity.getTurnEntity().setTurnAngle(0);
                try {
                    ruleEntity.getTurnEntity().setExecutionTime(0);
                } catch (IncompatibleStepMotorArguments e) {
                    e.printStackTrace();
                }
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.directionTypeFragment, turningFragment).commit();
        }
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
        if (isEditingExistingRule) {
            int indexOfRuleEntity = getIntent().getIntExtra(RULE_ENTITY_INDEX_CODE, -1);
            intent.putExtra(RULE_ENTITY_INDEX_CODE, indexOfRuleEntity);
            intent.putExtra(RULE_ENTITY_CODE, ruleEntity);
            setResult(RESULT_EDITED_OK, intent);
        } else {
            intent.putExtra(CreateRuleActivity.RULE_ENTITY_CODE, ruleEntity);
            setResult(RESULT_CREATED_OK, intent);
        }

        finish();
    }

    private String validateInputs() {
        String warnings = "";
        int warningsCount = 0;

        if (ruleEntity.getLeftMotor().getMeasurementValue() == 0 && ruleEntity.getRightMotor().getMeasurementValue() == 0
                && ruleEntity.getPanMotor().getMeasurementValue() == 0 && ruleEntity.getTiltMotor().getMeasurementValue() == 0
        ) {
            double totalExecutionTime = ruleEntity.getLeftMotor().getExecutionTime() + ruleEntity.getRightMotor().getExecutionTime()
                    + ruleEntity.getPanMotor().getExecutionTime() + ruleEntity.getTiltMotor().getExecutionTime();
            if (totalExecutionTime == 0) {
                warnings += ++warningsCount + ". This rule will not make anything.\n";
            } else {
                warnings += ++warningsCount + ". This rule will be used only as delay, since the movement and rotations are not specified.\n";
            }
        }

        return warningsCount == 0 ? "" : warnings + "\nCreate the rule anyways?";
    }

    /**
     * @param numberInputPopupDialog  instance of NumberInputPopupDialog which will manage the measurement input.
     * @param currentTextView         text view on which the measurement input will be displayed
     * @param correspondingTvExecTime text view which holds the execution time value
     * @param popupTitle              title of the input popup dialog
     * @param stepMotorEntity         motor on which the inputs will be applied
     */
    private void clickListener_measurementData(NumberInputPopupDialog numberInputPopupDialog, TextView currentTextView, TextView correspondingTvExecTime, String popupTitle, StepMotorEntity stepMotorEntity) {
        numberInputPopupDialog.setTitle(popupTitle);
        numberInputPopupDialog.setMinValue(0);
        numberInputPopupDialog.setMaxValue(360);
        numberInputPopupDialog.setValue(Math.abs((int) stepMotorEntity.getMeasurementValue()));
        numberInputPopupDialog.setNegativeNumbers(true);
        numberInputPopupDialog.addNumberSelectedListener(value -> {
            try {
                int selectedExecutionTime = Math.max(StepMotorEntity.getMinimalExecutionTimeCelled(stepMotorEntity, value), (int) stepMotorEntity.getExecutionTime());
                stepMotorEntity.setData(value, selectedExecutionTime);

                Context context = currentTextView.getContext();
                correspondingTvExecTime.setText(context.getString(R.string.execution_time, selectedExecutionTime));
                currentTextView.setText(context.getString(R.string.degree, value));
            } catch (IncompatibleStepMotorArguments e) {
                e.printStackTrace();
            }
        });
        numberInputPopupDialog.show();
    }

    /**
     * @param numberInputPopupDialog instance of NumberInputPopupDialog which will manage the measurement input.
     * @param currentTextView        text view on which the execution time inputted result will be displayed
     * @param popupTitle             title of the input popup dialog
     * @param stepMotorEntity        motor on which the input will be applied
     */
    private void clickListener_executionTime(NumberInputPopupDialog numberInputPopupDialog, TextView currentTextView, String popupTitle, StepMotorEntity stepMotorEntity) {
        int minExecTimeCelled = StepMotorEntity.getMinimalExecutionTimeCelled(stepMotorEntity, (int) stepMotorEntity.getMeasurementValue());

        numberInputPopupDialog.setTitle(popupTitle);
        numberInputPopupDialog.setMinValue(minExecTimeCelled);
        numberInputPopupDialog.setMaxValue(Short.MAX_VALUE);
        numberInputPopupDialog.setValue(Math.max(minExecTimeCelled, (int) stepMotorEntity.getExecutionTime()));
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
}