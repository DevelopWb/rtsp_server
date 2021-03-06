package org.easydarwin.easyplayer.navigation;

import com.juntai.wisdom.basecomponent.base.BaseResult;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2020/12/28 16:26
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/12/28 16:26
 */
public class MenuBean extends BaseResult {
    private int tagId;
    private String name;
    private String des;
    private int resId;

    public MenuBean(int tagId, String name, int resId) {
        this.tagId = tagId;
        this.name = name;
        this.resId = resId;
    }

    public MenuBean(int tagId, String name, String des, int resId) {
        this.tagId = tagId;
        this.name = name;
        this.des = des;
        this.resId = resId;
    }

    public String getDes() {
        return des == null ? "" : des;
    }

    public void setDes(String des) {
        this.des = des == null ? "" : des;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name == null ? "" : name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
