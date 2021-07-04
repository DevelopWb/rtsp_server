package org.easydarwin.easyplayer.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.easydarwin.easyplayer.navigation.NavigationActivity;
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

        startActivity(new Intent(this, NavigationActivity.class));
        finish();
    }

}
