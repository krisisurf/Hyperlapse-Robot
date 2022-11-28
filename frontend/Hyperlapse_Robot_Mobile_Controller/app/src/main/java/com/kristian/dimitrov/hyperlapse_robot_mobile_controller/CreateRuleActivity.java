package com.kristian.dimitrov.hyperlapse_robot_mobile_controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class CreateRuleActivity extends AppCompatActivity {

    public void buttonCancle(View view) {
        startActivity(new Intent(CreateRuleActivity.this, MainActivity.class));
    }
}
