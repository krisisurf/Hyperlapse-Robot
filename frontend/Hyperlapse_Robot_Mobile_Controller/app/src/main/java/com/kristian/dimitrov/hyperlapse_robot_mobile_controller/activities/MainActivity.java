package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.R;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.ArduinoRobot;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.ArduinoRobotConnection;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.RuleEntity;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.builders.RuleEntityBuilder;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.stepper.StepMotorEntity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_CONFIGURE_CONNECTION = 100;
    private static final int REQUEST_CODE_CREATE_RULE = 101;

    private ArduinoRobot arduinoRobot;
    private ArduinoRobotConnection arduinoRobotConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView tvConnectionStatus = findViewById(R.id.tvConnStatus);
        Thread uiThread = new Thread(() -> {
            if (arduinoRobotConnection.isConnectionEstablished()) {
                tvConnectionStatus.setText(getString(R.string.connected_status));
                tvConnectionStatus.setTextColor(getColor(R.color.green));
            } else {
                tvConnectionStatus.setText(getString(R.string.not_connected_status));
                tvConnectionStatus.setTextColor(getColor(R.color.red));
            }
        });

        final double wheelRadius = 5;
        final StepMotorEntity leftMotor = new StepMotorEntity(64, 16);
        final StepMotorEntity rightMotor = new StepMotorEntity(64, 16);
        final StepMotorEntity cameraPanMotor = new StepMotorEntity(64, 16);
        final StepMotorEntity cameraTiltMotor = new StepMotorEntity(64, 16);

        arduinoRobot = new ArduinoRobot();
        arduinoRobot.setHardwareData(wheelRadius, leftMotor, rightMotor, cameraPanMotor, cameraTiltMotor);
        arduinoRobotConnection = new ArduinoRobotConnection(5000) {
            @Override
            public void onConnectionStatusListener(boolean isConnectionEstablished) {
                runOnUiThread(uiThread);
            }
        };
        arduinoRobotConnection.start();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Hyperlapse Robot Controller");
        setSupportActionBar(toolbar);

        FloatingActionButton buttonSendRules = findViewById(R.id.btn_send_rules);

        FloatingActionButton buttonConfigureConnection = findViewById(R.id.btn_configure_connection);
        buttonConfigureConnection.setOnClickListener(this::openConfigureConnectionActivity);

        Button buttonAddRule = findViewById(R.id.btn_add_rule);
        buttonAddRule.setOnClickListener(this::openCreateRuleActivity);

        arduinoRobot.addRule(new RuleEntityBuilder(arduinoRobot).build());
        ArrayAdapter arrayAdapter = new RulesListAdapter(MainActivity.this, arduinoRobot.getRulesManagerEntity().getRules());
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);
    }

    private void openCreateRuleActivity(View view) {
        if (!arduinoRobotConnection.isConnectionEstablished()) {
            Toast.makeText(MainActivity.this, "Please, connect with the Robot, before adding a rule", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(MainActivity.this, CreateRuleActivity.class);
        intent.putExtra("arduinoRobot", arduinoRobot);
        startActivityForResult(intent, REQUEST_CODE_CREATE_RULE);
    }

    private void openConfigureConnectionActivity(View view) {
        Intent intent = new Intent(MainActivity.this, ConfigureConnectionActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CONFIGURE_CONNECTION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case (REQUEST_CODE_CONFIGURE_CONNECTION): {
                if (resultCode == Activity.RESULT_OK) {
                    String ipAddress = data.getStringExtra(ConfigureConnectionActivity.IP_ADDRESS_CODE);
                    String portNumber = data.getStringExtra(ConfigureConnectionActivity.PORT_NUMBER_CODE);
                    Log.i(TAG, "Connection configured: " + ipAddress + ":" + portNumber);

                    arduinoRobotConnection.setConnectionData(ipAddress, portNumber);
                }
                break;
            }
            case (REQUEST_CODE_CREATE_RULE): {
                if (resultCode == Activity.RESULT_OK) {
                    Log.i(TAG, "Create rule request code OK received.");
                    RuleEntity ruleEntity = (RuleEntity) data.getSerializableExtra(CreateRuleActivity.RULE_ENTITY_CODE);
                    arduinoRobot.addRule(ruleEntity);
                    Toast.makeText(MainActivity.this, "Staged rules count: " + arduinoRobot.getRulesManagerEntity().size(), Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}