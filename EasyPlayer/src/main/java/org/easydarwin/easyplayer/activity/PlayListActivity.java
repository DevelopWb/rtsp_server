package org.easydarwin.easyplayer.activity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.regmode.RegOperateUtil;
import com.regmode.Utils.RegLatestContact;

import org.easydarwin.easyplayer.BuildConfig;
import org.easydarwin.easyplayer.R;
import org.easydarwin.easyplayer.TheApp;
import org.easydarwin.easyplayer.bean.VedioAddrBean;
import org.easydarwin.easyplayer.data.VideoSource;
import org.easydarwin.easyplayer.databinding.ActivityPlayListBinding;
import org.easydarwin.easyplayer.databinding.DevListItemBinding;
import org.easydarwin.easyplayer.util.FileUtil;
import org.easydarwin.update.UpdateMgr;

import java.io.File;
import java.util.UUID;

import static org.easydarwin.update.UpdateMgr.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;

/**
 * 视频广场
 */
public class PlayListActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private static final int REQUEST_PLAY = 1000;
    public static final int REQUEST_CAMERA_PERMISSION = 1001;
    public static final int REQUEST_ADD_DEVICE = 1004;
    public static final String DEVICE_INFO = "added_device_info";  //添加的设备信息
    private static final int REQUEST_SCAN_TEXT_URL = 1003;      // 扫描二维码

    public static final String EXTRA_BOOLEAN_SELECT_ITEM_TO_PLAY = "extra-boolean-select-item-to-play";

    private int mPos;
    private ActivityPlayListBinding mBinding;
    private RecyclerView mRecyclerView;
    private EditText edit;

    private Cursor mCursor;

    private UpdateMgr update;

    private long mExitTime;//声明一个long类型变量：用于存放上一点击“返回键”的时刻

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_play_list);

        setSupportActionBar(mBinding.toolbar);

//        // 添加默认地址
        mCursor = TheApp.sDB.query(VideoSource.TABLE_NAME, null, null, null, null, null, null);
//        if (!mCursor.moveToFirst()) {
//            ContentValues cv = new ContentValues();
//            cv.put(VideoSource.URL, "rtsp://218.246.35.198:554/688844");
//            cv.put(VideoSource.NAME,"dev1");
//            cv.put(VideoSource.TRANSPORT_MODE, VideoSource.TRANSPORT_MODE_TCP);
//            cv.put(VideoSource.SEND_OPTION, VideoSource.SEND_OPTION_TRUE);
//
//            TheApp.sDB.insert(VideoSource.TABLE_NAME, null, cv);
//
//            mCursor.close();
//            mCursor = TheApp.sDB.query(VideoSource.TABLE_NAME, null, null, null, null, null, null);
//
//            SPUtil.setAutoAudio(this, true);
//            SPUtil.setswCodec(this, true);
//        }

        mRecyclerView = mBinding.recycler;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new PlayListViewHolder((DevListItemBinding) DataBindingUtil.inflate(getLayoutInflater(), R.layout.dev_list_item, parent, false));
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                PlayListViewHolder plvh = (PlayListViewHolder) holder;
                mCursor.moveToPosition(position);
                String name = mCursor.getString(mCursor.getColumnIndex(VideoSource.NAME));
                String url = mCursor.getString(mCursor.getColumnIndex(VideoSource.URL));

                plvh.mDevNameTv.setText(name);
                if (url.length()>8) {
                    String  ip = url.substring(7,url.lastIndexOf(":"));
                    String  regCode = url.substring(url.lastIndexOf("/")+1,url.lastIndexOf("."));
                    plvh.mDevUrlTv.setText(ip);
                    plvh.mDevRegTv.setText(regCode);
                }


                File file = FileUtil.getSnapFile(url);
                Glide.with(PlayListActivity.this).load(file).signature(new StringSignature(UUID.randomUUID().toString())).placeholder(R.mipmap.dev_icon).centerCrop().into(plvh.mImageView);

                int audienceNumber = mCursor.getInt(mCursor.getColumnIndex(VideoSource.AUDIENCE_NUMBER));

//                if (audienceNumber > 0) {
//                    plvh.mAudienceNumber.setText(String.format("当前观看人数:%d", audienceNumber));
//                    plvh.mAudienceNumber.setVisibility(View.VISIBLE);
//                } else {
//                    plvh.mAudienceNumber.setVisibility(View.GONE);
//                }
            }

            @Override
            public int getItemCount() {
                return mCursor.getCount();
            }
        });

//        // 如果当前进程挂起，则进入启动页
//        if (savedInstanceState == null) {
//            if (!getIntent().getBooleanExtra(EXTRA_BOOLEAN_SELECT_ITEM_TO_PLAY, false)) {
//                startActivity(new Intent(this, SplashActivity.class));
//            }
//        }

        if (!isPro()) {
            mBinding.pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mBinding.pullToRefresh.setRefreshing(false);
                }
            });

            mBinding.toolbarSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(PlayListActivity.this, SettingsActivity.class));
                }
            });
        } else {
            mBinding.toolbarSetting.setVisibility(View.GONE);
            mBinding.pullToRefresh.setEnabled(false);
        }

        mBinding.toolbarAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(PlayListActivity.this, AddAdressActivity.class), REQUEST_ADD_DEVICE);

//                displayDialog(-1);
            }
        });
///**
// * 添加设备
// */
//        mBinding.toolbarAbout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivityForResult(new Intent(PlayListActivity.this, AddAdressActivity.class), REQUEST_ADD_DEVICE);
//            }
//        });

//        /* ==================== 版本更新 ==================== */
//        String url;
//        if (PlayListActivity.isPro()) {
//            url = "http://www.easydarwin.org/versions/easyplayer_pro/version.txt";
//        } else {
//            url = "http://www.easydarwin.org/versions/easyplayer-rtmp/version.txt";
//        }
//
//        update = new UpdateMgr(this);
//        update.checkUpdate(url);
        RegOperateUtil regOprateUtil = RegOperateUtil.getInstance(this);
        regOprateUtil.setCancelCallBack(new RegLatestContact.CancelCallBack() {
            @Override
            public void toFinishActivity() {
                finish();
            }

            @Override
            public void toDoNext() {

            }
        });
    }

    @Override
    protected void onDestroy() {
        mCursor.close();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    update.doDownload();
                }
            case REQUEST_CAMERA_PERMISSION: {
                if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    toScanQRActivity();
                }

                break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        PlayListViewHolder holder = (PlayListViewHolder) view.getTag();
        int pos = holder.getAdapterPosition();

        if (pos != -1) {
            mCursor.moveToPosition(pos);
            String playDevName = mCursor.getString(mCursor.getColumnIndex(VideoSource.NAME));
            String playUrl = mCursor.getString(mCursor.getColumnIndex(VideoSource.URL));
            int sendOption = mCursor.getInt(mCursor.getColumnIndex(VideoSource.SEND_OPTION));
            int transportMode = mCursor.getInt(mCursor.getColumnIndex(VideoSource.TRANSPORT_MODE));

            if (!TextUtils.isEmpty(playUrl)) {
                if (getIntent().getBooleanExtra(EXTRA_BOOLEAN_SELECT_ITEM_TO_PLAY, false)) {
                    Intent data = new Intent();
                    data.putExtra("url", playUrl);
                    data.putExtra(VideoSource.SEND_OPTION, sendOption);
                    data.putExtra(VideoSource.TRANSPORT_MODE, transportMode);
                    setResult(RESULT_OK, data);
                    finish();
                } else {
                    if (BuildConfig.YUV_EXPORT) {
                        // YUV EXPORT DEMO..
                        Intent i = new Intent(PlayListActivity.this, YUVExportActivity.class);
                        i.putExtra("play_url", playUrl);
                        i.putExtra("play_name", playDevName);
                        mPos = pos;
                        startActivity(i);
                    } else {
                        Intent i = new Intent(PlayListActivity.this, PlayActivity.class);
                        i.putExtra("play_url", playUrl);
                        i.putExtra("play_name", playDevName);
                        i.putExtra(VideoSource.SEND_OPTION, sendOption);
                        i.putExtra(VideoSource.TRANSPORT_MODE, transportMode);
                        mPos = pos;
                        ActivityCompat.startActivityForResult(this, i, REQUEST_PLAY, ActivityOptionsCompat.makeSceneTransitionAnimation(this, holder.mImageView, "video_animation").toBundle());
                    }
                }
            }
        }
    }

    @Override
    public boolean onLongClick(View view) {
        PlayListViewHolder holder = (PlayListViewHolder) view.getTag();
        final int pos = holder.getAdapterPosition();

        if (pos != -1) {
            new AlertDialog.Builder(this).setItems(new CharSequence[]{"删除"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
//                    if (i == 0) {
//                        displayDialog(pos);
//                    } else {
                    new AlertDialog
                            .Builder(PlayListActivity.this)
                            .setMessage("确定要删除该设备吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mCursor.moveToPosition(pos);
                                    TheApp.sDB.delete(VideoSource.TABLE_NAME, VideoSource._ID + "=?", new String[]{String.valueOf(mCursor.getInt(mCursor.getColumnIndex(VideoSource._ID)))});
                                    mCursor.close();
                                    mCursor = TheApp.sDB.query(VideoSource.TABLE_NAME, null, null, null, null, null, null);
                                    mRecyclerView.getAdapter().notifyItemRemoved(pos);
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
//                    }
                }
            }).show();
        }

        return true;
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

    private void displayDialog(final int pos) {
        String url = "rtsp://";

        View view = getLayoutInflater().inflate(R.layout.new_media_source_dialog, null);
        final CheckBox sendOption = view.findViewById(R.id.send_option);
        final RadioGroup transportMode = view.findViewById(R.id.transport_mode);
        final RadioButton transportTcp = view.findViewById(R.id.transport_tcp);
        edit = view.findViewById(R.id.new_media_source_ip_et);

        if (pos > -1) {
            mCursor.moveToPosition(pos);
            url = mCursor.getString(mCursor.getColumnIndex(VideoSource.URL));

            final int sendOptionValue = mCursor.getInt(mCursor.getColumnIndex(VideoSource.SEND_OPTION));
            if (sendOptionValue == VideoSource.SEND_OPTION_TRUE) {
                sendOption.setChecked(true);
            } else {
                sendOption.setChecked(false);
            }

            final int transportModeValue = mCursor.getInt(mCursor.getColumnIndex(VideoSource.TRANSPORT_MODE));
            if (transportModeValue == VideoSource.TRANSPORT_MODE_UDP) {
                transportMode.check(R.id.transport_udp);
            } else {
                transportMode.check(R.id.transport_tcp);
            }
        }

        edit.setHint("RTSP://...");
        edit.setText(url);
        edit.setSelection(url.length());


        final AlertDialog dlg = new AlertDialog.Builder(PlayListActivity.this)
                .setView(view)
                .setTitle("请输入播放地址")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String url = String.valueOf(edit.getText());

                        if (TextUtils.isEmpty(url)) {
                            return;
                        }

                        if (url.toLowerCase().indexOf("rtsp://") != 0) {
                            Toast.makeText(PlayListActivity.this, "不是合法的RTSP地址，请重新添加.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        ContentValues cv = new ContentValues();
                        cv.put(VideoSource.URL, url);

                        if (transportTcp.isChecked()) {
                            cv.put(VideoSource.TRANSPORT_MODE, VideoSource.TRANSPORT_MODE_TCP);
                        } else {
                            cv.put(VideoSource.TRANSPORT_MODE, VideoSource.TRANSPORT_MODE_UDP);
                        }

                        cv.put(VideoSource.SEND_OPTION, sendOption.isChecked() ? VideoSource.SEND_OPTION_TRUE : VideoSource.SEND_OPTION_FALSE);

                        if (pos > -1) {
                            final int _id = mCursor.getInt(mCursor.getColumnIndex(VideoSource._ID));

                            TheApp.sDB.update(VideoSource.TABLE_NAME, cv, VideoSource._ID + "=?", new String[]{String.valueOf(_id)});

                            mCursor.close();
                            mCursor = TheApp.sDB.query(VideoSource.TABLE_NAME, null, null, null, null, null, null);
                            mRecyclerView.getAdapter().notifyItemChanged(pos);
                        } else {
                            TheApp.sDB.insert(VideoSource.TABLE_NAME, null, cv);

                            mCursor.close();
                            mCursor = TheApp.sDB.query(VideoSource.TABLE_NAME, null, null, null, null, null, null);
                            mRecyclerView.getAdapter().notifyItemInserted(mCursor.getCount() - 1);
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .create();

        dlg.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edit, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        dlg.show();
    }

    private void toScanQRActivity() {
        Intent intent = new Intent(PlayListActivity.this, ScanQRActivity.class);
        startActivityForResult(intent, REQUEST_SCAN_TEXT_URL);
        overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
    }


    public static boolean isPro() {
        return BuildConfig.FLAVOR.equals("pro");
    }

    public void onMultiPlay(View view) {
        Intent intent = new Intent(this, MultiPlayActivity.class);
        startActivity(intent);
    }

    /**
     * 视频源的item
     */
    class PlayListViewHolder extends RecyclerView.ViewHolder {
        private final TextView mDevNameTv;
        private final TextView mDevUrlTv;
        private final TextView mDevRegTv;
//        private final TextView mAudienceNumber;
        private final ImageView mImageView;

        public PlayListViewHolder(DevListItemBinding binding) {
            super(binding.getRoot());

            mDevNameTv = binding.devNameValueTv;
            mDevUrlTv = binding.devUrlValueTv;
            mDevRegTv = binding.devRegValueTv;
//            mAudienceNumber = binding.videoSourceItemAudienceNumber;
            mImageView = binding.videoSourceItemThumb;

            itemView.setOnClickListener(PlayListActivity.this);
            itemView.setOnLongClickListener(PlayListActivity.this);
            itemView.setTag(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SCAN_TEXT_URL) {
            if (resultCode == RESULT_OK) {
                String url = data.getStringExtra("text");
                edit.setText(url);
            }
        } else if (requestCode == REQUEST_ADD_DEVICE) {
            if (data != null) {
                VedioAddrBean bean = data.getParcelableExtra(DEVICE_INFO);
                StringBuilder  sb = new StringBuilder();
                sb.append("rtsp://").append(bean.getIp()).append(":554/").append(bean.getRegCode()).append(".sdp");
                ContentValues cv = new ContentValues();
                cv.put(VideoSource.URL, sb.toString());
                cv.put(VideoSource.NAME, bean.getName());
                if ("TCP".equals(bean.getProtocal())) {
                    cv.put(VideoSource.TRANSPORT_MODE, VideoSource.TRANSPORT_MODE_TCP);
                } else {
                    cv.put(VideoSource.TRANSPORT_MODE, VideoSource.TRANSPORT_MODE_UDP);
                }

                cv.put(VideoSource.SEND_OPTION, bean.isSendPakage() ? VideoSource.SEND_OPTION_TRUE : VideoSource.SEND_OPTION_FALSE);

                TheApp.sDB.insert(VideoSource.TABLE_NAME, null, cv);

                mCursor.close();
                mCursor = TheApp.sDB.query(VideoSource.TABLE_NAME, null, null, null, null, null, null);
                mRecyclerView.getAdapter().notifyItemInserted(mCursor.getCount() - 1);
            }
        } else {
            mRecyclerView.getAdapter().notifyItemChanged(mPos);

        }
    }
}