package com.ocwvar.muzi.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;

//Use this activity by " YourMainActivity.startActivityForResult(new Intent(this,PermissionCheckActivity.class),0); "
public class PermissionCheckActivity extends AppCompatActivity {

    static public int STATUS_NONEEDTOGRANTED = -1;
    static public int STATUS_ALLDEFENTED = 0;
    static public int STATUS_ALLGRANTED = 1;
    static public int STATUS_SOMEGRANTED = 2;
    static public int CHECK_SLEEPTIME = 1000;
    private boolean checked = false;
    private int status;
    private String[] requestPermissions = {
            //Set all the permissions your app needs
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 23) {
            checkALL(requestPermissions);

            //Check the status of request permissions
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!checked) {
                        Log.d("Check status", "Waitting...");
                        try {
                            Thread.sleep(CHECK_SLEEPTIME);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    setResult(status);
                    finish();
                }
            }).start();
        } else {
            setResult(STATUS_NONEEDTOGRANTED);
            finish();
        }
    }

    @TargetApi(23)
    public void checkALL(String[] requestPermissions) {
        int got = 0;
        for (String requestPer : requestPermissions) {
            if (checkSelfPermission(requestPer) == PackageManager.PERMISSION_GRANTED) {
                got++;
            }
        }
        if (got == requestPermissions.length) {
            Log.d("On Permissions Check", "On Pre-Check, all permissions GRANTED");
            status = STATUS_ALLGRANTED;
            checked = true;
        } else {
            Log.d("On Permissions Check", "On Pre-Check, need to request permissions");
            requestPermission(requestPermissions);
        }
    }

    @TargetApi(23)
    public void requestPermission(String[] requestPermissions) {
        this.requestPermissions(requestPermissions, 50);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 50) {
            int got = 0;
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_GRANTED) {
                    got++;
                }
            }
            if (got == permissions.length) {
                Log.d("OnResult", "ALL permissions GRANTED");
                status = STATUS_ALLGRANTED;
            } else if (got != 0) {
                Log.d("OnResult", "SOME permissions GRANTED");
                status = STATUS_SOMEGRANTED;
            } else {
                Log.d("OnResult", "ALL permissions DEFENTED");
                status = STATUS_ALLDEFENTED;
            }
            checked = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

}
