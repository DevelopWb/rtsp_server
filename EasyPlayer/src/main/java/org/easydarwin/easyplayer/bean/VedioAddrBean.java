package org.easydarwin.easyplayer.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:wang_sir
 * Time:2019/12/26 21:47
 * Description:This is VedioAddrBean
 */
public class VedioAddrBean implements Parcelable {

    private  String  Name ;
    private  String  URL ;
    private  String  Protocal ;
    private  boolean  sendPakage ;

    public String getName() {
        return Name == null ? "" : Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getURL() {
        return URL == null ? "" : URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getProtocal() {
        return Protocal == null ? "" : Protocal;
    }

    public void setProtocal(String protocal) {
        Protocal = protocal;
    }

    public boolean isSendPakage() {
        return sendPakage;
    }

    public void setSendPakage(boolean sendPakage) {
        this.sendPakage = sendPakage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Name);
        dest.writeString(this.URL);
        dest.writeString(this.Protocal);
        dest.writeByte(this.sendPakage ? (byte) 1 : (byte) 0);
    }

    public VedioAddrBean() {
    }

    protected VedioAddrBean(Parcel in) {
        this.Name = in.readString();
        this.URL = in.readString();
        this.Protocal = in.readString();
        this.sendPakage = in.readByte() != 0;
    }

    public static final Parcelable.Creator<VedioAddrBean> CREATOR = new Parcelable.Creator<VedioAddrBean>() {
        @Override
        public VedioAddrBean createFromParcel(Parcel source) {
            return new VedioAddrBean(source);
        }

        @Override
        public VedioAddrBean[] newArray(int size) {
            return new VedioAddrBean[size];
        }
    };
}
