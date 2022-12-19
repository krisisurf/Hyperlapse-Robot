package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.R;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.ArduinoRobot;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.stepper.MovementStepMotorEntity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForwardBackwardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForwardBackwardFragment extends Fragment {

    private static final String TAG = "ForwardBackwardFragment";

    private static final String IS_FORWARD_PARAM = "isForForwardDirection";
    private static final String ARDUINO_ROBOT_PARAM = "arduinoRobot";

    private ArduinoRobot arduinoRobot;
    private boolean isForForward;

    private EditText editTextDistance;
    private EditText editTextExecutionTime;

    public ForwardBackwardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ForwardBackwardFragment.
     */
    public static ForwardBackwardFragment newInstance(boolean isForForwardDirection, ArduinoRobot arduinoRobot) {
        ForwardBackwardFragment fragment = new ForwardBackwardFragment();

        Bundle args = new Bundle();
        args.putBoolean(IS_FORWARD_PARAM, isForForwardDirection);
        args.putSerializable(ARDUINO_ROBOT_PARAM, arduinoRobot);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isForForward = getArguments().getBoolean(IS_FORWARD_PARAM);
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

        editTextDistance = getView().findViewById(R.id.etDistance);
        editTextDistance.addTextChangedListener(new TextWatcher() {
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
                    float distance = Float.parseFloat(stringValue);
                    MovementStepMotorEntity movementStepMotorEntity = new MovementStepMotorEntity(arduinoRobot.getWheelRadius());
                    double minimalTime = movementStepMotorEntity.getMinimalTimeRequired(distance);
                    editTextExecutionTime.setText(String.valueOf(minimalTime));
                } catch (NumberFormatException e) {
                    editTextExecutionTime.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editTextExecutionTime = getView().findViewById(R.id.etExecutionTime);
    }

    public float getDistance() {
        float distance = Float.parseFloat(editTextDistance.getText().toString());
        if (!isForForward)
            distance = -distance;

        return distance;
    }

    public float getExecutionTime() {
        return Float.parseFloat(editTextExecutionTime.getText().toString());
    }

    public void setDirection(boolean isForForwardDirection) {
        this.isForForward = isForForwardDirection;
    }

    public void setArduinoRobot(ArduinoRobot arduinoRobot) {
        this.arduinoRobot = arduinoRobot;
    }
}