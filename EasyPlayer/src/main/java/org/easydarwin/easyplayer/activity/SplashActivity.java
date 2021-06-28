package org.easydarwin.easyplayer.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import org.easydarwin.easyplayer.R;

/**
 * 启动页
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        String versionName;

        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "1.0";
        }

        //        TextView txtVersion = (TextView) findViewById(R.id.txt_version);
        //        txtVersion.setText(String.format("EasyPlayer %s", versionName));

        startActivity(new Intent(this, PlayListActivity.class));
        finish();
    }

}
