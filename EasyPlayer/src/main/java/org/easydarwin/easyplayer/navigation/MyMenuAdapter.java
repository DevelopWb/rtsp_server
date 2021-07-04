package org.easydarwin.easyplayer.navigation;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.easydarwin.easyplayer.R;

/**
 * Describe:
 * Create by zhangzhenlong
 * 2020/3/7
 * email:954101549@qq.com
 */
public class MyMenuAdapter extends BaseQuickAdapter<MenuBean, BaseViewHolder> {

    public MyMenuAdapter(int layoutResId) {
        super(layoutResId);
    }


    @Override
    protected void convert(BaseViewHolder helper, MenuBean item) {
        helper.setImageResource(R.id.menu_icon_iv, item.getResId());
        helper.setText(R.id.menu_title_tv, item.getName());

    }
}
