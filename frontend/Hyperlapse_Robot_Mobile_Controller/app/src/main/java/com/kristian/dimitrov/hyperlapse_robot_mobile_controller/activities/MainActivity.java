package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.R;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.ArduinoRobot;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.ArduinoRobotConnection;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CONFIGURE_CONNECTION = 222;

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

        arduinoRobot = new ArduinoRobot();
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

        Button buttonTemp = findViewById(R.id.temporary);
        buttonTemp.setOnClickListener((v) -> {
            Toast.makeText(MainActivity.this, "Staged Rules count: " + arduinoRobot.getRulesManagerEntity().size(), Toast.LENGTH_SHORT).show();
        });
    }

    private void openCreateRuleActivity(View view) {
        if (!arduinoRobotConnection.isConnectionEstablished()) {
            Toast.makeText(MainActivity.this, "Please, connect with the Robot, before adding a rule", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(MainActivity.this, CreateRuleActivity.class);
        intent.putExtra("arduinoRobot", arduinoRobot);
        startActivity(intent);
    }

    private void openConfigureConnectionActivity(View view) {
        Intent intent = new Intent(MainActivity.this, ConfigureConnectionActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CONFIGURE_CONNECTION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case (REQUEST_CODE_CONFIGURE_CONNECTION): {
                if (resultCode == Activity.RESULT_OK) {
                    String ipAddress = data.getStringExtra(ConfigureConnectionActivity.IP_ADDRESS_CODE);
                    String portNumber = data.getStringExtra(ConfigureConnectionActivity.PORT_NUMBER_CODE);

                    arduinoRobotConnection.setConnectionData(ipAddress, portNumber);
                }
                break;
            }
        }
    }
}