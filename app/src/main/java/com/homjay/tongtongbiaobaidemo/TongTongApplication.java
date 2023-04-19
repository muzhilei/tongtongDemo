package com.homjay.tongtongbiaobaidemo;

import android.app.Application;

/**
 * @author Homjay
 * @date 2022/7/4 14:48
 * @describe
 */
public class TongTongApplication extends Application {
    public static TongTongApplication app;

    private String phone;
    private String sms;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static TongTongApplication getIns() {
        return app;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }
}
