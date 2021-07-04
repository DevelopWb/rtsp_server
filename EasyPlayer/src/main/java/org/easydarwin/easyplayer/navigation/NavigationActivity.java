package org.easydarwin.easyplayer.navigation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.wisdom.basecomponent.base.BaseActivity;
import com.juntai.wisdom.basecomponent.utils.HawkProperty;
import com.juntai.wisdom.basecomponent.utils.ToastUtils;
import com.orhanobut.hawk.Hawk;

import org.easydarwin.easyplayer.R;
import org.easydarwin.easyplayer.activity.PlayListActivity;
import org.easydarwin.easyplayer.util.GridDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * @aouther tobato
 * @description 描述  导航页面
 * @date 2021/7/4 11:19
 */
public class NavigationActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mRecyclerview;
    private LinearLayout mMenuQuitLl;
    private MyMenuAdapter mMenuAdapter;
    public static final int NAME_CAMERA = 0;
    public static final String APP_PACKAGE_NAME = "MonitorAB.Android";
    public static final int NAME_GLASSES = 1;//眼镜
    private long mExitTime;//声明一个long类型变量：用于存放上一点击“返回键”的时刻
    @Override
    public int getLayoutView() {
        return R.layout.activity_navigation;
    }

    @Override
    public void initView() {
        getTitleLeftTv().setVisibility(View.GONE);
        setTitleName("123");
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        mMenuQuitLl = (LinearLayout) findViewById(R.id.menu_quit_ll);
        mMenuQuitLl.setOnClickListener(this);
        mMenuAdapter = new MyMenuAdapter(R.layout.item_my_center_menu);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        mRecyclerview.setLayoutManager(gridLayoutManager);
        mRecyclerview.addItemDecoration(new GridDividerItemDecoration(mContext));
        mRecyclerview.setAdapter(mMenuAdapter);
        mMenuAdapter.setNewData(getMineMenus());
        mMenuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position) {
                    case 0:
                        launchapp(mContext);
                        break;
                    case 1:
                        startActivity(new Intent(mContext, PlayListActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        //与上次点击返回键时刻作差
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            //大于2000ms则认为是误操作，使用Toast进行提示
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            //并记录下本次点击“返回键”的时刻，以便下次进行判断
            mExitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }
    /**
     * @return
     */
    protected List<MenuBean> getMineMenus() {
        List<MenuBean> arrays = new ArrayList<>();
        arrays.add(new MenuBean(NAME_CAMERA, "摄像机", R.mipmap.camera_icon));
        arrays.add(new MenuBean(NAME_GLASSES, "眼镜", R.mipmap.glasses_icon));
        return arrays;
    }

    @Override
    public void initData() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.menu_quit_ll:
                finish();
                break;
        }
    }

    //跳转页面的方法
    private void launchapp(Context context) {
        //判断当前手机是否有要跳入的app
        if (isAppInstalled(context, APP_PACKAGE_NAME)) {
            //如果有根据包名跳转
            context.startActivity(context.getPackageManager().getLaunchIntentForPackage(APP_PACKAGE_NAME));
        } else {
            //如果没有，走进入系统商店找到这款APP，提示你去下载这款APP的程序
            ToastUtils.toast(mContext, "您还没有安装AB软件");
        }
    }

    //这里是判断APP中是否有相应APP的方法
    private boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
