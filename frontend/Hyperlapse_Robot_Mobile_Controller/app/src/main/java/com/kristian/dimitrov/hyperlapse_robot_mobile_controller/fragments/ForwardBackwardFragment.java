package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.R;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.activities.CreateRuleActivity;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.activities.NumberInputPopupDialog;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.ArduinoRobot;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.builders.RuleEntityBuilder;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.stepper.MovementStepMotorEntity;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.exception.IncompatibleStepMotorArguments;

import java.io.Serializable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForwardBackwardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForwardBackwardFragment extends Fragment {

    private static final String TAG = "ForwardBackwardFragment";

    public static final String NUMBER_INPUT_POPUP_DIALOG_PARAM = "numberInputPopupDialog";
    public static final String RULE_ENTITY_BUILDER_PARAM = "ruleEntityBuilder";

    private NumberInputPopupDialog numberInputPopupDialog;
    private RuleEntityBuilder ruleEntityBuilder;
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
    public static ForwardBackwardFragment newInstance(NumberInputPopupDialog numberInputPopupDialog, RuleEntityBuilder ruleEntityBuilder) {
        ForwardBackwardFragment fragment = new ForwardBackwardFragment();

        Bundle args = new Bundle();
        args.putSerializable(NUMBER_INPUT_POPUP_DIALOG_PARAM, numberInputPopupDialog);
        args.putSerializable(RULE_ENTITY_BUILDER_PARAM, ruleEntityBuilder);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            numberInputPopupDialog = (NumberInputPopupDialog) getArguments().getSerializable(NUMBER_INPUT_POPUP_DIALOG_PARAM);
            ruleEntityBuilder = (RuleEntityBuilder) getArguments().getSerializable(RULE_ENTITY_BUILDER_PARAM);
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

        btnDistance = getView().findViewById(R.id.btnDistance);
        btnDistance.setOnClickListener(view1 -> {
            String popupTitle = getString(R.string.label_movement) + " " + getString(R.string.distance);
            CreateRuleActivity.NumberPopupDialogFiller_listenerCreator
                    .clickListener_measurementData(numberInputPopupDialog, btnDistance, btnExecutionTime, popupTitle, ruleEntityBuilder.getLeftMotor());
        });

        btnExecutionTime = getView().findViewById(R.id.btnExecutionTime);
    }

    public float getExecutionTime() {
        return Float.parseFloat(btnExecutionTime.getText().toString());
    }
}