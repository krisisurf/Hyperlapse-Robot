package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.R;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.ArduinoRobot;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.RuleEntity;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.builders.RuleEntityBuilder;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.stepper.CameraStepMotorEntity;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.stepper.MovementStepMotorEntity;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.exception.IncompatibleStepMotorArguments;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.fragments.ForwardBackwardFragment;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.fragments.TurningFragment;

public class CreateRuleActivity extends AppCompatActivity {

    private static final String TAG = "CreateRuleActivity";

    public static final String RULE_ENTITY_CODE = "ruleEntity";

    private ArduinoRobot arduinoRobot;
    private RuleEntityBuilder ruleEntityBuilder;

    private final String[] directionStringOptions = {"Forward", "Turning"};
    private int selectedDirectionIndex = -1;

    private ForwardBackwardFragment forwardBackwardFragment;
    private TurningFragment turningFragment;

    private EditText editTextPanDegree;
    private EditText editTextPanExecutionTime;
    private EditText editTextTiltDegree;
    private EditText editTextTiltExecutionTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_rule);
        setTitle(R.string.title_create_rule_activity);

        Button buttonCancle = findViewById(R.id.btn_cancle);
        buttonCancle.setOnClickListener(this::cancleButton);

        Button buttonApply = findViewById(R.id.btn_apply);
        buttonApply.setOnClickListener(this::applyButton);

        arduinoRobot = (ArduinoRobot) getIntent().getSerializableExtra("arduinoRobot");
        assert arduinoRobot != null : buttonCancle.performClick();
        ruleEntityBuilder = new RuleEntityBuilder(arduinoRobot);

        AutoCompleteTextView directionAutoComplete = findViewById(R.id.auto_complete_txt);
        directionAutoComplete.setAdapter(new ArrayAdapter<>(this, R.layout.list_item, directionStringOptions));
        directionAutoComplete.setOnItemClickListener(this::directionOnItemClickListener);

        forwardBackwardFragment = ForwardBackwardFragment.newInstance(true, arduinoRobot);
        turningFragment = new TurningFragment();

        editTextPanExecutionTime = findViewById(R.id.editTextPanExecutionTime);
        editTextPanDegree = findViewById(R.id.editTextPanDegree);
        editTextPanDegree.addTextChangedListener(cameraTextWatcher(editTextPanExecutionTime));

        editTextTiltExecutionTime = findViewById(R.id.editTextTiltExecutionTime);
        editTextTiltDegree = findViewById(R.id.editTextTiltDegree);
        editTextTiltDegree.addTextChangedListener(cameraTextWatcher(editTextTiltExecutionTime));

        TextView textView_ruleNumber = findViewById(R.id.textView_ruleNumber);
        textView_ruleNumber.setText(getString(R.string.label_rule_number, String.valueOf(arduinoRobot.getRulesManagerEntity().size() + 1)));
    }

    private void cancleButton(View view) {
        finish();
    }

    private void applyButton(View view) {
        String errorMessage = validateInputs();
        if (errorMessage.isEmpty()) {
            Intent intent = getIntent();
            intent.putExtra(CreateRuleActivity.RULE_ENTITY_CODE, ruleEntityBuilder.build());
            setResult(Activity.RESULT_OK, intent);
            finish();
            return;
        }

        new AlertDialog.Builder(CreateRuleActivity.this)
                .setTitle("Input errors appeared! Please fix:")
                .setMessage(errorMessage)
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                }).create().show();
    }

    private String validateInputs() {
        StringBuilder errorBuilder = new StringBuilder();
        int errorsCount = 0;
        if (selectedDirectionIndex == -1) {
            errorBuilder.append(++errorsCount).append(". Not selected ").append(getString(R.string.direction_type)).append(".\n");
        } else {
            if (selectedDirectionIndex == 0) {
                float distance = forwardBackwardFragment.getDistance();
                float executionTime = forwardBackwardFragment.getExecutionTime();
                try {
                    ruleEntityBuilder.setLeftMotor(distance, executionTime);
                    ruleEntityBuilder.setRightMotor(distance, executionTime);
                } catch (IncompatibleStepMotorArguments incompatibleStepMotorArguments) {
                    errorBuilder.append(++errorsCount).append(". ").append(incompatibleStepMotorArguments.getMessage()).append("\n");
                }
            } else {
                // TODO: Finish the validation for turning direction type
            }
        }

        float panDegree = Float.parseFloat(editTextPanDegree.getText().toString());
        float panExecutionTime = Float.parseFloat(editTextPanExecutionTime.getText().toString());
        float tiltDegree = Float.parseFloat(editTextTiltDegree.getText().toString());
        float tiltExecutionTime = Float.parseFloat(editTextTiltExecutionTime.getText().toString());

        try {
            ruleEntityBuilder.setPanMotor(panDegree, panExecutionTime);
        } catch (IncompatibleStepMotorArguments incompatibleStepMotorArguments) {
            errorBuilder.append(++errorsCount).append(". ").append(incompatibleStepMotorArguments.getMessage()).append("\n");
        }
        try {
            ruleEntityBuilder.setTiltMotor(tiltDegree, tiltExecutionTime);
        } catch (IncompatibleStepMotorArguments incompatibleStepMotorArguments) {
            errorBuilder.append(++errorsCount).append(". ").append(incompatibleStepMotorArguments.getMessage()).append("\n");
        }

        return errorBuilder.toString();
    }

    private void directionOnItemClickListener(AdapterView<?> adapterView, View view, int index, long id) {
        selectedDirectionIndex = index;
        if (index == 0) {
            forwardBackwardFragment.setDirection(true);
            forwardBackwardFragment.setArduinoRobot(arduinoRobot);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.directionTypeFragment, forwardBackwardFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.directionTypeFragment, turningFragment).commit();
        }
    }

    private TextWatcher cameraTextWatcher(EditText editTextExecutionTime) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String stringValue = String.valueOf(s);
                if (stringValue.isEmpty()) {
                    editTextExecutionTime.setText("0");
                    return;
                }

                try {
                    float degree = Float.parseFloat(stringValue);
                    CameraStepMotorEntity movementStepMotorEntity = new CameraStepMotorEntity();
                    double minimalTime = movementStepMotorEntity.getMinimalTimeRequired(degree);
                    editTextExecutionTime.setText(String.valueOf(minimalTime));
                } catch (NumberFormatException e) {
                    editTextExecutionTime.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }
}