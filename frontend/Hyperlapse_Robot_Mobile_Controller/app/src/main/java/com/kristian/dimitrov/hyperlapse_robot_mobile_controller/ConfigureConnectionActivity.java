package com.kristian.dimitrov.hyperlapse_robot_mobile_controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.utills.ConnectionHTTP;

public class ConfigureConnectionActivity extends AppCompatActivity {

    private EditText editText_ipAddress;
    private EditText editText_portNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_connection);
        setTitle("Configure Connection");

        editText_ipAddress = findViewById(R.id.editText_ip_address);
        editText_portNumber = findViewById(R.id.editText_port_number);

        Button buttonTestConnection = findViewById(R.id.btn_test);
        buttonTestConnection.setOnClickListener(this::testConnection);

        Button buttonCancle = findViewById(R.id.btn_cancle);
        buttonCancle.setOnClickListener(this::cancleButton);
    }

    private void testConnection(View view) {
        String ipAddress = editText_ipAddress.getText().toString();
        String portNumber = editText_portNumber.getText().toString();
        String address = "http://" + ipAddress + ":" + portNumber + "/api/turnoff";

        try {
            String result = ConnectionHTTP.HTTP_GET(address);
            Log.i("configureConnection", result);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("configureConnection", "error, while trying to reach host: " + address);
        }
    }

    private void cancleButton(View view) {
        finish();
    }
}