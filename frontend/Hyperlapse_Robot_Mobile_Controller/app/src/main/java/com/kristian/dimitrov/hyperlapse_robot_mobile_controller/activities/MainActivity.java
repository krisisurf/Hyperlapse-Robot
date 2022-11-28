package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.ConfigureConnectionActivity;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Hyperlapse Robot Controller");
        setSupportActionBar(toolbar);

        FloatingActionButton buttonSendRules = findViewById(R.id.btn_send_rules);

        FloatingActionButton buttonConfigureConnection = findViewById(R.id.btn_configure_connection);
        buttonConfigureConnection.setOnClickListener(this::openConfigureConnectionActivity);

        Button buttonAddRule = findViewById(R.id.btn_add_rule);
        buttonAddRule.setOnClickListener(this::openCreateRuleActivity);
    }

    private void openCreateRuleActivity(View view){
        Intent intent = new Intent(MainActivity.this, CreateRuleActivity.class);
        startActivity(intent);
    }


    private void openConfigureConnectionActivity(View view){
        Intent intent = new Intent(MainActivity.this, ConfigureConnectionActivity.class);
        startActivity(intent);
    }
}