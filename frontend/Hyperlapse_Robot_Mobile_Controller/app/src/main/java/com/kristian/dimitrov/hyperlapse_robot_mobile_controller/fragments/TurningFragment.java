package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.R;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.activities.NumberInputPopupDialog;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.ArduinoRobot;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.RuleEntity;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.stepper.StepMotorEntity;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.exception.IncompatibleStepMotorArguments;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class TurningFragment extends Fragment {

    private static final String TAG = "TurningFragment";

    public static final String NUMBER_INPUT_POPUP_DIALOG_PARAM = "nipd";
    public static final String RULE_ENTITY_PARAM = "re";
    public static final String ARDUINO_ROBOT_PARAM = "ar";

    private NumberInputPopupDialog numberInputPopupDialog;
    private ArduinoRobot arduinoRobot;
    private RuleEntity ruleEntity;
    private Button btnTurnAngle;
    private Button btnTurnRadius;
    private Button btnExecutionTime;

    private int turnAngle;
    private int turnRadius; // in centimeters

    public TurningFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TurningFragment.
     */
    public static TurningFragment newInstance(NumberInputPopupDialog numberInputPopupDialog, RuleEntity ruleEntity, ArduinoRobot arduinoRobot) {
        TurningFragment fragment = new TurningFragment();

        Bundle args = new Bundle();
        args.putSerializable(NUMBER_INPUT_POPUP_DIALOG_PARAM, numberInputPopupDialog);
        args.putSerializable(RULE_ENTITY_PARAM, ruleEntity);
        args.putSerializable(ARDUINO_ROBOT_PARAM, arduinoRobot);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            numberInputPopupDialog = (NumberInputPopupDialog) getArguments().getSerializable(NUMBER_INPUT_POPUP_DIALOG_PARAM);
            ruleEntity = (RuleEntity) getArguments().getSerializable(RULE_ENTITY_PARAM);
            arduinoRobot = (ArduinoRobot) getArguments().getSerializable(ARDUINO_ROBOT_PARAM);
            turnAngle = 0;
            turnRadius = 0;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_turning, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnTurnAngle = view.findViewById(R.id.btnTurnAngle);
        btnTurnAngle.setOnClickListener(this::buttonTurnAngleListener);

        btnTurnRadius = view.findViewById(R.id.btnTurnRadius);
        btnTurnRadius.setOnClickListener(this::buttonTurnRadiusListener);

        btnExecutionTime = requireView().findViewById(R.id.btnExecutionTime);
        btnExecutionTime.setText(getString(R.string.execution_time, (int) ruleEntity.getLeftMotor().getExecutionTime()));
        btnExecutionTime.setOnClickListener(this::buttonExecutionTimeListener);
    }

    private void buttonTurnRadiusListener(View view) {
        numberInputPopupDialog.setTitle(R.string.turn_radius);
        numberInputPopupDialog.setMinValue(0);
        numberInputPopupDialog.setMaxValue(Short.MAX_VALUE);
        numberInputPopupDialog.setValue(0);
        numberInputPopupDialog.setNegativeNumbers(true);
        numberInputPopupDialog.addNumberSelectedListener(turnRadius -> {
            this.turnRadius = turnRadius;
            double leftMotorDistance = calculateWheelTravelDistance(turnAngle, turnRadius, arduinoRobot.getAxleTrack(), true);
            double rightMotorDistance = calculateWheelTravelDistance(turnAngle, turnRadius, arduinoRobot.getAxleTrack(), false);

            try {
                int selectedExecutionTime;
                if (Math.abs(leftMotorDistance) > Math.abs(rightMotorDistance)) {
                    StepMotorEntity shorterPathMotor = ruleEntity.getRightMotor();
                    StepMotorEntity longerPathMotor = ruleEntity.getLeftMotor();
                    selectedExecutionTime = Math.max(StepMotorEntity.getMinimalExecutionTimeCelled(longerPathMotor, leftMotorDistance), (int) longerPathMotor.getExecutionTime());

                    shorterPathMotor.setData(rightMotorDistance, selectedExecutionTime);
                    longerPathMotor.setData(leftMotorDistance, selectedExecutionTime);
                } else {
                    StepMotorEntity shorterPathMotor = ruleEntity.getLeftMotor();
                    StepMotorEntity longerPathMotor = ruleEntity.getRightMotor();
                    selectedExecutionTime = Math.max(StepMotorEntity.getMinimalExecutionTimeCelled(longerPathMotor, leftMotorDistance), (int) longerPathMotor.getExecutionTime());

                    shorterPathMotor.setData(leftMotorDistance, selectedExecutionTime);
                    longerPathMotor.setData(rightMotorDistance, selectedExecutionTime);
                }

                btnTurnRadius.setText(view.getContext().getString(R.string.centimeters, turnRadius));
                btnExecutionTime.setText(view.getContext().getString(R.string.execution_time, selectedExecutionTime));
            } catch (IncompatibleStepMotorArguments e) {
                e.printStackTrace();
            }
        });
        numberInputPopupDialog.show();
    }

    private void buttonTurnAngleListener(View view) {
        numberInputPopupDialog.setTitle(R.string.turn_angle);
        numberInputPopupDialog.setMinValue(0);
        numberInputPopupDialog.setMaxValue(Short.MAX_VALUE);
        numberInputPopupDialog.setValue(0);
        numberInputPopupDialog.setNegativeNumbers(true);
        numberInputPopupDialog.addNumberSelectedListener(turnAngle -> {
            this.turnAngle = turnAngle;
            double leftMotorDistance = calculateWheelTravelDistance(turnAngle, turnRadius, arduinoRobot.getAxleTrack(), true);
            double rightMotorDistance = calculateWheelTravelDistance(turnAngle, turnRadius, arduinoRobot.getAxleTrack(), false);

            try {
                int selectedExecutionTime;
                if (Math.abs(leftMotorDistance) > Math.abs(rightMotorDistance)) {
                    StepMotorEntity shorterPathMotor = ruleEntity.getRightMotor();
                    StepMotorEntity longerPathMotor = ruleEntity.getLeftMotor();
                    selectedExecutionTime = Math.max(StepMotorEntity.getMinimalExecutionTimeCelled(longerPathMotor, leftMotorDistance), (int) longerPathMotor.getExecutionTime());

                    shorterPathMotor.setData(rightMotorDistance, selectedExecutionTime);
                    longerPathMotor.setData(leftMotorDistance, selectedExecutionTime);
                } else {
                    StepMotorEntity shorterPathMotor = ruleEntity.getLeftMotor();
                    StepMotorEntity longerPathMotor = ruleEntity.getRightMotor();
                    selectedExecutionTime = Math.max(StepMotorEntity.getMinimalExecutionTimeCelled(longerPathMotor, leftMotorDistance), (int) longerPathMotor.getExecutionTime());

                    shorterPathMotor.setData(leftMotorDistance, selectedExecutionTime);
                    longerPathMotor.setData(rightMotorDistance, selectedExecutionTime);
                }

                btnTurnAngle.setText(view.getContext().getString(R.string.degree, turnAngle));
                btnExecutionTime.setText(view.getContext().getString(R.string.execution_time, selectedExecutionTime));
            } catch (IncompatibleStepMotorArguments e) {
                e.printStackTrace();
            }
        });
        numberInputPopupDialog.show();
    }

    private void buttonExecutionTimeListener(View view) {
        StepMotorEntity longerPathMotor = (ruleEntity.getLeftMotor().getMeasurementValue() > ruleEntity.getRightMotor().getMeasurementValue()) ?
                ruleEntity.getLeftMotor() : ruleEntity.getRightMotor();
        int minExecTimeCelled = StepMotorEntity.getMinimalExecutionTimeCelled(longerPathMotor, (int) longerPathMotor.getMeasurementValue());

        numberInputPopupDialog.setTitle("Turning " + getString(R.string.label_execution_time));
        numberInputPopupDialog.setMinValue(minExecTimeCelled);
        numberInputPopupDialog.setMaxValue(Short.MAX_VALUE);
        numberInputPopupDialog.setValue(Math.max(minExecTimeCelled, Math.abs((int) longerPathMotor.getExecutionTime())));
        numberInputPopupDialog.setNegativeNumbers(false);
        numberInputPopupDialog.addNumberSelectedListener(value -> {
            try {
                ruleEntity.getLeftMotor().setData(ruleEntity.getLeftMotor().getMeasurementValue(), value);
                ruleEntity.getRightMotor().setData(ruleEntity.getRightMotor().getMeasurementValue(), value);

                btnExecutionTime.setText(getString(R.string.execution_time, value));
            } catch (IncompatibleStepMotorArguments e) {
                e.printStackTrace();
            }
        });
        numberInputPopupDialog.show();
    }

    private double calculateWheelTravelDistance(double turnAngle, double turnRadius, double axleTrack, boolean isLeftSide) {
        turnAngle = (isLeftSide) ? turnAngle : -turnAngle;
        double motorPathRadius = turnRadius + (Math.signum(turnAngle) * axleTrack / 2);

        return Math.toRadians(Math.abs(turnAngle)) * motorPathRadius;
    }
}