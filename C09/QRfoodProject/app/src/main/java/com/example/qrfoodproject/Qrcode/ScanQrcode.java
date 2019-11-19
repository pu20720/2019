package com.example.qrfoodproject.Qrcode;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanQrcode extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView ScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScannerView = new ZXingScannerView(ScanQrcode.this);
        setContentView(ScannerView);

        checkCameraPermitted();

    }

    private void checkCameraPermitted(){
        //Changed the minimum API into 23 since it required 23 or later to work
        //reason that the permission wont been asked while installing is that USER could revoke permission after that
        final int PERMISSION_CAMERA = 1011;

        if (ContextCompat.checkSelfPermission(ScanQrcode.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            new AlertDialog.Builder(ScanQrcode.this)
                    .setMessage("此功能需要您提供相機權限方能使用")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(ScanQrcode.this,
                                    new String[]{android.Manifest.permission.CAMERA},
                                    PERMISSION_CAMERA);
                        }
                    }).setNegativeButton("Nope", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).show();
        }

    }

    @Override
    public void handleResult(Result result) {
        Qrcode_main.QrcodeResult = result.getText();
        startActivity(new Intent(ScanQrcode.this,Qrcode_main.class));
        finish();
    }

    @Override
    protected void onPause() {

        super.onPause();
        ScannerView.stopCamera();

    }
    @Override
    protected void onResume(){

        super.onResume();
        ScannerView.setResultHandler(this);
        ScannerView.startCamera();

    }

    //E/ZXingScannerView: java.lang.RuntimeException: Camera is being used after Camera.release() was called
}
