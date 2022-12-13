package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForwardBackwardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForwardBackwardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String IS_FORWARD_PARAM = "isForForwardDirection";

    // TODO: Rename and change types of parameters
    private boolean isForForward;

    public ForwardBackwardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ForwardBackwardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForwardBackwardFragment newInstance(boolean isForForwardDirection) {
        ForwardBackwardFragment fragment = new ForwardBackwardFragment();
        Bundle args = new Bundle();
        args.putBoolean(IS_FORWARD_PARAM, isForForwardDirection);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            isForForward = getArguments().getBoolean(IS_FORWARD_PARAM);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forward_backward, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void setDirection(boolean isForForwardDirection) {
        this.isForForward = isForForwardDirection;
    }
}