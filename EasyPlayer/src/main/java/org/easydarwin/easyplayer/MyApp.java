package org.easydarwin.easyplayer;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.basenetlib.util.NetWorkUtil;
import com.orhanobut.hawk.Hawk;

import org.easydarwin.easyplayer.activity.PlayListActivity;
import org.easydarwin.easyplayer.data.EasyDBHelper;
import org.easydarwin.video.Client;

import io.reactivex.annotations.Beta;

public class MyApp extends Application {

    public static SQLiteDatabase sDB;
    public static int activeDays = 9999;

    @Override
    public void onCreate() {
        super.onCreate();

        activeDays = Client.getActiveDays(this, BuildConfig.RTSP_KEY);
        sDB = new EasyDBHelper(this).getWritableDatabase();

//        Bugly.init(getApplicationContext(), "eb3d7319a8", false);
//        setBuglyInit();
        NetWorkUtil.initContext(this);
        Hawk.init(this).build();
    }
//    protected void attachBaseContext(Context base) {
//
//        super.attachBaseContext(base);
//
//        MultiDex.install(base);
//
//    }

}
