package com.example.mobilesecurity_assignment1;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class MainActivity extends AppCompatActivity {

    MaterialButton main_BTN_login;
    MaterialAutoCompleteTextView main_EDT_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        main_BTN_login.setOnClickListener(v -> checkConditions());

    }

    private void checkConditions() {
        if (!main_EDT_password.getText().toString().equals("1234")) {
            Toast.makeText(this, "Wrong Password", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean check1 = !checkIfConnectedToWifi();
        boolean check2 = checkIfDoNotDisturb();
        boolean check3 = getBatteryLevel() <= 50;
        boolean check4 = isDarkMode();

        if (check1 || check2 || check3 || check4) {
            StringBuilder message = new StringBuilder("Login failed due to: ");
            if (check1) message.append("No Internet Connection, ");
            if (check2) message.append("Phone is in Do Not Disturb mode, ");
            if (check3) message.append("Battery level is less than 50%, ");
            if (check4) message.append("Device is in Dark Mode, ");

            Toast.makeText(this, message.toString(), Toast.LENGTH_LONG).show();
            return;
        }

        changeIntent();

    }


    private boolean checkIfDoNotDisturb() {  //check if the phone is in Don't Disturb mode
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT;
    }

    private int getBatteryLevel() { //get the battery level
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        return (int) ((level / (float) scale) * 100);
    }

    private boolean checkIfConnectedToWifi() { //check if the phone is connected to wifi or internet
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isDarkMode() {
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }

    public void changeIntent() {
        Intent mainIntent = new Intent(this, OpeningActivity.class);
        startActivity(mainIntent);
    }

    private void findViews() {
        main_BTN_login = findViewById(R.id.main_BTN_login);
        main_EDT_password = findViewById(R.id.main_EDT_password);
    }
}