package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.R;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.ArduinoRobot;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.utills.ConnectionHTTP;

import okhttp3.Request;

public class ConfigureConnectionActivity extends AppCompatActivity {

    private final int REQUEST_CODE_INTERNET = 101;

    public static final String IP_ADDRESS_CODE = "ipAddress";
    public static final String PORT_NUMBER_CODE = "portNumber";

    private EditText editText_ipAddress;
    private EditText editText_portNumber;

    private interface ConnectionTested {
        void doFinally(boolean isConnected);
    }

    private ImageView imageView_connectionStatusIcon;
    private Drawable drawable_connectionStatusIcon_established;
    private int color_connectionSatusIcon_established;
    private Drawable getDrawable_connectionStatusIcon_notEstablished;

    private TextView textView_connectionStatus;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_connection);
        setTitle(getString(R.string.configure_connection));
        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);

        ArduinoRobot arduinoRobot = (ArduinoRobot) getIntent().getSerializableExtra("arduinoRobot");

        editText_ipAddress = findViewById(R.id.editText_ip_address);
        editText_portNumber = findViewById(R.id.editText_port_number);

        imageView_connectionStatusIcon = findViewById(R.id.imageView_connection_status_icon);
        drawable_connectionStatusIcon_established = getDrawable(R.drawable.ic_valid_foreground);
        color_connectionSatusIcon_established = getColor(R.color.ic_valid_background);
        getDrawable_connectionStatusIcon_notEstablished = getDrawable(R.drawable.ic_error);

        textView_connectionStatus = findViewById(R.id.textView_connection_status);

        Button buttonTestConnection = findViewById(R.id.btn_test);
        buttonTestConnection.setOnClickListener(v -> {
            String ipAddress = editText_ipAddress.getText().toString();
            String portNumber = editText_portNumber.getText().toString();

            testConnectionWithLoading(ipAddress, portNumber, this::setConnectionView);
        });

        Button buttonCancle = findViewById(R.id.btn_cancle);
        buttonCancle.setOnClickListener(this::cancleButton);

        Button buttonApply = findViewById(R.id.btn_apply);
        buttonApply.setOnClickListener(this::applyButton);

        if (ContextCompat.checkSelfPermission(ConfigureConnectionActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ConfigureConnectionActivity.this, Manifest.permission.INTERNET)) {
                new AlertDialog.Builder(ConfigureConnectionActivity.this)
                        .setMessage("We need permission for internet.")
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialogInterface, i) -> ActivityCompat.requestPermissions(ConfigureConnectionActivity.this, new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE}, REQUEST_CODE_INTERNET)).create().show();
            } else {
                ActivityCompat.requestPermissions(ConfigureConnectionActivity.this, new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE}, REQUEST_CODE_INTERNET);
            }
        }
    }

    private void testConnectionWithLoading(String ipAddress, String portNumber, ConnectionTested onConnectionTested) {
        ProgressDialog mProgressDialog = ProgressDialog.show(this, "Please wait", "Trying to connect...", true);
        new Thread(() -> {
            boolean connected = connectionEstablished(ipAddress, portNumber);

            runOnUiThread(() -> {
                mProgressDialog.dismiss();
                onConnectionTested.doFinally(connected);
            });
        }).start();
    }

    private boolean connectionEstablished(String ipAddress, String portNumber) {
        String address = "http://" + ipAddress + ":" + portNumber + "/api/testConnection";

        Log.i("configureConnection", "Trying to reach host: " + address);
        String result = "";
        try {
            Request request = new Request.Builder().url(address).build();
            result = ConnectionHTTP.HTTP_GET(request);
            Log.i("configureConnection", "HTTP result: " + result);
        } catch (Exception e) {
            Log.i("configureConnection", "Error, while trying to reach host: " + address);
        }

        return result.equals("OK");
    }

    private void cancleButton(View view) {
        finish();
    }

    private void applyButton(View view) {
        String ipAddress = editText_ipAddress.getText().toString();
        String portNumber = editText_portNumber.getText().toString();
        testConnectionWithLoading(ipAddress, portNumber, (isConnected -> {
            if (isConnected) {
                Intent output = getIntent();
                output.putExtra(IP_ADDRESS_CODE, ipAddress);
                output.putExtra(PORT_NUMBER_CODE, portNumber);
                setResult(RESULT_OK, output);
                finish();

            } else {
                new AlertDialog.Builder(ConfigureConnectionActivity.this)
                        .setTitle("Connection Error")
                        .setMessage("Could not connect to: " + ipAddress + ":" + portNumber + "\nPlease try again.")
                        .setPositiveButton("Try again", (dialogInterface, i) -> {
                        }).create().show();
            }
        }));
    }

    private void setConnectionView(boolean isConnectionEstablished) {
        if (isConnectionEstablished) {
            textView_connectionStatus.setText(R.string.conn_status_established);
            imageView_connectionStatusIcon.setForeground(drawable_connectionStatusIcon_established);
            imageView_connectionStatusIcon.setBackgroundColor(color_connectionSatusIcon_established);
        } else {
            textView_connectionStatus.setText(R.string.conn_status_unknown);
            imageView_connectionStatusIcon.setForeground(getDrawable_connectionStatusIcon_notEstablished);
            imageView_connectionStatusIcon.setBackgroundColor(0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_INTERNET) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permission granted
                return;
            }
            //Permission NOT granted
            if (!ActivityCompat.shouldShowRequestPermissionRationale(ConfigureConnectionActivity.this, Manifest.permission.INTERNET)) {
                new AlertDialog.Builder(ConfigureConnectionActivity.this)
                        .setMessage("You have permanently denied this permission.")
                        .setPositiveButton("OK", (dialogInterface, i) -> {

                        }).create().show();
                // Permanently denied permission
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            } else {
                new AlertDialog.Builder(ConfigureConnectionActivity.this)
                        .setMessage("We don't have permissions for INTERNET.")
                        .setPositiveButton("OK", (dialogInterface, i) -> {
                        }).create().show();
            }

        }
    }
}