package org.easydarwin.easyplayer.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.easydarwin.easyplayer.R;
import org.easydarwin.easyplayer.databinding.ActivityAboutBinding;
/**
 * created by 8级大的狂风
 * created date 2019/12/26 20:10.
 * application   添加地址
 */
public class AddAdressActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityAboutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_about);
        setSupportActionBar(binding.toolbar);
        binding.toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View v) {
    }
}