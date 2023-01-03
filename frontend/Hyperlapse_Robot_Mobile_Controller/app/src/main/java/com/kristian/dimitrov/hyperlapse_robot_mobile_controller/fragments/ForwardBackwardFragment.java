package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.R;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.activities.NumberInputPopupDialog;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.RuleEntity;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.stepper.StepMotorEntity;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.exception.IncompatibleStepMotorArguments;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForwardBackwardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForwardBackwardFragment extends Fragment {

    private static final String TAG = "ForwardBackwardFragment";

    public static final String NUMBER_INPUT_POPUP_DIALOG_PARAM = "nipd";
    public static final String RULE_ENTITY_PARAM = "re";

    private NumberInputPopupDialog numberInputPopupDialog;
    private RuleEntity ruleEntity;
    private Button btnDistance;
    private Button btnExecutionTime;

    public ForwardBackwardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ForwardBackwardFragment.
     */
    public static ForwardBackwardFragment newInstance(NumberInputPopupDialog numberInputPopupDialog, RuleEntity ruleEntity) {
        ForwardBackwardFragment fragment = new ForwardBackwardFragment();

        Bundle args = new Bundle();
        args.putSerializable(NUMBER_INPUT_POPUP_DIALOG_PARAM, numberInputPopupDialog);
        args.putSerializable(RULE_ENTITY_PARAM, ruleEntity);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            numberInputPopupDialog = (NumberInputPopupDialog) getArguments().getSerializable(NUMBER_INPUT_POPUP_DIALOG_PARAM);
            ruleEntity = (RuleEntity) getArguments().getSerializable(RULE_ENTITY_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forward_backward, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnDistance = requireView().findViewById(R.id.btnDistance);
        btnDistance.setOnClickListener(view1 -> {
            String popupTitle = getString(R.string.distance);
            clickListener_measurementData(numberInputPopupDialog, btnDistance, btnExecutionTime, popupTitle, ruleEntity.getLeftMotor(), ruleEntity.getRightMotor());
        });

        btnExecutionTime = requireView().findViewById(R.id.btnExecutionTime);
        btnExecutionTime.setOnClickListener(view1 -> {
            String popupTitle = "Movement" + getString(R.string.label_execution_time);
            clickListener_executionTime(numberInputPopupDialog, btnExecutionTime, popupTitle, ruleEntity.getLeftMotor(), ruleEntity.getRightMotor());
        });
    }

    /**
     * @param numberInputPopupDialog  instance of NumberInputPopupDialog which will manage the measurement input.
     * @param currentTextView         text view on which the measurement input will be displayed
     * @param correspondingTvExecTime text view which holds the execution time value
     * @param popupTitle              title of the input popup dialog
     * @param leftSideMotor           motor on which the inputs will be applied
     * @param rightSideMotor          motor on which the inputs will be applied
     */
    private void clickListener_measurementData(NumberInputPopupDialog numberInputPopupDialog,
                                               TextView currentTextView, TextView correspondingTvExecTime, String popupTitle,
                                               StepMotorEntity leftSideMotor, StepMotorEntity rightSideMotor) {
        numberInputPopupDialog.setTitle(popupTitle);
        numberInputPopupDialog.setMinValue(0);
        numberInputPopupDialog.setMaxValue(Short.MAX_VALUE);
        numberInputPopupDialog.setValue(0);
        numberInputPopupDialog.setNegativeNumbers(true);
        numberInputPopupDialog.addNumberSelectedListener(value -> {
            try {
                int minExecTime = getMinimalExecutionTimeCelled(leftSideMotor, value);
                leftSideMotor.setData(value, minExecTime);
                rightSideMotor.setData(value, minExecTime);

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
     * @param numberInputPopupDialog instance of NumberInputPopupDialog which will manage the measurement input.
     * @param currentTextView        text view on which the execution time inputted result will be displayed
     * @param popupTitle             title of the input popup dialog
     * @param leftSideMotor          motor on which the inputs will be applied
     * @param rightSideMotor         motor on which the inputs will be applied
     */
    private void clickListener_executionTime(NumberInputPopupDialog numberInputPopupDialog,
                                             TextView currentTextView, String popupTitle,
                                             StepMotorEntity leftSideMotor, StepMotorEntity rightSideMotor) {
        int minExecTimeCelled = getMinimalExecutionTimeCelled(leftSideMotor, (int) leftSideMotor.getMeasurementValue());

        numberInputPopupDialog.setTitle(popupTitle);
        numberInputPopupDialog.setMinValue(minExecTimeCelled);
        numberInputPopupDialog.setMaxValue(Short.MAX_VALUE);
        numberInputPopupDialog.setValue(minExecTimeCelled);
        numberInputPopupDialog.setNegativeNumbers(false);
        numberInputPopupDialog.addNumberSelectedListener(value -> {
            try {
                leftSideMotor.setData(leftSideMotor.getMeasurementValue(), value);
                rightSideMotor.setData(rightSideMotor.getMeasurementValue(), value);

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