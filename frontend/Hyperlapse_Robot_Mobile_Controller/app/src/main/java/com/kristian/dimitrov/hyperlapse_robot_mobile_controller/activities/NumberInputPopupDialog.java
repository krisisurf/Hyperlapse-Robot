package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.R;

import java.io.Serializable;

public class NumberInputPopupDialog implements Serializable {

    private static final ColorDrawable TRANSPARENCY;

    static {
        TRANSPARENCY = new ColorDrawable(Color.TRANSPARENT);
    }

    private final AlertDialog alertDialog;
    private final TextView tvLabelNumberPopup;

    private final NumberPicker numberSignPicker;
    private final NumberPicker numberPicker;

    // Faster Number Scroll
    private int fastAdd = 10;
    private int superFastAdd = 100;

    private NumberSelectedListener numberSelectedListener;

    public interface NumberSelectedListener {
        void onValueSelected(int value);
    }

    public NumberInputPopupDialog(@NonNull Context context, boolean hideAfterValueSelected) {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.getWindow().setBackgroundDrawable(TRANSPARENCY);
        alertDialog.setCancelable(true);

        @SuppressLint("InflateParams") final View view = alertDialog.getLayoutInflater().inflate(R.layout.number_input_popup, null);
        alertDialog.setView(view);

        tvLabelNumberPopup = view.findViewById(R.id.tvLabelNumberPopup);

        numberSignPicker = view.findViewById(R.id.signPicker);
        numberSignPicker.setDisplayedValues(new String[]{"-", "+"});
        numberSignPicker.setMaxValue(0);
        numberSignPicker.setMaxValue(1);
        numberSignPicker.setValue(1);

        numberPicker = view.findViewById(R.id.numberPicker);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(5);

        Button btnFastAdd = view.findViewById(R.id.btnFastAdd);
        btnFastAdd.setOnClickListener(view1 -> {
            int oldValue = numberPicker.getValue();
            int newValue = Math.min(oldValue + fastAdd, numberPicker.getMaxValue());
            numberPicker.setValue(newValue);
        });

        Button btnSuperFastAdd = view.findViewById(R.id.btnSuperFastAdd);
        btnSuperFastAdd.setOnClickListener(view1 -> {
            int oldValue = numberPicker.getValue();
            int newValue = Math.min(oldValue + superFastAdd, numberPicker.getMaxValue());
            numberPicker.setValue(newValue);
        });

        Button btnFastMinus = view.findViewById(R.id.btnFastMinus);
        btnFastMinus.setOnClickListener(view1 -> {
            int oldValue = numberPicker.getValue();
            int newValue = Math.max(oldValue - fastAdd, numberPicker.getMinValue());
            numberPicker.setValue(newValue);
        });

        Button btnSuperFastMinus = view.findViewById(R.id.btnSuperFastMinus);
        btnSuperFastMinus.setOnClickListener(view1 -> {
            int oldValue = numberPicker.getValue();
            int newValue = Math.max(oldValue - superFastAdd, numberPicker.getMinValue());
            numberPicker.setValue(newValue);
        });

        Button btnCancel = view.findViewById(R.id.btnCancle);
        btnCancel.setOnClickListener((v) -> alertDialog.cancel());

        Button btnApply = view.findViewById(R.id.btnApply);
        btnApply.setOnClickListener((view1 -> {
            if (numberSelectedListener != null)
                numberSelectedListener.onValueSelected(getValue());
            if (hideAfterValueSelected)
                hide();
        }));
    }

    public void setTitle(int titleId) {
        tvLabelNumberPopup.setText(titleId);
    }

    public void setTitle(String string) {
        tvLabelNumberPopup.setText(string);
    }

    public void setMinValue(int minValue) {
        numberPicker.setMinValue(minValue);
    }

    public void setMaxValue(int maxValue) {
        numberPicker.setMaxValue(maxValue);
    }

    public void setNegativeNumbers(boolean negativeNumbers) {
        if (negativeNumbers) {
            numberSignPicker.setVisibility(View.VISIBLE);
        } else {
            numberSignPicker.setVisibility(View.GONE);
        }
    }

    public int getValue() {
        if(numberSignPicker.getVisibility() == View.GONE)
            return numberPicker.getValue();

        return numberPicker.getValue() * (numberSignPicker.getValue() == 0 ? -1 : 1);
    }

    public void show() {
        alertDialog.show();
    }

    public void hide() {
        alertDialog.hide();
    }

    public void addNumberSelectedListener(NumberSelectedListener numberSelectedListener) {
        this.numberSelectedListener = numberSelectedListener;
    }

    public void setValue(int value) {
        numberPicker.setValue(value);
    }
}
