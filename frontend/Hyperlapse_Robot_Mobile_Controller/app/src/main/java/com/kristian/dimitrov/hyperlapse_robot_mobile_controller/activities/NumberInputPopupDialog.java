package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.R;

public class NumberInputPopupDialog {

    private static final ColorDrawable TRANSPARENCY;

    static {
        TRANSPARENCY = new ColorDrawable(Color.TRANSPARENT);
    }

    private final AlertDialog alertDialog;
    private final TextView tvLabelNumberPopup;

    private final NumberPicker numberSignPicker;
    private final NumberPicker numberPicker;

    private NumberSelectedListener numberSelectedListener;

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

    public interface NumberSelectedListener {
        void onValueSelected(int value);
    }
}
