package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.R;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.utills.ConnectionHTTP;

public class ConfigureConnectionActivity extends AppCompatActivity {

    private EditText editText_ipAddress;
    private EditText editText_portNumber;

    private final int REQUEST_CODE_INTERNET = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_connection);
        setTitle("Configure Connection");
        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);

        editText_ipAddress = findViewById(R.id.editText_ip_address);
        editText_portNumber = findViewById(R.id.editText_port_number);

        Button buttonTestConnection = findViewById(R.id.btn_test);
        buttonTestConnection.setOnClickListener(this::testConnection);

        Button buttonCancle = findViewById(R.id.btn_cancle);
        buttonCancle.setOnClickListener(this::cancleButton);

        if (ContextCompat.checkSelfPermission(ConfigureConnectionActivity.this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
            editText_ipAddress.setText("grandted!");
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ConfigureConnectionActivity.this, Manifest.permission.INTERNET)) {
                new AlertDialog.Builder(ConfigureConnectionActivity.this)
                        .setMessage("We need permission for internet.")
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialogInterface, i) -> ActivityCompat.requestPermissions(ConfigureConnectionActivity.this, new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE}, REQUEST_CODE_INTERNET));
            } else {
                ActivityCompat.requestPermissions(ConfigureConnectionActivity.this, new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE}, REQUEST_CODE_INTERNET);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_INTERNET) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permission granted
                editText_ipAddress.setText("granted");
            } else {
                //Permission NOT granted
                if (!ActivityCompat.shouldShowRequestPermissionRationale(ConfigureConnectionActivity.this, Manifest.permission.INTERNET)) {
                    new AlertDialog.Builder(ConfigureConnectionActivity.this)
                            .setMessage("You have permanently denied this permission.")
                            .setPositiveButton("OK", (dialogInterface, i) -> {
                            });
                    // Permanently denied permission
                    editText_portNumber.setText("deny");
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                } else {
                    //
                    editText_portNumber.setText("not");
                }
            }
        }
    }

    private void testConnection(View view) {

        Thread gfgThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String ipAddress = editText_ipAddress.getText().toString();
                String portNumber = editText_portNumber.getText().toString();
                String address = "http://" + ipAddress + ":" + portNumber + "/api/testConnection";

                Log.i("configureConnection", "Trying to reach host: " + address);
                try {
                    String result = ConnectionHTTP.HTTP_REQUEST(address);
                    Log.i("configureConnection", result);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("configureConnection", "error, while trying to reach host: " + address);
                }
            }
        });

        gfgThread.start();
    }

    private void cancleButton(View view) {
        finish();
    }
}