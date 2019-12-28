package org.easydarwin.easyplayer.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.easydarwin.easyplayer.R;
import org.easydarwin.easyplayer.bean.VedioAddrBean;
import org.easydarwin.easyplayer.databinding.ActivityAboutBinding;

/**
 * created by 8级大的狂风
 * created date 2019/12/26 20:10.
 * application   添加地址
 */
public class AddAdressActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityAboutBinding binding;
    /**
     * 输入设备名称
     */
    private EditText mDevNameEt;
    /**
     * 输入RTSP地址(格式为RTSP://...)
     */
    private EditText mNewMediaSourceUrl;
    /**
     * TCP
     */
    private RadioButton mTransportTcp;
    /**
     * UDP
     */
    private RadioButton mTransportUdp;
    private CheckBox mSendOption;
    /**
     * 保存
     */
    private TextView mSaveDeviceTv;

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
        /**
         * 保存
         */
        binding.saveDeviceTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String  name = binding.devNameEt.getText().toString().trim();
                String  ip = binding.newMediaSourceIpEt.getText().toString().trim();
                String  regCode = binding.newMediaRegCodeEt.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "请输入设备名称", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(ip)) {
                    Toast.makeText(getApplicationContext(), "请输入IP地址", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(regCode)) {
                    Toast.makeText(getApplicationContext(), "请输入注册码", Toast.LENGTH_LONG).show();
                    return;
                }
                VedioAddrBean  vedioAddrBean = new VedioAddrBean();
                vedioAddrBean.setName(name);
                vedioAddrBean.setIp(ip);
                vedioAddrBean.setRegCode(regCode);
                vedioAddrBean.setProtocal(binding.transportTcp.isChecked()?"TCP":"UDP");
                vedioAddrBean.setSendPakage(binding.sendOption.isChecked()?true:false);
                Intent  intent = new Intent();
                intent.putExtra(PlayListActivity.DEVICE_INFO,vedioAddrBean);
                setResult(PlayListActivity.REQUEST_ADD_DEVICE,intent);
                finish();
            }
        });
        initView();
    }

    @Override
    public void onClick(View v) {
    }

    private void initView() {
        mDevNameEt = (EditText) findViewById(R.id.dev_name_et);
        mNewMediaSourceUrl = (EditText) findViewById(R.id.new_media_source_ip_et);
        mTransportTcp = (RadioButton) findViewById(R.id.transport_tcp);
        mTransportUdp = (RadioButton) findViewById(R.id.transport_udp);
        mSendOption = (CheckBox) findViewById(R.id.send_option);
        mSaveDeviceTv = (TextView) findViewById(R.id.save_device_tv);



    }
}