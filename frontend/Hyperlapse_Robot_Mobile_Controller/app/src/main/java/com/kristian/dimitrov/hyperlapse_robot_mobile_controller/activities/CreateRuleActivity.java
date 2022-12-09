package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.R;

public class CreateRuleActivity extends AppCompatActivity {

    private static final String TAG = "CreateRuleActivity";

    private final String[] directionStringOptions = {"Forward", "Backward", "Turning"};
    private AutoCompleteTextView directionAutoComplete;
    private ArrayAdapter<String> directionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_rule);
        setTitle(R.string.title_create_rule_activity);

        Button buttonCancle = findViewById(R.id.btn_cancle);
        buttonCancle.setOnClickListener(this::cancleButton);

        Button buttonApply = findViewById(R.id.btn_apply);
        buttonApply.setOnClickListener(this::applyButton);

        directionAutoComplete = findViewById(R.id.auto_complete_txt);
        directionAdapter = new ArrayAdapter<>(this, R.layout.list_item, directionStringOptions);
        directionAutoComplete.setAdapter(directionAdapter);
    }

    private void cancleButton(View view) {
        finish();
    }

    private void applyButton(View view) {
    }
}