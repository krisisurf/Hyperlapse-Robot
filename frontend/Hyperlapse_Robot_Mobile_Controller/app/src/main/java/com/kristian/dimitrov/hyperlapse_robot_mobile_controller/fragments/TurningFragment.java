package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.R;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.activities.NumberInputPopupDialog;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.RuleEntity;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class TurningFragment extends Fragment {

    private static final String TAG = "TurningFragment";

    public static final String NUMBER_INPUT_POPUP_DIALOG_PARAM = "nipd";
    public static final String RULE_ENTITY_PARAM = "re";

    private NumberInputPopupDialog numberInputPopupDialog;
    private RuleEntity ruleEntity;

    public TurningFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TurningFragment.
     */
    public static TurningFragment newInstance(NumberInputPopupDialog numberInputPopupDialog, RuleEntity ruleEntity) {
        TurningFragment fragment = new TurningFragment();

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
        return inflater.inflate(R.layout.fragment_turning, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}